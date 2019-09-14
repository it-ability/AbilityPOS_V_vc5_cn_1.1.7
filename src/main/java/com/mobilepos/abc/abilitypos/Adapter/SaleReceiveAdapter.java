package com.mobilepos.abc.abilitypos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.SaleReceiveActivity;

import java.util.ArrayList;

public class SaleReceiveAdapter extends ArrayAdapter<DataEntry> {

    private LayoutInflater inflater;
    private ArrayList<DataEntry> orders;
    private int mresourceID;

    Activity mContext;

    public SaleReceiveAdapter(Context context, int tresourceID, ArrayList<DataEntry> orders) {
        super(context, tresourceID, orders);
        this.orders = orders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mresourceID = tresourceID;
    }

    public View getView(final int position, View convertView, ViewGroup parents) {
        convertView = inflater.inflate(mresourceID, null);

        final DataEntry service = orders.get(position);

        if (service != null) {
            TextView name = (TextView) convertView.findViewById(R.id.itemName);
            name.setTextColor(Color.parseColor("#8000ff"));
            final EditText amount = (EditText) convertView.findViewById(R.id.qty);
            final TextView receive = (TextView) convertView.findViewById(R.id.price);
            Button add = (Button) convertView.findViewById(R.id.add);

            if (name != null) {
                name.setText(service.getItemName());
            }

            if (amount!=null){
                amount.setText(service.getItemSPrice()+"");
            }

            if (receive!=null){
                receive.setText(service.getChangePrice()+"");
            }

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataEntry dataEntry = new DataEntry();
                    dataEntry.setItemName(orders.get(position).getItemName());
                    dataEntry.setItemSPrice(orders.get(position).getItemSPrice());
                    dataEntry.setChangePrice(orders.get(position).getChangePrice());
                    SaleReceiveActivity.addItemEntry.add(dataEntry);

                    SaleReceiveActivity.totalPayment+=orders.get(position).getItemSPrice();
                    SaleReceiveActivity.totalReceive+=orders.get(position).getChangePrice();

                    String voucherNo = orders.get(position).getItemName();
                    for (int i = 0; i < orders.size(); i++) {
                        if (orders.get(i).getItemName().equals(voucherNo)) {
                            orders.remove(i);
                            notifyDataSetChanged();

                        }
                    }
                    SaleReceiveActivity.prepareData(mContext);
                }
            });
        }

        return convertView;
    }
}
