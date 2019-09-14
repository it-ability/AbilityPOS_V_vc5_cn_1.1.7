package com.mobilepos.abc.abilitypos.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SaleListAdapter extends ArrayAdapter<DataEntry> {

    private LayoutInflater inflater;
    private ArrayList<DataEntry> saleList;
    private int mresourceID;
    private DecimalFormat myCustDecFormatter = new DecimalFormat("########.00");


    public SaleListAdapter(Context context, int tresourceID, ArrayList<DataEntry> saleList) {
        super(context, tresourceID, saleList);
        this.saleList = saleList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mresourceID = tresourceID;
    }

    public View getView(final int position, View convertView, ViewGroup parents) {
        convertView = inflater.inflate(mresourceID, null);

        final DataEntry detailList = saleList.get(position);
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(3);


        if (detailList != null) {
            TextView vrno, netTotal, creditsTotal;
            vrno = (TextView) convertView.findViewById(R.id.vrno);
            netTotal = (TextView) convertView.findViewById(R.id.nettotalPrice);
            creditsTotal = (TextView) convertView.findViewById(R.id.creditsTotalPrice);

            if (vrno != null || netTotal !=null || creditsTotal !=null) {
                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                    vrno.setText(detailList.getVoucherNo());
                    String cusName=(detailList.getCusName());  // null get printerPreviewActivity
                    netTotal.setText(cusName);
                    Double credittotal=Double.valueOf(detailList.getItemPPrice()+"");
                    creditsTotal.setText(df.format(credittotal));

                }else {
                vrno.setText(detailList.getVoucherNo());

                Double nettotal=Double.valueOf(detailList.getItemPPrice()+"");
                netTotal.setText(df.format(nettotal));

                Double credittotal=Double.valueOf(detailList.getPay()+"");
                creditsTotal.setText(df.format(credittotal));
                }
            }
        }
        return convertView;
    }

    public void refresh(ArrayList<DataEntry> saleList) {
        this.saleList = saleList;
        notifyDataSetChanged();
    }
}
