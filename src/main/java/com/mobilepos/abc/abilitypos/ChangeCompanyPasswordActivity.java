package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobilepos.abc.abilitypos.checkconnection.ConnectivityReceiver;
import com.mobilepos.abc.abilitypos.retrofit.ApiService;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClient;

import io.pcyan.sweetdialog.SweetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeCompanyPasswordActivity extends AppCompatActivity {

    private AppCompatEditText myOldPassword;
    private AppCompatEditText myNewPassword;
    private AppCompatEditText myConfirmPassword;
    String oldPassword;
    long IMEI;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_company_password);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        oldPassword = sharedPreferences.getString("password", "");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findById();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void confirm(View view) {
        if (validate()) {
            resetError();
            if (formatValidator()) {
                resetError();
                if (samePassword()) {
                    resetError();
                    if (!ConnectivityReceiver.isConnected()) {
                        SweetDialog.build()
                                .setCancelAble(false)
                                .autoCancel(true)
                                .setContent("Check internet connection")
                                .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetDialog sweetDialog) {
                                    }
                                }).showDialog(getSupportFragmentManager(), "normal_dialog");
                    } else {
                        postData();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getIMEI();
        }
    }


    private void getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI = Long.parseLong(telephonyManager.getDeviceId());
    }

    private void postData() {
        ApiService apiService = RetrofitClient.getApiService();
        getIMEI();
        Call<RegisterUser> call = apiService.changePassword(IMEI, myOldPassword.getText().toString(), myNewPassword.getText().toString());
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.body() != null && response.body().getResult().equalsIgnoreCase("true")) {
                    sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("password");
                    editor.putString("password", myNewPassword.getText().toString());
                    editor.commit();

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_change_password), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ChangeCompanyPasswordActivity.this, ManagementActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.change_password_error), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                System.out.println("Something wrong" + t.toString());
            }
        });
    }

    public boolean samePassword() {
        boolean same = true;
        if (!myOldPassword.getText().toString().equals(oldPassword)) {
            myOldPassword.setError(getResources().getString(R.string.err_old_password_wrong));
            myOldPassword.setBackgroundResource(R.drawable.err_background);
            same = false;
        }
        if (myNewPassword.getText().toString().equalsIgnoreCase(oldPassword)) {
            myNewPassword.setError(getResources().getString(R.string.err_already_used_password));
            myNewPassword.setBackgroundResource(R.drawable.err_background);
            same = false;
        }
        if (!myConfirmPassword.getText().toString().equals(myNewPassword.getText().toString())) {
            myConfirmPassword.setError(getResources().getString(R.string.err_new_confirm_password));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            same = false;
        }
        return same;
    }

    public boolean validate() {
        boolean validate = true;
        if (myOldPassword.getText().toString().isEmpty() || myOldPassword.getText().toString() == null) {
            myOldPassword.setError(getResources().getString(R.string.required));
            myOldPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myNewPassword.getText().toString().isEmpty() || myNewPassword.getText().toString() == null) {
            myNewPassword.setError(getResources().getString(R.string.required));
            myNewPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myConfirmPassword.getText().toString().isEmpty() || myConfirmPassword.getText().toString() == null) {
            myConfirmPassword.setError(getResources().getString(R.string.required));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    private void resetError() {
        myOldPassword.setError(null);
        myOldPassword.setBackground(null);
        myNewPassword.setError(null);
        myNewPassword.setBackground(null);
        myConfirmPassword.setError(null);
        myConfirmPassword.setBackground(null);
    }

    private boolean formatValidator() {
        boolean validate = true;
        if (myOldPassword.getText().toString().length() > 50) {
            myOldPassword.setError(getResources().getString(R.string.err_length_two));
            myOldPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myNewPassword.getText().toString().length() > 50) {
            myNewPassword.setError(getResources().getString(R.string.err_length_two));
            myNewPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myConfirmPassword.getText().toString().length() > 50) {
            myConfirmPassword.setError(getResources().getString(R.string.err_length_two));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    public void cancel(View view) {
        Intent intent = new Intent(ChangeCompanyPasswordActivity.this, ManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChangeCompanyPasswordActivity.this, ManagementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void findById() {
        myOldPassword = findViewById(R.id.edt_old_password);
        myNewPassword = findViewById(R.id.edt_change_new_password);
        myConfirmPassword = findViewById(R.id.edt_change_confirm_password);
    }
}
