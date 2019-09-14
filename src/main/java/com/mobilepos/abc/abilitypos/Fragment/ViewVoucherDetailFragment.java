package com.mobilepos.abc.abilitypos.Fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Adapter.ViewDetailAdapter;
import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ViewVoucherDetailFragment extends Fragment {

    DB_Controller controller;
    public ArrayList<DataEntry> dataEntries;
    DataEntry dataEntry;
    ViewDetailAdapter adapter;

    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataEntries = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_view_voucher_detail, container, false);

        controller=new DB_Controller(getActivity());

        listView=(ListView)view.findViewById(R.id.all_list);

        Cursor data = controller.all_vrdata(SaleItemFragment.check);
        int numRow = data.getCount();

        if (numRow == 0) {
            Toasty.error(getActivity(), "No data", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
        } else {
            while (data.moveToNext()) {
                dataEntry = new DataEntry();
                dataEntry.setVoucherNo(data.getString(0));
                dataEntries.add(dataEntry);
                adapter = new ViewDetailAdapter(getActivity(), R.layout.detail_list,dataEntries);
                listView.setAdapter(adapter);
            }
        }

        return view;

    }

}
