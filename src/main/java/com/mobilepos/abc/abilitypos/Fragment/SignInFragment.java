package com.mobilepos.abc.abilitypos.Fragment;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.MainActivity;
import com.mobilepos.abc.abilitypos.Model.RegisterUser;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.RegisterActivityMe;
import com.mobilepos.abc.abilitypos.checkconnection.ConnectivityReceiver;
import com.mobilepos.abc.abilitypos.retrofit.ApiService;
import com.mobilepos.abc.abilitypos.retrofit.RetrofitClient;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.Calendar;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class SignInFragment extends Fragment {

    private TextView toolbar_title;
    private AppCompatEditText myPassword;
    private AppCompatEditText myUserName;
    private TextView register;

    Button signIn, signup;
    String active = "", role = "";
    private boolean sessionUser;

    DB_Controller controller;
    View view;
    SharedPreferences sharedPreferences;
    TelephonyManager telephonyManager;
    long IMEI;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    android.support.v4.app.Fragment fragment;


    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        myUserName = view.findViewById(R.id.edt_user_name);
        myPassword = view.findViewById(R.id.edt_password);
        signIn = view.findViewById(R.id.signIn);
        register = view.findViewById(R.id.text_register);
        sharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
        sessionUser = sharedPreferences.getBoolean("session", false);

        if (MainActivity.cname.equals("")) {
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("CompanySetting", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("name", "Shop Name");
            editor.putString("address", "Shop Address");
            editor.putString("contact", "09000000000");
            editor.putString("tax", "0");
            editor.putString("discount", "0");
            editor.apply();
        }

        ShimmerTextView shimmer_tv = view.findViewById(R.id.shimmer_tv);
        Shimmer shimmer = new Shimmer();
        shimmer.setRepeatCount(100)
                .setDuration(2500)
                .setStartDelay(0)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR)
                .setAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        shimmer.start(shimmer_tv);

        controller = new DB_Controller(getActivity());
//        username = view.findViewById(R.id.name_editText);
//        password = view.findViewById(R.id.pass_editText);
//        signin = view.findViewById(R.id.signin);
//        signup = (Button) view.findViewById(R.id.signup);


        boolean user = controller.defaultUser();
        if (!user == true) {
            String date = "";
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int d = calendar.get(Calendar.DATE);

            if (d < 10 && m < 10) {
                date = y + "/0" + m + "/0" + d;
            } else if (d > 10 && m < 10) {
                date = y + "/0" + m + "/" + d;
            } else if (d < 10 && m > 10) {
                date = y + "/" + m + "/0" + d;
            } else {
                date = y + "/" + m + "/" + d;
            }
            controller.activateAccount("Default", "09000000000", "Male", "no address", "default@gmail.com", "pass", "O", "1", date);
        }

        if (MDetect.INSTANCE.isUnicode()) {
            String uniSt = Rabbit.zg2uni(getResources().getString(R.string.signin));
            signIn.setText(uniSt);
//            signup.setText(Rabbit.zg2uni(getResources().getString(R.string.signup)));
/*
            myEmail.setHint(Rabbit.zg2uni(getResources().getString(R.string.email)));
            myPassword.setHint(Rabbit.zg2uni(getResources().getString(R.string.password)));
*/
        } else {
            signIn.setText(getResources().getString(R.string.signin));
//          signup.setText(getResources().getString(R.string.signup));
/*
            myEmail.setHint(getResources().getString(R.string.email));
            myPassword.setHint(getResources().getString(R.string.password));
*/
        }

        /*signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager(); // getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.frame, new SignUpFragment()).commit();
                Intent intent = new Intent(getActivity(), SingupActivity.class);
                startActivity(intent);
            }
        });*/

/*
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor data = controller.signin_owner(myEmail.getText().toString(), myPassword.getText().toString());
                int numRow = data.getCount();
                String comId = "";
                String email = "";
                Integer userId;
                String name = "";

                if (numRow > 0) {
                    while (data.moveToNext()) {
                        userId = data.getInt(0);
                        comId = data.getString(8);
                        email = data.getString(5);
                        active = data.getString(10);
                        role = data.getString(7);
                        name = data.getString(1);
                    }

                    if (active.equals("1")) {
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Activate", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putBoolean("session", true);
                        editor.putString("cmid", comId);
                        editor.putString("email", email);
                        editor.putString("name", name);
                        editor.putString("role", role);
                        editor.apply();

                        Toasty.success(getActivity(), "Successfully Signin", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Toasty.warning(getActivity(), "User Account is inactive.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(getActivity(), "User Account can't exist! Please register your infromation", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterActivityMe.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    resetError();
                    postData();
                }
            }
        });
        return view;
    }

   /* private void postData() {
        ApiService apiService = RetrofitClient.getApiService();
        getIMEI();
        Call<RegisterUser> call = apiService.loginUser(myUserName.getText().toString(), Long.parseLong("5555555"), myPassword.getText().toString());
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.body().getResult().equalsIgnoreCase("true") && response.body() != null) {
                    sharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("session", true);
                    editor.putLong("IMEINo", IMEI);
                    editor.putString("password", myPassword.getText().toString());
                    editor.commit();

                    //    session.passwordSession(Long.parseLong("123456789123456789"), myPassword.getText().toString());
                    Toast.makeText(getActivity(), getResources().getString(R.string.loginSuccess), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.loginInvalid), Toast.LENGTH_LONG).show();
                }
            }
*/
   //555555555

    private void postData() {

        ApiService apiService = RetrofitClient.getApiService();
        getIMEI();
        Call<RegisterUser> call = apiService.loginUser(myUserName.getText().toString(), Long.parseLong("5"), myPassword.getText().toString());
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                try {
                   if (response.body().getResult().equalsIgnoreCase("true") && response.body() != null)

                    sharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("session", true);
                    editor.putLong("IMEINo", IMEI);
                    editor.putString("password", myPassword.getText().toString());
                    editor.commit();

                    //    session.passwordSession(Long.parseLong("123456789123456789"), myPassword.getText().toString());
                   // Toast.makeText(getActivity(), getResources().getString(R.string.loginSuccess), Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),getResources().getString(R.string.loginSuccess), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(getActivity(), getResources().getString(R.string.loginInvalid), Toast.LENGTH_LONG).show();
                }
            }





            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                System.out.println("Something wrong" + t.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getIMEI();
        }
    }

    private void getIMEI() {
        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        IMEI = Long.parseLong(telephonyManager.getDeviceId());
    }

    private boolean validate() {
        boolean validate = true;
        if (myPassword.getText().toString().isEmpty() || myPassword.getText().toString() == null) {
            myPassword.setError(getResources().getString(R.string.required));
            myPassword.setBackgroundResource(R.drawable.err_background);
            validate = false;
        }
        return validate;
    }

    private void resetError() {
        myPassword.setError(null);
        myPassword.setBackground(null);
    }

}
