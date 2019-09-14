package com.mobilepos.abc.abilitypos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    private final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 3;
    TelephonyManager telephonyManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        if (isPermissionGrandOrRequestPermission(Manifest.permission.READ_PHONE_STATE,PERMISSIONS_REQUEST_READ_PHONE_STATE)) {
            showDeviceInfo();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean isPermissionGrandOrRequestPermission(String permission, int request){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        request);

                // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        }else{
            return true;
        }
    }

    public void phoneCallActionView(View view){
        Intent callViewIntent = new Intent(Intent.ACTION_VIEW);
        callViewIntent.setData(Uri.parse("tel:" + getString(R.string.phone_no)));
        startActivity(callViewIntent);
    }

    /***
     * Also you need to give CALL_PHONE permission in AndroidManifest.xml to send message
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     * @param view onClickView
     */
    public void directPhoneCall(View view){
        if(isPermissionGrandOrRequestPermission(Manifest.permission.CALL_PHONE,MY_PERMISSIONS_REQUEST_CALL_PHONE)) {
            phoneCallAction();
        }
    }

    public void sendSMSActionView(View view){
        Intent sendSMS = new Intent(Intent.ACTION_SEND);
        sendSMS.setData(Uri.parse("sms:" + getString(R.string.phone_no)));
        sendSMS.putExtra("sms_body", "Dear Sir! How are you?");
        startActivity(sendSMS);
    }

    /***
     * Also you need to give SEND_SMS permission in AndroidManifest.xml to send message
     * <uses-permission android:name="android.permission.SEND_SMS" />
     * @param view onClickView
     */
    public void directSendSMSAction(View view){
        if(isPermissionGrandOrRequestPermission(Manifest.permission.SEND_SMS,MY_PERMISSIONS_REQUEST_SEND_SMS)) {
            directSendSMS();
        }
    }

    private void directSendSMS(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(getString(R.string.phone_no), null, "Dear Sir! How are you", null, null);
            Toast.makeText(getApplicationContext(), getString(R.string.msg_sent),
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),getString(R.string.msg_sent_fail),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void phoneCallAction(){
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + getString(R.string.phone_no)));
            startActivity(callIntent);
        }catch (SecurityException se){
            Log.e(TAG,"directPhoneCall : SecurityException caught",se);
        }
    }

    public void openMail(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"ability.itsolution@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        i.putExtra(Intent.EXTRA_TEXT   , "Sent from " + getString(R.string.app_name));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    phoneCallAction();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"This action need permission CALL_PHONE!",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    directSendSMS();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"This action need permission SEND_SMS!",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    showDeviceInfo();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"This action need permission SEND_SMS!",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            // other 'case' lines to check for other
            // permissions this app might request
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

    private String getTelephonyDeviceInfo() throws SecurityException{
        String telephonyDeviceInfo = "\n";
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyDeviceInfo = telephonyDeviceInfo.concat("IMEI : ") + telephonyManager.getDeviceId() + "\n";
        telephonyDeviceInfo = telephonyDeviceInfo.concat("Software Version : ") + telephonyManager.getDeviceSoftwareVersion() + "\n";
        telephonyDeviceInfo = telephonyDeviceInfo.concat("Network Operator : ") + telephonyManager.getNetworkOperator()+"\n";
        telephonyDeviceInfo = telephonyDeviceInfo.concat("Sim Operator : ") + telephonyManager.getSimOperator() + "\n";
        String phoneNumber = telephonyManager.getLine1Number();
        telephonyDeviceInfo = telephonyDeviceInfo.concat("Phone Number : ").concat(phoneNumber == null ? "not detected" : phoneNumber);
        return telephonyDeviceInfo;
    }


    private LinearLayout getLinearLayout(String label,String value){
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setWeightSum(2);
        TextView labelTextView = new TextView(this);
        labelTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f));
        labelTextView.setText(label);
        labelTextView.setTypeface(null, Typeface.BOLD);
        labelTextView.setPadding(5,5,5,5);
        linearLayout.addView(labelTextView);
        TextView valueTextView = new TextView(this);
        valueTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f));
        valueTextView.setText(value);
        valueTextView.setPadding(5,5,5,5);
        linearLayout.addView(valueTextView);
        return linearLayout;
    }

    private void showDeviceInfo(){
        CardView deviceInfoCardView = findViewById(R.id.device_info);
        deviceInfoCardView.removeAllViews();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(getLinearLayout("Telephony Info",getTelephonyDeviceInfo()));
        for(int i = 0 ; i< deviceInfoList.size() ; i++) {
            linearLayout.addView(getLinearLayout(deviceInfoList.get(i).first, deviceInfoList.get(i).second));
        }
        deviceInfoCardView.addView(linearLayout);
    }
    List<Pair<String,String>> deviceInfoList = Arrays.asList(new Pair<>("System Info",System.getProperties().toString()),

            new Pair<>("Serial",Build.SERIAL),
            new Pair<>("Model",Build.MODEL),
            new Pair<>("Id",Build.ID),
            new Pair<>("Manufacture",Build.MANUFACTURER),
            new Pair<>("Brand",Build.BRAND),
            new Pair<>("Type",Build.TYPE),
            new Pair<>("User",Build.USER),
            new Pair<>("Incremental",Build.VERSION.INCREMENTAL),
            new Pair<>("SDK",Build.VERSION.SDK),
            new Pair<>("Board",Build.BOARD),
            new Pair<>("FingerPrint",Build.FINGERPRINT),
            new Pair<>("Version Code",Build.VERSION.RELEASE),
            new Pair<>("Host",Build.HOST),
            new Pair<>("Base",String.valueOf(Build.VERSION_CODES.BASE)));
    }
