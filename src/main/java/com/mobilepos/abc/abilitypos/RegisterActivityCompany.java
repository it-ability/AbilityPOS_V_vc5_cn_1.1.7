package com.mobilepos.abc.abilitypos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Model.RegisterCompany;
import com.mobilepos.abc.abilitypos.retrofit.ApiServices;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClients;

import java.util.concurrent.ConcurrentMap;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivityCompany extends AppCompatActivity {

    EditText CompanyName, Address, Country, Province, Township, ContactPerson, Email;
    RadioGroup DataStorein;
    RadioButton Phone,Cloud;
    EditText PhNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);

        findId();

    }
        public void register_com(View v) {
            if (validate()) {
                resetError();
                if (formatValidator()) {
                    resetError();
                        postData();
                    }
                }
            }

    private void postData() {

        String CompanyNameStr = CompanyName.getText().toString();
        String AddressStr = Address.getText().toString();
        String CountryStr = Country.getText().toString();
        String ProvinceStr = Province.getText().toString();
        String TownshipStr = Township.getText().toString();
        String ContactPersonStr = ContactPerson.getText().toString();
        String EmailStr = Email.getText().toString();
        Long PhNoStr = Long.parseLong(PhNo.getText().toString());
        String Storage = "";

        if(Phone.isChecked())
        {
            Storage= "P";
        }
        else if(Cloud.isChecked())
        {
            Storage = "C";
        }

        ApiServices ApiServices = RetrofitClients.getApiServices();
        Call<RegisterCompany> CallRegisterCompanyApi = ApiServices.registerCompany(CompanyNameStr,AddressStr,CountryStr,ProvinceStr,TownshipStr,ContactPersonStr,EmailStr,PhNoStr,Storage);

        CallRegisterCompanyApi.enqueue(new Callback<RegisterCompany>() {
            @Override
            public void onResponse(Call<RegisterCompany> call, Response<RegisterCompany> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getResult().equalsIgnoreCase("true") || response.body() != null)
                    {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.registerSuccess), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivityCompany.this, LoginCompanyActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.registerFail),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterCompany> call, Throwable t) {

            }
        });
    }


    private void findId() {

        CompanyName = findViewById(R.id.edit_company_name);
        Address = findViewById(R.id.edit_company_address);
        Country = findViewById(R.id.edit_com_country);
        Province = findViewById(R.id.edit_com_province);
        Township = findViewById(R.id.edit_com_township);
        ContactPerson = findViewById(R.id.edit_com_contact_person);
        Email = findViewById(R.id.edit_com_email);
        DataStorein = findViewById(R.id.radio_group_com);
        PhNo = findViewById(R.id.edit_com_phno);
        Phone = findViewById(R.id.phonest_com);
        Cloud = findViewById(R.id.cloudst_com);
    }

    private boolean formatValidator() {
        boolean validate = true;
        if (CompanyName.getText().toString().length() > 50) {
            CompanyName.setError(getResources().getString(R.string.err_length_two));
            CompanyName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (Address.getText().toString().length() > 50) {
            Address.setError(getResources().getString(R.string.err_length_two));
            Address.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (Country.getText().toString().length() > 50) {
            Country.setError(getResources().getString(R.string.err_length_two));
            Country.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (Province.getText().toString().length() > 50) {
            Province.setError(getResources().getString(R.string.err_length_two));
            Province.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (Township.getText().toString().length() > 30) {
            Township.setError(getResources().getString(R.string.err_length_one));
            Township.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (ContactPerson.getText().toString().length() > 100) {
            ContactPerson.setError(getResources().getString(R.string.err_length_three));
            ContactPerson.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (Email.getText().toString().length() > 100) {
            Email.setError(getResources().getString(R.string.err_length_three));
            Email.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (PhNo.getText().toString().length() > 100) {
            PhNo.setError(getResources().getString(R.string.err_length_three));
            PhNo.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }

        return validate;
    }
    private boolean validate()
    {
        boolean validate = true;
//        if(CompanyName.getText().toString() == null || CompanyName.getText().toString().isEmpty())
//        {
//            CompanyName.setError(getResources().getString(R.string.required));
//            CompanyName.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//
//        if(Address.getText().toString() == null || Address.getText().toString().isEmpty())
//        {
//            Address.setError(getResources().getString(R.string.required));
//            Address.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if (Country.getText().toString() == null || Country.getText().toString().isEmpty())
//
//        {
//            Country.setError(getResources().getString(R.string.required));
//            Country.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if (Province.getText().toString() == null || Province.getText().toString().isEmpty())
//        {
//            Province.setError(getResources().getString(R.string.required));
//            Province.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if(Township.getText().toString() == null || Township.getText().toString().isEmpty())
//        {
//            Township.setError(getResources().getString(R.string.required));
//            Township.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if(ContactPerson.getText().toString() == null || ContactPerson.getText().toString().isEmpty())
//        {
//            ContactPerson.setError(getResources().getString(R.string.required));
//            ContactPerson.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if(Email.getText().toString() == null || Email.getText().toString().isEmpty())
//        {
//            Email.setError(getResources().getString(R.string.required));
//            Email.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }
//        if(PhNo.getText().toString() == null || PhNo.getText().toString().isEmpty())
//        {
//            PhNo.setError(getResources().getString(R.string.required));
//            PhNo.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }

        if(Phone.isChecked()==false && Cloud.isChecked() == false)
        {
            Phone.setError(getResources().getString(R.string.required));
            Phone.setBackgroundResource(R.drawable.err_background);

            Cloud.setError(getResources().getString(R.string.required));
            Cloud.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
//        if(Cloud.isChecked()==false )
//        {
//            Cloud.setError(getResources().getString(R.string.required));
//            Cloud.setBackgroundResource(R.drawable.err_background);
//            validate = false;
//        }

        return validate;


    }

    private void resetError()
    {
     CompanyName.setError(null);
     CompanyName.setBackgroundResource(R.drawable.edt_background_color);

     Address.setError(null);
     Address.setBackgroundResource(R.drawable.edt_background_color);

     Country.setError(null);
     Country.setBackgroundResource(R.drawable.edt_background_color);

     Province.setError(null);
     Province.setBackgroundResource(R.drawable.edt_background_color);

     Township.setError(null);
     Township.setBackgroundResource(R.drawable.edt_background_color);

     ContactPerson.setError(null);
     ContactPerson.setBackgroundResource(R.drawable.edt_background_color);

     Email.setError(null);
     Email.setBackgroundResource(R.drawable.edt_background_color);

     PhNo.setError(null);
     PhNo.setBackgroundResource(R.drawable.edt_background_color);

     Phone.setError(null);
     Phone.setBackgroundResource(R.drawable.edt_background_color);

     Cloud.setError(null);
     Cloud.setBackgroundResource(R.drawable.edt_background_color);


    }
}
