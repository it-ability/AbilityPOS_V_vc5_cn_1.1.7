package com.mobilepos.abc.abilitypos.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Adapter.SaleListAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.MainActivity;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.MyApplication;
import com.mobilepos.abc.abilitypos.PrintService.DeviceListActivity;
import com.mobilepos.abc.abilitypos.PrintService.UnicodeFormatter;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.VoucherDetailActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class SaleListFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public static String[] cType = {"Select Voucher Type", "Sale", "Sale Receive", "Purchase", "Payment", "Cash Receive",
            "Cash Payment", "Stock Receive", "Stock Issue"};
    ListView listView;
    Button selectDate;
    TextView totalAmount,txtTotalAmount,txtTotalReceive;

    String voucherType = "";
    String dateV="",bill="";
    Double netTotal=0.0,credits=0.0,pay=0.0;
    protected static final String TAG = "TAG";
    private DatePickerDialog.OnDateSetListener dateSetListener;
    OutputStream os;

    public static ArrayList<DataEntry> dataEntries = new ArrayList<>();
    DataEntry dataEntry;
    SaleListAdapter adapter;
    ImageButton scanDevice;
    Button print;
    TextView btext;

    DB_Controller controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale_list, container, false);

        controller = new DB_Controller(getActivity());

        listView       = (ListView) view.findViewById(R.id.all_list);
        selectDate     = (Button) view.findViewById(R.id.date_select);
        totalAmount    =(TextView)view.findViewById(R.id.totalAmount);
        scanDevice     =(ImageButton)view.findViewById(R.id.scan_device);
        print          =(Button)view.findViewById(R.id.print);
        btext          =(TextView)view.findViewById(R.id.blutetoothtext);
        txtTotalAmount =(TextView)view.findViewById(R.id.txtTotalAmount);
        txtTotalReceive=(TextView)view.findViewById(R.id.txtTotalReceive);

        btext.setText("Device did'nt connect!-----");

        Spinner spinc = (Spinner) view.findViewById(R.id.voucherType);
        spinc.setOnItemSelectedListener(this);
        ArrayAdapter aac = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, cType);
        aac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinc.setAdapter(aac);

        listView.setOnItemClickListener(this);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                dateV=dayOfMonth+"/"+month+"/"+year;
                selectDate.setText(dateV);

                Cursor data=null;
//                Cursor data = controller.select_DV_detailH(dateV,voucherType);
                dataEntries.clear();
                netTotal=0.0;
                credits=0.0;
                pay=0.0;
                int numRow = data.getCount();
                if (numRow > 0) {
                    listView.setVisibility(View.VISIBLE);
                    while (data.moveToNext()) {
                        dataEntry = new DataEntry();
                        dataEntry.setVoucherNo(data.getString(0));
                        dataEntry.setItemPPrice(data.getDouble(7));
                        dataEntry.setPay(data.getDouble(8));
                        dataEntries.add(dataEntry);

   //changing
                     //   netTotal+=data.getDouble(7);

                        netTotal+=data.getDouble(7)+data.getDouble(5)-data.getDouble(4);
                        pay+=data.getDouble(8);
                        credits+=data.getDouble(9)*(-1);
                        adapter = new SaleListAdapter(getActivity(), R.layout.sale_list, dataEntries);
                        listView.setAdapter(adapter);
                    }
                    totalAmount.setText(netTotal+"\n\n"+pay+"\n\n"+credits);
                } else {
                    listView.setVisibility(View.GONE);
                    totalAmount.setText("----- \n\n----- \n\n-----");

                }
            }
        };

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printJob();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            voucherType = "0";
            selectDate.setEnabled(false);
        } else if (position == 1) {
            voucherType = "1";
            selectDate.setEnabled(true);
        } else if (position == 2) {
            voucherType = "3";
            selectDate.setEnabled(true);
        } else if (position == 3) {
            voucherType = "2";
            selectDate.setEnabled(true);
        } else if (position == 4) {
            voucherType = "4";
            selectDate.setEnabled(true);
        } else if (position == 5) {
            voucherType = "5";
            selectDate.setEnabled(true);
        } else if (position == 6) {
            voucherType = "6";
            selectDate.setEnabled(true);
        } else if (position == 7) {
            voucherType = "7";
            selectDate.setEnabled(true);
        } else if (position == 8) {
            voucherType = "8";
            selectDate.setEnabled(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String voucherNo="";
        String detailName="";
        String detailQty="";
        String detailAmt="";
        String tax="";
        String discount="";
        String total="";
        String netTotals="";
        String pays="";
        String refund="";
        String cusName="";
        String remark="";

        Double SumAmt=0.0;

        final String code=dataEntries.get(position).getVoucherNo();
        Cursor data=controller.all_detailVr(code);
        int numRow = data.getCount();

        voucherNo="Voucher NO\t:\t"+code;

        if (numRow == 0) {

        } else {
            while (data.moveToNext()) {

                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){

                }else {
                detailName+=data.getString(1)+"\n\n";
                detailQty+=(Integer.valueOf(data.getString(5))*(-1))+"\n\n";
                detailAmt+=data.getString(6)+"\n\n";
                SumAmt+=Double.valueOf(data.getString(6));
                }
            }
        }

        Cursor datas = controller.all_detailH(code);
        int numRows = datas.getCount();

        if (numRows == 0) {

        } else {
            while (datas.moveToNext()) {

                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                    cusName=datas.getString(2);
                }else {
                    cusName=datas.getString(2);
                    tax=datas.getString(5);
                    discount=datas.getString(4);
                    total=String.valueOf(SumAmt);
                    pays=datas.getString(8);
                    refund=datas.getString(9);
                    remark=datas.getString(11);
                    netTotals=Double.valueOf(total)+Double.valueOf(datas.getString(5))-Double.valueOf(datas.getString(4))+"";

                }
             }
        }
        Intent intent=new Intent(getActivity(), VoucherDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("vrno", voucherNo);
        mBundle.putString("name",detailName);
        mBundle.putString("qty",detailQty);
        mBundle.putString("amt",detailAmt);
        mBundle.putString("tax",tax);
        mBundle.putString("discount",discount);
        mBundle.putString("total",total);
        mBundle.putString("netTotal",netTotals);
        mBundle.putString("pay",pays);
        mBundle.putString("refund",refund);
        mBundle.putString("cusName",cusName);
        mBundle.putString("remark",remark);
        intent.putExtras(mBundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);


    }


    //print
    private void printJob(){

        Thread t = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();

                    bill+=String.format("%1$15s %2$8s %3$8s","Voucher No","Total Amt","Receive")+"\n";

                    for (int i=0; i<2; i++){
                        bill+=String.format("%1$15s %2$8s %3$8s","02121236021230","500000","200000")+"\n";
                    }
                    bill+="---------------------------------\n";
//                    bill+=String.format("%1$17s %2$8s", "Total Amount    : ",nettotal.getText().toString())+"\n";
//                    bill+=String.format("%1$17s %2$8s", "Receive         : ",cusPay.getText().toString()+".0")+"\n";
//                    bill+=String.format("%1$17s %2$8s", "Balance         : ",refund.getText().toString())+"\n";
                    bill+="------------Thank you------------\n\n\n\n";
                    System.out.print(bill);

                    os.write(bill.getBytes());
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }
}
