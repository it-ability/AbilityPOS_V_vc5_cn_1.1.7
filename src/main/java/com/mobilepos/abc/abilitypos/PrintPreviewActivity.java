package com.mobilepos.abc.abilitypos;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.lang.UScript;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.print.PrintHelper;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Adapter.VoucherAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.VoucherDDataEntry;

import com.mobilepos.abc.abilitypos.Model.PrinterCommands;
import com.mobilepos.abc.abilitypos.Model.StringUtilis;
import com.mobilepos.abc.abilitypos.Model.TestUtils;
import com.mobilepos.abc.abilitypos.Model.Utils;
import com.mobilepos.abc.abilitypos.Model.VoucherD;

import com.mobilepos.abc.abilitypos.Model.VoucherH;
import com.mobilepos.abc.abilitypos.Model.VoucherHDataEntry;
import com.mobilepos.abc.abilitypos.PrintService.UnicodeFormatter;
import com.mobilepos.abc.abilitypos.Procedure.InsertVoucherToCloud;
import com.mobilepos.abc.abilitypos.checkconnection.ConnectivityReceiver;
import com.mobilepos.abc.abilitypos.retrofit.ApiServices;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClients;
import com.otaliastudios.printer.JpegPrinter;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import io.pcyan.sweetdialog.SweetDialog;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintPreviewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static ArrayList<VoucherHDataEntry> dataEntries = new ArrayList<>();
    public static ArrayList<VoucherDDataEntry> dataEntriesD = new ArrayList<>();
    private final static String PRINT_JPEG = "image/jpeg";
    public static String address1="DC:0D:30:70:87:53";
    protected static final String TAG = "TAG";
    private JpegPrinter mJpegPrinter;
    private String mFilename,mStatus="";
    private File mDirectory;
    byte FONT_TYPE;
    protected long peer;
    private PrintPreviewActivity activity;

    private static VoucherAdapter vAdapter;
    public static ArrayList<String> groupType;
    static RecyclerView recyclerView;
    OutputStream os;
    TextView vno, ttax,total, refund, tprice, nettotal, blutetoothtext,tdiscount, ttotal, tnettotal, treceive, trefund,comName,comAddress,comContact,todayDate;
    String itemName="",itemQty="",itemAmt="";
    EditText cusPay;
    EditText remarkCus;
    EditText tax;
    EditText dprice;
    EditText cName;
    public static String sellDate = "", voucherNo = "", remarks = "", bill = "",cusName="",currentDate;
    Button save_insert, previous;
    ImageButton scanDevice;
    Double refunds = 0.0,taxPrice = 0.0, discountPrice = 0.0,NetTotal,price,cusAmt, taxAmt, discountAmt;
    AutoCompleteTextView spin;
    int printerSize;
    DB_Controller controller;
    InsertVoucherToCloud in = null;

    Layout_to_Image layout_to_image;  //Create Object of Layout_to_Image Class

    NestedScrollView nestedScrollView;   //Define Any Layout

    Bitmap bitmap;
    OutputStream mService;
    BitSet dots;
    int  mWidth ;
    int  mHeight;
    public Pointer h = Pointer.NULL;
    PrintHelper printHelper = new PrintHelper(this);

    Date currentDate1 = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  hh:mm:ss a");
    String todayString = formatter.format(currentDate1);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_and_print);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        printerSize = sharedPreferences.getInt("printer_size", 3);
        System.out.print("Text size is"+printerSize);



        final String StringPrint=getIntent().getStringExtra("StringPrint");
        final String ImagePrint=getIntent().getStringExtra("ImagePrint");

        controller = new DB_Controller(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        vAdapter = new VoucherAdapter(SaleItemFragment.addItemEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);

        final double totalprice = SaleItemFragment.totalPayment;

        findId();
        fontChanged();
        DateFormatForVoucher();

        groupType = new ArrayList<>();
        String codeT = "";



        Log.d(TAG,"today date"+todayString);
        todayDate.setText(todayString);

// payment click show total
        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            customVisible();
        }else if (SaleItemFragment.check.equals("5") || SaleItemFragment.check.equals("6") || SaleItemFragment.check.equals("7")) {
            customVisible();
        }


        // if (SaleItemFragment.check.equals("1")) {
        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("1")  || SaleItemFragment.check.equals("3"))  {
            codeT = "C";
        }else{
            codeT = "V";
        }

        final Cursor data = controller.all_cus(codeT);
        int numRow = data.getCount();
        if (/*SaleItemFragment.check.equals("3") || */SaleItemFragment.check.equals("4")) {
            groupType.add(SaleItemFragment.cusname);
            cName.setText(SaleItemFragment.cusname);
            spin.setVisibility(View.GONE);
            cName.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), SaleItemFragment.cusname, Toast.LENGTH_SHORT).show();
        } else {
            cName.setVisibility(View.GONE);
            if (numRow > 0) {
                while (data.moveToNext()) {
                    groupType.add(data.getString(0));
                    spin.setVisibility(View.VISIBLE);
                }
            }
        }

        ArrayAdapter aa = new ArrayAdapter(PrintPreviewActivity.this, android.R.layout.simple_list_item_1, groupType);
        spin.setAdapter(aa);
        spin.setOnItemClickListener(this);


        taxPrice = (Double.valueOf(MainActivity.ctax) * SaleItemFragment.totalPayment) / 100;
        discountPrice = (Double.valueOf(MainActivity.cdiscount) * SaleItemFragment.totalPayment) / 100;
        price = SaleItemFragment.totalPayment + taxPrice - discountPrice;
        vno.setText(voucherNo);
        tprice.append(MainActivity.ctax + "%");
        tax.setText(taxPrice + "");
        dprice.setText(discountPrice + "");
        total.setText(SaleItemFragment.totalPayment + "");
        nettotal.setText((SaleItemFragment.totalPayment + taxPrice - discountPrice) + "");
        cusPay.setText((SaleItemFragment.totalPayment + taxPrice - discountPrice) + "");

        comName.setText(MainActivity.cname);
        comAddress.setText(MainActivity.caddress);
        comContact.setText(MainActivity.ccontact);


        try {
            cusAmt = Double.valueOf((SaleItemFragment.totalPayment + taxPrice - discountPrice) + "");
            refunds = cusAmt-price;
            DecimalFormat f=new DecimalFormat("#.##");
            f.setRoundingMode(RoundingMode.CEILING);
            refund.setText(f.format(refunds) + "");
        } catch (NumberFormatException e) {

        }


        if (cusPay.getText().length() == 0) {
            cusPay.requestFocus();
        }

        cusPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    cusAmt = SaleItemFragment.totalPayment+Double.valueOf(tax.getText().toString())-Double.valueOf(dprice.getText().toString());
                    refunds = cusAmt-Double.valueOf(cusPay.getText().toString());
                    DecimalFormat f=new DecimalFormat("#.##");
                    f.setRoundingMode(RoundingMode.CEILING);
                    refund.setText(f.format(refunds) + "");
                } catch (NumberFormatException e) {

                }
            }
        });
        Toast.makeText(getApplicationContext(), SaleItemFragment.totalPayment * (-1) + "", Toast.LENGTH_SHORT).show();


        tax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    calculateNetTotal();
                    cusPay.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                cusAmt = Double.valueOf(nettotal.getText().toString());
                                price = SaleItemFragment.totalPayment + Double.valueOf(tax.getText().toString()) - Double.valueOf(dprice.getText().toString());
                                refunds = Double.valueOf(nettotal.getText().toString())-Double.valueOf(cusPay.getText().toString());
                                DecimalFormat f=new DecimalFormat("#.##");
                                f.setRoundingMode(RoundingMode.CEILING);
                                refund.setText(f.format(refunds) + "");
                            } catch (NumberFormatException e) {

                            }
                        }
                    });

                }catch (NumberFormatException e){

                }
            }
        });

        dprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    calculateNetTotal();
                    cusPay.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                cusAmt = Double.valueOf(cusPay.getText().toString());
                                price = SaleItemFragment.totalPayment + Double.valueOf(tax.getText().toString()) - Double.valueOf(dprice.getText().toString());
                                refunds = cusAmt - NetTotal;
                                DecimalFormat f=new DecimalFormat("#.##");
                                f.setRoundingMode(RoundingMode.CEILING);
                                refund.setText(f.format(refunds) + "");
                            } catch (NumberFormatException e) {

                            }
                        }
                    });

                }catch (NumberFormatException e){

                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                final NestedScrollView idlayout = (NestedScrollView) findViewById(R.id.mainlayoutId);
                if (ManagementActivity.checkedtrue==true){
                    if (printerSize == 3) {
                        printStringJobSize3();
                    } else if (printerSize == 4) {
                        printStringJobSize4();
                    }
                }/*else if(ManagementActivity.radio_btn_image.isChecked() || ((!ManagementActivity.radio_btn_string.isChecked()) && (!ManagementActivity.radio_btn_image.isChecked()))) {
                    if (printerSize == 3) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(PrintPreviewActivity.this, bitmap);
                        printImageJobSize3();
                        finish();
                    } else if (printerSize == 4) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(PrintPreviewActivity.this, bitmap);
                        printImageJobSize4();
                        finish();
                    }
                }*/
                else {
                    if (printerSize == 3) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(PrintPreviewActivity.this, bitmap);
                        printImageJobSize3();

                    } else if (printerSize == 4) {
                        Bitmap bitmap = getBitmapFromView(idlayout, idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(PrintPreviewActivity.this, bitmap);
                        printImageJobSize4();

                    }
                }

            }
        });


        save_insert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {





                if (SaleItemFragment.check.equals("0")) {
                    if (cusName.equals("")) {
                        cusName = "cash receive ";
                    }
                }
                if (SaleItemFragment.check.equals("1")) {
                    if (cusName.equals("")) {
                        cusName = "cash";
                    }
                }
                if (SaleItemFragment.check.equals("2")) {
                    if (cusName.equals("")) {
                        cusName = "vender";
                    }
                }
                if (SaleItemFragment.check.equals("3")) {
                    if (cusName.equals("")) {
                        cusName = "cash payment";
                    }
                }

                if (remarkCus.getText().toString().equals("")) {
                    remarks = "";
                } else {
                    remarks = remarkCus.getText().toString();
                }

                for (int i = 0; i < SaleItemFragment.addItemEntry.size(); i++) {
                    // Cash Receive
                    if (SaleItemFragment.check.equals("0")) {

                        controller.insert_vourherD(voucherNo, SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemCode(),
                                0.0, 0.0,
                                0.0, SaleItemFragment.addItemEntry.get(i).getItemSPrice() * (-1),
                                "A", MainActivity.companyId, sellDate);
                    }

                    // Sale Vr Save
                    else if (SaleItemFragment.check.equals("1")) {
                        controller.insert_vourherD(voucherNo, SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemCode(),
                                SaleItemFragment.addItemEntry.get(i).getItemPPrice(), SaleItemFragment.addItemEntry.get(i).getItemSPrice(),
                                SaleItemFragment.addItemEntry.get(i).getItemQty() * (-1), SaleItemFragment.addItemEntry.get(i).getChangePrice() * (-1),
                                "I", MainActivity.companyId, sellDate);
                    }
                    // Purchase Vr Save
                    else if (SaleItemFragment.check.equals("2")) {
                        controller.insert_vourherD(voucherNo, SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemCode(),
                                SaleItemFragment.addItemEntry.get(i).getChangePrice(), SaleItemFragment.addItemEntry.get(i).getItemSPrice(),
                                SaleItemFragment.addItemEntry.get(i).getItemQty(), SaleItemFragment.addItemEntry.get(i).getChangePrice(),
                                "I", MainActivity.companyId, sellDate);
                    }
                    // Cash Payment
                    // delete * (-1)
                    else if (SaleItemFragment.check.equals("3")) {
                        controller.insert_vourherD(voucherNo, SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemCode(),
                                0.0, 0.0,
                                0.0, SaleItemFragment.addItemEntry.get(i).getItemSPrice(),
                                "A", MainActivity.companyId, sellDate);
                    } else {
                          /*  Double amt = 0.0;
                        if (SaleItemFragment.check.equals("5")) {
                            amt = (-1) * SaleItemFragment.addItemEntry.get(i).getItemTotalPrice();
                        } else if (SaleItemFragment.check.equals("7")) {
                            amt = SaleItemFragment.customTotal;
                        } else {
                            amt = SaleItemFragment.addItemEntry.get(i).getItemTotalPrice();
                        }
                        controller.insert_vourherD(voucherNo, SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemCode(),
                                0.0, 0.0, 0.0, amt,
                                "A", MainActivity.companyId, sellDate);
*/
                    }
                }

                // ReceiveAndPayment not apply

                if (SaleItemFragment.check.equals("3") || SaleItemFragment.check.equals("4")) {

                  /*  controller.insert_voucherH(voucherNo, MainActivity.userId, SaleItemFragment.cusname, SaleItemFragment.check,
                                                discountPrice, taxPrice, MainActivity.companyId, 0.0,
                                                price, totalprice * (-1), totalprice * (-1), remarks, sellDate);

                    controller.insert_vourherD(voucherNo, SaleItemFragment.cusname, SaleItemFragment.cusname,
                                                0.0, 0.0, 0.0, SaleItemFragment.totalPayment * (-1),
                                                "A", MainActivity.companyId, sellDate);*/
                }


                // Cash not apply

                if (SaleItemFragment.check.equals("5") || SaleItemFragment.check.equals("6") || SaleItemFragment.check.equals("7")) {

                    /*controller.insert_voucherH(voucherNo, MainActivity.userId, cusName, SaleItemFragment.check,
                                                discountPrice, taxPrice, MainActivity.companyId, SaleItemFragment.totalPayment,
                                                SaleItemFragment.totalPayment, 0.0, 0.0, remarks, sellDate);*/

                } else {

                    if (refunds < 0) {
                        if (cusAmt >= price) {
                            controller.insert_voucherH(voucherNo, MainActivity.userId, cusName, SaleItemFragment.check,
                                    Double.valueOf(dprice.getText().toString()), Double.valueOf(tax.getText().toString()), MainActivity.companyId, cusAmt,
                                    Double.valueOf(cusPay.getText().toString()), refunds, refunds, remarks, sellDate);
                            cusName = "";
                        } else {
                            controller.insert_voucherH(voucherNo, MainActivity.userId, cusName, SaleItemFragment.check,
                                    Double.valueOf(dprice.getText().toString()), Double.valueOf(tax.getText().toString()), MainActivity.companyId, price,
                                    cusAmt, refunds, refunds, remarks, sellDate);
                            cusName = "";
                        }
                    } else {

                        if (cusAmt >= price) {
                            controller.insert_voucherH(voucherNo, MainActivity.userId, cusName, SaleItemFragment.check,
                                    Double.valueOf(dprice.getText().toString()), Double.valueOf(tax.getText().toString()), MainActivity.companyId, cusAmt, Double.valueOf(cusPay.getText().toString()), refunds, refunds, remarks, sellDate);
                            cusName = "";
                        } else {

                            controller.insert_voucherH(voucherNo, MainActivity.userId, cusName, SaleItemFragment.check,
                                    discountAmt, taxAmt, MainActivity.companyId, price,
                                    cusAmt, refunds, refunds, remarks, sellDate);
                            cusName = "";
                        }

                    }


                    if (cusAmt > 0) {
                        if (refunds > 0) {
                            cusAmt = SaleItemFragment.totalPayment + taxPrice - discountPrice;
                        } else {
                            cusAmt = Double.valueOf(cusPay.getText().toString());
                        }


                        if (SaleItemFragment.check.equals("0")) {
                            controller.insert_vourherD(voucherNo, "Cash Book", "Cash_A/C",
                                    0.0, 0.0, 0.0, cusAmt,
                                    "A", MainActivity.companyId, sellDate);
                        } else if (SaleItemFragment.check.equals("3")) {
                            controller.insert_vourherD(voucherNo, "Cash Book", "Cash_A/C",
                                    0.0, 0.0, 0.0, cusAmt * (-1),
                                    "A", MainActivity.companyId, sellDate);
                        }
                        //for different sale and purchase
                        if (SaleItemFragment.check.equals("1") || SaleItemFragment.check.equals("3")) {
                            cusAmt = cusAmt;
                        } else {
                            cusAmt = cusAmt * (-1);
                        }

                        // ReceiveAndPayment not apply

                       /* if (SaleItemFragment.check.equals("3") || SaleItemFragment.check.equals("4")) {
                            controller.insert_vourherD(voucherNo, "Sale Credit Reveice", "Cash",
                                    0.0, 0.0, 0.0, (SaleItemFragment.totalPayment + taxPrice - discountPrice),
                                    "A", MainActivity.companyId, sellDate);
                        } else*/
                        if (SaleItemFragment.check.equals("1") || SaleItemFragment.check.equals("2")) {
                            controller.insert_vourherD(voucherNo, "Cash Book", "Cash_A/C",
                                    0.0, 0.0, 0.0, cusAmt,
                                    "A", MainActivity.companyId, sellDate);
                        }
                        /* else if (SaleItemFragment.check.equals("5") || SaleItemFragment.check.equals("6")) {
                            Double netT = 0.0;
                            String name = "";

                            if (SaleItemFragment.check.equals("5")) {
                                name = "Cash Receive";
                                netT = SaleItemFragment.totalPayment + taxPrice - discountPrice;
                            } else {
                                name = "Cash Payment";
                                netT = (SaleItemFragment.totalPayment + taxPrice - discountPrice) * (-1);
                            }
                            controller.insert_vourherD(voucherNo, name, "Cash",
                                    0.0, 0.0, 0.0, netT,
                                    "A", MainActivity.companyId, sellDate);
                        }*/
                    }
                }
// Sale and Purchase Vr
                if (SaleItemFragment.check.equals("1") || SaleItemFragment.check.equals("2")) {
                    if (refunds < 0) {
                        // for balance
                        if (SaleItemFragment.check.equals("1")) {
                            refunds = refunds * (-1);
                        } else {
                            cusAmt = cusAmt * (-1);
                        }

                        /*controller.insert_vourherD(voucherNo, cusName, remarkCus.getText().toString(),
                                0.0, 0.0, 0.0, refunds, "A", MainActivity.companyId, sellDate);*/

                        controller.insert_vourherD(voucherNo, voucherNo, "Receivable",
                                0.0, 0.0, 0.0, refunds, "A", MainActivity.companyId, sellDate);
                    }
                }
                SaleItemFragment.dataEntries.clear();
                SaleItemFragment.addItemEntry.clear();
                SaleItemFragment.totalPayment = 0.0;
                prepare();
                SaleItemFragment.prepareData(PrintPreviewActivity.this);
                finish();
                Toasty.success(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();



         //   in.InsertDataToCloud();

                //repair code for cloud data..................
                // ................................

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




                        Toasty.success(getApplicationContext()," Network Connected ",Toast.LENGTH_SHORT).show();






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

                                 //   Toasty.success(getApplicationContext(),dataEntriesD.toString(),Toast.LENGTH_LONG).show();

                            Toasty.success(getApplicationContext(),"Network Connected, Your Datas Successfully Save To Cloud.",Toast.LENGTH_LONG).show();


                        }

                    }
                    else
                    {
                        Toasty.error(getApplicationContext(),"Nothing",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    private void customVisible() {
        ttax.setVisibility(View.GONE);
        tdiscount.setVisibility(View.GONE);
        tnettotal.setVisibility(View.GONE);
        treceive.setVisibility(View.GONE);
        trefund.setVisibility(View.GONE);
        tax.setVisibility(View.GONE);
        nettotal.setVisibility(View.GONE);
        dprice.setVisibility(View.GONE);
        refund.setVisibility(View.GONE);
        cusPay.setVisibility(View.GONE);
    }

    private void calculateNetTotal(){
        taxAmt = Double.valueOf(tax.getText().toString());
        discountAmt = Double.valueOf(dprice.getText().toString());
        NetTotal = taxAmt+SaleItemFragment.totalPayment-discountAmt;
        // cusAmt=NetTotal;
        nettotal.setText(NetTotal+"");
        cusPay.setText(NetTotal+"");
    }

    private void prepare() {
        vAdapter = new VoucherAdapter(SaleItemFragment.addItemEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        cusName = parent.getItemAtPosition(position).toString();
    }

    public void onActivityResult(int mRequestCode, int mResultCode,Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
    public String toString() {
        return "native@" + Long.toHexString(this.peer);
    }

    private void printImageJobSize3() {

        Thread t = new Thread() {
            public void run() {
                try {
                    String address=MyApplication.getmDeviceAddress();
                    String msg="";
                    String addnull="";
                    if (address.equals("")){
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA("DC:0D:30:70:87:53");
                    }else {
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    }
                    /*if (address.equals("DC:0D:30:70:87:53")){
                        address="";
                        if (address.equals("")){
                            h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA("DC:0D:30:70:87:53");
                        }
                    }else {
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    }*/
                   // h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA("DC:0D:30:70:87:53");
                    if (h!= Pointer.NULL) {

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
                    bill = String.format("%1$-15s %2$5s", " ", StringUtilis.center( MainActivity.cname,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.caddress,10)) + "\n";
                    bill += String.format("%1$-15s %2$5s"," " , StringUtilis.center( MainActivity.ccontact,10)) + "\n";
                    bill += String.format("%1$-22s %2$10s"," " ,todayString) + "\n";

                    bill += "------------------------------------------------\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Item", "Qty", "Amount") + "\n";
                    for (int i = 0; i < SaleItemFragment.addItemEntry.size(); i++) {
                        String aa=SaleItemFragment.addItemEntry.get(i).getItemName();
                        String bb=SaleItemFragment.addItemEntry.get(i).getItemName();
                        int phraseLength = aa.length();
                        String cc="";
                        String dd=" ";
                        String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                        if (aa.length()>=45 && bb.length()>=45){
                            String temp =aa.substring(aa.length() - 1);
                            aa=aa.substring(45,phraseLength);
                            bb=bb.substring(1,44);
                            cc=bb+"\n"+aa;

                            bill += String.format("%1$-30s %2$5s %3$10s", cc, SaleItemFragment.addItemEntry.get(i).getItemQty(), SaleItemFragment.addItemEntry.get(i).getChangePrice()) + "\n";
                        }else {
                            bill += String.format("%1$-30s %2$5s %3$10s", SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemQty(), SaleItemFragment.addItemEntry.get(i).getChangePrice()) + "\n";
                        }

                    }
                    bill += "------------------------------------------------\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Total",":", total.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Tax ",":", tax.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Discount",":", dprice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Net Total",":", nettotal.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Payment",":", cusPay.getText().toString() + ".0") + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Balance",":", refund.getText().toString()) + "\n";
                    bill = bill + "---------------------Thank you------------------\n\n\n\n\n\n\n\n\n";
                    bill = bill + " ";
                    System.out.print(bill);

                    os.write(bill.getBytes());

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

                    scanDevice.setEnabled(true);
                    if (MDetect.INSTANCE.isUnicode()) {
                        blutetoothtext.setText(Rabbit.zg2uni(getResources().getString(R.string.bluetooth_text1)));
                    } else {
                        blutetoothtext.setText(getResources().getString(R.string.bluetooth_text1));
                    }

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
                    String address=MyApplication.getmDeviceAddress();
                    String msg="";
                    String addnull="";
                    if (address.equals("")){
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address1);
                    }else {
                        h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
                    }

                    if ( h!= Pointer.NULL) {

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
                    for (int i = 0; i < SaleItemFragment.addItemEntry.size(); i++) {
                        String aa=SaleItemFragment.addItemEntry.get(i).getItemName();
                        String bb=SaleItemFragment.addItemEntry.get(i).getItemName();
                        int phraseLength = aa.length();
                        String cc="";

                        //  String middle3 = aa.substring(phraseLength/2 - 1, phraseLength/2 + 2);


                        if (aa.length()>=45 && bb.length()>=45){
                            String temp =aa.substring(aa.length() - 1);
                            aa=aa.substring(45,phraseLength);
                            bb=bb.substring(1,44);
                            cc=bb+"\n"+aa;

                            bill += String.format("%1$-30s %2$5s %3$10s", cc, SaleItemFragment.addItemEntry.get(i).getItemQty(), SaleItemFragment.addItemEntry.get(i).getChangePrice()) + "\n";
                        }else {
                            bill += String.format("%1$-30s %2$5s %3$10s", SaleItemFragment.addItemEntry.get(i).getItemName(), SaleItemFragment.addItemEntry.get(i).getItemQty(), SaleItemFragment.addItemEntry.get(i).getChangePrice()) + "\n";
                        }


                    }
                    bill += "------------------------------------------------\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Total",":", total.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Tax ",":", tax.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Discount",":", dprice.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Net Total",":", nettotal.getText().toString()) + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Payment",":", cusPay.getText().toString() + ".0") + "\n";
                    bill += String.format("%1$-30s %2$5s %3$10s", "Balance",":", refund.getText().toString()) + "\n";
                    bill = bill + "----------------------Thank you-------------------\n\n\n\n\n\n\n\n\n";
                    bill = bill + " ";
                    System.out.print(bill);

                    os.write(bill.getBytes());
                    //This is printer specific code you can comment ==== > Start



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

                    scanDevice.setEnabled(true);
                    if (MDetect.INSTANCE.isUnicode()) {
                        blutetoothtext.setText(Rabbit.zg2uni(getResources().getString(R.string.bluetooth_text1)));
                    } else {
                        blutetoothtext.setText(getResources().getString(R.string.bluetooth_text1));
                    }

                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }


    public Drawable ConvertBitmapToDrawable(Bitmap bitmap) {
        /*
            Drawable
                A Drawable is a general abstraction for "something that can be drawn."
                Most often you will deal with Drawable as the type of resource retrieved
                for drawing things to the screen; the Drawable class provides a generic
                API for dealing with an underlying visual resource that may take a variety
                of forms. Unlike a View, a Drawable does not have any facility
                to receive events or otherwise interact with the user.
        */

        /*
            BitmapDrawable
                A Drawable that wraps a bitmap and can be tiled, stretched, or aligned.
                You can create a BitmapDrawable from a file path, an input stream,
                through XML inflation, or from a Bitmap object.
        */

        /*
            public abstract Resources getResources ()
                Return a Resources instance for your application's package.
        */

        Drawable drawable = new BitmapDrawable(getResources(),bitmap);
        return drawable;
    }

    public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        }
        catch (FileNotFoundException e) {}
        return null;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }



    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }
    public void findId(){
        tax            = (EditText) findViewById(R.id.tax);
        total          = (TextView) findViewById(R.id.totalPrice);
        tprice         = (TextView) findViewById(R.id.tprice);
        nettotal       = (TextView) findViewById(R.id.nettotalPrice);
        dprice         = (EditText) findViewById(R.id.discount);
        tdiscount      = (TextView) findViewById(R.id.dprice);
        vno            = (TextView) findViewById(R.id.voucherNo);
        refund         = (TextView) findViewById(R.id.refundAmt);
        cusPay         = (EditText) findViewById(R.id.receivedPayment);
        remarkCus      = (EditText) findViewById(R.id.remark);
        scanDevice     = (ImageButton) findViewById(R.id.scan_device);
        blutetoothtext = (TextView) findViewById(R.id.blutetoothtext);
        save_insert    = (Button) findViewById(R.id.save_insert);
        previous       = (Button) findViewById(R.id.preview);
        //  previous       = (Button) findViewById(R.id.preview);
        spin           = (AutoCompleteTextView) findViewById(R.id.groupcode);
        ttax           = (TextView) findViewById(R.id.tprice);
        tdiscount      = (TextView) findViewById(R.id.dprice);
        ttotal         = (TextView) findViewById(R.id.totaltext);
        tnettotal      = (TextView) findViewById(R.id.nettotaltext);
        treceive       = (TextView) findViewById(R.id.receivetext);
        trefund        = (TextView) findViewById(R.id.refundtext);
        cName          = (EditText) findViewById(R.id.cusName);

        comName        = (TextView) findViewById(R.id.comName);
        comAddress     = (TextView) findViewById(R.id.comAddress);
        comContact     = (TextView) findViewById(R.id.comContact);
        todayDate      = (TextView) findViewById(R.id.toDayDate);


    }
    protected void fontChanged(){
        if (MDetect.INSTANCE.isUnicode()) {
            ttax.setText(Rabbit.zg2uni(getResources().getString(R.string.taxtext)));
            tdiscount.setText(Rabbit.zg2uni(getResources().getString(R.string.distext)));
            ttotal.setText(Rabbit.zg2uni(getResources().getString(R.string.total)));
            tnettotal.setText(Rabbit.zg2uni(getResources().getString(R.string.nettotal)));
            trefund.setText(Rabbit.zg2uni(getResources().getString(R.string.refunds)));
            save_insert.setText(Rabbit.zg2uni(getResources().getString(R.string.save)));
            if (SaleItemFragment.check.equals("0")){
                //    previous.setText(Rabbit.zg2uni(getResources().getString(R.string.cash_receive_invoice)));
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.cash_receive_invoice)));
            }else if (SaleItemFragment.check.equals("1")){
                //    previous.setText(Rabbit.zg2uni(getResources().getString(R.string.sale_invoice)));
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.sale_invoice)));
            }else if (SaleItemFragment.check.equals("2")){
                //    previous.setText(Rabbit.zg2uni(getResources().getString(R.string.purchase_invoice)));
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.purchase_invoice)));
                treceive.setText(Rabbit.zg2uni(getResources().getString(R.string.paid)));
            }else if (SaleItemFragment.check.equals("3")){
                //    previous.setText(Rabbit.zg2uni(getResources().getString(R.string.cash_payment_invoice)));
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.cash_payment_invoice)));
            }


        } else {
            ttax.setText(getResources().getString(R.string.taxtext));
            tdiscount.setText(getResources().getString(R.string.distext));
            ttotal.setText(getResources().getString(R.string.total));
            tnettotal.setText(getResources().getString(R.string.nettotal));
            trefund.setText(getResources().getString(R.string.refunds));
            save_insert.setText(getResources().getString(R.string.save));
            if (SaleItemFragment.check.equals("0")){
                //    previous.setText(getResources().getString(R.string.cash_receive_invoice));
                this.setTitle(getResources().getString(R.string.cash_receive_invoice));
            }else if (SaleItemFragment.check.equals("1")){
                //    previous.setText(getResources().getString(R.string.sale_invoice));
                treceive.setText(getResources().getString(R.string.receives));
                this.setTitle(getResources().getString(R.string.sale_invoice));
            }else if (SaleItemFragment.check.equals("2")){
                //    previous.setText(getResources().getString(R.string.purchase_invoice));
                this.setTitle(getResources().getString(R.string.purchase_invoice));
                treceive.setText(getResources().getString(R.string.paid));
            }else if (SaleItemFragment.check.equals("3")){
                //    previous.setText(getResources().getString(R.string.cash_payment_invoice));
                this.setTitle(getResources().getString(R.string.cash_payment_invoice));
            }

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.print_menu, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.print_menu) {
            final Dialog dialog = new Dialog(PrintPreviewActivity.this);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.border);
            dialog.setContentView(R.layout.print_selects);
            dialog.show();

            Button btn_english = dialog.findViewById(R.id.btn_english_print);
            Button btn_myanmar = dialog.findViewById(R.id.btn_myanmar_print);

            btn_english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (printerSize == 3) {
                        printStringJobSize3();
                        dialog.dismiss();

                    } else if (printerSize == 4) {
                        printStringJobSize4();
                        dialog.dismiss();
                    }

                }
            });

            btn_myanmar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NestedScrollView idlayout=(NestedScrollView)findViewById(R.id.mainlayoutId);
                    if (printerSize==3){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize3(PrintPreviewActivity.this,bitmap);
                        printImageJobSize3();
                        finish();

                    }else if (printerSize==4){
                        Bitmap bitmap = getBitmapFromView(idlayout,idlayout.getChildAt(0).getHeight(), idlayout.getChildAt(0).getWidth());
                        fileSaveSize4(PrintPreviewActivity.this,bitmap);
                        printImageJobSize4();
                        finish();

                    }

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public void DateFormatForVoucher(){
        String dayS,monthS,hourS,minS,secS;
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        final String yearS = String.valueOf(y).substring(2);
        int m = calendar.get(Calendar.MONTH) + 1;
        if (m < 10) {
            monthS = "0" + String.valueOf(m);
        } else {
            monthS = String.valueOf(m);
        }
        int d = calendar.get(Calendar.DATE);
        if (d < 10) {
            dayS = "0" + String.valueOf(d);
        } else {
            dayS = String.valueOf(d);
        }
        int h = calendar.get(Calendar.HOUR);
        if (h < 10) {
            hourS = "0" + String.valueOf(h);
        } else {
            hourS = String.valueOf(h);
        }
        int min = calendar.get(Calendar.MINUTE);
        if (min < 10) {
            minS = "0" + String.valueOf(min);
        } else {
            minS = String.valueOf(min);
        }
        int sec = calendar.get(Calendar.SECOND);
        if (sec < 10) {
            secS = "0" + String.valueOf(sec);
        } else {
            secS = String.valueOf(sec);
        }


        if (d < 10 && m < 10) {
            sellDate = y + "/0" + m + "/0" + d;
        } else if (d > 10 && m < 10) {
            sellDate = y + "/0" + m + "/" + d;
        } else if (d < 10 && m > 10) {
            sellDate = y + "/" + m + "/0" + d;
        } else {
            sellDate = y + "/" + m + "/" + d;
        }
        voucherNo = yearS + "" + monthS + "" + dayS + "" + hourS + "" + minS + "" + secS;
        currentDate="20"+yearS + "/" + monthS + "/" + dayS + "   " + hourS + ":" + minS + ":" + secS;
    }
    private static void scanGallery(Context cntx, String path) {
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
        // Bitmap tzb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);
        int nh = (int) ( bitmap.getHeight() * (550.0 / bitmap.getWidth()) );
        Bitmap tzb = Bitmap.createScaledBitmap(bitmap, 550, nh, true);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
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
        // Bitmap tzb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(),false);
        int nh = (int) ( bitmap.getHeight() * (575.0 / bitmap.getWidth()) );
        Bitmap tzb = Bitmap.createScaledBitmap(bitmap, 575, nh, true);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Do the file write
        } else {
            // Request permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        }
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

    public static void saveBitmapToJpg(Context context,Bitmap bitmap,int dpi) throws IOException {
        ByteArrayOutputStream imageByteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageByteArray);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images");
        if (!file.exists()) {
            file.mkdirs();
        }
        Random generator = new Random();
       /* int n = 10000;
        n = generator.nextInt(n);*/
        String fname = "Image.jpg";
        File file1 = new File (file, fname);
        if (file.exists ())
            file.delete ();
        byte[] imageData = imageByteArray.toByteArray();

        setDpi(imageData, dpi);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(imageData);
            fileOutputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,file.getAbsolutePath());
    }

    private static void setDpi(byte[] imageData, int dpi) {
        imageData[13] = 1;
        imageData[14] = (byte) (dpi >> 8);
        imageData[15] = (byte) (dpi & 0xff);
        imageData[16] = (byte) (dpi >> 8);
        imageData[17] = (byte) (dpi & 0xff);
    }

    /*private void print_image(File file,OutputStream os) {

        if (file.exists()) {
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg");
            convertBitmap(bmp);
            try {
                os.write(PrinterCommands.SET_LINE_SPACING_24);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int offset = 0;
            while (offset < bmp.getHeight()) {
                try {
                    os.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        try {
                            os.write(slice);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                offset += 24;
                try{
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                    os.write(PrinterCommands.FEED_LINE);
                    os.write(PrinterCommands.FEED_LINE);
                    os.write(PrinterCommands.FEED_LINE);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            try {
                os.write(PrinterCommands.SET_LINE_SPACING_30);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

   /* public String convertBitmap(Bitmap inputBitmap) {


       mWidth = inputBitmap.getWidth();
       mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        mStatus = "ok";
        return mStatus;

    }*/

   /* private void convertArgbToGrayscale(Bitmap bmpOriginal, int width, int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        }
    }*/

   /* protected void printDemo() {
        if(MyApplication.getMmBluetoothSocket() != null){
            *//*Intent BTIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
            this.startActivityForResult(BTIntent,DeviceListActivity.REQUEST_CONNECT_BT);*//*
            OutputStream opstream = null;
            try {
                opstream = MyApplication.getMmBluetoothSocket().getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            OutputStream outputStream = opstream;

            //print command
            try {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                outputStream = MyApplication.getMmBluetoothSocket().getOutputStream();

                byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
                //outputStream.write(printformat);

                //print title
              //  printUnicode();
                //print normal text
            //    printCustom(message.getText().toString(),0,0);
            //    printPhoto(R.drawable.img);
            //    printNewLine();
            //   printText("     >>>>   Thank you  <<<<     "); // total 32 char in a single line
                //resetPrint(); //reset printer
           //    printUnicode();
            //    printNewLine();
             //   printNewLine();

                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*public void printPhoto(int img) {
            try {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                        img);
                if(bmp!=null){
                  //  byte[] command = Utils.decodeBitmap(bmp);
                    os.write(PrinterCommands.ESC_ALIGN_CENTER);
                   // printText(command);
                }else{
                    Log.e("Print Photo error", "the file isn't exists");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PrintTools", "the file isn't exists");
            }
        }*/

   /* private void print_image(String file) {
        try {
            File fl = new File(file);
            if (fl.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(file);
                convertBitmap(bmp);
                mService.write(PrinterCommands.SET_LINE_SPACING_24);

                int offset = 0;
                while (offset < bmp.getHeight()) {
                    mService.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                    for (int x = 0; x < bmp.getWidth(); ++x) {

                        for (int k = 0; k < 3; ++k) {

                            byte slice = 0;
                            for (int b = 0; b < 8; ++b) {
                                int y = (((offset / 8) + k) * 8) + b;
                                int i = (y * bmp.getWidth()) + x;
                                boolean v = false;
                                if (i < dots.length()) {
                                    v = dots.get(i);
                                }
                                slice |= (byte) ((v ? 1 : 0) << (7 - b));
                            }
                            mService.write(slice);
                        }
                    }
                    offset += 24;
                    mService.write(PrinterCommands.FEED_LINE);
                    mService.write(PrinterCommands.FEED_LINE);
                    mService.write(PrinterCommands.FEED_LINE);
                    mService.write(PrinterCommands.FEED_LINE);
                    mService.write(PrinterCommands.FEED_LINE);
                    mService.write(PrinterCommands.FEED_LINE);
                }
                mService.write(PrinterCommands.SET_LINE_SPACING_30);


            } else {
                Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
                        .show();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/

   /* public void printPhoto(Bitmap bitmap) {
        try {
            File f=new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg");
            System.out.println("FileFleFle8888"+f);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            if(bitmap!=null){
                byte[] command = Utils.decodeBitmap(bitmap);
                os.write(PrinterCommands.ESC_ALIGN_CENTER);
               // printText(command.toString());
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }*/

    private void printText(String msg) {
        try {
            // Print normal text
            os.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void printDemo() {

        os = MyApplication.getMmOutputStream();

        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            os = MyApplication.getMmOutputStream();

            byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
            //outputStream.write(printformat);
            printPhoto(bitmap);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //print photo
    public void printPhoto(Bitmap bmp) {
        try {

            File f=new File(Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg");
            System.out.println("FileFleFle8888"+f);
            bmp = BitmapFactory.decodeStream(new FileInputStream(f));
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                os.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    private void printText(byte[] msg) {
        try {
            // Print normal text
            os.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public String convertBitmap(Bitmap inputBitmap) {

        mWidth = inputBitmap.getWidth();
        mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        mStatus = "ok";
        return mStatus;

    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        }
    }


    private void print_image(String file) throws IOException {
        os=MyApplication.getMmOutputStream();
        File fl = new File(file);
       /* path=Environment.getExternalStorageDirectory() + File.separator + "saved_images"+File.separator+"Image.jpg";
        File f2=new File(file);
        System.out.println("FileFleFle8888"+f2);
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f2));*/
        if (fl.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(file);
            convertBitmap(bmp);
            os.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                os.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        os.write(slice);
                    }
                }
                offset += 24;
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
                os.write(PrinterCommands.FEED_LINE);
            }
            os.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void print_images(String path) throws IOException {
        os = MyApplication.getMmOutputStream();
        File f = new File(path);
        if (f.createNewFile()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            os.write(bitmapdata);
            fos.flush();
            fos.close();
        }
    }
}