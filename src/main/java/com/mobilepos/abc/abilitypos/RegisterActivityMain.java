package com.mobilepos.abc.abilitypos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegisterActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);


    }
    public void gotoComReg(View v)
    {
        Intent intent = new Intent(RegisterActivityMain.this, RegisterActivityCompany.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void gotoMemReg(View V)
    {
        Intent intent = new Intent(RegisterActivityMain.this, RegisterActivityMe.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
