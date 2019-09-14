package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.RegisterCompany;
import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobilepos.abc.abilitypos.Model.VoucherD;
import com.mobilepos.abc.abilitypos.Model.VoucherDDataEntry;
import com.mobilepos.abc.abilitypos.Model.VoucherH;
import com.mobilepos.abc.abilitypos.Model.VoucherHDataEntry;
import com.mobilepos.abc.abilitypos.checkconnection.ConnectivityReceiver;
import com.mobilepos.abc.abilitypos.retrofit.ApiService;
import com.mobilepos.abc.abilitypos.retrofit.ApiServices;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClient;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClients;
import com.mobilepos.abc.abilitypos.session.SessionManager;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import io.pcyan.sweetdialog.SweetDialog;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class LoginCompanyActivity extends AppCompatActivity {

    public static ArrayList<VoucherHDataEntry> dataEntries = new ArrayList<>();
    public static ArrayList<VoucherDDataEntry> dataEntriesD = new ArrayList<>();

    private AppCompatEditText myUserName,myPassword;
    Button signIn;

    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    private boolean sessionUser;
    long IMEI;
    TelephonyManager telephonyManager;
    SharedPreferences sharedPreferences;
    DB_Controller controller;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_company);
        findById();

        //    toolbar_title.setText(getResources().getString(R.string.app_name));

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        sessionUser = sharedPreferences.getBoolean("session", false);
        userName = sharedPreferences.getString("UserName", "");
        myUserName.setText(userName);
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

/*
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
*/


        /*old code*/
        if (MainActivity.cname.equals("")) {
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("CompanySetting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("name",RegisterActivityMe.companyName);
            editor.putString("address", RegisterActivityMe.companyAddress);
            editor.putString("contact", RegisterActivityMe.companyContact);
            editor.putString("tax", "0");
            editor.putString("discount", "0");
            editor.apply();
        }

        ShimmerTextView shimmer_tv = findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(100)
                .setDuration(2500)
                .setStartDelay(0)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        shimmer.start(shimmer_tv);

        controller = new DB_Controller(getApplicationContext());
        boolean user = controller.defaultUser();
        if (!user == true) {
            String date = "";
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DATE);

            if (d < 10 && m < 10) {
                date = y + "/0" + m + "/0" + d;
            } else if (d > 10 && m < 10) {
                date = y + "/0" + m + "/" + d;
            } else if (d < 10 && m > 10) {
                date = y + "/" + m + "/0" + d;
            } else {
                date = y + "/" + m + "/" + d;
            }
            controller.activateAccount("Default", "09000000000", "Male", "no address", "default@gmail.com", "pass", "O", "1", date);
        }

        if (MDetect.INSTANCE.isUnicode()) {
            String uniSt = Rabbit.zg2uni(getResources().getString(R.string.signin));
            signIn.setText(uniSt);
        } else {
            signIn.setText(getResources().getString(R.string.signin));
        }

        /*old Code*/

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!ConnectivityReceiver.isConnected())
                {

                    Toasty.success(getApplicationContext(),"Network Connection Error",Toast.LENGTH_SHORT).show();

                }
                else
                {


                    Cursor datasVH = null;
                    datasVH = controller.getVoucherHDatasForCloud();
                    int numRow = datasVH.getCount();

                    if(numRow == 0)
                    {
                        Toasty.success(getApplicationContext(),"No data in VoucherH Table such FlatH N",Toast.LENGTH_LONG).show();
                    }
                    else if(numRow >=0)
                    {



                        while (datasVH.moveToNext()) {


                            VoucherHDataEntry dataEntryD = new VoucherHDataEntry();

                            String VoucherNo = dataEntryD.setVoucherNo(datasVH.getString(0));
                            dataEntryD.setVoucherNo(datasVH.getString(0));

                            dataEntryD.setUserId(datasVH.getInt(1));
                            Integer UserId = dataEntryD.setUserId(datasVH.getInt(1));

                            dataEntryD.setCusName(datasVH.getString(2));
                            String CusName = dataEntryD.setCusName(datasVH.getString(2));

                            dataEntryD.setVrType(datasVH.getString(3));
                            String VrType = dataEntryD.setVrType(datasVH.getString(3));

                            dataEntryD.setDiscount(datasVH.getDouble(4));
                            Double Discount = dataEntryD.setDiscount(datasVH.getDouble(4));

                            dataEntryD.setTax(datasVH.getDouble(5));
                            Double Tax = dataEntryD.setTax(datasVH.getDouble(5));

                            dataEntryD.setCompanyId(datasVH.getString(6));
                            String CompanyId = dataEntryD.setCompanyId(datasVH.getString(6));

                            dataEntryD.setAmount(datasVH.getDouble(7));
                            Double Amount = dataEntryD.setAmount(datasVH.getDouble(7));

                            dataEntryD.setPaid(datasVH.getDouble(8));
                            Double Paid = dataEntryD.setPaid(datasVH.getDouble(8));

                            dataEntryD.setBalance(datasVH.getDouble(9));
                            Double Balance = dataEntryD.setBalance(datasVH.getDouble(9));

                            dataEntryD.setCurBalance(datasVH.getDouble(10));
                            Double CurrentBal = dataEntryD.setCurBalance(datasVH.getDouble(10));

                            dataEntryD.setRemark(datasVH.getString(11));
                            String Remark = dataEntryD.setRemark(datasVH.getString(11));

                            dataEntryD.setVDate(datasVH.getString(12));
                            String VDate = dataEntryD.setVDate(datasVH.getString(12));

                            dataEntryD.setFlatH(datasVH.getString(13));
                            String FlatH = dataEntryD.setFlatH(datasVH.getString(13));

                            //this is the repair code for api testing and some error have in this databse

                            dataEntries.add(dataEntryD);

                            controller.update_voucherHFlatH("Y");

                            //Toasty.success(getApplicationContext(), dataEntries.toString(),Toast.LENGTH_LONG).show();

                            ApiServices apiservices = RetrofitClients.getApiServices();

                            Call<VoucherH> ApiCall = apiservices.insertVoucherH(VoucherNo, UserId, CusName, VrType, Discount, Tax, CompanyId, Amount, Paid, Balance, CurrentBal, Remark, VDate, FlatH);

                            ApiCall.enqueue(new Callback<VoucherH>() {
                                @Override
                                public void onResponse(Call<VoucherH> call, Response<VoucherH> response) {

                                    String msg = response.body().toString();
                                    // Toasty.success(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<VoucherH> call, Throwable t) {
                                    // Toasty.success(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

                                }
                            });


//                          for(int i=0; i<dataEntries.size();i++)
//                          {
//                              Toasty.success(getApplicationContext(), dataEntries.get(i).toString(),Toast.LENGTH_LONG).show();
//                          }


                            // Toasty.success(getApplicationContext(), "Successfully update", Toast.LENGTH_SHORT).show();

                        }
                    }

                    else if(numRow <=0)
                    {
                        Toasty.success(getApplicationContext(),"Nothing Can Forever We Can Change The Future",Toast.LENGTH_LONG).show();
                    }


                    //Toasty.success(getApplicationContext()," Network Connected ",Toast.LENGTH_SHORT).show();


                    //For VoucherD Data to API Web Serices


                    Cursor datasVD = null;
                    datasVD = controller.getVoucherDDatasForCloud();
                    int numRowD = datasVD.getCount();

                    if( numRowD == 0)
                    {
                        Toasty.error(getApplicationContext(),"No Data in VoucherD Table Such Flat N").show();

                    }

                    else if( numRowD >= 0 )
                    {
                        while( datasVD.moveToNext())
                        {
                            VoucherDDataEntry dataEntryD = new VoucherDDataEntry();

                            dataEntryD.setVoucherNo(datasVD.getString(0));
                            String VoucherNoD =   dataEntryD.setVoucherNo(datasVD.getString(0));

                            dataEntryD.setName(datasVD.getString(1));
                            String Name = dataEntryD.setName(datasVD.getString(1));

                            dataEntryD.setDetailsCode(datasVD.getString(2));
                            String DetailsCode = dataEntryD.setDetailsCode(datasVD.getString(2));

                            dataEntryD.setPprice(datasVD.getDouble(3));
                            Double PPrice = dataEntryD.setPprice(datasVD.getDouble(3));

                            dataEntryD.setSPrice(datasVD.getDouble(4));
                            Double SPrice = dataEntryD.setSPrice(datasVD.getDouble(4));

                            dataEntryD.setQty(datasVD.getDouble(5));
                            Double Qty = dataEntryD.setQty(datasVD.getDouble(5));

                            dataEntryD.setAmt(datasVD.getDouble(6));
                            Double Amt = dataEntryD.setAmt(datasVD.getDouble(6));

                            dataEntryD.setCodeType(datasVD.getString(7));
                            String CodeType = dataEntryD.setCodeType(datasVD.getString(7));

                            dataEntryD.setCompanyId(datasVD.getString(8));
                            String CompanyId = dataEntryD.setCompanyId(datasVD.getString(8));

                            dataEntryD.setVDate(datasVD.getString(9));
                            String VDate = dataEntryD.setVDate(datasVD.getString(9));

                            dataEntryD.setFlatD(datasVD.getString(10));
                            String FlatD = dataEntryD.setFlatD(datasVD.getString(10));

                            ApiServices apiServices = RetrofitClients.getApiServices();
                            Call<VoucherD> callApi = apiServices.insertVoucherD(VoucherNoD, Name, DetailsCode, PPrice, SPrice, Qty, Amt,CodeType, CompanyId, VDate, FlatD);

                            callApi.enqueue(new Callback<VoucherD>() {
                                @Override
                                public void onResponse(Call<VoucherD> call, Response<VoucherD> response) {

                                }

                                @Override
                                public void onFailure(Call<VoucherD> call, Throwable t) {

                                }
                            });

                            controller.update_voucherDFlatD("Y");
                            dataEntriesD.add(dataEntryD);

                          //  Toasty.success(getApplicationContext(),dataEntriesD.toString(),Toast.LENGTH_LONG).show();
                            Toasty.success(getApplicationContext(),"Network Connected, Your Datas Successfully Save To Cloud.",Toast.LENGTH_LONG).show();


                        }

                    }
                    else
                    {
                        Toasty.error(getApplicationContext(),"Nothing",Toast.LENGTH_SHORT).show();
                    }

                }











                if (validate()) {
                    resetError();
                    postDatas();
                 //   Toast.makeText(getBaseContext(),"Buttton click",Toast.LENGTH_LONG).show();

                }
            }
        });
    }



    public void loginIn(View view) {
        if (validate()) {
            resetError();
            if (!ConnectivityReceiver.isConnected()) {
                SweetDialog.build()
                        .setCancelAble(false)
                        .autoCancel(true)
                        .setContent("Check internet connection")
                        .setOnConfirmListener(new SweetDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetDialog sweetDialog) {
                            }
                        }).showDialog(getSupportFragmentManager(), "normal dialog");
            } else {
                postDatas();
            }
          //  postData();
        }
    }

    public void register(View view) {

        Intent intent = new Intent(LoginCompanyActivity.this, RegisterActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    private void postDatas() {

        final String ucs=myUserName.getText().toString().trim();
        final String pass=myPassword.getText().toString().trim();


        ApiService apiService = RetrofitClient.getApiService();
        getIMEI();
        Call<RegisterUser> call = apiService.loginUser(myUserName.getText().toString().trim(), IMEI, myPassword.getText().toString().trim());//original code
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {



               // repair code
               // response.body().getResult().startsWith("OK");


                String aa=response.body().getResult().substring(0,2);
                String bb=response.body().getResult().substring(2);

                System.out.print("EEEEEEEEEE"+aa);

               // if (response.body().getResult().equalsIgnoreCase("OK") && response.body() != null) {
               //  if (aa.equalsIgnoreCase("OK") && response.body() != null) {

                  if ((aa.equalsIgnoreCase("OK") && response.body() != null) /*|| (ucs.equals("ucs") && pass.equals("123"))*/){




                    sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("session", true);
                        editor.putLong("IMEINo", IMEI);

                        editor.putString("UserName", myUserName.getText().toString().trim());
                        editor.putString("password", myPassword.getText().toString().trim());
                        editor.commit();
                        //session.passwordSession(Long.parseLong("123456789123456789"), myPassword.getText().toString());
                        //Toast.makeText(getApplicationContext(), getResources().getString(R.string.loginSuccess), Toast.LENGTH_LONG).show();

                   //repair code

                    //    Toast.makeText(getApplicationContext(), bb, Toast.LENGTH_LONG).show();



                        RetrieveCompanyIdandDataStorein();


                        Intent intent = new Intent(LoginCompanyActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();





                  //      repair code
                }else {
                    Toast.makeText(getApplicationContext(), response.body().getResult(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                System.out.println("Something wrong" + t.toString());
            }
        });
    }

    private void RetrieveCompanyIdandDataStorein()
    {

        SharedPreferences spf = getApplicationContext().getSharedPreferences("CompanyId",MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString("CompanyId","001A212212");
        edit.apply();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getIMEI();
        }
    }


    private void getIMEI() {
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI = Long.parseLong(telephonyManager.getDeviceId());
    }

    private void findById() {
        //  toolbar_title = findViewById(R.id.toolbar_title);
        myUserName = findViewById(R.id.edt_user_name);
        myPassword = findViewById(R.id.edt_password);
        signIn = findViewById(R.id.submit);
    }

    private boolean validate() {
        boolean validate = true;
        if (myUserName.getText().toString().isEmpty() || myUserName.getText().toString() == null) {
            if (MDetect.INSTANCE.isUnicode()) {
                myUserName.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
            } else {
                myUserName.setError(getResources().getString(R.string.required));
            }
            myUserName.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        if (myPassword.getText().toString().isEmpty() || myPassword.getText().toString() == null) {
            if (MDetect.INSTANCE.isUnicode()) {
                myPassword.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
            } else {
                myPassword.setError(getResources().getString(R.string.required));
            }
            myPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    private void resetError() {
        myPassword.setError(null);
        myPassword.setBackground(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
