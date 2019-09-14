package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.TextView;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Adapter.VoucherAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.sun.jna.Pointer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;


public class InvoiceActivity extends AppCompatActivity {
    OutputStream os;
    DB_Controller controller;
    private NestedScrollView nestedScrollView;
    Button print, cancel;
    TextView itemNameText,itemPriceText,itemQtyText,taxText,discountTax,totalTax,netTotalTax,receiveTax,balancxeTax,companyName,companyAddress,companyPhopne;
    TextView itemName,itemPrice,itemQty,tax,discount,total,netTotal,receive,balancxe;
    RecyclerView recyclerView;

    private static VoucherAdapter vAdapter;
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_invoice_print);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nestedScrollView = findViewById(R.id.mainlayoutId);
        findById();
        prepareData();
        if (MDetect.INSTANCE.isUnicode()) {

            itemNameText.setText(Rabbit.zg2uni(getResources().getString(R.string.detailNe)));
            itemQtyText.setText(Rabbit.zg2uni(getResources().getString(R.string.detailQt)));
            itemPriceText.setText(Rabbit.zg2uni(getResources().getString(R.string.detailAm)));
            taxText.setText(Rabbit.zg2uni(getResources().getString(R.string.taxtext)));
            discountTax.setText(Rabbit.zg2uni(getResources().getString(R.string.distext)));
            totalTax.setText(Rabbit.zg2uni(getResources().getString(R.string.total)));
            netTotalTax.setText(Rabbit.zg2uni(getResources().getString(R.string.nettotal)));
            if (SaleItemFragment.check.equals("1")){
                receiveTax.setText(Rabbit.zg2uni(getResources().getString(R.string.receives)));
            }else if (SaleItemFragment.check.equals("2")) {
                receiveTax.setText(Rabbit.zg2uni(getResources().getString(R.string.paid)));
            }
            balancxeTax.setText(Rabbit.zg2uni(getResources().getString(R.string.balances)));
            print.setText(Rabbit.zg2uni(getResources().getString(R.string.print)));

        } else {

            itemNameText.setText(getResources().getString(R.string.detailNe));
            itemQtyText.setText(getResources().getString(R.string.detailQt));
            itemPriceText.setText(getResources().getString(R.string.detailAm));
            taxText.setText(getResources().getString(R.string.taxtext));
            discountTax.setText(getResources().getString(R.string.distext));
            totalTax.setText(getResources().getString(R.string.total));
            netTotalTax.setText(getResources().getString(R.string.nettotal));
            if (SaleItemFragment.check.equals("1")) {
                receiveTax.setText(getResources().getString(R.string.receives));
            }else if (SaleItemFragment.check.equals("2")){
                receiveTax.setText(getResources().getString(R.string.paid));
            }
            balancxeTax.setText(getResources().getString(R.string.balances));
            print.setText(getResources().getString(R.string.print));

        }
        companyName.setText(MainActivity.cname);
        companyAddress.setText(MainActivity.caddress);
        companyPhopne.setText(MainActivity.ccontact);


        /*itemName.setText(getIntent().getExtras().getString("name"));
        itemPrice.setText(getIntent().getExtras().getString("amt"));
        itemQty.setText(getIntent().getExtras().getString("qty"));
        */

        tax.setText(getIntent().getExtras().getString("tax"));
        discount.setText(getIntent().getExtras().getString("discount"));
        total.setText(getIntent().getExtras().getString("total"));
        netTotal.setText(getIntent().getExtras().getString("netTotal"));
        receive.setText(getIntent().getExtras().getString("pay"));
        balancxe.setText(getIntent().getExtras().getString("refund"));
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Bitmap bitmap = getBitmapFromView(nestedScrollView,nestedScrollView.getChildAt(0).getHeight(), nestedScrollView.getChildAt(0).getWidth());
                fileSave(InvoiceActivity.this,bitmap);
                printJobSize3();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void findById() {
        companyName=(TextView)findViewById(R.id.comName);
        companyAddress=(TextView)findViewById(R.id.comAddress);
        companyPhopne=(TextView)findViewById(R.id.comContact);
        itemNameText=(TextView)findViewById(R.id.textItemInvoice);
        itemPriceText=(TextView)findViewById(R.id.textAmytInvoice);
        itemQtyText=(TextView)findViewById(R.id.textQtyInvoice);
        taxText=(TextView)findViewById(R.id.tpriceInvoice);
        discountTax=(TextView)findViewById(R.id.dpriceInvoice);
        totalTax=(TextView)findViewById(R.id.totaltextInvoice);
        netTotalTax=(TextView)findViewById(R.id.nettotaltextInvoice);
        receiveTax=(TextView)findViewById(R.id.receivetextInvoice);
        balancxeTax=(TextView)findViewById(R.id.balancetextInvoice);
        ///////
   //     itemName=(TextView)findViewById(R.id.detailNeInvoice);
   //     itemQty=(TextView)findViewById(R.id.detailQInvoice);
   //     itemPrice =(TextView)findViewById(R.id.detailAInvoice);
        tax=(TextView)findViewById(R.id.taxInvoice);
        discount=(TextView)findViewById(R.id.discountInvoice);
        total=(TextView)findViewById(R.id.totalPriceInvoice);
        netTotal=(TextView)findViewById(R.id.nettotalPriceInvoice);
        receive=(TextView)findViewById(R.id.receivedPaymentInvoice);
        balancxe=(TextView)findViewById(R.id.refundAmtInvoice);
        print=(Button)findViewById(R.id.printInvoice);
        cancel=(Button)findViewById(R.id.cancelPrint);

        recyclerView=(RecyclerView) findViewById(R.id.invoice_recycler);


    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


    private void printJobSize3() {


        Thread t = new Thread() {
            public void run() {

                try {
                    os = MyApplication.getMmOutputStream();
                    String address=MyApplication.getmDeviceAddress();
                    Pointer h = Pointer.NULL;
                    String msg="";
                    h = printerlibs_caysnpos.INSTANCE.CaysnPos_OpenBT2ByConnectA(address);
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

    public void fileSave(Context context,Bitmap bitmap){
        // Bitmap bb=Bitmap.createScaledBitmap(bitmap, 550,bitmap.getHeight(), false);
        Bitmap bb=Bitmap.createScaledBitmap(bitmap,575,bitmap.getHeight(), false);
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
            bb.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,file.getAbsolutePath());


    }

    private void prepareData() {
        vAdapter = new VoucherAdapter(SaleItemFragment.addItemEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(vAdapter);
    }

}
