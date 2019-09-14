package com.mobilepos.abc.abilitypos;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.PrintStreamPrinter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Adapter.VoucherAdapter;
import com.mobilepos.abc.abilitypos.Adapter.VoucherDetailAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.Model.StringUtilis;
import com.mobilepos.abc.abilitypos.PrintService.UnicodeFormatter;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

import org.apache.commons.beanutils.converters.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class VoucherDetailActivity extends AppCompatActivity {

    protected static final String TAG = "TAG";
    public static String address1="DC:0D:30:70:87:53";
    OutputStream os;
    DB_Controller controller;
    Button print, delete;
    TextView detailNe,detailQt,detailAm,cusNametxt,remarktxt,comName,comAddress,comContact,todayDate;
    TextView vrdata, detailN, detailQ, detailA, tax, discount, totoalPrice, netTotalPrice, pay, refund, cusName, remark,ttax, tdiscount, ttotal, tnettotal, treceive, tbalance;
    int pos = 0;
    int positionCount = 0;
    public static ArrayList<DataEntry> dataEntries;
    private ArrayList<String> voucherNoList;
    private ArrayList<DataEntry> voucherEntry;
    String voucherNo = "",detailName = "",detailQty = "",detailPrice = "",detailAmt = "",taxS = "",discountS = "",total = "",netTotals = "",pays = "",refundS = "",cusNameS = "",remarkS = "";
    String root = Environment.getExternalStorageDirectory().toString();
    int printSize;
    ListView listView;

    private String vrdataS;




    String voucherType = "";
    String startDate = "", endDate = "", bill = "", name = "", dateSelected = "", headerDate = "", voucherHeader = "";
    Double netTotal = 0.0, credits = 0.0, payS = 0.0;
    Double SumAmt=0.0;
    DecimalFormat f=new DecimalFormat("#");
    DataEntry dataEntry;
    ImageView next, previous;
    static RecyclerView recyclerView;
   private static VoucherAdapter vAdapter;
  //  private static VoucherDetailAdapter vAdapter;

    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  hh:mm:ss aa");
    String todayString = formatter.format(currentDate);


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        voucherNoList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_detail_print);
        findById();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences1 = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        printSize = sharedPreferences1.getInt("printer_size", 0);
        controller = new DB_Controller(getApplicationContext());



        final String StringPrint=getIntent().getStringExtra("StringPrint");
        final String ImagePrint=getIntent().getStringExtra("ImagePrint");

        prepareData();



        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NestedScrollView idlayout = (NestedScrollView) findViewById(R.id.mainlayoutId);
               /* if (StringPrint!=null){
                    if (printSize == 3) {
                        printStringJobSize3();
                        finish();
                    } else if (printSize == 4) {
                        printStringJobSize4();
                        finish();
                    }
                }else if(ImagePrint!=null || ((StringPrint==null) && (ImagePrint==null))) {
                    if (printSize == 3) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(VoucherDetailActivity.this, bitmap);
                        printImageJobSize3();
                        finish();
                    } else if (printSize == 4) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(VoucherDetailActivity.this, bitmap);
                        printImageJobSize4();
                        finish();
                    }
                }*/
               if (ManagementActivity.checkedtrue==true){
                   if (printSize == 3) {
                       Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                       fileSaveSize3(VoucherDetailActivity.this, bitmap);
                       printStringJobSize3();
                       finish();
                   } else if (printSize == 4) {
                       Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                       fileSaveSize4(VoucherDetailActivity.this, bitmap);
                       printStringJobSize4();
                       finish();
                   }
               }else {
                   if (printSize == 3) {
                       Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                       fileSaveSize3(VoucherDetailActivity.this, bitmap);
                       printImageJobSize3();
                       finish();
                   } else if (printSize == 4) {
                       Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                       fileSaveSize4(VoucherDetailActivity.this, bitmap);
                       printImageJobSize4();
                       finish();
                   }
               }

            }
        });



        Log.d(TAG,"today date"+todayString);
        todayDate.setText(""+todayString);


        if (MDetect.INSTANCE.isUnicode()) {
            detailNe.setText(Rabbit.zg2uni(getResources().getString(R.string.detailNe)));
            detailQt.setText(Rabbit.zg2uni(getResources().getString(R.string.detailQt)));
            detailAm.setText(Rabbit.zg2uni(getResources().getString(R.string.detailAm)));

            cusName.setText(Rabbit.zg2uni(getResources().getString(R.string.cusNametxt)));
            remark.setText(Rabbit.zg2uni(getResources().getString(R.string.remarktxt)));
            vrdata.setText(Rabbit.zg2uni(getResources().getString(R.string.vrdata)));

            ttax.setText(Rabbit.zg2uni(getResources().getString(R.string.taxtext)));
            tdiscount.setText(Rabbit.zg2uni(getResources().getString(R.string.distext)));
            ttotal.setText(Rabbit.zg2uni(getResources().getString(R.string.total)));
            tnettotal.setText(Rabbit.zg2uni(getResources().getString(R.string.nettotal)));
            if (SaleItemFragment.check.equals("1")){
                treceive.setText(Rabbit.zg2uni(getResources().getString(R.string.receives)));
            }else if (SaleItemFragment.check.equals("2")) {
                treceive.setText(Rabbit.zg2uni(getResources().getString(R.string.paid)));
            }
            tbalance.setText(Rabbit.zg2uni(getResources().getString(R.string.balances)));
            print.setText(Rabbit.zg2uni(getResources().getString(R.string.print)));
         //   deletebtn.setText(Rabbit.zg2uni(getResources().getString(R.string.deletevd)));
            this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.vdetail)));
        } else {

            detailNe.setText(getResources().getString(R.string.detailNe));
            detailQt.setText(getResources().getString(R.string.detailQt));
            detailAm.setText(getResources().getString(R.string.detailAm));

            cusName.setText(getResources().getString(R.string.cusNametxt));
            remark.setText(getResources().getString(R.string.remarktxt));
            vrdata.setText(getResources().getString(R.string.vrdata));


            ttax.setText(getResources().getString(R.string.taxtext));
            tdiscount.setText(getResources().getString(R.string.distext));
            ttotal.setText(getResources().getString(R.string.total));
            tnettotal.setText(getResources().getString(R.string.nettotal));
            if (SaleItemFragment.check.equals("1")) {
                treceive.setText(getResources().getString(R.string.receives));
            }else if (SaleItemFragment.check.equals("2")){
                treceive.setText(getResources().getString(R.string.paid));
            }
            tbalance.setText(getResources().getString(R.string.balances));
            print.setText(getResources().getString(R.string.print));
            this.setTitle(getResources().getString(R.string.vdetail));
        }


        comName.setText(MainActivity.cname);
        comAddress.setText(MainActivity.caddress);
        comContact.setText(MainActivity.ccontact);

        vrdata.setText(getIntent().getExtras().getString("vrno"));
        vrdataS = getIntent().getExtras().getString("vrnos");
        System.out.println("VrdataS >>>> " + vrdataS);


        pos = getIntent().getExtras().getInt("pos");
        positionCount = getIntent().getExtras().getInt("positionCount");
        voucherNoList = getIntent().getExtras().getStringArrayList("voucherNoList");


        tax.setText(getIntent().getExtras().getString("tax"));
        discount.setText(getIntent().getExtras().getString("discount"));
        totoalPrice.setText(getIntent().getExtras().getString("total"));
        netTotalPrice.setText(getIntent().getExtras().getString("netTotal"));
        pay.setText(getIntent().getExtras().getString("pay"));
        refund.setText(getIntent().getExtras().getString("refund"));
        cusName.append("     :    "+getIntent().getExtras().getString("cusName") + "");
        remark.append("  :    "+getIntent().getExtras().getString("remark") + "");


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.deleteVoucher(vrdataS);
                System.out.println("VrDateS >>>>> " + vrdataS);
                Toasty.success(getApplicationContext(), "Successfully delete!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            ttax.setVisibility(View.GONE);
            tax.setVisibility(View.GONE);
            tdiscount.setVisibility(View.GONE);
            discount.setVisibility(View.GONE);
            ttotal.setVisibility(View.VISIBLE);
            tnettotal.setVisibility(View.GONE);
            tbalance.setVisibility(View.GONE);
            totoalPrice.setVisibility(View.VISIBLE);
            netTotalPrice.setVisibility(View.GONE);
            refund.setVisibility(View.GONE);
            treceive.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
            detailNe.setText("Title");
            detailQt.setText("");
            detailAm.setText("Amount");

        }else if (SaleItemFragment.check.equals("1") || SaleItemFragment.check.equals("2")){
            ttax.setVisibility(View.VISIBLE);
            tax.setVisibility(View.VISIBLE);
            tdiscount.setVisibility(View.VISIBLE);
            discount.setVisibility(View.VISIBLE);
            ttotal.setVisibility(View.VISIBLE);
            tnettotal.setVisibility(View.VISIBLE);
            tbalance.setVisibility(View.VISIBLE);
            totoalPrice.setVisibility(View.VISIBLE);
            netTotalPrice.setVisibility(View.VISIBLE);
            refund.setVisibility(View.VISIBLE);
            treceive.setVisibility(View.VISIBLE);
            pay.setVisibility(View.VISIBLE);
            detailNe.setText("Item Name");
            detailQt.setText("Qty");
            detailAm.setText("Amount");
        }


    }
    private void findById() {
        print = findViewById(R.id.save);
        delete = findViewById(R.id.delete);

        vrdata = findViewById(R.id.vrno);
        detailN = findViewById(R.id.detailN);
        detailQ = findViewById(R.id.detailQ);
        detailA = findViewById(R.id.detailA);
        tax = findViewById(R.id.tax);
        discount = findViewById(R.id.discount);
        totoalPrice = findViewById(R.id.totalPrice);
        netTotalPrice = findViewById(R.id.nettotalPrice);
        pay = findViewById(R.id.receivedPayment);
        refund = findViewById(R.id.refundAmt);
        cusName = findViewById(R.id.cusName);
        remark = findViewById(R.id.remark);

        ttax = findViewById(R.id.tprice);
        tdiscount = findViewById(R.id.dprice);
        ttotal = findViewById(R.id.totaltext);
        tnettotal = findViewById(R.id.nettotaltext);
        treceive = findViewById(R.id.receivetext);
        tbalance = findViewById(R.id.balancetext);

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        detailNe= findViewById(R.id.detailNe);
        detailQt= findViewById(R.id.detailQt);
        detailAm= findViewById(R.id.detailAm);


        comName = findViewById(R.id.comName);
        comAddress = findViewById(R.id.comAddress);
        comContact=findViewById(R.id.comContact);

        todayDate=findViewById(R.id.toDayDate);


        recyclerView=(RecyclerView)findViewById(R.id.voucher_detail_recycler);

   //     listView =(ListView)findViewById(R.id.voucher_detail_list);




    }

    //for print job
    //for scan
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {

        /*Intent intent=new Intent(this,ReportSummaryActivity.class);
        startActivityForResult(intent,1000);
        finishActivity(1000);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
        setResult(RESULT_CANCELED);
        finish();

    }

    private void printStringJobSize3() {

        Thread t = new Thread() {
            public void run() {
                try {
                    os = MyApplication.getMmOutputStream();
                    String text="oooo";


                    bill = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    bill += String.format("%1$-22s %2$10s"," " ,todayString) + "\n";
                    bill += "------------------------------------------------\n";

                    bill += String.format("%1$-30s %2$5s %3$10s", "Item", "Qty", "Amount") + "\n";
                    bill += "------------------------------------------------\n";
                    for (int i = 0; i < ReportSummaryActivity.voucherEntry.size(); i++) {
                        String aa=ReportSummaryActivity.voucherEntry.get(i).getItemName();
                        String bb=ReportSummaryActivity.voucherEntry.get(i).getItemQty().toString();
                        String cc=ReportSummaryActivity.voucherEntry.get(i).getChangePrice().toString();
                        if (MDetect.INSTANCE.isUnicode()){
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(aa)));
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(bb)));
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(cc)));
                        }
                        bill += String.format("%1$-30s %2$5s %3$10s", ReportSummaryActivity.voucherEntry.get(i).getItemName(), ReportSummaryActivity.voucherEntry.get(i).getItemQty(), ReportSummaryActivity.voucherEntry.get(i).getChangePrice()) + "\n";
                    }
                    bill += "------------------------------------------------\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Total ",":", totoalPrice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Tax",":", tax.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Discount",":", discount.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Net Total",":", netTotalPrice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Payment",":",pay.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Balance",":", refund.getText().toString()) + "\n";
                    bill = bill + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";


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
    private void printImageJobSize3() {


        Thread t = new Thread() {
            public void run() {

                try {
                    os = MyApplication.getMmOutputStream();
                    String address=MyApplication.getmDeviceAddress();
                    Pointer h = Pointer.NULL;
                    String msg="";

                    //Only Thermal Printer
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
                    bill = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    bill += String.format("%1$-22s %2$10s"," " ,todayString) + "\n";
                    bill += "------------------------------------------------\n";

                    bill += String.format("%1$-30s %2$5s %3$10s", "Item", "Qty", "Amount") + "\n";
                    bill += "------------------------------------------------\n";
                    for (int i = 0; i < ReportSummaryActivity.voucherEntry.size(); i++) {
                        String aa=ReportSummaryActivity.voucherEntry.get(i).getItemName();
                        String bb=ReportSummaryActivity.voucherEntry.get(i).getItemQty().toString();
                        String cc=ReportSummaryActivity.voucherEntry.get(i).getChangePrice().toString();
                        if (MDetect.INSTANCE.isUnicode()){
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(aa)));
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(bb)));
                            Rabbit.zg2uni(getResources().getString(Integer.parseInt(cc)));
                        }
                        bill += String.format("%1$-30s %2$5s %3$10s", ReportSummaryActivity.voucherEntry.get(i).getItemName(), ReportSummaryActivity.voucherEntry.get(i).getItemQty(), ReportSummaryActivity.voucherEntry.get(i).getChangePrice()) + "\n";
                    }
                    bill += "------------------------------------------------\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Total ",":", totoalPrice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Tax",":", tax.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Discount",":", discount.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Net Total",":", netTotalPrice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Payment",":",pay.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Balance",":", refund.getText().toString()) + "\n";
                    bill = bill + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";


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

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public void onClickNext(View view) {

        detailName = "";
        detailQty = "";
        detailAmt = "";
        detailPrice= "";
        SumAmt=0.0;

        voucherEntry = new ArrayList<>();
        dataEntry = new DataEntry();
        ReportSummaryActivity.voucherEntry.clear();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
        f.setRoundingMode(RoundingMode.CEILING);

        if (pos < positionCount - 1) {
            cusName.setText(null);
            remark.setText(null);
            cusName.setText("Name   " + "\t" + ":" + "     " + "\t");
            remark.setText("Remark" + "\t" + ":" + "     " + "\t");

            pos++;
            vrdataS = voucherNoList.get(pos);
            System.out.println("vrdataS >>>>> " + vrdataS);
            Cursor data = controller.all_detailVr(vrdataS);

            voucherNo = "Voucher NO:" + vrdataS;
            if (data.moveToFirst()) {
                do {
                    dataEntry = new DataEntry();
                    detailName += data.getString(1) + "\n\n";
                    detailQty += f.format(Double.valueOf(data.getString(5)) * (-1)) + "\n\n";
                    detailPrice += df.format(Double.valueOf(data.getString(4))) + "\n\n";
                    detailAmt += df.format(Double.valueOf(data.getString(6))) + "\n\n";
                    SumAmt +=Double.valueOf(data.getString(6));

                    dataEntry.setItemName(data.getString(1));          //Name
                    dataEntry.setItemQty(data.getDouble(5) * (-1));    //Qty
                    dataEntry.setItemSPrice(data.getDouble(4));       // Price
                    dataEntry.setChangePrice(data.getDouble(6));       // Amount

                    voucherEntry.add(dataEntry);

                    prepareDatathis();



                } while (data.moveToNext());

            }

            Cursor datas = controller.all_detailH(vrdataS);
            int numRows = datas.getCount();

            if (numRows == 0) {

            } else {
                while (datas.moveToNext()) {
                    cusNameS = datas.getString(2);
                    taxS = datas.getString(5);
                    discountS = datas.getString(4);
                    // total = datas.getString(7);
                    total = String.valueOf(SumAmt);
                    pays = datas.getString(8);
                    refundS = datas.getString(9);
                    remarkS = datas.getString(11);
                    netTotals = SumAmt - Double.valueOf(datas.getString(5)) + Double.valueOf(datas.getString(4)) + "";
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "No More Voucher ", Toast.LENGTH_SHORT).show();
            return;
        }

        vrdata.setText(voucherNo);

       /* detailN.setText(detailName);
        detailQ.setText(detailQty);
        detailA.setText(detailAmt);*/
        tax.setText(taxS);
        discount.setText(discountS);
        totoalPrice.setText(df.format(Double.valueOf(total)));
        netTotalPrice.setText(df.format(Double.valueOf(netTotals)));
        pay.setText(df.format(Double.valueOf(pays)));
        refund.setText(refundS);
        cusName.append(cusNameS + "");
        remark.append(remarkS + "");
    }

    public void onClickPrevious(View view) {
        detailName = "";
        detailQty = "";
        detailAmt = "";
        SumAmt=0.0;

        voucherEntry = new ArrayList<>();
        dataEntry = new DataEntry();
        ReportSummaryActivity.voucherEntry.clear();

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);
        f.setRoundingMode(RoundingMode.CEILING);


        if (pos <= positionCount && pos != 0) {
            cusName.setText(null);
            remark.setText(null);
            cusName.setText("Name   " + "\t" + ":" + "     " + "\t");
            remark.setText("Remark" + "\t" + ":" + "     " + "\t");
            pos--;
            vrdataS = voucherNoList.get(pos);
            System.out.println("vrdataS >>>>> " + vrdataS);
            Cursor data = controller.all_detailVr(vrdataS);

            voucherNo = "Voucher NO:" + vrdataS;

            if (data.moveToFirst()) {
                do {
                    dataEntry = new DataEntry();
                    detailName += data.getString(1) + "\n\n";
                    detailQty += f.format(Double.valueOf(data.getString(5)) * (-1)) + "\n\n";
                    detailPrice += df.format(Double.valueOf(data.getString(4))) + "\n\n";
                    detailAmt += df.format(Double.valueOf(data.getString(6))) + "\n\n";
                    SumAmt +=Double.valueOf(data.getString(6));

                    dataEntry.setItemName(data.getString(1));
                    dataEntry.setItemQty(data.getDouble(5) * (-1));
                    dataEntry.setItemSPrice(data.getDouble(4));
                    dataEntry.setChangePrice(data.getDouble(6));

                    voucherEntry.add(dataEntry);
                    prepareDatathis();

                } while (data.moveToNext());


            }

            Cursor datas = controller.all_detailH(vrdataS);

            if (datas.moveToFirst()) {
                do {
                    cusNameS = datas.getString(2);
                    taxS = datas.getString(5);
                    discountS = datas.getString(4);
                    //   total = datas.getString(7);
                    total = String.valueOf(SumAmt);
                    pays = datas.getString(8);
                    refundS = datas.getString(9);
                    remarkS = datas.getString(11);
                    netTotals = SumAmt - Double.valueOf(datas.getString(5)) + Double.valueOf(datas.getString(4)) + "";
                } while (datas.moveToNext());
            }


            df.setMaximumFractionDigits(3);
            vrdata.setText(voucherNo);

            /*detailN.setText(detailName);
            detailQ.setText(detailQty);
            detailA.setText(detailAmt);*/

            tax.setText(taxS);
            discount.setText(discountS);
            totoalPrice.setText(df.format(Double.valueOf(total)));
            netTotalPrice.setText(df.format(Double.valueOf(netTotals)));
            pay.setText(df.format(Double.valueOf(pays)));
            refund.setText(refundS);
            cusName.append(cusNameS + "");
            remark.append(remarkS + "");
        } else {
            Toast.makeText(getApplicationContext(), "No More Voucher ", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    public static Bitmap setViewToBitmapImage(View view) {
//Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    public static void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private File saveBitMap(Context context, View drawView){
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
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            //filePath.createNewFile();
            FileOutputStream oStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,file.getAbsolutePath());

        return file;
    }

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    // used for scanning gallery
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



    public  void prepareData() {
        vAdapter = new VoucherAdapter(ReportSummaryActivity.voucherEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);
    }
    public  void prepareDatathis() {
        vAdapter = new VoucherAdapter(voucherEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);
    }

}

