package com.mobilepos.abc.abilitypos;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.media.MediaScannerConnection;
import android.media.midi.MidiDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Adapter.MyListAdapter;
import com.mobilepos.abc.abilitypos.Adapter.VoucherAdapter;
import com.mobilepos.abc.abilitypos.Adapter.VoucherDetailAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.ReportListData;
import com.mobilepos.abc.abilitypos.Model.StringUtilis;
import com.mobilepos.abc.abilitypos.PrintService.UnicodeFormatter;
import com.sun.jna.Pointer;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class ReportDetailActivity<linearLayout> extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private Spinner myCodeTypeSpinner;
    private EditText myFromDetailCode;
    private EditText myToDetailCode;
    private Button myPrintBt,cancel;
    private ListView myList;
    DB_Controller db;
    private List<String> codeTypes;
    private String codeTypeString,detailCodeFrom,detailCodeTo,reportString="";
    ReportListData reportListData;
    int printSize;
    List<ReportListData> list;
    TextView txtgroup, txtcode, txtname, txttype, textValue, txtTotals,bluetoothtxt,VoucherDate,opening,vtotalamount,todayDate;
    DB_Controller controller;
    double TSAmt;
    double TProfit;
    double TQty;
    Button btndtfrom, btndtto;
    LinearLayout llTotalText;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListener1;
    String startDate = "", endDate = "", bill = "", name = "", dateSelected = "", headerDate = "", voucherHeader = "",remainStartDate = "",remainEndDate = "";
    private int day,month,year;
    protected static final String TAG = "TAG";
    BluetoothSocket bs;
    OutputStream os;
    private String codeType;


    ArrayList<String> codeAndname1;
    String codeAndName="";


    LinearLayout linearLayoutFrom,linearLayoutTo,lldistance;
    Button btnDtFrom, btnDtTo;
    ListView listView;
    TextView totalAmount,vrno,comName,comAddress,comContact;

    String codeAndname ="";
    String spinnerText;
    RecyclerView recyclerView;
    //    private static VoucherAdapter vAdapter;
    private static VoucherDetailAdapter vAdapter;



    DecimalFormat f = new DecimalFormat("#.##");

    Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd  hh:mm:ss aa");
    String todayString = formatter1.format(currentDate);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_detail_print);
        txtgroup = (TextView) findViewById(R.id.txtGroup);
        txtcode = (TextView) findViewById(R.id.txtCode);
        txtname = (TextView) findViewById(R.id.txtName);
        txttype = (TextView) findViewById(R.id.txtType);
        textValue = (TextView) findViewById(R.id.textValue);
        txtTotals = (TextView) findViewById(R.id.txtTotals);
        btndtfrom = (Button) findViewById(R.id.btndtFrom);
        btndtto = (Button) findViewById(R.id.btndtTo);
        llTotalText = (LinearLayout) findViewById(R.id.lltotal);
        comName     =  (TextView) findViewById(R.id.comName);
        comAddress  =(TextView)findViewById(R.id.comAddress);
        comContact=(TextView)findViewById(R.id.comContact);
        todayDate=(TextView)findViewById(R.id.toDayDate);

        llTotalText.setVisibility(View.GONE);




        comName.setText(MainActivity.cname);
        comAddress.setText(MainActivity.caddress);
        comContact.setText(MainActivity.ccontact);




        Log.d(TAG,"today date"+todayString);
        todayDate.setText(""+todayString);


        final String StringPrint=getIntent().getStringExtra("StringPrint");
        final String ImagePrint=getIntent().getStringExtra("ImagePrint");



        //   setDynamicHeight(listView);
        //   cname = (TextView)findViewById(R.id.cname);
        //   caddress=(TextView)findViewById(R.id.caddress);
        //   ccontact=(TextView)findViewById(R.id.ccontact);

        //    lldistance =(LinearLayout)findViewById(R.id.lldistance);
        //    VoucherDate=(TextView)findViewById(R.id.VoucherDate);
        //    opening = (TextView) findViewById(R.id.opening);
        //    vtotalamount=(TextView)findViewById(R.id.vtotalamount);

        //    lldistance.setVisibility(View.GONE);


        vrno = findViewById(R.id.vrno);
        totalAmount = findViewById(R.id.totalAmount);
        bs =MyApplication.getMmBluetoothSocket();
      /*  if (bs==null){
            bluetoothtxt.setText(getResources().getString(R.string.bluetooth_text1));
        }else{
            bluetoothtxt.setText(getResources().getString(R.string.bluetooth_text2));
        }*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new DB_Controller(ReportDetailActivity.this);


        findById();
        codeTypes = new ArrayList<>();
        codeTypes.add("Select");
        codeTypes.add("Item");
        codeTypes.add("Customer");
        codeTypes.add("Supplier");
        codeTypes.add("Profit by voucher");
        /* codeTypes.add("Purchase Reports");*/
        codeTypes.add("Profit by item (Qty A_Z)");
        codeTypes.add("Profit by item (Qty Z_A)");
        codeTypes.add("Profit by item (Amt A_Z)");
        codeTypes.add("Profit by item (Amt Z_A)");
        codeTypes.add("Profit by item (Profit A_Z)");
        codeTypes.add("Profit by item (Profit Z_A)");






        Cursor data = db.select_distinct();
        if (data.moveToFirst()){
            do {
                codeTypes.add(data.getString(0)+" "+data.getString(1));
            }while (data.moveToNext());

            System.out.println("~~~~~~~~~~~~~"+codeAndName);

        }
        data.close();

        SharedPreferences sharedPreferences1 = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        printSize = sharedPreferences1.getInt("printer_size", 0);
        myCodeTypeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter code_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, codeTypes);
        code_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myCodeTypeSpinner.setAdapter(code_adapter);

        final NestedScrollView idlayout=(NestedScrollView)findViewById(R.id.mainlayoutId);
        myPrintBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ManagementActivity.checkedtrue==true){
                    if (printSize==3){
                        printStringSize3();
                        finish();
                    }else if (printSize==4){
                        printStringSize4();
                        finish();
                    }

                }else {
                    if (printSize==3){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(ReportDetailActivity.this,bitmap);
                        printImageSize3();
                        finish();
                    }else if (printSize==4){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(ReportDetailActivity.this,bitmap);
                        printImageSize4();
                        finish();
                    }
                }
               /* if (printSize==3){
                    Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                    fileSaveSize3(ReportDetailActivity.this,bitmap);
                    printImageSize3();
                    finish();
                }else if (printSize==4){
                    Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                    fileSaveSize4(ReportDetailActivity.this,bitmap);
                    printImageSize4();
                    finish();
                }*/



            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnDtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ReportDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        btnDtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ReportDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener1, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                day = selectedDay;
                month = selectedMonth + 1;
                year = selectedYear;

                if (day < 10 && month < 10) {
                    startDate = year + "/0" + month + "/0" + day;
                    remainStartDate = year + "/0" + month + "/0" + day;
                } else if (day > 10 && month < 10) {
                    startDate = year + "/0" + month + "/" + day;
                    remainStartDate = year + "/0" + month + "/" + day;
                } else if (day < 10 && month > 10) {
                    startDate = year + "/" + month + "/0" + day;
                    remainStartDate = year + "/" + month + "/0" + day;
                } else {
                    startDate = year + "/" + month + "/" + day;
                    remainStartDate = year + "/" + month + "/" + day;
                }

                btnDtFrom.setText(startDate);
            }
        };
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                day = selectedDay;
                month = selectedMonth + 1;
                year = selectedYear;

                if (day < 10 && month < 10) {
                    endDate = year + "/0" + month + "/0" + day;
                    remainEndDate = year + "/0" + month + "/0" + day;
                } else if (day > 10 && month < 10) {
                    endDate = year + "/0" + month + "/" + day;
                    remainEndDate = year + "/0" + month + "/" + day;
                } else if (day < 10 && month > 10) {
                    endDate = year + "/" + month + "/0" + day;
                    remainEndDate = year + "/" + month + "/0" + day;
                } else {
                    endDate = year + "/" + month + "/" + day;
                    remainEndDate = year + "/" + month + "/" + day;
                }
                btnDtTo.setText(endDate);
            }
        };
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerText=myCodeTypeSpinner.getSelectedItem().toString();
        if (spinnerText.equalsIgnoreCase("Select")) {
            codeTypeString = null;
            //    llTotalText.setVisibility(View.GONE);
        }else if (spinnerText.equals("Item")) {
            codeTypeString="I";
            txtgroup.setText("Name");
            txtcode.setText("Group");
            txtname.setText("Code");
            txttype.setText("Balance");
            //    lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.VISIBLE);
            myToDetailCode.setVisibility(View.VISIBLE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.GONE);
            btnDtTo.setVisibility(View.GONE);
            //    llTotalText.setVisibility(View.GONE);
            txtTotals.setVisibility(View.GONE);
            textValue.setVisibility(View.GONE);


        }else if (spinnerText.equals("Customer")){
            codeTypeString  =  "C";
            txtgroup.setText("Group");
            txtcode.setText("Code");
            txtname.setText("Name");
            txttype.setText("Balance");
            //     lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.VISIBLE);
            myToDetailCode.setVisibility(View.VISIBLE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.GONE);
            btnDtTo.setVisibility(View.GONE);
            //    llTotalText.setVisibility(View.GONE);
            txtTotals.setVisibility(View.GONE);
            textValue.setVisibility(View.GONE);
        }else if (spinnerText.equals("Supplier")){
            codeTypeString  =  "V";
            txtgroup.setText("Group");
            txtcode.setText("Code");
            txtname.setText("Name");
            txttype.setText("Balance");
            //     lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.VISIBLE);
            myToDetailCode.setVisibility(View.VISIBLE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.GONE);
            btnDtTo.setVisibility(View.GONE);
            //    llTotalText.setVisibility(View.GONE);
            txtTotals.setVisibility(View.GONE);
            textValue.setVisibility(View.GONE);
        }
        else if (spinnerText.equals("Profit by voucher")){


            llTotalText.setVisibility(View.VISIBLE);


            codeTypeString  =  "Pv";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("VrNo");
            txtcode.setText("SaleAmt");
            txtname.setText("Discount");
            txttype.setText("Profit");
            //   lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");


            txtTotals.setText("Total Vr          \n\nTotal SaleAmt               \n\nTotal Discount            \n\nTotal Profits ");
        }else if (spinnerText.equals("Profit by item (Qty A_Z)")){
            codeTypeString  =  "QtyAZ";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //   lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
            txtTotals.setText("Total Item          \n\nTotal Qty                \n\nTotal SaleAmt            \n\nTotal Profits ");
        }else if (spinnerText.equals("Profit by item (Qty Z_A)")){
            codeTypeString  =  "QtyZA";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //   lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
            txtTotals.setText("Total Item          \n\nTotal Qty                \n\nTotal SaleAmt            \n\nTotal Profits ");
        }else if (spinnerText.equals("Profit by item (Amt A_Z)")){
            codeTypeString  =  "AmtAZ";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //    lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
        }else if (spinnerText.equals("Profit by item (Amt Z_A)")){
            codeTypeString  =  "AmtZA";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //   lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
        }else if (spinnerText.equals("Profit by item (Profit A_Z)")){
            codeTypeString  =  "ProfitAZ";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //    lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
        }else if (spinnerText.equals("Profit by item (Profit Z_A)")) {
            codeTypeString = "ProfitZA";
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("Name");
            txtcode.setText("Qty");
            txtname.setText("SaleAmt");
            txttype.setText("Profit");
            //    lldistance.setVisibility(View.GONE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.VISIBLE);
        }else {
            String temp = spinnerText;
            char[] myCharArray = temp.toCharArray();
            String Acode ="";
            for (int i = 0; i < myCharArray.length; i++) {
                if (String.valueOf(myCharArray[i]).equals(" ")) {
                    break;
                } else {
                    Acode += String.valueOf(myCharArray[i]);
                }
            }
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%" + Acode);
            codeTypeString=Acode;

            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            txtgroup.setText("VDate");
            txtcode.setText("VrNO");
            txtname.setText("");
            txttype.setText("Amount");
            //    lldistance.setVisibility(View.VISIBLE);
            myFromDetailCode.setVisibility(View.GONE);
            myToDetailCode.setVisibility(View.GONE);
            linearLayoutFrom.setVisibility(View.VISIBLE);
            linearLayoutTo.setVisibility(View.VISIBLE);
            btnDtFrom.setVisibility(View.VISIBLE);
            btnDtTo.setVisibility(View.VISIBLE);
            btndtfrom.setText(formatter.format(today) + "");
            btndtto.setText(formatter.format(today) + "");
            llTotalText.setVisibility(View.GONE);
            txtTotals.setVisibility(View.GONE);
            textValue.setVisibility(View.GONE);
        }
        selectData();
    }

    private void selectData() {

        detailCodeFrom = myFromDetailCode.getText().toString();
        detailCodeTo = myToDetailCode.getText().toString();




        if (codeTypeString == null || detailCodeFrom == null || detailCodeFrom == null) {
            Toast.makeText(getBaseContext(), "Select Filter", Toast.LENGTH_SHORT).show();
            return;
        }


        if (detailCodeFrom.equals("")) {
            detailCodeFrom = null;
        }
        if (detailCodeTo.equals("")) {
            detailCodeTo = null;
        }
        if (startDate == "") {
            startDate = formatter.format(today);
        }
        if (endDate == "") {
            endDate = formatter.format(today);
        }
        Cursor data = db.selectReportByCodeType(codeTypeString, detailCodeFrom, detailCodeTo, startDate, endDate);
        if (data.moveToFirst()) {
            list = new ArrayList<>();
            TSAmt = 0.0;
            TProfit = 0.0;
            TQty = 0.0;
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(3);
            do {
                int columnIndex = data.getColumnIndex("GroupCode");
                String group = data.getString(columnIndex);
                columnIndex = data.getColumnIndex("DetailCode");
                String code = data.getString(columnIndex);
                columnIndex = data.getColumnIndex("Name");
                String name = data.getString(columnIndex);
                columnIndex = data.getColumnIndex("Unit");
                String type = data.getString(columnIndex);

                reportListData = new ReportListData();
                reportListData.setGroup(group);
                reportListData.setCode(code);
                reportListData.setName(name);
                reportListData.setType(type);

                if(codeTypeString.equals("Pv") ||/* codeTypeString.equals("PR") ||*/
                        codeTypeString.equals("AmtAZ") || codeTypeString.equals("QtyAZ") ||
                        codeTypeString.equals("AmtZA") || codeTypeString.equals("QtyZA") ||
                        codeTypeString.equals("ProfitZA") || codeTypeString.equals("ProfitAZ")) {

                    TQty += Double.valueOf(code);
                    TSAmt += Double.valueOf(name);
                    TProfit += Double.valueOf(type);

                    reportListData.setCode(df.format(Double.valueOf(code)));
                    reportListData.setName(df.format(Double.valueOf(name)));
                    reportListData.setType(df.format(Double.valueOf(type)));

                }
                list.add(reportListData);

            } while (data.moveToNext());

            if (codeTypeString != ("I") || codeTypeString != ("C")) {
                textValue.setText(list.size() + "\n\n" + df.format(TQty) + "\n\n" + df.format(TSAmt) + "\n\n" + df.format(TProfit));
            }

            prepareData();


        }

    }
    private void prepareData() {
        vAdapter = new VoucherDetailAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);
    }


    public void onClickView(View view) {

        selectData();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void printImageSize3() {
        Thread thread = new Thread() {
            public void run() {
                try {
                   // os = MyApplication.getMmOutputStream();
                    String address=MyApplication.getmDeviceAddress();
                    Pointer h = Pointer.NULL;
                    String msg="";
                    //Only thermal printer
                    //h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    if (address.equals("")){
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA("DC:0D:30:70:87:53");
                    }else {
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    }
                    if (h != Pointer.NULL) {

                        // Query Status
                        int status = printerlibs_caysnpos.INSTANCE.CaysnPos_QueryPrinterStatus(h, 3000);
                        if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_QUERYFAILED(status)) {
                            msg += "Query Printer Status Failed\r\n";
                        } else {
                            if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_OFFLINE(status)) { // Status Error
                                msg += "Printer offline\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_COVERUP(status))
                                    msg += "Cover up\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_NOPAPER(status))
                                    msg += "No paper\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_ERROR_OCCURED(status))
                                    msg += "Error occured\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_CUTTER_ERROR(status))
                                    msg += "Cutter error\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_UNRECOVERABLE_ERROR(status))
                                    msg += "Unrecoverable error\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_DEGREE_OR_VOLTAGE_OVERRANGE(status))
                                    msg += "Degree or voltage overrange\r\n";
                            } else { // Status OK
                                // Environment.getExternalStorageDirectory() + File.separator + "saved_images"
                                File f=new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg");
                                System.out.println("FileFleFle8888"+f);
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                                int result = 0xFFFFFFFF;
                                result &= printerlibs_caysnpos.CaysnPos_PrintRasterImage_Helper.CaysnPos_PrintRasterImageFromBitmap(h, bitmap.getWidth(), bitmap.getHeight(), bitmap, printerlibs_caysnpos.ImageBinarizationMethod_Thresholding);
                                result &= printerlibs_caysnpos.INSTANCE.CaysnPos_FeedAndHalfCutPaper(h);
                                if (result != 0) { // Data Sended To Printer
                                    // Check Print Result
                                    result = printerlibs_caysnpos.INSTANCE.CaysnPos_QueryPrintResult(h, 30000);
                                    switch (result) {
                                        case printerlibs_caysnpos.PL_PRINTRESULT_SUCCESS:
                                            msg = "Print success";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_CLOSED:
                                            msg = "Port closed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_WRITEFAILED:
                                            msg = "Write failed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_READFAILED:
                                            msg = "Read failed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PRINTER_OFFLINE:
                                            msg = "Printer offline";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PRINTER_NOPAPER:
                                            msg = "No paper";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_OTHER_RERROR:
                                        default:
                                            msg = "Print failed";
                                            break;
                                    }
                                } else {
                                    msg += "CaysnPos_PrintRasterImageFromBitmap Failed\r\n";
                                }

                            }
                        }

                        // Close Port
                        printerlibs_caysnpos.INSTANCE.CaysnPos_Close(h);
                    } else {
                        msg += "Open Failed\r\n";
                    }
                    Toast.makeText(getApplicationContext(),"Printer Message"+msg,Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);

                }
            }
        };
        thread.start();

    }
    private void printStringSize3() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    reportString = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    reportString += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    reportString += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    reportString += String.format("%1$-22s %2$10s"," " ,(todayString))+ "\n";



                    reportString += "------------------------------------------------\n";

                    if (codeTypeString.equals("I") || codeTypeString.equals("C") ) {
                        reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", "Group", "Code", "Name", "Type" + "\n");
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                    }else if (codeTypeString.equals("Pv")) {
                        reportString += String.format("%1$-15s %2$10s %3$10s %4$10s", "Vrno", "SaleAmt", "Discount", "Profit" + "\n");
                        //reportString += "-----------------------------------------------------------------------------------------\n";
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                        reportString += "------------------------------------------------\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Vr",":", list.size()) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total SaleAmt",":", TQty) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Discount",":", TSAmt) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Profit",":", TProfit) + "\n\n";
                        reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n";
                        reportString = reportString + " ";
                    }else if (codeTypeString.equals("AmtAZ")    || codeTypeString.equals("QtyAZ")    ||
                            codeTypeString.equals("AmtZA")    || codeTypeString.equals("QtyZA")    ||
                            codeTypeString.equals("ProfitZA") || codeTypeString.equals("ProfitAZ"))  {
                        reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", "Name", "Qty", "SaleAmt", "Profit" + "\n");
                        //  reportString += "-------------------------------------------------------------------------------------------\n";
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                        reportString += "------------------------------------------------\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Item",":", list.size()) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Qty",":", TQty) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total SaleAmt",":", TSAmt) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Profits",":", TProfit) + "\n\n";

                        reportString = reportString + "----------------------Thank you-------------------\n\n";

                    }
                    System.out.print(reportString);
                    os.write(reportString.getBytes());

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
        thread.start();
    }
    private void printImageSize4() {
        Thread thread = new Thread() {
            public void run() {
                try {
                   // os = MyApplication.getMmOutputStream();
                    String address=MyApplication.getmDeviceAddress();
                    Pointer h = Pointer.NULL;
                    String msg="";
                    //Only thermal printer
                    // h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    if (address.equals("")){
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA("DC:0D:30:70:87:53");
                    }else {
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    }

                    if (h != Pointer.NULL) {

                        // Query Status
                        int status = printerlibs_caysnpos.INSTANCE.CaysnPos_QueryPrinterStatus(h, 3000);
                        if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_QUERYFAILED(status)) {
                            msg += "Query Printer Status Failed\r\n";
                        } else {
                            if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_OFFLINE(status)) { // Status Error
                                msg += "Printer offline\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_COVERUP(status))
                                    msg += "Cover up\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_NOPAPER(status))
                                    msg += "No paper\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_ERROR_OCCURED(status))
                                    msg += "Error occured\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_CUTTER_ERROR(status))
                                    msg += "Cutter error\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_UNRECOVERABLE_ERROR(status))
                                    msg += "Unrecoverable error\r\n";
                                if (printerlibs_caysnpos.PL_PRINTERSTATUS_Helper.PL_PRINTERSTATUS_DEGREE_OR_VOLTAGE_OVERRANGE(status))
                                    msg += "Degree or voltage overrange\r\n";
                            } else { // Status OK
                                // Environment.getExternalStorageDirectory() + File.separator + "saved_images"
                                File f=new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg");
                                System.out.println("FileFleFle8888"+f);
                                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                                int result = 0xFFFFFFFF;
                                result &= printerlibs_caysnpos.CaysnPos_PrintRasterImage_Helper.CaysnPos_PrintRasterImageFromBitmap(h, bitmap.getWidth(), bitmap.getHeight(), bitmap, printerlibs_caysnpos.ImageBinarizationMethod_Thresholding);
                                result &= printerlibs_caysnpos.INSTANCE.CaysnPos_FeedAndHalfCutPaper(h);
                                if (result != 0) { // Data Sended To Printer
                                    // Check Print Result
                                    result = printerlibs_caysnpos.INSTANCE.CaysnPos_QueryPrintResult(h, 30000);
                                    switch (result) {
                                        case printerlibs_caysnpos.PL_PRINTRESULT_SUCCESS:
                                            msg = "Print success";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_CLOSED:
                                            msg = "Port closed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_WRITEFAILED:
                                            msg = "Write failed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PORT_READFAILED:
                                            msg = "Read failed";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PRINTER_OFFLINE:
                                            msg = "Printer offline";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_PRINTER_NOPAPER:
                                            msg = "No paper";
                                            break;
                                        case printerlibs_caysnpos.PL_PRINTRESULT_OTHER_RERROR:
                                        default:
                                            msg = "Print failed";
                                            break;
                                    }
                                } else {
                                    msg += "CaysnPos_PrintRasterImageFromBitmap Failed\r\n";
                                }
                            }
                        }

                        // Close Port
                        printerlibs_caysnpos.INSTANCE.CaysnPos_Close(h);
                    } else {
                        msg += "Open Failed\r\n";
                    }

                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        thread.start();
    }
    private void printStringSize4() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    reportString = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    reportString += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    reportString += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    reportString += String.format("%1$-22s %2$10s"," " ,(todayString))+ "\n";
                    reportString += "------------------------------------------------\n";

                    if (codeTypeString.equals("I") || codeTypeString.equals("C") ) {
                        reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", "Group", "Code", "Name", "Type" + "\n");
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                    }else if (codeTypeString.equals("Pv")) {
                        reportString += String.format("%1$-15s %2$10s %3$10s %4$10s", "Vrno", "SaleAmt", "Discount", "Profit" + "\n");
                        //reportString += "-----------------------------------------------------------------------------------------\n";
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                        reportString += "------------------------------------------------\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Vr",":", list.size()) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total SaleAmt",":", TQty) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Discount",":", TSAmt) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Profit",":", TProfit) + "\n\n";
                        reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n";
                        reportString = reportString + " ";
                    }else if (codeTypeString.equals("AmtAZ")    || codeTypeString.equals("QtyAZ")    ||
                            codeTypeString.equals("AmtZA")    || codeTypeString.equals("QtyZA")    ||
                            codeTypeString.equals("ProfitZA") || codeTypeString.equals("ProfitAZ"))  {
                        reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", "Name", "Qty", "SaleAmt", "Profit" + "\n");
                        //  reportString += "-------------------------------------------------------------------------------------------\n";
                        reportString += "------------------------------------------------\n";
                        for (int i = 0; i < list.size(); i++) {
                            String aa= list.get(i).getGroup();
                            String bb=list.get(i).getGroup();
                            int phraseLength = aa.length();
                            String cc="";
                            String dd=" ";
                            String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                            if (aa.length()>=20 && bb.length()>=20){
                                String temp =aa.substring(aa.length() - 1);
                                aa=aa.substring(20,phraseLength);
                                bb=bb.substring(1,19);
                                cc=bb+"\n"+aa;

                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", cc, list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                            }else {
                                reportString += String.format("%1$-10s %2$10s %3$10s %4$10s", list.get(i).getGroup(), list.get(i).getCode(), list.get(i).getName(), list.get(i).getType() + "\n");
                                reportString = reportString + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                                reportString = reportString + " ";
                            }
                        }

                        reportString += "------------------------------------------------\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Item",":", list.size()) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Qty",":", TQty) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total SaleAmt",":", TSAmt) + "\n";
                        reportString = reportString + String.format("%1$-30s %2$5s %3$10s", "Total Profits",":", TProfit) + "\n\n";

                        reportString = reportString + "----------------------Thank you-------------------\n\n";
                        reportString = reportString + " ";
                    }
                    System.out.print(reportString);
                    os.write(reportString.getBytes());

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
        thread.start();
    }




    private int intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }
    @SuppressLint("WrongViewCast")
    private void findById() {
        myCodeTypeSpinner = findViewById(R.id.id_spinner);
        myFromDetailCode = findViewById(R.id.id_edt_from);
        myToDetailCode = findViewById(R.id.id_edt_to);
        myPrintBt = findViewById(R.id.printReport);
        cancel    = findViewById(R.id.cancelPrint);

        linearLayoutFrom = findViewById(R.id.lineatLayoutFrom);
        linearLayoutTo = findViewById(R.id.linearLayoutTo);
        btnDtFrom = findViewById(R.id.btndtFrom);
        btnDtTo = findViewById(R.id.btndtTo);
        //  myList = findViewById(R.id.id_my_list_report1);

        //  lldistance=findViewById(R.id.lldistance);
        recyclerView=findViewById(R.id.recycler_view);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }

    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    public void fileSaveSize3(Context context,Bitmap bitmap){
        //    Bitmap tzb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);
        int nh = (int) ( bitmap.getHeight() * (550.0 / bitmap.getWidth()) );
        Bitmap tzb = Bitmap.createScaledBitmap(bitmap, 550, nh, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }

        //File myDir = new File(root + "/saved_images");
        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
       /* int n = 10000;
        n = generator.nextInt(n);*/
        String fname = "Image.jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();

        try {
            //filePath.createNewFile();
            FileOutputStream oStream = new FileOutputStream(file);
            tzb.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,file.getAbsolutePath());


    }

    public void fileSaveSize4(Context context,Bitmap bitmap){
        //    Bitmap tzb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);
        int nh = (int) ( bitmap.getHeight() * (575.0 / bitmap.getWidth()) );
        Bitmap tzb = Bitmap.createScaledBitmap(bitmap, 575, nh, true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }

        //File myDir = new File(root + "/saved_images");
        File myDir = new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
       /* int n = 10000;
        n = generator.nextInt(n);*/
        String fname = "Image.jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();

        try {
            //filePath.createNewFile();
            FileOutputStream oStream = new FileOutputStream(file);
            tzb.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,file.getAbsolutePath());


    }
    public static void setDynamicHeight(ListView listView){
        ListAdapter adapter=listView.getAdapter();
        if (adapter==null){
            return;
        }
        int height=0;
        int desiredWidth=View.MeasureSpec.makeMeasureSpec(listView.getWidth(),View.MeasureSpec.UNSPECIFIED);
        for (int i=0;i<adapter.getCount();i++){
            View listItem=adapter.getView(i,null,listView);
            listItem.measure(desiredWidth,View.MeasureSpec.UNSPECIFIED);
            height+=listItem.getMeasuredHeight();

        }
        ViewGroup.LayoutParams layoutParams=listView.getLayoutParams();
        layoutParams.height=height+(listView.getDividerHeight()*(adapter.getCount()-1));
        listView.requestLayout();
    }
}