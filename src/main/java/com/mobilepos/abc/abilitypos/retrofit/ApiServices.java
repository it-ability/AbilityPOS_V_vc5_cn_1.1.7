package com.mobilepos.abc.abilitypos.retrofit;

import com.mobilepos.abc.abilitypos.Model.RegisterCompany;
import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobilepos.abc.abilitypos.Model.VoucherD;
import com.mobilepos.abc.abilitypos.Model.VoucherH;

import kotlin.jvm.JvmMultifileClass;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {

    @FormUrlEncoded
    @POST("insertVoucherH.php")
    Call<VoucherH> insertVoucherH(

                                @Field("VoucherNo") String VoucherNo,
                                @Field("UserId") Integer UserId,
                                @Field("CusName") String CusName,
                                @Field("VrType") String VrType,
                                @Field("Discount") Double Discount,
                                @Field("Tax") Double Tax,
                                @Field("CompanyId") String CompanyId,
                                @Field("Amount") Double Amount,
                                @Field("Paid") Double Paid,
                                @Field("Balance") Double Balance,
                                @Field("CurrentBal") Double CurrentBal,
                                @Field("Remark") String Remark,
                                @Field("VDate") String VDate,
                                @Field("FlatH") String FlatH


                                 );


    @FormUrlEncoded
    @POST("insertVoucherD.php")
    Call<VoucherD> insertVoucherD (

                        @Field("VoucherNo") String VoucherNo,
                        @Field("Name") String Name,
                        @Field("DetailsCode") String DetailsCode,
                        @Field("PPrice") Double PPrice,
                        @Field("SPrice") Double SPrice,
                        @Field("Qty") Double Qty,
                        @Field("Amt") Double Amt,
                        @Field("CodeType") String CodeType,
                        @Field("CompanyId") String CompanyId,
                        @Field("VDate") String VDate,
                        @Field("FlatD") String FlatD

                                  );


    @GET("retriveVoucherDData.php")
    Call<VoucherD> retrieveVoucherDData(@Path("VoucherNo") String VoucherNo);

    @GET("retrieveVoucherHData.php")
    Call<VoucherH> retrieveVoucherHData(@Path("VoucherNo") String VoucherNo);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon1(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon2(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon3(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon4(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherD> retrieveVoucherDDataCon5(@Path("") String a);

    @GET("retrieveVoucherDData")
    Call<VoucherH> retrieveVoucherHDataCon6(@Path("") String a);


    @FormUrlEncoded
    @POST("RegisterCompany")
    Call<RegisterCompany> registerCompany(@Field("CompanyName") String CompanyName ,
                                          @Field("Address") String Address,
                                          @Field("Country") String Country,
                                          @Field("Province") String Province,
                                          @Field("Township") String Township,
                                          @Field("ContactPerson") String ContactPerson,
                                          @Field("Email") String Email,
                                          @Field("PhNo") Long PhNo,
                                          @Field("DataStorein") String DataStorein);
    



}
