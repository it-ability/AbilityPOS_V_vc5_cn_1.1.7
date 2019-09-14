package com.mobilepos.abc.abilitypos.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.MainActivity;
import com.mobilepos.abc.abilitypos.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class SignUpFragment extends Fragment {

    Button createAcc;
    public RadioButton maleRadioButton;
    public RadioButton fmaleRadioButton;

    public static String gender = "Male";
    public static String uName = "";
    public static String uContact = "";
    public static String uAddress = "";
    public static String uEmail = "";
    public static String uPassowrd = "";
    public static String uniquecomid = "";

    public static String date = "";
    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH) + 1;
    int d = calendar.get(Calendar.DATE);


    public static final String EMAIL_REGEX_PATTERN = "([a-zA-Z0-9]{1,256}[\\@]gmail[\\.]com)";
    public static final String PHONE_PATTERN = "((09[0-9]{9})|(01[0-9]{5})|(02[0-9]{5})|(071[0-9]{5}))";

    public boolean male = false;
    public boolean female = false;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(1)
    public EditText username;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(2)
    public EditText contact;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Pattern(sequence = 2, regex = SignUpFragment.EMAIL_REGEX_PATTERN, messageResId = R.string.invalid_email)
    @Order(3)
    public EditText email;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(5)
    public EditText userAddr;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(4)
    public EditText confirmPassowrd;


    private Validator mValidator;

    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mContactTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mAddressTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;

    DB_Controller controller;

    public SignUpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString("title", param1);
        args.putString("page", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);

        controller = new DB_Controller(getActivity());

        mNameTextInputLayout = view.findViewById(R.id.name);
        mContactTextInputLayout = view.findViewById(R.id.phone);
        mEmailTextInputLayout = view.findViewById(R.id.email);
        mAddressTextInputLayout = view.findViewById(R.id.addr);
        mConfirmPasswordTextInputLayout = view.findViewById(R.id.confirmPass);

        username = view.findViewById(R.id.name_editText);
        contact = view.findViewById(R.id.phone_editText);
        email = view.findViewById(R.id.email_editText);
        userAddr = view.findViewById(R.id.address_editText);
        confirmPassowrd = view.findViewById(R.id.confirmPass_editText);

        maleRadioButton = view.findViewById(R.id.radioButton);
        fmaleRadioButton = view.findViewById(R.id.radioButton2);
        createAcc = view.findViewById(R.id.create_acc);


        if (d < 10 && m < 10) {
            date = "0" + d + "/0" + m + "/" + y;
        } else if (d > 10 && m < 10) {
            date = d + "/0" + m + "/" + y;
        } else if (d < 10 && m > 10) {
            date = "0" + d + "/" + m + "/" + y;
        } else {
            date = d + "/" + m + "/" + y;
        }

        mValidator = new Validator(this);

        fmaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Female";
                female = true;
                male = false;

            }
        });
        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "Male";
                female = false;
                male = true;

            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                    String message = error.getFailedRules().get(0).getMessage(getActivity());

                    if (view instanceof EditText) {
                        // Only request focus in the first view that messes up!
                        if (i == 0)
                            view.requestFocus();

                        view.setContentDescription("Error");

                        switch (view.getId()) {

                            case R.id.name_editText:
                                mNameTextInputLayout.setErrorEnabled(true);
                                mNameTextInputLayout.setContentDescription("Error");
                                mNameTextInputLayout.setError(message);
                                break;

                            case R.id.phone_editText:
                                mContactTextInputLayout.setErrorEnabled(true);
                                mContactTextInputLayout.setContentDescription("Error");
                                mContactTextInputLayout.setError(message);
                                break;

                            case R.id.email_editText:
                                mEmailTextInputLayout.setErrorEnabled(true);
                                mEmailTextInputLayout.setContentDescription("Error");
                                mEmailTextInputLayout.setError(message);
                                break;

                            case R.id.address_editText:
                                mAddressTextInputLayout.setErrorEnabled(true);
                                mAddressTextInputLayout.setContentDescription("Error");
                                mAddressTextInputLayout.setError(message);
                                break;

                            case R.id.confirmPass_editText:
                                mConfirmPasswordTextInputLayout.setErrorEnabled(true);
                                mConfirmPasswordTextInputLayout.setContentDescription("Error");
                                mConfirmPasswordTextInputLayout.setError(message);
                                break;
                        }

                    }
                }
            }

            @Override
            public void onValidationSucceeded() {

                EditText user_name = (EditText) getActivity().findViewById(R.id.name_editText);
                EditText user_phone = (EditText) getActivity().findViewById(R.id.phone_editText);
                EditText user_address = (EditText) getActivity().findViewById(R.id.address_editText);
                EditText user_email = (EditText) getActivity().findViewById(R.id.email_editText);
                EditText user_confirmpass = (EditText) getActivity().findViewById(R.id.confirmPass_editText);

                uName = user_name.getText().toString();
                uContact = user_phone.getText().toString();
                uAddress = user_address.getText().toString();
                uEmail = user_email.getText().toString();
                uPassowrd = user_confirmpass.getText().toString();
                generate();

                controller.activateAccount(uName, uContact, gender, uAddress, uEmail, uPassowrd, "S", uniquecomid, date);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toasty.success(getActivity(), "Successfully created", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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

    private void generate() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy0123456789".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        uniquecomid = String.valueOf(stringBuilder);
    }
}
