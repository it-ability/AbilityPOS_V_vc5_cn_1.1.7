package com.mobilepos.abc.abilitypos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;


public class CompanySettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(1)
    public EditText name;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(2)
    public EditText address;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(3)
    public EditText contact;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(4)
    public EditText tax;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(5)
    public EditText discount;

    Button addVoucher, clearVoucher;

    private Validator mValidator;

    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mAddressTextInputLayout;
    private TextInputLayout mContactTextInputLayout;
    private TextInputLayout mTaxTextInputLayout;
    private TextInputLayout mDiscountTextInputLayout;
    private Spinner myPrintSizeSpinner;
    private List<String> list;
    private int printerSize;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_voucher_profile);

        //for keyboard hidden
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mValidator = new Validator(this);
        mNameTextInputLayout = findViewById(R.id.name);
        mAddressTextInputLayout = findViewById(R.id.address);
        mContactTextInputLayout = findViewById(R.id.contact);
        mTaxTextInputLayout = findViewById(R.id.tax);
        mDiscountTextInputLayout = findViewById(R.id.discount);

        name = findViewById(R.id.name_editText);
        address = findViewById(R.id.address_editText);
        contact = findViewById(R.id.contact_editText);
        tax = findViewById(R.id.tax_editText);
        discount = findViewById(R.id.discount_editText);

        name.setText(MainActivity.cname);
        //name.setEnabled(false);
        address.setText(MainActivity.caddress);
        contact.setText(MainActivity.ccontact);
        tax.setText(MainActivity.ctax);
        System.out.println("The tax is"+MainActivity.ctax);
        discount.setText(MainActivity.cdiscount);
        System.out.println("The discount is"+MainActivity.cdiscount);

        addVoucher = findViewById(R.id.create_voucher);
        clearVoucher = findViewById(R.id.clear_voucher);
        myPrintSizeSpinner = findViewById(R.id.spinner_code_type);

        list = new ArrayList<>();
        list.add("Select Printer Size");
        list.add("3 inches");
        list.add("4 inches");
        SharedPreferences sharedpreferences1 = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        printerSize = sharedpreferences1.getInt("printer_size", 0);
        myPrintSizeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter code_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myPrintSizeSpinner.setAdapter(code_adapter);
        if (printerSize != 0) {
            if (printerSize == 3) {
                myPrintSizeSpinner.setSelection(1);
            } else if (printerSize == 4) {
                myPrintSizeSpinner.setSelection(2);
            }
        }
        if (MDetect.INSTANCE.isUnicode()) {
            name.setHint(Rabbit.zg2uni(getResources().getString(R.string.shopname)));
            address.setHint(Rabbit.zg2uni(getResources().getString(R.string.address)));
            contact.setHint(Rabbit.zg2uni(getResources().getString(R.string.contact)));
            tax.setHint(Rabbit.zg2uni(getResources().getString(R.string.tax)));
            discount.setHint(Rabbit.zg2uni(getResources().getString(R.string.discount)));
            addVoucher.setText(Rabbit.zg2uni(getResources().getString(R.string.add_v)));
            clearVoucher.setText(Rabbit.zg2uni(getResources().getString(R.string.clear)));
            this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.companysetting)));

        } else {
            name.setHint(getResources().getString(R.string.shopname));
            address.setHint(getResources().getString(R.string.address));
            contact.setHint(getResources().getString(R.string.contact));
            tax.setHint(getResources().getString(R.string.tax));
            discount.setHint(getResources().getString(R.string.discount));
            addVoucher.setText(getResources().getString(R.string.add_v));
            clearVoucher.setText(getResources().getString(R.string.clear));
            this.setTitle(getResources().getString(R.string.companysetting));
        }
        clearVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                name.setText("");
                address.setText("");
                contact.setText("");
                tax.setText("");
                discount.setText("");
                Toasty.success(CompanySettingActivity.this, "Succesfully clear", Toast.LENGTH_SHORT).show();

            }
        });


        addVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    resetError();
                    SharedPreferences sharedpreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.apply();
                    mValidator.validate();
                }
            }
        });

        mValidator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                EditText name = (EditText) findViewById(R.id.name_editText);
                EditText address = (EditText) findViewById(R.id.address_editText);
                EditText contact = (EditText) findViewById(R.id.contact_editText);
                EditText tax = (EditText) findViewById(R.id.tax_editText);
                EditText discount = (EditText) findViewById(R.id.discount_editText);

                MainActivity.cname = name.getText().toString();
                MainActivity.caddress = address.getText().toString();
                MainActivity.ccontact = contact.getText().toString();
                MainActivity.ctax = tax.getText().toString();
                MainActivity.cdiscount = discount.getText().toString();
                MainActivity.printerSize = printerSize;

                SharedPreferences sharedpreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("name", name.getText().toString());
                editor.putString("address", address.getText().toString());
                editor.putString("contact", contact.getText().toString());
                editor.putString("tax", tax.getText().toString());
                editor.putString("discount", discount.getText().toString());
                editor.putInt("printer_size", printerSize);

                editor.apply();
                Toasty.success(CompanySettingActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                clearErrors();
                for (int i = 0; i < errors.size(); i++) {
                    ValidationError error = errors.get(i);
                    View view = error.getView();
                    String message = error.getFailedRules().get(0).getMessage(CompanySettingActivity.this);

                    if (view instanceof EditText) {
                        if (i == 0) {
                            view.requestFocus();
                        }
                        view.setContentDescription("Error");

                        switch (view.getId()) {
                            case R.id.name_editText:
                                mNameTextInputLayout.setErrorEnabled(true);
                                mNameTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mNameTextInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mNameTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.address_editText:
                                mAddressTextInputLayout.setErrorEnabled(true);
                                mAddressTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mAddressTextInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mAddressTextInputLayout.setError(message);
                                }
                                break;

                            case R.id.contact_editText:
                                mContactTextInputLayout.setErrorEnabled(true);
                                mContactTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mContactTextInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mContactTextInputLayout.setError(message);
                                }

                            case R.id.tax_editText:
                                mTaxTextInputLayout.setErrorEnabled(true);
                                mTaxTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mTaxTextInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mTaxTextInputLayout.setError(message);
                                }

                            case R.id.discount_editText:
                                mDiscountTextInputLayout.setErrorEnabled(true);
                                mDiscountTextInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mDiscountTextInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mDiscountTextInputLayout.setError(message);
                                }
                        }
                    }
                }
            }
        });
    }

/*
    public void savePrintSetting() {
        if (validate()) {
            resetError();
            SharedPreferences sharedpreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            mValidator.validate();
        }
    }
*/

    private void resetError() {
        myPrintSizeSpinner.setBackgroundResource(R.drawable.edt_background_color);
    }

    private boolean validate() {
        boolean validate = true;
        if (myPrintSizeSpinner.getSelectedItem().equals("Select Printer Size")) {
            myPrintSizeSpinner.setBackgroundResource(R.drawable.err_background);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.code_spinner), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        return validate;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_code_type) {
            if (position == 0) {
                printerSize = 0;
            } else if (position == 1) {
                printerSize = 3;
            } else if (position == 2) {
                printerSize = 4;
            }
        } else {
            if (position != 0) {
                Toast.makeText(getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /* @Override
             public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState) {
                 // Inflate the layout for this fragment
                 View view=inflater.inflate(R.layout.fragment_voucher_profile, container, false);*/
        /*mValidator = new Validator(this);

        mNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.name);
        mAddressTextInputLayout = (TextInputLayout) view.findViewById(R.id.address);
        mContactTextInputLayout = (TextInputLayout) view.findViewById(R.id.contact);
        mTaxTextInputLayout=(TextInputLayout)view.findViewById(R.id.tax);
        mDiscountTextInputLayout=(TextInputLayout)view.findViewById(R.id.discount);

        name=(EditText)view.findViewById(R.id.name_editText);
        address=(EditText)view.findViewById(R.id.address_editText);
        contact=(EditText)view.findViewById(R.id.contact_editText);
        tax=(EditText)view.findViewById(R.id.tax_editText);
        discount=(EditText)view.findViewById(R.id.discount_editText);

        name.setText(MainActivity.cname);
        address.setText(MainActivity.caddress);
        contact.setText(MainActivity.ccontact);
        tax.setText(MainActivity.ctax);
        discount.setText(MainActivity.cdiscount);

        addVoucher=(Button) view.findViewById(R.id.create_voucher);
        clearVoucher=(Button)view.findViewById(R.id.clear_voucher);

        if (MDetect.INSTANCE.isUnicode()) {
            name.setHint(Rabbit.zg2uni(getResources().getString(R.string.shopname)));
            address.setHint(Rabbit.zg2uni(getResources().getString(R.string.address)));
            contact.setHint(Rabbit.zg2uni(getResources().getString(R.string.contact)));
            tax.setHint(Rabbit.zg2uni(getResources().getString(R.string.tax)));
            discount.setHint(Rabbit.zg2uni(getResources().getString(R.string.discount)));
            addVoucher.setText(Rabbit.zg2uni(getResources().getString(R.string.add_v)));
            clearVoucher.setText(Rabbit.zg2uni(getResources().getString(R.string.clear)));

        } else {
            name.setHint(getResources().getString(R.string.shopname));
            address.setHint(getResources().getString(R.string.address));
            contact.setHint(getResources().getString(R.string.contact));
            tax.setHint(getResources().getString(R.string.tax));
            discount.setHint(getResources().getString(R.string.discount));
            addVoucher.setText(getResources().getString(R.string.add_v));
            clearVoucher.setText(getResources().getString(R.string.clear));
        }
        clearVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanySetting", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                name.setText("");
                address.setText("");
                contact.setText("");
                tax.setText("");
                discount.setText("");
                Toasty.success(getActivity(),"Succesfully clear",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

        addVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanySetting", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.apply();
                mValidator.validate();
            }
        });

        mValidator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                EditText name=(EditText)getActivity().findViewById(R.id.name_editText);
                EditText address=(EditText)getActivity().findViewById(R.id.address_editText);
                EditText contact=(EditText)getActivity().findViewById(R.id.contact_editText);
                EditText tax=(EditText)getActivity().findViewById(R.id.tax_editText);
                EditText discount=(EditText)getActivity().findViewById(R.id.discount_editText);

                MainActivity.cname=name.getText().toString();
                MainActivity.caddress=address.getText().toString();
                MainActivity.ccontact=contact.getText().toString();
                MainActivity.ctax=tax.getText().toString();
                MainActivity.cdiscount=discount.getText().toString();

                SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanySetting", MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedpreferences.edit();

                editor.putString("name",name.getText().toString());
                editor.putString("address",address.getText().toString());
                editor.putString("contact",contact.getText().toString());
                editor.putString("tax",tax.getText().toString());
                editor.putString("discount",discount.getText().toString());

                editor.apply();
                Toasty.success(getActivity(),"Successfully created", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                clearErrors();
                for (int i=0; i<errors.size(); i++){
                    ValidationError error=errors.get(i);
                    View view=error.getView();
                    String message=error.getFailedRules().get(0).getMessage(getActivity());

                    if (view instanceof EditText){
                        if (i==0){
                            view.requestFocus();
                        }
                        view.setContentDescription("Error");

                        switch (view.getId()){
                            case R.id.name_editText:
                                mNameTextInputLayout.setErrorEnabled(true);
                                mNameTextInputLayout.setContentDescription("Error");
                                mNameTextInputLayout.setError(message);
                                break;

                            case R.id.address_editText:
                                mAddressTextInputLayout.setErrorEnabled(true);
                                mAddressTextInputLayout.setContentDescription("Error");
                                mAddressTextInputLayout.setError(message);
                                break;

                            case R.id.contact_editText:
                                mContactTextInputLayout.setErrorEnabled(true);
                                mContactTextInputLayout.setContentDescription("Error");
                                mContactTextInputLayout.setError(message);

                            case R.id.tax_editText:
                                mTaxTextInputLayout.setErrorEnabled(true);
                                mTaxTextInputLayout.setContentDescription("Error");
                                mTaxTextInputLayout.setError(message);

                            case R.id.discount_editText:
                                mDiscountTextInputLayout.setErrorEnabled(true);
                                mDiscountTextInputLayout.setContentDescription("Error");
                                mDiscountTextInputLayout.setError(message);
                        }
                    }
                }
            }
        });*/
       /* return view;
    }*/
    private void clearErrors() {
        mNameTextInputLayout.setErrorEnabled(false);
        mAddressTextInputLayout.setErrorEnabled(false);
        mContactTextInputLayout.setErrorEnabled(false);
        mTaxTextInputLayout.setErrorEnabled(false);
        mDiscountTextInputLayout.setErrorEnabled(false);

        mNameTextInputLayout.setContentDescription("");
        mAddressTextInputLayout.setContentDescription("");
        mContactTextInputLayout.setContentDescription("");
        mTaxTextInputLayout.setContentDescription("");
        mDiscountTextInputLayout.setContentDescription("");
    }
}
