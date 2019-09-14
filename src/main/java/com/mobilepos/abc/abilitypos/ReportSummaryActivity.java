package com.mobilepos.abc.abilitypos;
import android.Manifest;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Adapter.SaleListAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.Model.StringUtilis;
import com.mobilepos.abc.abilitypos.PrintService.UnicodeFormatter;
import com.sun.jna.Pointer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
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


public class ReportSummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    ListView listView;
    protected static final String TAG = "TAG";
    TextView VoucherN0,Total,Receive,vrno,totalAmount,txtTotalAmount,txtTotalReceive,totalAmounttext,comName,comAddress,comContact,todayDate;
    static int pos,positionCount;
    String voucherNo = "",detailName = "",detailQty = "",detailP = "",detailAmt = "",tax = "",discount = "",total = "",netTotals = "",pays = "",refund = "",cusName = "",remark = "";
    String voucherType = "",startDate = "", endDate = "", bill = "", name = "", dateSelected = "", headerDate = "", voucherHeader = "";
    Double netTotal = 0.0, credits = 0.0, pay = 0.0,profit=0.0;
    public static ArrayList<DataEntry> voucherEntry = new ArrayList<>();
    private ArrayList<String> voucherNoList;
    private int day,dayOfMonth,month,year;
    OutputStream os;
    BluetoothSocket bs;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    public static ArrayList<DataEntry> dataEntries = new ArrayList<>();
    public static DataEntry dataEntry;
    SaleListAdapter adapter;
    Button print,cancel,selectDate;
    DB_Controller controller;
    List<String> categories,dateData;
    Cursor data = null;
    Spinner dateType,spinner;
    int printSize;
    DecimalFormat f=new DecimalFormat("#.##");

    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  hh:mm:ss aa");
    String todayString = formatter.format(currentDate);


    Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
    String today1=formatter1.format(today);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_summary_print);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences1 = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        Calendar cal = Calendar.getInstance();



        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        findId();
        bs =MyApplication.getMmBluetoothSocket();
        /*if (bs==null){
            BluetoothText.setText(getResources().getString(R.string.bluetooth_text1));
        }else{
            BluetoothText.setText(getResources().getString(R.string.bluetooth_text2));
        }*/


        comName.setText(MainActivity.cname);
        comAddress.setText(MainActivity.caddress);
        comContact.setText(MainActivity.ccontact);


        Log.d(TAG,"today date"+todayString);
        todayDate.setText(""+todayString);



        final String StringPrint=getIntent().getStringExtra("StringPrint");
        final String ImagePrint=getIntent().getStringExtra("ImagePrint");


        if (SaleItemFragment.check.equals("1") || SaleItemFragment.check.equals("2")){
            txtTotalAmount.setText("TotalAmount");
            txtTotalReceive.setText("Total Receive");
            totalAmounttext.setText("Total Amount\n\n           Receive\n\n           Balance");
        }else if ( SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            txtTotalAmount.setText("CusName");
            txtTotalReceive.setText("TotalAmount");
            totalAmounttext.setText("Total Amount");
        }

        printSize = sharedPreferences1.getInt("printer_size", 0);
        controller = new DB_Controller(ReportSummaryActivity.this);


        /*if (MyApplication.getmMenuItem()==R.id.daily_sale){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_sale)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_sale));
            }
        }else if (MyApplication.getmMenuItem()==R.id.daily_purchase){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_purchase)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_purchase));
            }
        }else if (MyApplication.getmMenuItem()==R.id.daily_sale_debt){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_sale_debt)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_sale_debt));
            }
        }else if (MyApplication.getmMenuItem()==R.id.daily_purchase_debt){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_purchase_dept)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_purchase_dept));
            }
        }else if (MyApplication.getmMenuItem()==R.id.daily_cash_receive){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_receive)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_cash_receive));
            }
        }else if (MyApplication.getmMenuItem()==R.id.daily_cash_payment){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_payment)));
            }else {
                this.setTitle(getResources().getString(R.string.menu_report_cash_payment));
            }
        }else if (MyApplication.getmMenuItem()==R.id.ledger_adjustment_report){
            if (MDetect.INSTANCE.isUnicode()){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.ledger_adjustment_report)));
            }else {
                this.setTitle(getResources().getString(R.string.ledger_adjustment_report));
            }
        }*/

        if (MDetect.INSTANCE.isUnicode()) {
            print.setText(Rabbit.zg2uni(getResources().getString(R.string.print)));

            }if (SaleItemFragment.check.equals("1")) {
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_sale)));
            }else if (SaleItemFragment.check.equals("2")){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_purchase)));
            }else if (SaleItemFragment.check.equals("0")){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_receive)));
            }else if (SaleItemFragment.check.equals("3")){
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_payment)));
            }
            else {
            print.setText(getResources().getString(R.string.print));
            if (SaleItemFragment.check.equals("1")) {
                this.setTitle(getResources().getString(R.string.menu_report_sale));
            }else if (SaleItemFragment.check.equals("2")){
                this.setTitle(getResources().getString(R.string.menu_report_purchase));
            }else if (SaleItemFragment.check.equals("0")){
                this.setTitle(getResources().getString(R.string.menu_report_cash_receive));
            }else if (SaleItemFragment.check.equals("3")){
                this.setTitle(getResources().getString(R.string.menu_report_cash_payment));
            }
        }

        final   NestedScrollView idlayout=(NestedScrollView)findViewById(R.id.mainlayoutId);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManagementActivity.checkedtrue==true){
                    if (printSize==3){
                        printStringJobSize3();
                        finish();

                    }else if (printSize==4){
                        printStringJobSize4();
                        finish();
                    }

                }else {
                    if (printSize==3){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(ReportSummaryActivity.this,bitmap);
                        printImageJobSize3();
                        finish();

                    }else if (printSize==4){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(ReportSummaryActivity.this,bitmap);
                        printImageJobSize4();
                        finish();

                    }
                }
               /* if (printSize==3){
                    Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                    fileSaveSize3(ReportSummaryActivity.this,bitmap);
                    printImageJobSize3();
                    finish();

                }else if (printSize==4){
                    Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                    fileSaveSize4(ReportSummaryActivity.this,bitmap);
                    printImageJobSize4();
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


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        categories = new ArrayList<String>();
        data=controller.select_Customer();
        categories.add("All");
      //  categories.add(data.getString(0));
        if (data.moveToFirst()) {
            do {
                categories.add(data.getString(0));
            } while (data.moveToNext());
        }


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //Date Spinner Adding String
        dateData = new ArrayList<String>();
        dateData.add("Daily");
        dateData.add("Weekly");
        dateData.add("Monthly");
        dateData.add("Yearly");
        dateData.add("Quarterly");

        dateType = (Spinner) findViewById(R.id.dateType);
        dateType.setOnItemSelectedListener(this);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, dateData);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateType.setAdapter(dateAdapter);

        listView.setOnItemClickListener(this);



        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ReportSummaryActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
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
                } else if (day > 10 && month < 10) {
                    startDate = year + "/0" + month + "/" + day;
                } else if (day < 10 && month > 10) {
                    startDate = year + "/" + month + "/0" + day;
                } else {
                    startDate = year + "/" + month + "/" + day;
                }
                if (dateSelected.equals("Daily")) {
                    if (day < 10 && month < 10) {
                        headerDate = year + "/0" + month + "/0" + day;
                    } else if (day > 10 && month < 10) {
                        headerDate = year + "/0" + month + "/" + day;
                    } else if (day < 10 && month > 10) {
                        headerDate = year + "/" + month + "/0" + day;
                    } else {
                        headerDate = year + "/" + month + "/" + day;
                    }
                    daily(headerDate);
                } else if (dateSelected.equals("Weekly")) {
                    weekly(day, month, year);
                } else if (dateSelected.equals("Monthly")) {
                    monthly(month, year);
                } else if (dateSelected.equals("Yearly")) {
                    yearly(year);
                } else {
                    quartly(month, year);
                }
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DATE);
        startDate = year + "/" + month + "/0" + dayOfMonth;
        Spinner spinner = (Spinner) parent;
        voucherType = "1";

        if (spinner.getId() == R.id.userSelect) {


            cusName = categories.get(position);
            if(position==0) {
                cusName = categories.get(position).toString();
                System.out.println("*********************" + cusName);
                //selectDate.setEnabled(true);
            }else{
                cusName = categories.get(position).toString();
                System.out.println("*********************" + cusName);
                //selectDate.setEnabled(true);
            }
//uts
            if (cusName!=null){
                selectDate.setText(today1);
            }else {
                selectDate.setText(startDate);

            }


            //selectDate.setText(startDate);
            System.out.println(">>>>>>>>>"+startDate);
            data=controller.select_DV_detailHBy(startDate,startDate,voucherType,cusName,today1);
            select();

        } else if (spinner.getId() == R.id.dateType) {
            dateSelected = dateData.get(position);
            System.out.println("<<<<<<<<<<<<<<<<<<"+dateData.get(position).toString());
            if (position == 0) {
                if (dayOfMonth < 10 && month < 10) {
                    startDate = year + "/0" + month + "/0" + dayOfMonth;
                } else if (dayOfMonth > 10 && month < 10) {
                    startDate = year + "/0" + month + "/" + dayOfMonth;
                } else if (dayOfMonth < 10 && month > 10) {
                    startDate = year + "/" + month + "/0" + dayOfMonth;
                } else {
                    startDate = year + "/" + month + "/" + dayOfMonth;
                }
                selectDate.setText(startDate);
                daily(startDate);
            } else if (position == 1) {
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH) + 1;
                int d = calendar.get(Calendar.DATE);
                weekly(d, m, y);
               // weeklyHeader(d, m, y);
            } else if (position == 2) {
                monthly(month, year);
            } else if (position == 3) {
                yearly(year);
            } else {
                quartly(month, year);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        detailName = "";
        detailQty = "";
        detailAmt = "";
        detailP ="";
        Double SumAmt=0.0;
        pos = 0;
        positionCount = 0;
        positionCount = parent.getCount();
        pos = position;


        DecimalFormat df=new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
        f.setRoundingMode(RoundingMode.CEILING);

// Get Code and All detailVr
        final String code = dataEntries.get(position).getVoucherNo();
        Cursor data = controller.all_detailVr(code);
        voucherEntry.clear();
        int numRow = data.getCount();

        voucherNo = "Voucher No:" + code;

        if (numRow == 0) {

        } else {

            while (data.moveToNext()) {
                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){


                    detailName += data.getString(1) + "\n\n";
                   // detailQty += f.format(Double.valueOf(data.getString(5)) * (-1)) + "\n\n";
                    detailP += df.format(Double.valueOf(data.getString(4))) + "\n\n";
                    detailAmt += df.format(Double.valueOf(data.getString(6))) + "\n\n";


                    //   totalPrice+=data.getDouble(6);


                    SumAmt +=Double.valueOf(data.getString(6));

                    dataEntry = new DataEntry();
                    dataEntry.setItemName(data.getString(1));           //name
                   // dataEntry.setItemQty(data.getDouble(5) * (-1));             //qty
                    dataEntry.setItemSPrice(data.getDouble(4));        //Price
                    dataEntry.setChangePrice(data.getDouble(6));       //Amount
               //     dataEntry.setItemTotalPrice(data.);
                    voucherEntry.add(dataEntry);

                }else {
                    if(data.getString(1).length()>=10){
                        String aa,bb,cc,dd;
                        detailName += data.getString(1) + "\n\n";
                        detailQty += f.format(Double.valueOf(data.getString(5)) * (-1)) + "\n\n\n";
                        detailP += df.format(Double.valueOf(data.getString(4))) + "\n\n\n"; //Price
//                        aa=df.format(Double.valueOf(data.getString(3))); //PPrice
                        detailAmt +=df.format(Double.valueOf(data.getString(6)))+ "\n\n\n"; //Amount

                    }
                    else{
                        detailName += data.getString(1) + "\n\n";
                        detailQty += f.format(Double.valueOf(data.getString(5)) * (-1)) + "\n\n\n";
                        detailP += df.format(Double.valueOf(data.getString(4))) + "\n\n\n";
                        detailAmt += df.format(Double.valueOf(data.getString(6))) + "\n\n\n";
                    }

                    //   totalPrice+=data.getDouble(6);

                    SumAmt +=Double.valueOf(data.getString(6));

                    dataEntry = new DataEntry();
                    dataEntry.setItemName(data.getString(1));           //name
                    dataEntry.setItemQty(data.getDouble(5) * (-1));     //qty
                    dataEntry.setItemSPrice(data.getDouble(4));         //price
                    dataEntry.setChangePrice(data.getDouble(6));        //amount
                    voucherEntry.add(dataEntry);

                }

            }

        }

        Cursor datas = controller.all_detailH(code);
        int numRows = datas.getCount();

        if (numRows == 0) {

        } else {
            while (datas.moveToNext()) {
                cusName = datas.getString(2);
                tax = datas.getString(5);
                discount = datas.getString(4);
                //  total = datas.getString(7);
                total = String.valueOf(SumAmt);
                pays = datas.getString(8);
                refund = datas.getString(9);
                remark = datas.getString(11);
                //  netTotals = Integer.valueOf(datas.getString(7)) + Float.valueOf(datas.getString(5)) - Float.valueOf(datas.getString(4)) + "";
                netTotals = Double.valueOf(total) - Double.valueOf(datas.getString(5)) + Double.valueOf(datas.getString(4)) + "";
                //  netTotals=String.valueOf(total);
            }
            Intent intent = new Intent(ReportSummaryActivity.this, VoucherDetailActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putString("vrno", voucherNo);
            mBundle.putString("vrnos", code);
           /* mBundle.putString("name", detailName);
            mBundle.putString("qty", detailQty);
            mBundle.putString("amt", detailAmt);*/
            mBundle.putString("tax", tax);
            mBundle.putString("discount", discount);
            mBundle.putString("total", df.format(Double.valueOf(String.valueOf(total))));
            mBundle.putString("netTotal",df.format(Double.valueOf( netTotals)));
            mBundle.putString("pay", df.format(Double.valueOf(pays)));
            mBundle.putString("refund", refund);
            mBundle.putString("cusName", cusName);
            mBundle.putString("remark", remark);
            mBundle.putInt("pos", pos);
            mBundle.putInt("positionCount", positionCount);
            mBundle.putStringArrayList("voucherNoList", voucherNoList);


            intent.putExtras(mBundle);
            startActivity(intent);
            cusName="";
            remark="";
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void printImageJobSize3() {

        Thread t = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    String address=MyApplication.getmDeviceAddress();
                    Pointer h = Pointer.NULL;
                    String msg="";
                    //Only Thermal printer
                    //h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    if (address.equals("")){
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(PrintPreviewActivity.address1);
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
        t.start();
    }
    private void printStringJobSize3() {

        Thread t = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    bill = "";
                    /*bill = bill + "\n";
                    bill=bill+"          "+name+"\n ";
                    bill = bill + String.format("%1$25s", voucherHeader) + "\n";
                    bill = bill + String.format("%1$25s", headerDate) + "\n";
                    bill=bill+"         "+headerDate+"\n";*/
                    bill = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    bill += String.format("%1$-22s %2$10s"," " ,todayString) + "\n";
                    bill += "------------------------------------------------\n";

                    bill = bill + String.format("%1$-20s %2$5s %3$20s", "Voucher No"," ", "Amount") + "\n";
                    bill += "------------------------------------------------\n";
                    for (int i = 0; i < dataEntries.size(); i++) {
                        bill = bill + String.format("%1$-20s %2$5s %3$20s", dataEntries.get(i).getVoucherNo()," ", dataEntries.get(i).getItemPPrice()) + "\n";
                    }
                    bill += "------------------------------------------------\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Total Amount",":", netTotal) + "\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Receive",":", pay) + "\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Balance",":", credits) + "\n\n";

                    bill = bill + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                    bill = bill + " ";
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
    private void printImageJobSize4() {

       Thread t = new Thread() {
           public void run() {
               try {
                   os = MyApplication.getMmOutputStream();
                   String address=MyApplication.getmDeviceAddress();
                   Pointer h = Pointer.NULL;
                   String msg="";
                   //Only thermal Printer
                   //h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);

                   if (address.equals("")){
                       h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(PrintPreviewActivity.address1);
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
       t.start();
   }
    private void printStringJobSize4() {

        Thread t = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    /*bill = "";
                    bill = bill + "\n";
                    //  bill=bill+"          "+name+"\n ";
                    bill = bill + String.format("%1$25s", voucherHeader) + "\n";
                    bill = bill + String.format("%1$25s", headerDate) + "\n";
                    bill=bill+"         "+headerDate+"\n";*/
                    bill = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    bill += String.format("%1$-22s %2$10s"," " ,todayString) + "\n";
                    bill += "------------------------------------------------\n";
                    bill = bill + String.format("%1$-20s %2$5s %3$20s", "Voucher No"," ", "Amount") + "\n";
                    bill += "------------------------------------------------\n";
                    for (int i = 0; i < dataEntries.size(); i++) {
                        bill = bill + String.format("%1$-20s %2$5s %3$20s", dataEntries.get(i).getVoucherNo()," ", dataEntries.get(i).getItemPPrice()) + "\n";
                    }
                    bill += "------------------------------------------------\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Total Amount",":", netTotal) + "\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Receive",":", pay) + "\n";
                    bill = bill + String.format("%1$-30s %2$5s %3$10s", "Balance",":", credits) + "\n\n";

                    bill = bill + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                    bill = bill + " ";
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

    private void daily(String date) {
        selectDate.setText(date);
        System.out.println(">>>>>>>>>"+date);
        data=controller.select_DV_detailHBy(date,date,voucherType,cusName,today1);
        select();
    }


    private void weekly(int d, int m, int y) {
        switch (m) {
            case 1:
                dayOfMonth = 31;
                break;
            case 2:
                if (y % 4 == 0) {
                    dayOfMonth = 29;
                } else {
                    dayOfMonth = 28;
                }
                break;
            case 3:
                dayOfMonth = 31;
                break;
            case 4:
                dayOfMonth = 30;
                break;
            case 5:
                dayOfMonth = 31;
                break;
            case 6:
                dayOfMonth = 30;
                break;
            case 7:
                dayOfMonth = 31;
                break;
            case 8:
                dayOfMonth = 31;
                break;
            case 9:
                dayOfMonth = 30;
                break;
            case 10:
                dayOfMonth = 31;
                break;
            case 11:
                dayOfMonth = 30;
                break;
            case 12:
                dayOfMonth = 31;
                break;
        }

        int endDay = 6 + d;
        if (endDay <= dayOfMonth) {
            //    startDate = y + "/" + m + "/" + d ;
            //    dateFormat(endDay,m,y);
            if (endDay < 10 && m < 10) {
                endDate = y + "/0" + m + "/0" + endDay;
            }else if (m < 10){
                endDate = y + "/0" + m + "/" + endDay;
            }else if (d < 10){
                endDate = y + "/" + m + "/0" + endDay;
            }else {
                endDate = y + "/" + m + "/" + endDay;
            }

        } else {
            int em = m + 1;
            int ed = endDay - dayOfMonth;
            //    startDate = y + "/" + m + "/" + d ;

            // dateFormat(ed,em,y);
            if (ed < 10 && em < 10) {
                endDate = y + "/0" + em + "/0" + ed;
            }else if (m < 10){
                endDate = y + "/0" + em + "/" + ed;
            }else if (d < 10){
                endDate = y + "/" + em + "/0" + ed;
            }else {
                endDate = y + "/" + em + "/" + ed;
            }

        }
        if (d < 10 && m < 10) {
            startDate = y + "/0" + m + "/0" + d;
        }else if (m < 10){
            startDate = y + "/0" + m + "/" + d;
        }else if (d < 10){
            startDate = y + "/" + m + "/0" + d;
        }else {
            startDate = y + "/" + m + "/" + d;
        }
        data = controller.select_DV_detailHBy(startDate, endDate, voucherType,cusName,today1);
        select();
        selectDate.setText(startDate + "~" + endDate);

    }


    private void monthly(int m, int y) {
        switch (m) {
            case 1:
                dayOfMonth = 31;
                break;
            case 2:
                if (y % 4 == 0) {
                    dayOfMonth = 29;
                } else {
                    dayOfMonth = 28;
                }
                break;
            case 3:
                dayOfMonth = 31;
                break;
            case 4:
                dayOfMonth = 30;
                break;
            case 5:
                dayOfMonth = 31;
                break;
            case 6:
                dayOfMonth = 30;
                break;
            case 7:
                dayOfMonth = 31;
                break;
            case 8:
                dayOfMonth = 31;
                break;
            case 9:
                dayOfMonth = 30;
                break;
            case 10:
                dayOfMonth = 31;
                break;
            case 11:
                dayOfMonth = 30;
                break;
            case 12:
                dayOfMonth = 31;
                break;
        }
        if (m < 10) {
            startDate = y + "/0" + m + "/01";
            endDate = y + "/0" + m + "/" + dayOfMonth;
        } else {
            startDate = y + "/" + m + "/01";
            endDate = y + "/" + m + "/" + dayOfMonth;
        }
        selectDate.setText(y + "/" + m);
        data = controller.select_DV_detailHBy(startDate, endDate, voucherType,cusName,today1);
        select();
    }

    private void quartly(int m, int y) {
        if (m <= 3) {
            startDate = y + "/01/01";
            endDate = y + "/03/31";
        } else if (m <= 6) {
            startDate = y + "/04/01";
            endDate = y + "/06/30";
        } else if (m <= 9) {
            startDate = y + "/07/01";
            endDate = y + "/09/30";
        } else {
            startDate = y + "/10/01";
            endDate = y + "/12/31";
        }
        selectDate.setText(startDate + "~" + endDate);
        data = controller.select_DV_detailHBy(startDate, endDate, voucherType,cusName,today1);
        select();
    }

    private void yearly(int y) {
        selectDate.setText(y + "");
        startDate = y + "/01/01";
        endDate = y + "/12/31";
        data = controller.select_DV_detailHBy(startDate, endDate, voucherType,cusName,today1);
        select();
    }

    public void weeklyHeader(int d, int m, int y) {
        int month = 0;
        switch (m) {
            case 1:
                month = 31;
                break;
            case 2:
                if (y % 4 == 0) {
                    month = 29;
                } else {
                    month = 28;
                }
                break;
            case 3:
                month = 31;
                break;
            case 4:
                month = 30;
                break;
            case 5:
                month = 31;
                break;
            case 6:
                month = 30;
                break;
            case 7:
                month = 31;
                break;
            case 8:
                month = 31;
                break;
            case 9:
                month = 30;
                break;
            case 10:
                month = 31;
                break;
            case 11:
                month = 30;
                break;
            case 12:
                month = 31;
                break;
        }

        int dend = d + 6;
        if (dend <= month) {
           dateFormat(y,m,dend);
        } else {
            m++;
            dend -= month;
            dateFormat(y,m,dend);

        }
        startDate = y + "/" + m + "/" + d ;
        endDate   = y + "/" + m + "/" + dend;
        data = controller.select_DV_detailHBy(startDate, endDate, voucherType,cusName,today1);
        select();
        selectDate.setText(startDate + "~" + endDate);
    }
    private void select() {
        voucherNoList = new ArrayList<>();
        dataEntries.clear();
        netTotal = 0.0;
        credits = 0.0;
        pay = 0.0;
        profit=0.0;
        Double SumAmt=0.0;
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);


        if (data.moveToFirst()) {
            do {
                dataEntry = new DataEntry();
                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                    listView.setVisibility(View.VISIBLE);
                    dataEntry.setVoucherNo(data.getString(0));
                    dataEntry.setCusName(data.getString(2));
                    dataEntry.setItemPPrice(data.getDouble(7));

                    netTotal += data.getDouble(7);
                    dataEntries.add(dataEntry);
                    voucherNoList.add(data.getString(0));
                }else
                {
                    listView.setVisibility(View.VISIBLE);
                    dataEntry.setVoucherNo(data.getString(0));
                    dataEntry.setItemPPrice(data.getDouble(7));    //origin code

                    SumAmt+=data.getDouble(7);

                    dataEntry.setPay(data.getDouble(8));
                    dataEntries.add(dataEntry);
                    voucherNoList.add(data.getString(0));

                    netTotal += data.getDouble(7);
                    pay += data.getDouble(8);
                    credits += data.getDouble(9) * (-1);

                }

                adapter = new SaleListAdapter(ReportSummaryActivity.this, R.layout.sale_list, dataEntries);
            } while (data.moveToNext());
        }

        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            vrno.setText(dataEntries.size()+"");
            totalAmount.setText(df.format(netTotal)+"");
        }else {
            vrno.setText(dataEntries.size() + "");
            totalAmount.setText(df.format(netTotal) + "\n\n" + df.format(pay) + "\n\n" + df.format(credits));
        }
        listView.setAdapter(adapter);
        setDynamicHeight(listView);

    }

    private void dateFormat(int d, int m, int y) {
        if (d < 10 && m < 10) {
            endDate = y + "/0" + m + "/0" + d;
        } else if (d > 10 && m < 10) {
            endDate = y + "/0" + m + "/" + d;
        } else if (d < 10 && m > 10) {
            endDate = y + "/" + m + "/0" + d;
        } else {
            endDate = y + "/" + m + "/" + d;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dateType.getSelectedItem().toString().equalsIgnoreCase("Weekly")) {
            weekly(day, month, year);
        }
        if (dateType.getSelectedItem().toString().equalsIgnoreCase("Monthly")) {
            monthly(month, year);
        }
        if (dateType.getSelectedItem().toString().equalsIgnoreCase("Yearly")) {
            yearly(year);
        }
        if (dateType.getSelectedItem().toString().equalsIgnoreCase("Quartly")) {
            quartly(month, year);
        }
        if (dateType.getSelectedItem().toString().equalsIgnoreCase("Daily")) {
            daily(startDate);
        }
    }
    public void findId(){
    print          = findViewById(R.id.save);
    cancel         = findViewById(R.id.cancel);
    listView       = findViewById(R.id.all_list);
    selectDate     = findViewById(R.id.date_select);
    vrno           = findViewById(R.id.vrno);
    totalAmount    = findViewById(R.id.totalAmount);
    VoucherN0      = findViewById(R.id.voucherNo);
    Total          = findViewById(R.id.total);
    Receive        = findViewById(R.id.receive);
    spinner        = findViewById(R.id.userSelect);
  //  BluetoothText  = findViewById(R.id.bluetoothTxt);
    txtTotalAmount = findViewById(R.id.txtTotalAmount);
    txtTotalReceive= findViewById(R.id.txtTotalReceive);
    totalAmounttext= findViewById(R.id.totalAmountText);

    comName = findViewById(R.id.comName);
    comAddress=findViewById(R.id.comAddress);
    comContact=findViewById(R.id.comContact);
    todayDate=findViewById(R.id.toDayDate);

}
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

    public void fileSaveSize3(Context context, Bitmap bitmap){
        // Bitmap bb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);

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

    public void fileSaveSize4(Context context, Bitmap bitmap){
       // Bitmap bb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);

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
