package com.mobilepos.abc.abilitypos.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.UpdateItemActivity;
import com.mobilepos.abc.abilitypos.VoucherDetailActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ViewDetailAdapter extends ArrayAdapter<DataEntry> {

    private LayoutInflater inflater;
    private ArrayList<DataEntry> manageItem;
    private int mresourceID;
    DB_Controller controller;
    String voucherNo = "",detailName = "",detailQty = "",detailAmt = "",tax = "",discount = "",total = "",netTotal = "",pay = "",refund = "",cusName = "",remark = "";

    public ViewDetailAdapter(Context context, int tresourceID, ArrayList<DataEntry> manageItem) {
        super(context, tresourceID, manageItem);
        this.manageItem = manageItem;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mresourceID = tresourceID;
    }

    public View getView(final int position, View convertView, ViewGroup parents) {
        convertView = inflater.inflate(mresourceID, null);

        controller = new DB_Controller(getContext());

        final DataEntry detailList = manageItem.get(position);

        if (detailList != null) {
            final TextView vrno;
            Button view;

            vrno = (TextView) convertView.findViewById(R.id.voucherNo);
            view = (Button) convertView.findViewById(R.id.view);

            if (vrno != null) {
                vrno.setText(detailList.getVoucherNo());
            }

            view.setOnClickListener(new View.OnClickListener() {

                Double SumAmt=0.0;

                @Override
                public void onClick(View v) {
                    String code = detailList.getVoucherNo();
                    String vrnos = vrno.getText().toString();
                    Cursor data = controller.all_detailVr(code);
                    int numRow = data.getCount();

                    DecimalFormat df = new DecimalFormat("#");
                    df.setMaximumFractionDigits(3);

                    voucherNo = "Voucher NO\t:\t" + code;

                    if (numRow == 0) {

                    } else {
/*
                        if(data.moveToFirst()){

                        }while (data.moveToNext());
                        controller.close();
*/
                        while (data.moveToNext()) {
                            detailName += data.getString(1) + "\n\n";
                            detailQty += (Integer.valueOf(data.getString(5)) * (-1)) + "\n\n";
                            detailAmt += data.getString(6) + "\n\n";

                            SumAmt+=Double.valueOf(data.getString(6));

                        }
                        controller.close();
                    }

                    Cursor datas = controller.all_detailH(vrno.getText().toString());
                    int numRows = datas.getCount();

                    if (numRows == 0) {

                    } else {
                        while (datas.moveToNext()) {
                            cusName = datas.getString(2);
                            tax = datas.getString(5);
                            discount = datas.getString(4);
                            total = String.valueOf(SumAmt);
                            pay = datas.getString(8);
                            refund = datas.getString(9);
                            remark = datas.getString(11);
                            netTotal = Double.valueOf(total) + Double.valueOf(datas.getString(5)) - Double.valueOf(datas.getString(4)) + "";
                        }
                    }
                    Intent intent = new Intent(getContext(), VoucherDetailActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("code", code);
                    mBundle.putString("vrno", voucherNo);
                    mBundle.putString("vrnos",vrnos);
                    mBundle.putString("name", detailName);
                    mBundle.putString("qty", detailQty);
                    mBundle.putString("amt", detailAmt);
                    mBundle.putString("tax", tax);
                    mBundle.putString("discount", discount);
                    mBundle.putString("total", total);
                    mBundle.putString("netTotal", netTotal);
                    mBundle.putString("pay", pay);
                    mBundle.putString("refund", refund);
                    mBundle.putString("cusName", cusName);
                    mBundle.putString("remark", remark);
                    intent.putExtras(mBundle);
                    getContext().startActivity(intent);
                }
            });
        }
        return convertView;

    }
}
