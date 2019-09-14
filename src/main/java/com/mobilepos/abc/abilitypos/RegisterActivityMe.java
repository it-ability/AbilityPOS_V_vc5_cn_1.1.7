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
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.os.Build;
import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobilepos.abc.abilitypos.retrofit.ApiService;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityMe extends AppCompatActivity {

    private TextView toolbar_title;
    private EditText myCompanyName;
    private EditText myUserName;
    private EditText myMobileNo;
    private EditText myEmail;
    private EditText myAddress;
    private EditText myTownship;
    private EditText myStateDivision;
    private EditText myCountry;
    private EditText myPassword;
    private EditText myConfirmPassword;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    String dateTime;
    long IMEI=0;
    String IMEIS=null;
    TelephonyManager telephonyManager;
    public static String companyName,companyAddress,companyContact;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_me);


        findById();
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
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
//        toolbar_title.setText(getResources().getString(R.string.register_title));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateTime = simpleDateFormat.format(new Date());
    }

    public void register(View v) {
        if (validate()) {
            resetError();
            if (formatValidator()) {
                resetError();
                if (samePassword()) {
                    resetError();
                    postData();
                }
            }
        }
    }

    public void postData() {
        SharedPreferences sharedpreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("name", myCompanyName.getText().toString());
        editor.putString("address", myAddress.getText().toString());
        editor.putString("contact", myMobileNo.getText().toString());
        companyName=myCompanyName.getText().toString();
        companyAddress=myAddress.getText().toString();
        companyContact=myMobileNo.getText().toString();
        editor.putString("tax", "0");
        editor.putString("discount", "0");
        editor.apply();
        //Toasty.success(CompanySettingActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
        finish();
        ApiService apiService = RetrofitClient.getApiService();
        getIMEI();
                Call<RegisterUser> call = apiService.registerUser("P", myCompanyName.getText().toString(), myUserName.getText().toString(),
                myMobileNo.getText().toString(), IMEI, myEmail.getText().toString(), myAddress.getText().toString(),
                myTownship.getText().toString(), myStateDivision.getText().toString(), myCountry.getText().toString(),
                dateTime, myPassword.getText().toString(), dateTime);

        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult().equalsIgnoreCase("true") && response.body() != null) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerSuccess), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivityMe.this, LoginCompanyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerFail), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                System.out.println("Something wrong" + t.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getIMEI();
        }
    }

    private void getIMEI() {
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
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
        //IMEIS = telephonyManager.getDeviceId();
        String innnn=telephonyManager.getDeviceId();
        IMEIS=telephonyManager.getDeviceId();
        IMEI = Long.parseLong(telephonyManager.getDeviceId());
    }

    private boolean formatValidator() {
        boolean validate = true;
        if (myCompanyName.getText().toString().length() > 50) {
            myCompanyName.setError(getResources().getString(R.string.err_length_two));
            myCompanyName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myUserName.getText().toString().length() > 50) {
            myUserName.setError(getResources().getString(R.string.err_length_two));
            myUserName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myUserName.getText().toString().length() > 50) {
            myUserName.setError(getResources().getString(R.string.err_length_two));
            myUserName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myMobileNo.getText().toString().length() > 50) {
            myMobileNo.setError(getResources().getString(R.string.err_length_two));
            myMobileNo.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myEmail.getText().toString().length() > 30) {
            myEmail.setError(getResources().getString(R.string.err_length_one));
            myEmail.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myAddress.getText().toString().length() > 100) {
            myAddress.setError(getResources().getString(R.string.err_length_three));
            myAddress.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myTownship.getText().toString().length() > 100) {
            myTownship.setError(getResources().getString(R.string.err_length_three));
            myTownship.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myStateDivision.getText().toString().length() > 100) {
            myStateDivision.setError(getResources().getString(R.string.err_length_three));
            myStateDivision.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myPassword.getText().toString().length() > 50) {
            myPassword.setError(getResources().getString(R.string.err_length_two));
            myPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myCountry.getText().toString().length() > 50) {
            myCountry.setError(getResources().getString(R.string.err_length_two));
            myCountry.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myConfirmPassword.getText().toString().length() > 50) {
            myConfirmPassword.setError(getResources().getString(R.string.err_length_two));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    private boolean validate() {
        boolean validate = true;
        if (myCompanyName.getText().toString() == null || myCompanyName.getText().toString().isEmpty()) {
            myCompanyName.setError(getResources().getString(R.string.required));
            myCompanyName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myUserName.getText().toString() == null || myUserName.getText().toString().isEmpty()) {
            myUserName.setError(getResources().getString(R.string.required));
            myUserName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myMobileNo.getText().toString() == null || myMobileNo.getText().toString().isEmpty()) {
            myMobileNo.setError(getResources().getString(R.string.required));
            myMobileNo.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myEmail.getText().toString() == null || myEmail.getText().toString().isEmpty()) {
            myEmail.setError(getResources().getString(R.string.required));
            myEmail.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myAddress.getText().toString() == null || myAddress.getText().toString().isEmpty()) {
            myAddress.setError(getResources().getString(R.string.required));
            myAddress.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myTownship.getText().toString() == null || myTownship.getText().toString().isEmpty()) {
            myTownship.setError(getResources().getString(R.string.required));
            myTownship.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myStateDivision.getText().toString() == null || myStateDivision.getText().toString().isEmpty()) {
            myStateDivision.setError(getResources().getString(R.string.required));
            myStateDivision.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myCountry.getText().toString() == null || myCountry.getText().toString().isEmpty()) {
            myCountry.setError(getResources().getString(R.string.required));
            myCountry.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myPassword.getText().toString() == null || myPassword.getText().toString().isEmpty()) {
            myPassword.setError(getResources().getString(R.string.required));
            myPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myConfirmPassword.getText().toString() == null || myConfirmPassword.getText().toString().isEmpty()) {
            myConfirmPassword.setError(getResources().getString(R.string.required));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    private void resetError() {
        myCompanyName.setError(null);
        myCompanyName.setBackgroundResource(R.drawable.edt_background_color);
        myUserName.setError(null);
        myUserName.setBackgroundResource(R.drawable.edt_background_color);
        myMobileNo.setError(null);
        myMobileNo.setBackgroundResource(R.drawable.edt_background_color);
        myEmail.setError(null);
        myEmail.setBackgroundResource(R.drawable.edt_background_color);
        myAddress.setError(null);
        myAddress.setBackgroundResource(R.drawable.edt_background_color);
        myTownship.setError(null);
        myTownship.setBackgroundResource(R.drawable.edt_background_color);
        myStateDivision.setError(null);
        myStateDivision.setBackgroundResource(R.drawable.edt_background_color);
        myCountry.setError(null);
        myCountry.setBackgroundResource(R.drawable.edt_background_color);
        myPassword.setError(null);
        myPassword.setBackgroundResource(R.drawable.edt_background_color);
        myConfirmPassword.setError(null);
        myConfirmPassword.setBackgroundResource(R.drawable.edt_background_color);
    }

    public boolean samePassword() {
        boolean same = true;
        if (!myPassword.getText().toString().equals(myConfirmPassword.getText().toString())) {
            myConfirmPassword.setError(getResources().getString(R.string.err_password));
            myConfirmPassword.setBackgroundResource(R.drawable.err_background);
            same = false;
        }
        return same;
    }

    private void findById() {
        myCompanyName = findViewById(R.id.edt_company_name);
        myUserName = findViewById(R.id.edt_user_name);
        myMobileNo = findViewById(R.id.edt_mobile_no);
        myEmail = findViewById(R.id.edt_email);
        myAddress = findViewById(R.id.edt_address);
        myTownship = findViewById(R.id.edt_township);
        myStateDivision = findViewById(R.id.edt_state_division);
        myCountry = findViewById(R.id.edt_country);
        myPassword = findViewById(R.id.edt_password);
        myConfirmPassword = findViewById(R.id.edt_confirm_password);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(RegisterActivityMe.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//       // finish();
//    }
}
