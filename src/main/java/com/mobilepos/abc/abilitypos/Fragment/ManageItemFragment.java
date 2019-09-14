package com.mobilepos.abc.abilitypos.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Adapter.ItemManageAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.MainActivity;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.SaleReceiveActivity;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class ManageItemFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Activity content;
    DB_Controller controller;
    public static ArrayList<DataEntry> dataEntries = new ArrayList<>();
    public static List<String> cType;
    ImageButton clearText;

    ListView listView;
    public static EditText search_item;
    String codeType;
    Spinner groupcode;
    public static String[] groupType = {"Select Code Type", "Customer", "Item","Supplier","Account"};

    DataEntry dataEntry;
    public static String textVal = "";
    ItemManageAdapter adapter;

    public static String checked="";



    public ManageItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_item, container, false);

//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        cType = new ArrayList<String>();
        cType.add(getResources().getString(R.string.codetype));
        cType.add(getResources().getString(R.string.Customer));
        cType.add(getResources().getString(R.string.Item));
        cType.add(getResources().getString(R.string.Supplier));
        /*cType.add(getResources().getString(R.string.Account));*/
        groupcode = (Spinner) view.findViewById(R.id.type);
        groupcode.setOnItemSelectedListener(this);
        ArrayAdapter<String> group = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cType);
        group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupcode.setAdapter(group);

        controller = new DB_Controller(getActivity());
        listView = (ListView) view.findViewById(R.id.all_list);
        search_item = (EditText) view.findViewById(R.id.search_item);





        clearText = (ImageButton) view.findViewById(R.id.clearText);
        if (MDetect.INSTANCE.isUnicode()) {
            search_item.setHint(Rabbit.zg2uni(getResources().getString(R.string.searchitem)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.codetype)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Customer)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Item)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Supplier)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Account)));


        } else {

            search_item.setHint(getResources().getString(R.string.searchitem));
        }




        search_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("OnTextChanged +++++++ ");
                textVal = search_item.getText().toString();
                //textVal = MainActivity.barcode;

                if (textVal.equals("")) {
                    dataEntries.clear();
                    listView.setVisibility(View.GONE);
                } else {
                    dataEntries.clear();
                    listView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                dataEntries = new ArrayList<>();

// Get Data Edit Search_Item put update_data
                Cursor data = controller.update_data(codeType, search_item.getText().toString());
                //Cursor data = controller.update_data(codeType, MainActivity.barcode);

                int numRow = data.getCount();
                if (numRow == 0) {
                    Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                    listView.setVisibility(View.GONE);
                } else {
                    while (data.moveToNext()) {
                        dataEntry = new DataEntry();

                        //dataEntry.setCodeType(data.getString(1));
                        dataEntry.setItemCode(data.getString(2));
                        dataEntry.setItemName(data.getString(3));
                        dataEntry.setItemPPrice(data.getDouble(4));
                        dataEntry.setItemSPrice(data.getDouble(5));
                        dataEntry.setItemUnit(data.getString(6));
                        dataEntry.setCodeType(data.getString(8));
                        dataEntries.add(dataEntry);
                    }
                    adapter = new ItemManageAdapter(getActivity(), R.layout.update_delete_item, (ArrayList<DataEntry>) dataEntries);
                    listView.setAdapter(adapter);


                }
            }
        });

        clearText.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                search_item.setText("");
            }
        });

        return view;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {
            codeType = "";
        } else if (position == 1) {
            codeType = "C";
            checked=getResources().getString(R.string.Customer);

        } else if (position == 2) {
            codeType = "I";
        } else if (position == 3) {
            codeType = "V";
            checked=getResources().getString(R.string.Customer);

        }
        /*else if (position == 4) {
            codeType = "A";
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
