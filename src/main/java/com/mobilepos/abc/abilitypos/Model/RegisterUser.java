package com.mobilepos.abc.abilitypos.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;

import java.io.Serializable;

public class RegisterUser implements Serializable {

    @SerializedName("ApplicationType")
    @Expose
    private String ApplicationType;

    @SerializedName("CompanyName")
    @Expose
    private String CompanyName;

    @SerializedName("UserName")
    @Expose
    private String UserName;

    @SerializedName("PhoneNo")
    @Expose
    private String PhoneNo;

    @SerializedName("IMENo")
    @Expose
    private long IMENo;

    @SerializedName("Email")
    @Expose
    private String Email;

    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("TownShip")
    @Expose
    private String TownShip;

    @SerializedName("Division")
    @Expose
    private String Division;

    @SerializedName("Country")
    @Expose
    private String Country;

    @SerializedName("CreateDate")
    @Expose
    private String CreateDate;

    @SerializedName("Password")
    @Expose
    private String Password;

    @SerializedName("PasswordChangeDate")
    @Expose
    private String PasswordChangeDate;

    @SerializedName("Result")
    @Expose
    private String result;

    public RegisterUser(String applicationType, String companyName, String userName, String mobileNo, long IMENo, String email, String address, String township, String stateDivision, String country, String createDate, String password, String passwordChangeDate) {
        this.ApplicationType = applicationType;
        this.CompanyName = companyName;
        this.UserName = userName;
        this.PhoneNo = mobileNo;
        this.IMENo = IMENo;
        this.Email = email;
        this.Address = address;
        this.TownShip = township;
        this.Division = stateDivision;
        this.Country = country;
        this.CreateDate = createDate;
        this.Password = password;
        this.PasswordChangeDate = passwordChangeDate;
    }

    public RegisterUser() {
    }

    public String getApplicationType() {
        return ApplicationType;
    }

    public void setApplicationType(String applicationType) {
        ApplicationType = applicationType;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public long getIMENo() {
        return IMENo;
    }

    public void setIMENo(long IMENo) {
        this.IMENo = IMENo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTownShip() {
        return TownShip;
    }

    public void setTownShip(String townShip) {
        TownShip = townShip;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        Division = division;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordChangeDate() {
        return PasswordChangeDate;
    }

    public void setPasswordChangeDate(String passwordChangeDate) {
        PasswordChangeDate = passwordChangeDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RegisterUser{" +
                "ApplicationType='" + ApplicationType + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", UserName='" + UserName + '\'' +
                ", PhoneNo='" + PhoneNo + '\'' +
                ", IMENo=" + IMENo +
                ", Email='" + Email + '\'' +
                ", Address='" + Address + '\'' +
                ", TownShip='" + TownShip + '\'' +
                ", Division='" + Division + '\'' +
                ", Country='" + Country + '\'' +
                ", CreateDate='" + CreateDate + '\'' +
                ", Password='" + Password + '\'' +
                ", PasswordChangeDate='" + PasswordChangeDate + '\'' +
                '}';
    }
}
