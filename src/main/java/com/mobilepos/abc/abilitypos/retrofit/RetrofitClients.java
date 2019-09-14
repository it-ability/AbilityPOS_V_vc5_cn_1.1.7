package com.mobilepos.abc.abilitypos.retrofit;

import org.jetbrains.annotations.NotNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClients {

public static final String ROOT_URL = "https://abilitysoftwareservices.000webhostapp.com/mposapi/emp/";


    @NotNull
    public static Retrofit getRetrofitInstances() {
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static ApiServices getApiServices() {
        return getRetrofitInstances().create(ApiServices.class);
    }
}
