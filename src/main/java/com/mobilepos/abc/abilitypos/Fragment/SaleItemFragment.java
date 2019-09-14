package com.mobilepos.abc.abilitypos.Fragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobilepos.abc.abilitypos.Adapter.ListViewAdapter;
import com.mobilepos.abc.abilitypos.Adapter.MoviesAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.PrintPreviewActivity;
import com.mobilepos.abc.abilitypos.R;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;
public class SaleItemFragment extends Fragment {
    private static MoviesAdapter mAdapter;
    public static ArrayList<DataEntry> dataEntries = new ArrayList<>();
    public static ArrayList<DataEntry> addItemEntry = new ArrayList<>();
    public static Double totalPayment = 0.0, customTotal = 0.0;
    public static EditText search_item;
    public static String check = "",cusname = "";
    static String textVal = "";
    static RecyclerView recyclerView;
    ImageButton clearText;
    ListView listView;
    Activity content;
    DB_Controller controller;
    Button payment, clearArr;
    TextView header;
    ListViewAdapter adapter;

    public SaleItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sale_item, container, false);
        controller = new DB_Controller(getActivity());
        listView = view.findViewById(R.id.all_list);
        search_item = view.findViewById(R.id.search_item);
        payment = view.findViewById(R.id.proceedBtn);
        recyclerView = view.findViewById(R.id.recycler_view);
        clearArr = view.findViewById(R.id.clearBtn);
        clearText = view.findViewById(R.id.clearText);
        header = view.findViewById(R.id.heading);

        fontChanged();
        dataEntries = new ArrayList<>();
        prepareData(content);
        search_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textVal = search_item.getText().toString();
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
                dataEntries.clear();

                if (check.equals("0") || check.equals("3")){
                    Account(); //cash receive or payment
                } else
                if (check.equals("1") || check.equals("2")) {
                    SaleAndPurchase();
                }
                else if (check.equals("4")) {
                    ReceiveAndPayment("1");
                } else if (check.equals("5")) {
                    ReceiveAndPayment("2");

                }else if (check.equals("6") || check.equals("7") || check.equals("8")) {
                    Cash();
                }

            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addItemEntry.size() == 0) {
                    if (MDetect.INSTANCE.isUnicode()) {
                        Toast.makeText(getActivity(), Rabbit.zg2uni(getResources().getString(R.string.add_sale_item)), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.add_sale_item), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), PrintPreviewActivity.class);
                    startActivity( intent);
                }
            }
        });

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_item.setText("");
            }
        });

        clearArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                if (MDetect.INSTANCE.isUnicode()) {
                    alertDialogBuilder.setMessage(Rabbit.zg2uni(getResources().getString(R.string.clear_data)));
                    alertDialogBuilder.setPositiveButton(Rabbit.zg2uni(getResources().getString(R.string.ok)),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    addItemEntry.clear();
                                    totalPayment = 0.0;
                                    prepareData(content);
                                }
                            });
                    alertDialogBuilder.setNegativeButton(Rabbit.zg2uni(getResources().getString(R.string.cancel)), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                } else {
                    alertDialogBuilder.setMessage(getResources().getString(R.string.clear_data));
                    alertDialogBuilder.setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    addItemEntry.clear();
                                    totalPayment = 0.0;
                                    prepareData(content);
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
            }
        });
        return view;

    }




    // Search Datas   for Account
    private void Account() {
        dataEntries = new ArrayList<>();
        Cursor data = controller.search_data(search_item.getText().toString());
        if (data.moveToFirst()) {
            do {
                DataEntry dataEntry = new DataEntry();
                dataEntry.setItemCode(data.getString(2));
                dataEntry.setItemName(data.getString(3));
                dataEntry.setItemPPrice(data.getDouble(4));
                dataEntry.setItemSPrice(data.getDouble(5));
                dataEntry.setItemUnit(data.getString(6));
                dataEntry.setCodeType(data.getString(8));
// Add dataEntry
                dataEntries.add(dataEntry);
                adapter = new ListViewAdapter(getActivity(), R.layout.item_salelist, dataEntries);
                listView.setAdapter(adapter);

                //check account

            } while (data.moveToNext());
        } else {
            Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
        }
    }


    // Search Datas   for Sale and Purchase
    private void SaleAndPurchase() {
        dataEntries = new ArrayList<>();
        Cursor data = controller.search_data(search_item.getText().toString());
        if (data.moveToFirst()) {
            do {
                DataEntry dataEntry = new DataEntry();
                dataEntry.setItemCode(data.getString(2));
                dataEntry.setItemName(data.getString(3));
                dataEntry.setItemPPrice(data.getDouble(4));
                dataEntry.setItemSPrice(data.getDouble(5));
// Add dataEntry
                dataEntries.add(dataEntry);

                adapter = new ListViewAdapter(getActivity(), R.layout.item_salelist, dataEntries);
                listView.setAdapter(adapter);

            } while (data.moveToNext());
        } else {
            Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
        }
    }


    //Search Customer for Receive and payment
    public void ReceiveAndPayment(String vrType) {
        Cursor data = controller.search_customer(search_item.getText().toString(), vrType);
        int numRow = data.getCount();

        if (numRow == 0) {
            Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
        } else {
            while (data.moveToNext()) {
                DataEntry dataEntry = new DataEntry();
                dataEntry.setItemCode(data.getString(2));
                dataEntry.setCusName(data.getString(3));
                dataEntry.setItemName(data.getString(0));
                dataEntry.setItemPPrice(0.0);
                dataEntry.setItemSPrice(data.getDouble(10));
//                        dataEntry = new DataEntry(data.getString(2), data.getString(4), data.getString(1));
                dataEntries.add(dataEntry);

                adapter = new ListViewAdapter(getActivity(), R.layout.item_salelist, dataEntries);

                listView.setAdapter(adapter);

            }
        }
    }


    // Search custom data for cash
    private void Cash() {
        Cursor data = controller.search_custom_data(search_item.getText().toString());
        int numRow = data.getCount();

        if (numRow == 0) {
            Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
        } else {
            while (data.moveToNext()) {
                DataEntry dataEntry = new DataEntry();
                dataEntry.setItemCode(data.getString(2));
                dataEntry.setItemName(data.getString(3));
                dataEntry.setItemPPrice(data.getDouble(4));
                dataEntry.setItemSPrice(data.getDouble(5));
//                        dataEntry = new DataEntry(data.getString(2), data.getString(4), data.getString(1));
                dataEntries.add(dataEntry);

                adapter = new ListViewAdapter(getActivity(), R.layout.item_salelist, dataEntries);

                listView.setAdapter(adapter);

            }
        }
    }
    private void fontChanged() {
        if (MDetect.INSTANCE.isUnicode())
        {
            search_item.setHint(Rabbit.zg2uni(getResources().getString(R.string.searchitem)));
            header.setText(Rabbit.zg2uni(getResources().getString(R.string.itemlist)));
            clearArr.setText(Rabbit.zg2uni(getResources().getString(R.string.clear)));
            payment.setText(Rabbit.zg2uni(getResources().getString(R.string.payment)));

        }
        else
        {
            search_item.setHint(getResources().getString(R.string.searchitem));
            header.setText(getResources().getString(R.string.itemlist));
            clearArr.setText(getResources().getString(R.string.clear));
            payment.setText(getResources().getString(R.string.payment));
        }
    }

    @Override
    public void onPause() {
        System.out.println("<<On Pause ====== >>");
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("<<On Pause ====== >>");
        super.onResume();
    }

    public static void prepareData(Activity mActivity) {
        mAdapter = new MoviesAdapter(addItemEntry);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL , false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    // this is the all search custom data


}
