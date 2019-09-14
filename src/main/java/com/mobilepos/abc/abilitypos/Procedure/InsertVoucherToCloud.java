package com.mobilepos.abc.abilitypos.Procedure;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.VoucherD;
import com.mobilepos.abc.abilitypos.Model.VoucherDDataEntry;
import com.mobilepos.abc.abilitypos.Model.VoucherH;
import com.mobilepos.abc.abilitypos.Model.VoucherHDataEntry;
import com.mobilepos.abc.abilitypos.checkconnection.ConnectivityReceiver;
import com.mobilepos.abc.abilitypos.retrofit.ApiServices;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClients;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertVoucherToCloud extends AppCompatActivity {

    public static ArrayList<VoucherHDataEntry> dataEntries = new ArrayList<>();
    public static ArrayList<VoucherDDataEntry> dataEntriesD = new ArrayList<>();
    DB_Controller controller;

        public void InsertDataToCloud() {
            if (!ConnectivityReceiver.isConnected()) {

                Toasty.success(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();

            } else {


                Cursor datasVH = null;
                datasVH = controller.getVoucherHDatasForCloud();
                int numRow = datasVH.getCount();

                if (numRow == 0) {
                    Toasty.success(getApplicationContext(), "No data in VoucherH Table such FlatH N", Toast.LENGTH_LONG).show();
                } else if (numRow >= 0) {


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
                } else if (numRow <= 0) {
                    Toasty.success(getApplicationContext(), "Nothing Can Forever We Can Change The Future", Toast.LENGTH_LONG).show();
                }


                Toasty.success(getApplicationContext(), " Network Connected ", Toast.LENGTH_SHORT).show();


                //For VoucherD Data to API Web Serices


                Cursor datasVD = null;
                datasVD = controller.getVoucherDDatasForCloud();
                int numRowD = datasVD.getCount();

                if (numRowD == 0) {
                    Toasty.error(getApplicationContext(), "No Data in VoucherD Table Such Flat N").show();

                } else if (numRowD >= 0) {
                    while (datasVD.moveToNext()) {
                        VoucherDDataEntry dataEntryD = new VoucherDDataEntry();

                        dataEntryD.setVoucherNo(datasVD.getString(0));
                        String VoucherNoD = dataEntryD.setVoucherNo(datasVD.getString(0));

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
                        Call<VoucherD> callApi = apiServices.insertVoucherD(VoucherNoD, Name, DetailsCode, PPrice, SPrice, Qty, Amt, CodeType, CompanyId, VDate, FlatD);

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

                        Toasty.success(getApplicationContext(), dataEntriesD.toString(), Toast.LENGTH_LONG).show();


                    }

                } else {
                    Toasty.error(getApplicationContext(), "Nothing", Toast.LENGTH_SHORT).show();
                }

            }
        }




}
