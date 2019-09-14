package com.mobilepos.abc.abilitypos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Model.ReportListData;
import com.mobilepos.abc.abilitypos.R;

import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<ReportListData> {

    public TextView myRecordName;
    public  TextView myRecordType;
    private List<ReportListData> reporDataList;
    private Activity context;

    public MyListAdapter(@NonNull Context context, int resource, List<ReportListData> list) {
        super(context, resource);
    }

    public MyListAdapter(
            List<ReportListData> dataList,
            @NonNull Activity context) {
        super(context, R.layout.my_custom_report_list, dataList);
        this.reporDataList = dataList;
        this.context = context;
        Log.e("<<System>>", "List Adapter Constructor");
        Log.e("<<System>>", "List Adapter Constructor Size =" +
                this.reporDataList.size());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e("<<System>>", "getView :" + position);
        ReportListData myReportListData = this.reporDataList.get(position);
        LayoutInflater myLayoutInflater =
                this.context.getLayoutInflater();
        View recordView = myLayoutInflater.inflate(
                R.layout.my_custom_report_list, null, true);

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);


        TextView myRecordGroup = recordView.findViewById(R.id.id_group);
        TextView myRecordCode = recordView.findViewById(R.id.id_code);
        TextView myRecordName = recordView.findViewById(R.id.id_name);
        TextView myRecordType = recordView.findViewById(R.id.id_type);

        myRecordGroup.setText(myReportListData.getGroup());
        myRecordCode.setText(myReportListData.getCode());


        /*Double dname=Double.valueOf(myReportListData.getName());
        myRecordName.setText(df.format(dname));*/
        myRecordName.setText(myReportListData.getName());
       /* Double dtype=Double.valueOf(myReportListData.getType());
        myRecordType.setText(df.format(dtype));*/
        myRecordType.setText(myReportListData.getType());


       // Double nettotal=Double.valueOf(detailList.getItemPPrice()+"");
      //  netTotal.setText(df.format(nettotal));

        return recordView;
    }

}
