package com.mobilepos.abc.abilitypos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Adapter.ListViewAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SignUpFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class ManageUserActivity extends AppCompatActivity {
    Button updateAcc,deleteAcc;
    public RadioButton maleRadioButton;
    public RadioButton fmaleRadioButton;
    public CheckBox inactive;

    public static String id = "";
    public static String gender = "";
    public static String uName="";
    public static String uContact="";
    public static String uAddress="";
    public static String uEmail="";
    public static String uPassowrd="";
    public static String active="";



    public static final String EMAIL_REGEX_PATTERN="([a-zA-Z0-9]{1,256}[\\@]gmail[\\.]com)";
    public static final String PHONE_PATTERN="((09[0-9]{9})|(01[0-9]{5})|(02[0-9]{5})|(071[0-9]{5}))";

    public boolean male=false;
    public boolean female=false;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Order(1)
    public EditText username;

    @NotEmpty(sequence = 1,messageResId = R.string.validation_empty)
    @Pattern(sequence = 2, regex = SignUpFragment.PHONE_PATTERN, messageResId = R.string.invalid_phone)
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
        setContentView(R.layout.activity_manage_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

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

        updateAcc=(Button)findViewById(R.id.update_acc);
        deleteAcc=(Button)findViewById(R.id.delete_acc);
        inactive=(CheckBox)findViewById(R.id.change);

        if (MDetect.INSTANCE.isUnicode()){
            updateAcc.setText(Rabbit.zg2uni(getResources().getString(R.string.updateUser)));
            deleteAcc.setText(Rabbit.zg2uni(getResources().getString(R.string.deleteUser)));
            inactive.setText(Rabbit.zg2uni(getResources().getString(R.string.inactive)));
            this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.edituser)));
        }else {
            updateAcc.setText(getResources().getString(R.string.updateUser));
            deleteAcc.setText(getResources().getString(R.string.deleteUser));
            inactive.setText(getResources().getString(R.string.inactive));
            this.setTitle(getResources().getString(R.string.edituser));
        }

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Cursor data = controller.getUserData(username.getText().toString());

                int numRow = data.getCount();

                if (numRow == 0) {
                } else {
                    while (data.moveToNext()) {
                        id=data.getString(0);
                        username.setText(data.getString(1));
                        contact.setText(data.getString(2));
                        userAddr.setText(data.getString(4));
                        email.setText(data.getString(5));
                        confirmPassowrd.setText(data.getString(6));
                        active=data.getString(10);
                        gender=data.getString(3);

                        if (active.equals("1")){
                            inactive.setChecked(false);
                        }else {
                            inactive.setChecked(true);
                        }

                        if (gender.equals("male")){
                            maleRadioButton.setChecked(true);
                            fmaleRadioButton.setChecked(false);
                        }else {
                            maleRadioButton.setChecked(false);
                            fmaleRadioButton.setChecked(true);
                        }

                    }
                }
            }
        });


        if (MDetect.INSTANCE.isUnicode()) {
            username.setHint(Rabbit.zg2uni(getResources().getString(R.string.username)));
            contact.setHint(Rabbit.zg2uni(getResources().getString(R.string.contact)));
            email.setHint(Rabbit.zg2uni(getResources().getString(R.string.email)));
            userAddr.setHint(Rabbit.zg2uni(getResources().getString(R.string.address)));
            confirmPassowrd.setHint(Rabbit.zg2uni(getResources().getString(R.string.password)));
            maleRadioButton.setText(Rabbit.zg2uni(getResources().getString(R.string.male)));
            fmaleRadioButton.setText(Rabbit.zg2uni(getResources().getString(R.string.female)));
            updateAcc.setText(Rabbit.zg2uni(getResources().getString(R.string.update)));
        } else {
            username.setHint(getResources().getString(R.string.username));
            contact.setHint(getResources().getString(R.string.contact));
            email.setHint(getResources().getString(R.string.email));
            userAddr.setHint(getResources().getString(R.string.address));
            confirmPassowrd.setHint(getResources().getString(R.string.password));
            maleRadioButton.setText(getResources().getString(R.string.male));
            fmaleRadioButton.setText(getResources().getString(R.string.female));
            updateAcc.setText(getResources().getString(R.string.update));
        }

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

        inactive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    active="0";
                }else {
                    active="1";
                }
            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                controller.deleteUser(id);
                Toasty.success(getApplicationContext(),"Successfully delete!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        updateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageUserActivity.this);
                alertDialogBuilder.setMessage("Do you want to edit account?");
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

                controller.updateUser(id,uName,uContact,gender,uEmail,uAddress,uPassowrd,active);
//                controller.activateAccount(uName,uContact,gender,uAddress,uEmail,uPassowrd,"owner",uniquecomid,date);
                Intent intent= new Intent(ManageUserActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toasty.success(getApplicationContext(),"Successfully updated", Toast.LENGTH_SHORT).show();
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

}
