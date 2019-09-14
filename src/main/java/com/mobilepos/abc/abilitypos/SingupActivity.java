package com.mobilepos.abc.abilitypos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SignUpFragment;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class SingupActivity extends AppCompatActivity {

    Button createAcc;
    public RadioButton maleRadioButton;
    public RadioButton fmaleRadioButton;

    public static String gender = "Male";
    public static String uName="";
    public static String uContact="";
    public static String uAddress="";
    public static String uEmail="";
    public static String uPassowrd="";
    public static String uniquecomid="";

    public static String date="";
    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH)+1;
    int d = calendar.get(Calendar.DATE);


    public static final String EMAIL_REGEX_PATTERN="([a-zA-Z0-9]{1,256}[\\@]gmail[\\.]com)";
    public static final String PHONE_PATTERN="((09[0-9]{9})|(01[0-9]{5})|(02[0-9]{5})|(071[0-9]{5}))";

    public boolean male=false;
    public boolean female=false;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Order(1)
    public EditText username;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Order(2)
    public EditText contact;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Pattern(sequence = 2, regex = SignUpFragment.EMAIL_REGEX_PATTERN, messageResId = R.string.invalid_email)
    @Order(3)
    public EditText email;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Order(5)
    public EditText userAddr;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Order(4)
    public EditText confirmPassowrd;


    private Validator mValidator;

    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mContactTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mAddressTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;

    DB_Controller controller;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sing_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        controller=new DB_Controller(getApplicationContext());

        mNameTextInputLayout = (TextInputLayout)findViewById(R.id.name);
        mContactTextInputLayout = (TextInputLayout)findViewById(R.id.phone);
        mEmailTextInputLayout = (TextInputLayout)findViewById(R.id.email);
        mAddressTextInputLayout = (TextInputLayout)findViewById(R.id.addr);
        mConfirmPasswordTextInputLayout = (TextInputLayout)findViewById(R.id.confirmPass);

        username=(EditText)findViewById(R.id.name_editText);
        contact=(EditText)findViewById(R.id.phone_editText);
        email=(EditText)findViewById(R.id.email_editText);
        userAddr=(EditText)findViewById(R.id.address_editText);
        confirmPassowrd=(EditText)findViewById(R.id.confirmPass_editText);

        maleRadioButton = (RadioButton)findViewById(R.id.radioButton);
        fmaleRadioButton = (RadioButton)findViewById(R.id.radioButton2);

        createAcc=(Button)findViewById(R.id.create_acc);

        if (MDetect.INSTANCE.isUnicode()) {
            username.setHint(Rabbit.zg2uni(getResources().getString(R.string.username)));
            contact.setHint(Rabbit.zg2uni(getResources().getString(R.string.contact)));
            email.setHint(Rabbit.zg2uni(getResources().getString(R.string.email)));
            userAddr.setHint(Rabbit.zg2uni(getResources().getString(R.string.address)));
            confirmPassowrd.setHint(Rabbit.zg2uni(getResources().getString(R.string.password)));
            maleRadioButton.setText(Rabbit.zg2uni(getResources().getString(R.string.male)));
            fmaleRadioButton.setText(Rabbit.zg2uni(getResources().getString(R.string.female)));
            createAcc.setText(Rabbit.zg2uni(getResources().getString(R.string.createAcc)));
            this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.signup)));
        } else {
            username.setHint(getResources().getString(R.string.username));
            contact.setHint(getResources().getString(R.string.contact));
            email.setHint(getResources().getString(R.string.email));
            userAddr.setHint(getResources().getString(R.string.address));
            confirmPassowrd.setHint(getResources().getString(R.string.password));
            maleRadioButton.setText(getResources().getString(R.string.male));
            fmaleRadioButton.setText(getResources().getString(R.string.female));
            createAcc.setText(getResources().getString(R.string.createAcc));
            this.setTitle(getResources().getString(R.string.signup));
        }

        date=y+"/"+m+"/"+d;

        mValidator = new Validator(this);

        fmaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Female";
                female=true;
                male=false;

            }
        });
        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
                female=false;
                male=true;

            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SingupActivity.this);
                alertDialogBuilder.setMessage("Do you want to create account?");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                mValidator.validate();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        mValidator.setValidationListener(new Validator.ValidationListener() {

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                clearErrors();

                for (int i = 0; i < errors.size(); i++) {
                    ValidationError error = errors.get(i);
                    View view = error.getView();
                    String message = error.getFailedRules().get(0).getMessage(getApplicationContext());

                    if (view instanceof EditText) {
                        // Only request focus in the first view that messes up!
                        if (i == 0)
                            view.requestFocus();

                        view.setContentDescription("Error");

                        switch (view.getId()) {

                            case R.id.name_editText:
                                mNameTextInputLayout.setErrorEnabled(true);
                                mNameTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()){
                                    mNameTextInputLayout.setError(Rabbit.zg2uni(message));
                                }else {
                                    mNameTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.phone_editText:
                                mContactTextInputLayout.setErrorEnabled(true);
                                mContactTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()){
                                    mContactTextInputLayout.setError(Rabbit.zg2uni(message));
                                }else {
                                    mContactTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.email_editText:
                                mEmailTextInputLayout.setErrorEnabled(true);
                                mEmailTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()){
                                    mEmailTextInputLayout.setError(Rabbit.zg2uni(message));
                                }else {
                                    mEmailTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.address_editText:
                                mAddressTextInputLayout.setErrorEnabled(true);
                                mAddressTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()){
                                    mAddressTextInputLayout.setError(Rabbit.zg2uni(message));
                                }else {
                                    mAddressTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.confirmPass_editText:
                                mConfirmPasswordTextInputLayout.setErrorEnabled(true);
                                mConfirmPasswordTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()){
                                    mConfirmPasswordTextInputLayout.setError(Rabbit.zg2uni(message));
                                }else {
                                    mConfirmPasswordTextInputLayout.setError(message);
                                }
                                break;
                        }

                    }
                }
            }

            @Override
            public void onValidationSucceeded() {

                EditText user_name = (EditText)findViewById(R.id.name_editText);
                EditText user_phone = (EditText)findViewById(R.id.phone_editText);
                EditText user_address = (EditText)findViewById(R.id.address_editText);
                EditText user_email = (EditText)findViewById(R.id.email_editText);
                EditText user_confirmpass = (EditText)findViewById(R.id.confirmPass_editText);

                uName=user_name.getText().toString();
                uContact=user_phone.getText().toString();
                uAddress=user_address.getText().toString();
                uEmail=user_email.getText().toString();
                uPassowrd=user_confirmpass.getText().toString();
//                generate();
                uniquecomid="1";

                controller.activateAccount(uName,uContact,gender,uAddress,uEmail,uPassowrd,"owner",uniquecomid,date);
                Intent intent= new Intent(SingupActivity.this,ManagementActivity.class);
                startActivity(intent);
                finish();
                Toasty.success(getApplicationContext(),"Successfully created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearErrors() {
        mNameTextInputLayout.setErrorEnabled(false);
        mContactTextInputLayout.setErrorEnabled(false);
        mEmailTextInputLayout.setErrorEnabled(false);
        mAddressTextInputLayout.setErrorEnabled(false);
        mConfirmPasswordTextInputLayout.setErrorEnabled(false);

        mNameTextInputLayout.setContentDescription("");
        mContactTextInputLayout.setContentDescription("");
        mEmailTextInputLayout.setContentDescription("");
        mAddressTextInputLayout.setContentDescription("");
        mConfirmPasswordTextInputLayout.setContentDescription("");


    }

    private void generate(){
        char[] chars="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy0123456789".toCharArray();
        StringBuilder stringBuilder=new StringBuilder();
        Random random = new Random();
        for (int i=0; i<10; i++){
            char c=chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        uniquecomid= String.valueOf(stringBuilder);
    }
}
