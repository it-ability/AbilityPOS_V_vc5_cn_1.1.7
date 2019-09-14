package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.caysn.printerlibs.printerlibs_caysnpos.printerlibs_caysnpos;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.AddItemFragment;
import com.mobilepos.abc.abilitypos.Fragment.ManageItemFragment;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.PrintService.DeviceListActivity;
import com.mobilepos.abc.abilitypos.Service.ScreenReceiver;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener ,Runnable{

    NavigationView navigationView;
    protected static final String TAG = "TAG";

    // For bluetooth 
    private static final int REQUEST_CONNECT_DEVICE = 1,REQUEST_ENABLE_BT = 2;
    TextView blutetoothtext;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothDevice  mBluetoothDevice;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket,tempBs;
    OutputStream mmOutputStream;

    public static boolean user = false;
    public static String  companyId = "";

    //133, 138, 337, 348, 370, 380  AddItemFrag
    //
    public static String  name = "";
    public static String  role = "";
    public static Integer userId = 0;
    public static String mDeviceAddress="";
    boolean doubleBackToExitPressedOnce = false;
    private Fragment currentMainFragment = null;
    boolean cust = false;
    BroadcastReceiver mReceiver;
    public static int menu_number;

    DB_Controller controller;
    SharedPreferences sharedPreferences;
    public static String cname, caddress, ccontact, ctax = "0", cdiscount = "0", barcode = "";
    public static int printerSize;

    Fragment fragment = null;Class fragmentClass = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new DB_Controller(getApplicationContext());
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        user = sharedPreferences.getBoolean("session", false);

        if (user == true) {
            sessionTimeOut();
        }
        //for keyboard hidden
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        NavigationView navigationView = findViewById(R.id.nav_view);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem add_item = menu.findItem(R.id.add_item);
        MenuItem manage_item = menu.findItem(R.id.manage_item);
        MenuItem sale_item = menu.findItem(R.id.sale_item);
        MenuItem purchase_item=menu.findItem(R.id.purchase_item);
      //  MenuItem creceive=menu.findItem(R.id.creceive);
      //  MenuItem cpayment=menu.findItem(R.id.cpayment);
        MenuItem daily_sale = menu.findItem(R.id.daily_sale);
        MenuItem daily_purchase=menu.findItem(R.id.daily_purchase);
    //    MenuItem daily_cash_receive=menu.findItem(R.id.daily_cash_receive);
    //    MenuItem daily_cash_payment=menu.findItem(R.id.daily_cash_payment);
        MenuItem v_detail = menu.findItem(R.id.v_detail);
        MenuItem other=menu.findItem(R.id.other);
        MenuItem printer = menu.findItem(R.id.connect_printer);
        MenuItem manage = menu.findItem(R.id.manage);
        MenuItem contact=menu.findItem(R.id.contact);
        MenuItem logoutconfirm = menu.findItem(R.id.logoutconfirm);

        SharedPreferences sharedPreferences2 = getSharedPreferences("lang", MODE_PRIVATE);

        if (sharedPreferences2.contains("mm")) {
            setLocale("mm");
        } else if (sharedPreferences2.contains("en")) {
            setLocale("en");
        } else {
            setLocale("en");
        }

        if (MDetect.INSTANCE.isUnicode()) {
            add_item.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_add)));
            manage_item.setTitle(Rabbit.zg2uni(getResources().getString(R.string.manage_item)));
            sale_item.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_sale)));
            purchase_item.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_purchase)));
        //    creceive.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_cash_receive)));
        //    cpayment.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_cash_payment)));
            //  account_item.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_account)));
            daily_sale.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_sale)));
            daily_purchase.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_purchase)));
        //    daily_cash_receive.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_receive)));
        //    daily_cash_payment.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_cash_payment)));
            //  daily_account.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_report_account)));
            v_detail.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_reportdetail)));
            other.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_other)));
            printer.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_printer)));
            manage.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_management)));
            contact.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_contact)));
            logoutconfirm.setTitle(Rabbit.zg2uni(getResources().getString(R.string.logouts)));

        } else {
            add_item.setTitle(getResources().getString(R.string.menu_add));
            manage_item.setTitle(getResources().getString(R.string.manage_item));
            sale_item.setTitle(getResources().getString(R.string.menu_search_sale));
            purchase_item.setTitle(getResources().getString(R.string.menu_search_purchase));
        //    creceive.setTitle(getResources().getString(R.string.menu_search_cash_receive));
        //    cpayment.setTitle(getResources().getString(R.string.menu_search_cash_payment));
            // account_item.setTitle(getResources().getString(R.string.menu_search_account));
            daily_sale.setTitle(getResources().getString(R.string.menu_report_sale));
            // daily_account.setTitle(getResources().getString(R.string.menu_report_account));
            daily_purchase.setTitle(getResources().getString(R.string.menu_report_purchase));
        //    daily_cash_receive.setTitle(getResources().getString(R.string.menu_report_cash_receive));
        //    daily_cash_payment.setTitle(getResources().getString(R.string.menu_report_cash_payment));
            v_detail.setTitle(getResources().getString(R.string.menu_reportdetail));
            other.setTitle(getResources().getString(R.string.menu_other));
            printer.setTitle(R.string.menu_printer);
            manage.setTitle(getResources().getString(R.string.menu_management));
            contact.setTitle(getResources().getString(R.string.menu_contact));
            logoutconfirm.setTitle(getResources().getString(R.string.logouts));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        user = sharedPreferences.getBoolean("session", false);

        SharedPreferences sharedPreferencesCompanyId = getSharedPreferences("CompanyId", MODE_PRIVATE);
        companyId = sharedPreferencesCompanyId.getString("CompanyId","");

    //    companyId = sharedPreferences.getString("cmid", "");


/*
        email = sharedPreferences.getString("email", "");
        name = sharedPreferences.getString("name", "");
*/
        name = sharedPreferences.getString("UserName", "");
        role = sharedPreferences.getString("role", "");
        userId = sharedPreferences.getInt("userId", 0);

        View headerView = navigationView.getHeaderView(0);
        //   TextView txt_email = headerView.findViewById(R.id.txt_email);
        TextView txt_name = headerView.findViewById(R.id.txt_name);
        txt_name.setText(name);


        String dates = "";
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DATE);
        dates = y + "/" + m + "/" + d;

        if (user == true) {
            view();
        } else {
            viewout();
        }

        SharedPreferences sharedPreferences1 = getSharedPreferences("CompanySetting", MODE_PRIVATE);
        cname = sharedPreferences1.getString("name", "");
        caddress = sharedPreferences1.getString("address", "");
        ccontact = sharedPreferences1.getString("contact", "");
        ctax = sharedPreferences1.getString("tax", "0");
        cdiscount = sharedPreferences1.getString("discount", "0");
        printerSize = sharedPreferences1.getInt("printer_size", 0);

    }

    private void sessionTimeOut() {
        if (sharedPreferences.contains("logout_time")) {
            String strLogout = sharedPreferences.getString("logout_time", "");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH");
            String loginTime = formatTime.format(new Date());
            int result = Integer.valueOf(strLogout) - Integer.valueOf(loginTime);
            if (result >= 1) {
                logout();
                Toast.makeText(getApplicationContext(), "Session Time out", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void view() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.READ_PHONE_STATE};
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (MDetect.INSTANCE.isUnicode()) {
            setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_sale)));
        } else {
            setTitle(getResources().getString(R.string.menu_search_sale));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentManager fragmentManager = getFragmentManager();
        // getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, new SaleItemFragment()).commit();
        SaleItemFragment.check = "1";
        SaleItemFragment.addItemEntry.clear();
    }

    private void viewout() {
        Intent intent = new Intent(MainActivity.this, LoginCompanyActivity.class);
        startActivity(intent);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
            finish();

        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;

            if (MDetect.INSTANCE.isUnicode()) {
                Toasty.info(this, Rabbit.zg2uni(getResources().getString(R.string.back)), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toasty.info(this, getResources().getString(R.string.back), Toast.LENGTH_SHORT).show();
                finish();
            }


        } else {
            super.onBackPressed();
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*hsulattaung*/
        /* to change menu when click barcode */
        sharedPreferences =  getSharedPreferences("MyPref",MODE_PRIVATE);
        menu_number = sharedPreferences.getInt("clickmenu",0);
        System.out.println("Click Menu====="+menu_number);

        if (id == R.id.barcode) {
            if(menu_number == 0) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new SaleItemFragment()).commit();
                SaleItemFragment.check = "1";
            }
            if (menu_number == 1 && AddItemFragment.cType.equals("Item")) {
                FragmentManager fragmentManager = getFragmentManager(); // getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new AddItemFragment()).commit();
            }

            if (menu_number == 3){
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, new SaleItemFragment()).commit();
                SaleItemFragment.check = "2";
            }
            /*hsulattaung*/
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("This is a scanner for item");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();

        } else if (id == R.id.language) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.border);
            dialog.setContentView(R.layout.language);
            dialog.show();
            RadioButton radio_english = dialog.findViewById(R.id.radio_english);
            RadioButton radio_myanmar = dialog.findViewById(R.id.radio_myanmar);

            if (MDetect.INSTANCE.isUnicode()) {
                dialog.setTitle(Rabbit.zg2uni(getResources().getString(R.string.language)));
                radio_english.setText(Rabbit.zg2uni(getResources().getString(R.string.english)));
                radio_myanmar.setText(Rabbit.zg2uni(getResources().getString(R.string.myanmar)));
            } else {
                dialog.setTitle(getResources().getString(R.string.language));
                radio_english.setText(getResources().getString(R.string.english));
                radio_myanmar.setText(getResources().getString(R.string.myanmar));
            }

            final SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("lang", MODE_PRIVATE);
            if (sharedpreferences.contains("mm")) {
                radio_english.setChecked(false);
                radio_myanmar.setChecked(true);
            } else if (sharedpreferences.contains("en")) {
                radio_english.setChecked(true);
                radio_myanmar.setChecked(false);
            } else {
                radio_english.setChecked(true);
                radio_myanmar.setChecked(false);
            }

            radio_english.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        setLocaleChange("en");
                        SharedPreferences.Editor editor1 = sharedpreferences.edit();
                        editor1.clear();
                        editor1.apply();
                        editor1.putString("en", "en");
                        editor1.apply();
                        dialog.cancel();

                    }
                }
            });

            radio_myanmar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        setLocaleChange("mm");
                        SharedPreferences.Editor editor2 = sharedpreferences.edit();
                        editor2.clear();
                        editor2.apply();
                        editor2.putString("mm", "mm");
                        editor2.apply();
                        dialog.cancel();
                    }
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (user == true) {
            sessionTimeOut();
        }
    }

    @Override
    protected void onResume() {
        // ONLY WHEN SCREEN TURNS ON
        if (ScreenReceiver.wasScreenOn) {
            // THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
            logout();
            ScreenReceiver.wasScreenOn = false;
            Toast.makeText(getApplicationContext(), "Session expired", Toast.LENGTH_LONG).show();
        } else {
            // THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        }
        super.onResume();
        if (user == true) {
            sessionTimeOut();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (user == true) {
            sessionLogoutTime();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        SharedPreferences mypref = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        switch (id){

            case R.id.add_item:{
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu",1);
                editor.apply();
                fragmentClass = AddItemFragment.class;
                fragmentReplace();
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_add)));
                } else {
                    setTitle(R.string.menu_add);
                }}break;


            case R.id.manage_item:{
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu",2);
                editor.apply();
                fragmentClass = ManageItemFragment.class;
                fragmentReplace();
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.manage_item)));
                } else {
                    setTitle(R.string.manage_item);
                }}break;



           /* case R.id.creceive:{
                SaleItemFragment.addItemEntry.clear();
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu",0);
                editor.apply();
                fragmentClass = SaleItemFragment.class;
                fragmentReplace();
                SaleItemFragment.check = "0"; //cash receive
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_cash_receive)));
                } else {
                    setTitle(R.string.menu_search_cash_receive);
                }}break;*/



           /* case R.id.cpayment:{
                SaleItemFragment.addItemEntry.clear();
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu",0);
                editor.apply();
                fragmentClass = SaleItemFragment.class;
                fragmentReplace();
                SaleItemFragment.check = "3"; //cash payment
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_cash_payment)));
                } else {
                    setTitle(R.string.menu_search_cash_payment);
                }}break;
*/


            case R.id.sale_item:{
                SaleItemFragment.addItemEntry.clear();
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu",0);
                editor.apply();
                fragmentClass = SaleItemFragment.class;
                fragmentReplace();
                SaleItemFragment.check = "1";  // Sale
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_sale)));
                } else {
                    setTitle(R.string.menu_search_sale);
                }}break;


            case R.id.purchase_item:{
                SaleItemFragment.addItemEntry.clear();
                SharedPreferences.Editor editor = mypref.edit();
                editor.putInt("clickmenu", 3); // 0 to 3 for barcode scan result
                editor.apply();
                fragmentClass = SaleItemFragment.class;
                fragmentReplace();
                SaleItemFragment.check = "2"; // Purchase
                if (MDetect.INSTANCE.isUnicode()) {
                    setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_search_purchase)));
                } else {
                    setTitle(R.string.menu_search_purchase);
                }}break;




          /*  case R.id.daily_cash_receive:{
                SaleItemFragment.check="0";
                Intent intent = new Intent(MainActivity.this, ReportSummaryActivity.class);
                startActivity(intent);
            }break;*/


           /* case R.id.daily_cash_payment:{
                SaleItemFragment.check="3";
                Intent intent = new Intent(MainActivity.this, ReportSummaryActivity.class);
                startActivity(intent);
            }break;*/



            case R.id.daily_sale:{
                SaleItemFragment.check="1";
                Intent intent = new Intent(MainActivity.this, ReportSummaryActivity.class);
                startActivity(intent);
            }break;


            case R.id.daily_purchase:{

                SaleItemFragment.check="2";
                Intent intent = new Intent(MainActivity.this, ReportSummaryActivity.class);
                startActivity(intent);
            }break;



            case R.id.v_detail:{

                Intent intent = new Intent(MainActivity.this, ReportDetailActivity.class);
                startActivity(intent);
                }break;


            case R.id.contact:{
                Intent intent=new Intent(MainActivity.this,ContactActivity.class);
                startActivity(intent);
            }break;


            case R.id.manage:{
                Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
                startActivity(intent);
            }break;


            case R.id.connect_printer:{
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    if (MDetect.INSTANCE.isUnicode()) {
                        blutetoothtext.setText(Rabbit.zg2uni(getResources().getString(R.string.bluetooth_text3)));
                    } else {
                        blutetoothtext.setText(getResources().getString(R.string.bluetooth_text3));
                    }
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(MainActivity.this,DeviceListActivity.class);
                        startActivityForResult(connectIntent,REQUEST_CONNECT_DEVICE);
                    }}

            }break;


            case R.id.logoutconfirm:{
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                if (MDetect.INSTANCE.isUnicode()) {
                    alertDialogBuilder.setMessage(Rabbit.zg2uni(getResources().getString(R.string.logout)));
                    alertDialogBuilder.setPositiveButton(Rabbit.zg2uni(getResources().getString(R.string.ok)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    logout();
                                }
                            });

                    alertDialogBuilder.setNegativeButton(Rabbit.zg2uni(getResources().getString(R.string.cancel)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else {
                    alertDialogBuilder.setMessage(getResources().getString(R.string.logout));
                    alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    logout();
                                }
                            });

                    alertDialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }break;
        }
        return true;
    }
    public void fragmentReplace(){
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            currentMainFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void logout() {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("session", false);
        editor.remove("password");
        editor.remove("IMEINo");
        editor.remove("logout_time");
        editor.apply();
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle mExtra = data.getExtras();
                    mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    if (mDeviceAddress.equals("DC:0D:30:70:87:53")){
                        Toasty.success(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
                        MyApplication.setmDeviceAddress("");
                    }else {
                        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                        MyApplication.setmDeviceAddress(mDeviceAddress);
                        mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
                        Thread mBlutoothConnectThread = new Thread(this);
                        mBlutoothConnectThread.start();
                    }
                  /*  mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                   // MyApplication.setmDeviceAddress(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();*/

                }
                break;

            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(MainActivity.this, "Message", Toast.LENGTH_SHORT).show();
                }break;

            default: break;

        }

        System.out.println("onActivityResult" + requestCode + "   " + resultCode + "     " + data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            System.out.println("Result:::::::::" + result.getContents());
            if (result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                sharedPreferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                menu_number = sharedPreferences.getInt("clickmenu", 0);
                System.out.println("MenuNumberis--------" + menu_number);

                barcode = result.getContents();
                if (menu_number == 0) {
                    SaleItemFragment.search_item.setText(result.getContents());
                }
                if (menu_number == 1) {
                    // AddItemFragment.itemCode.setText(result.getContents());
                    AddItemFragment.itemCode.setText(barcode);
                    AddItemFragment.itemCode.clearComposingText();
                }
                if (menu_number == 2) {
                    ManageItemFragment.search_item.setText(result.getContents());
                }
                if (menu_number == 3){
                    SaleItemFragment.search_item.setText(result.getContents());
                }
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }}

    private void setLocale(String language) {
        Locale myLocale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);
    }

    private void setLocaleChange(String language) {
        Locale myLocale = new Locale(language);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);
        startActivity(new Intent(MainActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }


    private void sessionLogoutTime() {
        SimpleDateFormat formatTime = new SimpleDateFormat("HH");
        String logoutTime = formatTime.format(new Date());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("logout_time");
        editor.apply();
        editor.putString("logout_time", logoutTime);
        editor.apply();
    }

    public void run() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
                    mBluetoothAdapter.cancelDiscovery();
                    if (mDeviceAddress.equals("DC:0D:30:70:87:53")){
                        Toasty.success(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
                        MyApplication.setmDeviceAddress("");

                        mHandler.sendEmptyMessage(0);
                    }else
                        {
                        mBluetoothSocket.connect();
                        mmOutputStream=mBluetoothSocket.getOutputStream();
                        mHandler.sendEmptyMessage(0);
                        MyApplication.setMmOutputStream(mmOutputStream);
                        System.out.println("@#$%^&*()"+mBluetoothDevice.getAddress());
                        }

                    /*mBluetoothSocket.connect();
                    mmOutputStream=mBluetoothSocket.getOutputStream();
                    MyApplication.setMmOutputStream(mmOutputStream);
                    MyApplication.setmDeviceAddress(mBluetoothDevice.getAddress());
                    mHandler.sendEmptyMessage(0);
                    System.out.println("@#$%^&*()"+mBluetoothDevice.getAddress());*/

                } catch (IOException eConnectException) {
                    Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
                    closeSocket(mBluetoothSocket);
                    return;
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint({"ResourceAsColor", "ResourceType"})
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toasty.success(MainActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();

        }
    };




}