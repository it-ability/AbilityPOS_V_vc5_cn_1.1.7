package com.mobilepos.abc.abilitypos.retrofit;

import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.Max;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("MobileRegister")
    Call<RegisterUser> registerUser(@Field("ApplicationType") String ApplicationType,
                                    @Field("CompanyName") String CompanyName,
                                    @Field("UserName") String UserName,
                                    @Field("PhoneNo") String PhoneNo,
                                    @Field("IMENo") long IMENo,
                                    @Field("Email") String Email,
                                    @Field("Address") String Address,
                                    @Field("TownShip") String TownShip,
                                    @Field("Division") String Division,
                                    @Field("Country") String Country,
                                    @Field("CreateDate") String CreateDate,
                                    @Field("Password") String Password,
                                    @Field("PasswordChangeDate") String PasswordChangeDate
    );

    @FormUrlEncoded
    @POST("LogIn")
    Call<RegisterUser> loginUser(
                                 @Field("UserName") String userName,
                                 @Field("IMENo") long IMENo,
                                 @Field("Password") String Password
    );


    @FormUrlEncoded
    @POST("ChangePassword")
    Call<RegisterUser> changePassword(@Field("IMENo") long IMENo,
                                      @Field("Password") String password,
                                      @Field("NewPassword") String newPassword
    );
}
