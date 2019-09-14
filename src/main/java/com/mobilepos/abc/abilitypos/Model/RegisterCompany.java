package com.mobilepos.abc.abilitypos.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobilepos.abc.abilitypos.RegisterActivityCompany;
import java.io.Serializable;

public class RegisterCompany implements Serializable {

    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;

    @SerializedName("Address")
    @Expose
    private String CompanyAddress;

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("Province")
    @Expose
    private String Province;

    @SerializedName("Township")
    @Expose
    private String Township;

    @SerializedName("ContactPerson")
    @Expose
    private String ContactPerson;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Phnp")
    @Expose
    private String Phno;

    @SerializedName("DataStrorein")
    @Expose
    private String DataStorein;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    @SerializedName("Result")
    @Expose
    private String Result;


    public void RegisterActivityCompany(String CompanyName, String CompanyAddress, String Country,
                                   String Province, String Township, String ContactPerson,
                                   String Email, String Phno, String DataStorein )
    {
        this.CompanyName = CompanyName;
        this.CompanyAddress = CompanyAddress;
        this.Country = Country;
        this.Province = Province;
        this.Township = Township;
        this.ContactPerson = ContactPerson;
        this.Email = Email;
        this.Phno = Phno;
        this.DataStorein = DataStorein;

    }


    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getTownship() {
        return Township;
    }

    public void setTownship(String township) {
        Township = township;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhno() {
        return Phno;
    }

    public void setPhno(String phno) {
        Phno = phno;
    }

    public String getDataStorein() {
        return DataStorein;
    }

    public void setDataStorein(String dataStorein) {
        DataStorein = dataStorein;
    }
}
