package com.mobilepos.abc.abilitypos;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Adapter.ReceiveAdapter;
import com.mobilepos.abc.abilitypos.Adapter.SaleReceiveAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.DataEntry;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SaleReceiveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    List<String> typeData;
    Activity content;
    DB_Controller controller;
    public static ArrayList<DataEntry> dataEntries = new ArrayList<>();
    public static ArrayList<DataEntry> addItemEntry = new ArrayList<>();
    public static Double totalPayment = 0.0;
    public static Double totalReceive = 0.0;

    ListView listView;
    Button payment, clearArr;
    TextView header;
    public static String check="";
    SaleReceiveAdapter adapter;

    static RecyclerView recyclerView;
    private static ReceiveAdapter mAdapter;

    Cursor data=null;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_receive);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addItemEntry.clear();
        totalPayment=0.0;
        totalReceive = 0.0;

        controller = new DB_Controller(getApplicationContext());
        listView = (ListView)findViewById(R.id.all_list);
        payment = (Button)findViewById(R.id.proceedBtn);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        clearArr = (Button)findViewById(R.id.clearBtn);

        typeData=new ArrayList<String>();
        typeData.add("Sale Receive");
        typeData.add("Purchase Payment");

        Spinner dateType = (Spinner) findViewById(R.id.chooseType);
        dateType.setOnItemSelectedListener(this);
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeData);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateType.setAdapter(dateAdapter);

        dataEntries = new ArrayList<>();
        prepareData(content);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),totalPayment+"/"+totalReceive,Toast.LENGTH_SHORT).show();
            }
        });

        clearArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemEntry.clear();
                totalPayment=0.0;
                totalReceive = 0.0;
                prepareData(content);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position==0){
            Toast.makeText(SaleReceiveActivity.this,typeData.get(position),Toast.LENGTH_SHORT).show();
            dataEntries.clear();

// Get VoucherH Datas()

            data = controller.getVoucherHDatas();
            int numRow = data.getCount();

            if (numRow == 0) {
                Toasty.error(SaleReceiveActivity.this, "No data", Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
            } else {
                while (data.moveToNext()) {
                    DataEntry dataEntry = new DataEntry();
                    dataEntry.setItemName(data.getString(0));
                    dataEntry.setItemSPrice(data.getDouble(7));
                    dataEntry.setChangePrice(data.getDouble(8));
                    dataEntries.add(dataEntry);

                    adapter = new SaleReceiveAdapter(SaleReceiveActivity.this, R.layout.item_salelist, dataEntries);

                    listView.setAdapter(adapter);

                }
            }
        }else {
            Toast.makeText(SaleReceiveActivity.this,typeData.get(position)+"------",Toast.LENGTH_SHORT).show();
            dataEntries.clear();

// Get VoucherD Datas()
            data = controller.getVoucherDDatas();
            int numRow = data.getCount();

            if (numRow == 0) {
                Toasty.error(SaleReceiveActivity.this, "No data", Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
            } else {
                while (data.moveToNext()) {
                    DataEntry dataEntry = new DataEntry();
                    dataEntry.setItemCode(data.getString(2));
                    dataEntry.setItemName(data.getString(3));
                    dataEntry.setItemPPrice(data.getDouble(4));
                    dataEntry.setItemSPrice(data.getDouble(5));
                    dataEntries.add(dataEntry);

                    adapter = new SaleReceiveAdapter(SaleReceiveActivity.this, R.layout.item_salelist, dataEntries);

                    listView.setAdapter(adapter);

                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static void prepareData(Activity mActivity) {
        mAdapter = new ReceiveAdapter(addItemEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
}
