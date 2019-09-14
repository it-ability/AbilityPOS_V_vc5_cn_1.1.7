package com.mobilepos.abc.abilitypos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Backup.LocalBackup;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

import static android.media.CamcorderProfile.get;


public class ManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //    Button signup, manageUser;
    Button companysetting, exportdata, importdata, backup, restore;

    DB_Controller controller;

    //this two sentence is for backup
    private LocalBackup localBackup;
    public static final int REQUEST_CODE_PERMISSIONS = 2;

    List<String> tableType;
    String type = "Replace";
    int data = 0;
    ImageView imageView;

    public RadioButton overideRadioButton;
    public RadioButton appendRadioButton;
    public boolean override = false;
    public boolean append = false;
    public static RadioButton radio_btn_string,radio_btn_image;
    public static boolean checkedtrue=false,checkfalse=false;
   // public static RadioGroup radioGroup;



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        controller = new DB_Controller(getApplicationContext());
        localBackup = new LocalBackup(ManagementActivity.this);

//        signup = (Button) findViewById(R.id.signup);
//        manageUser = (Button) findViewById(R.id.manageUser);
        imageView = findViewById(R.id.manageimage);
        companysetting = (Button) findViewById(R.id.company_setting);
        exportdata = (Button) findViewById(R.id.export_data);
        importdata = (Button) findViewById(R.id.import_data);
        backup = (Button) findViewById(R.id.export_db);
        restore = (Button) findViewById(R.id.import_db);

        tableType = new ArrayList<>();
        tableType.add("User");
        tableType.add("Item");
        tableType.add("Voucher");

        Spinner importType = (Spinner) findViewById(R.id.importType);
        importType.setOnItemSelectedListener(this);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, tableType);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importType.setAdapter(dateAdapter);

        overideRadioButton = (RadioButton) findViewById(R.id.radioButton1);
        appendRadioButton = (RadioButton) findViewById(R.id.radioButton2);

        radio_btn_string= (RadioButton) findViewById(R.id.radio_btn_string);
        radio_btn_image = (RadioButton) findViewById(R.id.radio_btn_image);

        if (radio_btn_string.isChecked()){
            checkedtrue=true;
        }
        if (radio_btn_image.isChecked()){
            checkfalse=true;
        }
        /*radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                Intent INT1=new Intent(ManagementActivity.this, PrintPreviewActivity.class);
                Intent INT2=new Intent(ManagementActivity.this,ReportSummaryActivity.class);
                Intent INT3=new Intent(ManagementActivity.this,VoucherDetailActivity.class);
                Intent INT4=new Intent(ManagementActivity.this,ReportDetailActivity.class);

                if(checkedId==R.id.radio_btn_string)
                {
                    INT1.putExtra("StringPrint", radio_btn_string.getText());
                    INT2.putExtra("StringPrint", radio_btn_string.getText());
                    INT3.putExtra("StringPrint", radio_btn_string.getText());
                    INT4.putExtra("StringPrint", radio_btn_string.getText());


                }
                else if (checkedId==R.id.radio_btn_image)
                {
                    INT1.putExtra("Image_Print", radio_btn_image.getText());
                    INT2.putExtra("Image_Print", radio_btn_image.getText());
                    INT3.putExtra("Image_Print", radio_btn_image.getText());
                    INT4.putExtra("Image_Print", radio_btn_image.getText());

                }


                startActivityForResult(INT1,1000);
                startActivityForResult(INT2,1000);
                startActivityForResult(INT3,1000);
                startActivityForResult(INT4,1000);
                finishActivity(1000);
                INT1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                INT2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                INT3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                INT4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            }


        });*/







        if (MDetect.INSTANCE.isUnicode()) {
//            signup.setText(Rabbit.zg2uni(getResources().getString(R.string.newuser)));
//            manageUser.setText(Rabbit.zg2uni(getResources().getString(R.string.edituser)));
            companysetting.setText(Rabbit.zg2uni(getResources().getString(R.string.companysetting)));
            exportdata.setText(Rabbit.zg2uni(getResources().getString(R.string.exportdata)));
            importdata.setText(Rabbit.zg2uni(getResources().getString(R.string.importdata)));
            this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.menu_management)));
        } else {
//            signup.setText(getResources().getString(R.string.newuser));
//            manageUser.setText(getResources().getString(R.string.edituser));
            companysetting.setText(getResources().getString(R.string.companysetting));
            exportdata.setText(getResources().getString(R.string.exportdata));
            importdata.setText(getResources().getString(R.string.importdata));
            this.setTitle(getResources().getString(R.string.menu_management));
        }

        overideRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Replace";
                override = true;
                append = false;

            }
        });
        appendRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Append";
                override = false;
                append = true;

            }
        });

/*
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, SingupActivity.class);
                startActivity(intent);
            }
        });

        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, ManageUserActivity.class);
                startActivity(intent);

            }
        });
*/

        companysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagementActivity.this, CompanySettingActivity.class);
                startActivity(intent);
            }
        });

        exportdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    exportUserInCSV();
                    exportItemInCSV();
                    exportVoucherDInCSV();
                    exportVoucherHInCSV();

                    Toasty.success(getApplicationContext(), "Export Successfully.....", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        importdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    if (data == 0 && type.equals("Replace")) {
                        controller.deleteUser();
                        importUserDatas();
                    } else if (data == 0 && type.equals("Append")) {
                        importUserDatas();
                    } else if (data == 1 && type.equals("Replace")) {
                        controller.deleteItem();
                        importItemDatas();
                    } else if (data == 1 && type.equals("Append")) {
                        importItemDatas();
                    } else if (data == 2 && type.equals("Replace")) {
                        controller.deleteVoucherD();
                        controller.deleteVoucherH();
                        importVoucherDDatas();
                        importVoucherHDatas();
                    } else if (data == 2 && type.equals("Append")) {
                        importVoucherDDatas();
                        importVoucherHDatas();
                    } else {
                        Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toasty.success(getApplicationContext(), "Export Successfully.....", Toast.LENGTH_SHORT).show();
                }




                /*importUserDatas();
                importItemDatas();
                importVoucherDDatas();
                importVoucherHDatas();*/

                // My Input Methods
              /*  try {

                    importUserDatas();
                    importItemDatas();
                    importVoucherDDatas();
                    importVoucherHDatas();

                    Toasty.success(getApplicationContext(), "Import Successfully.....", Toast.LENGTH_SHORT).show();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }*/



            }
        });

        //for backup database
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                localBackup.performBackup(controller, outFileName);
            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localBackup.performRestore(controller);
            }
        });

    }



    public void exportUserInCSV() throws IOException {
        {
            String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + "EXPORT" + File.separator;
            if (outFileName.equals(Environment.MEDIA_MOUNTED));
            File folder = new File(outFileName);
/*
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Ability/Data");
*/
//            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            final String filename = folder.toString() + "/" + "User.csv";

            // show waiting screen
            CharSequence contentTitle = getString(R.string.app_name);
            final ProgressDialog progDailog = ProgressDialog.show(
                    ManagementActivity.this, "Export", "Export User.....",
                    true);//please wait
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {


                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename,true);

                        Cursor cursor = controller.getUserDatas();


                        if (cursor.moveToFirst()) {
                            do {
                                fw.append(cursor.getString(0));
                                fw.append(',');

                                fw.append(cursor.getString(1));
                                fw.append(',');

                                fw.append(cursor.getString(2));
                                fw.append(',');

                                fw.append(cursor.getString(3));
                                fw.append(',');

                                fw.append(cursor.getString(4));
                                fw.append(',');

                                fw.append(cursor.getString(5));
                                fw.append(',');

                                fw.append(cursor.getString(6));
                                fw.append(',');

                                fw.append(cursor.getString(7));
                                fw.append(',');

                                fw.append(cursor.getString(8));
                                fw.append(',');

                                fw.append(cursor.getString(9));
                                fw.append(',');

                                fw.append(cursor.getString(10));
                                fw.append(',');

                                fw.append('\n');

                            } while (cursor.moveToNext());
                        }
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }

                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                    progDailog.dismiss();
                }
            }.start();

        }

    }

    public void exportItemInCSV() throws IOException {
        {

            String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + "EXPORT" + File.separator;
            File folder = new File(outFileName);

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            final String filename = folder.toString() + "/" + "Item.csv";

            // show waiting screen
            CharSequence contentTitle = getString(R.string.app_name);
            final ProgressDialog progDailog = ProgressDialog.show(
                    ManagementActivity.this, "Export", "Export Item.....",
                    true);//please wait
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {


                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename,true);

                        Cursor cursor = controller.getItemDatas();

                        if (cursor.moveToFirst()) {
                            do {
                                fw.append(cursor.getString(0));
                                fw.append(',');

                                fw.append(cursor.getString(1));
                                fw.append(',');

                                fw.append(cursor.getString(2));
                                fw.append(',');

                                fw.append(cursor.getString(3));
                                fw.append(',');

                                fw.append(cursor.getString(4));
                                fw.append(',');

                                fw.append(cursor.getString(5));
                                fw.append(',');

                                fw.append(cursor.getString(6));
                                fw.append(',');

                                fw.append(cursor.getString(7));
                                fw.append(',');

                                fw.append(cursor.getString(8));
                                fw.append(',');

                                fw.append('\n');

                            } while (cursor.moveToNext());
                        }
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }

                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                    progDailog.dismiss();
                }
            }.start();

        }

    }

    public void exportVoucherDInCSV() throws IOException {
        {

            String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + "EXPORT" + File.separator;
            File folder = new File(outFileName);

/*
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Ability/Data");
*/
//            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            final String filename = folder.toString() + "/" + "VoucherD.csv";

            // show waiting screen
            CharSequence contentTitle = getString(R.string.app_name);
            final ProgressDialog progDailog = ProgressDialog.show(
                    ManagementActivity.this, "Export", "Export voucherD.....",
                    true);//please wait
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {


                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename,true);

                        Cursor cursor = controller.getVoucherDDatas();

                        if (cursor.moveToFirst()) {
                            do {
                                fw.append(cursor.getString(0));
                                fw.append(',');

                                fw.append(cursor.getString(1));
                                fw.append(',');

                                fw.append(cursor.getString(2));
                                fw.append(',');

                                fw.append(cursor.getString(3));
                                fw.append(',');

                                fw.append(cursor.getString(4));
                                fw.append(',');

                                fw.append(cursor.getString(5));
                                fw.append(',');

                                fw.append(cursor.getString(6));
                                fw.append(',');

                                fw.append(cursor.getString(7));
                                fw.append(',');

                                fw.append(cursor.getString(8));
                                fw.append(',');

                                fw.append(cursor.getString(9));
                                fw.append(',');

                                fw.append('\n');

                            } while (cursor.moveToNext());
                        }
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }

                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                    progDailog.dismiss();
                }
            }.start();

        }

    }

    public void exportVoucherHInCSV() throws IOException {
        {

            String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + "EXPORT" + File.separator;
            File folder = new File(outFileName);

/*
            File folder = new File(Environment.getExternalStorageDirectory()
                    + "/Ability/Datas");
*/
//            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

            boolean var = false;
            if (!folder.exists())
                var = folder.mkdir();

            final String filename = folder.toString() + "/" + "VoucherH.csv";

            // show waiting screen
            CharSequence contentTitle = getString(R.string.app_name);
            final ProgressDialog progDailog = ProgressDialog.show(
                    ManagementActivity.this, "Export", "Export voucherH.....",
                    true);//please wait
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {


                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename,true);

                        Cursor cursor = controller.getVoucherHDatas();

                        if (cursor.moveToFirst()) {
                            do {



                                fw.append(cursor.getString(0));
                                fw.append(',');


                                fw.append(cursor.getString(1));
                                fw.append(',');

                                fw.append(cursor.getString(2));
                                fw.append(',');

                                fw.append(cursor.getString(3));
                                fw.append(',');

                                fw.append(cursor.getString(4));
                                fw.append(',');

                                fw.append(cursor.getString(5));
                                fw.append(',');

                                fw.append(cursor.getString(6));
                                fw.append(',');

                                fw.append(cursor.getString(7));
                                fw.append(',');

                                fw.append(cursor.getString(8));
                                fw.append(',');

                                fw.append(cursor.getString(9));
                                fw.append(',');

                                fw.append(cursor.getString(10));
                                fw.append(',');

                                fw.append(cursor.getString(11));
                                fw.append(',');

                                fw.append(cursor.getString(12));
                                fw.append(',');
                                fw.append('\n');

                            } while (cursor.moveToNext());
                        }
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }

                        // fw.flush();
                        fw.close();

                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessage(0);
                    progDailog.dismiss();
                }
            }.start();

        }

    }

    public void importUserDatas() {
        Boolean success = true;

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Ability MPosEXPORT/");


        // Not Update path
//        File folder = new File(Environment.getExternalStorageDirectory()
//                      + "/MarketingEXPORT");
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

        final String filename = folder.toString() + "/" + "User.csv";



        try {

            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line

              controller.activateAccount(nextLine[1], nextLine[2], nextLine[3], nextLine[4],
                        nextLine[5], nextLine[6], nextLine[7], nextLine[8], nextLine[9]);
            }
            Toasty.success(ManagementActivity.this, "Import User Data.....", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
              Toast.makeText(getApplicationContext(), "User.csv not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void importItemDatas() {
        boolean success = true;

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Ability MPosEXPORT/");


        // Not Update path
//        File folder = new File(Environment.getExternalStorageDirectory()
//                      + "/MarketingEXPORT");
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
        final String filename = folder.toString() + "/" + "Item.csv";


        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line

                success = controller.add_item(nextLine[1], nextLine[2], nextLine[3], Double.valueOf(nextLine[4]),
                        Double.valueOf(nextLine[5]), nextLine[6], nextLine[7], nextLine[8]);
            }
            if (success) {
              //  Toasty.success(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Toasty.success(ManagementActivity.this, "Import Item Data.....", Toast.LENGTH_SHORT).show();
            } else if (!success) {
                Toast.makeText(getApplicationContext(), "Insert Fail,Insert Code is already exit!!!!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Item.csv not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void importVoucherDDatas() {


        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Ability MPosEXPORT/");

        // Not Update path
//        File folder = new File(Environment.getExternalStorageDirectory()
//                      + "/MarketingEXPORT");
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));
        final String filename = folder.toString() + "/" + "VoucherD.csv";
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line

                controller.insert_vourherD(nextLine[0], nextLine[1], nextLine[2], Double.valueOf(nextLine[3]),
                        Double.valueOf(nextLine[4]), Double.valueOf(nextLine[5]),
                        Double.valueOf(nextLine[6]), (nextLine[7]), nextLine[8], nextLine[9]);
            }
            Toasty.success(ManagementActivity.this, "Import VoucherD Data.....", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "VoucherD.csv not Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void importVoucherHDatas() {

//My change is filepath
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Ability MPosEXPORT/");

// Not Update path
//        File folder = new File(Environment.getExternalStorageDirectory()
//                      + "/MarketingEXPORT");
//        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));


        final String filename = folder.toString() + "/" + "VoucherH.csv";
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {


                // nextLine[] is an array of values from the line

                controller.insert_voucherH(nextLine[0], Integer.valueOf(nextLine[1]), nextLine[2], nextLine[3],
                        Double.valueOf(nextLine[4]), Double.valueOf(nextLine[5]), nextLine[6],
                        Double.valueOf(nextLine[7]), Double.valueOf(nextLine[8]),
                        Double.valueOf(nextLine[9]), Double.valueOf(nextLine[10]), nextLine[11], nextLine[12]);
            }
            Toasty.success(ManagementActivity.this, "Import VoucherH Data.....", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "VoucherH.csv not Found", Toast.LENGTH_SHORT).show();
        }


    }

    public void onClickChangePassword(View view) {
        Intent intent = new Intent(ManagementActivity.this, ChangeCompanyPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        data = position;
        Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}
