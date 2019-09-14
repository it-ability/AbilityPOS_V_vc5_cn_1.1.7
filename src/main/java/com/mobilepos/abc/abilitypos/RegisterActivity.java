package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends AppCompatActivity {
    EditText applicationType_editText;
    EditText companyName_editText;
    EditText name_editText;
    EditText phone_editText;
    EditText IME_editText;
    EditText email_editText;
    EditText address_editText;
    EditText township_editText;
    EditText state_editText;
    EditText country_editText;
    EditText pass_editText;

    TextInputLayout password, country, state, township, address, email, IME, phone, username, companyName, applicationType;
    Button register;
    TelephonyManager tm;
    String IMEI;


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        register = (Button) findViewById(R.id.register);

        applicationType_editText = (EditText) findViewById(R.id.applicationType_editText);
        companyName_editText = (EditText) findViewById(R.id.companyName_editText);
        name_editText = (EditText) findViewById(R.id.name_editText);
        phone_editText = (EditText) findViewById(R.id.phone_editText);
        IME_editText =(EditText) findViewById(R.id.IME_editText);
        email_editText = (EditText) findViewById(R.id.email_editText);
        address_editText = (EditText) findViewById(R.id.address_editText);
        township_editText = (EditText) findViewById(R.id.township_editText);
        state_editText = (EditText) findViewById(R.id.state_editText);
        country_editText = (EditText) findViewById(R.id.country_editText);
        pass_editText = (EditText) findViewById(R.id.pass_editText);

        password = (TextInputLayout) findViewById(R.id.password);
        country = (TextInputLayout) findViewById(R.id.country);
        state = (TextInputLayout) findViewById(R.id.state);
        township = (TextInputLayout) findViewById(R.id.township);
        address = (TextInputLayout) findViewById(R.id.address);
        email = (TextInputLayout) findViewById(R.id.email);
        IME = (TextInputLayout) findViewById(R.id.IME);
        phone = (TextInputLayout) findViewById(R.id.phone);
        username = (TextInputLayout) findViewById(R.id.username);
        companyName = (TextInputLayout) findViewById(R.id.companyName);
        applicationType = (TextInputLayout) findViewById(R.id.applicationType);


        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
        IMEI = tm.getImei();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IMEI!=null){
                    IME_editText.setText(IMEI);

                }
            }
        });

    }

}

