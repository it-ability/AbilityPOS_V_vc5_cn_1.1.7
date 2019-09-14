package com.mobilepos.abc.abilitypos;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText currentPass_et,newPass_et,confirmPass_et;
    TextInputLayout newPassword,currentPassword,confirmPassword;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPass_et=(EditText) findViewById(R.id.currentPass_et);
        newPass_et=(EditText) findViewById(R.id.newPass_et);
        confirmPass_et=(EditText) findViewById(R.id.confirmPass_et);

        newPassword=(TextInputLayout) findViewById(R.id.newPassword);
        currentPassword=(TextInputLayout) findViewById(R.id.currentPassword);
        confirmPassword=(TextInputLayout) findViewById(R.id.confirmPassword);

        update_btn=(Button) findViewById(R.id.update_btn);
    }
}
