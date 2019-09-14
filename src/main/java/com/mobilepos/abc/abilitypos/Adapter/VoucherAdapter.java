package com.mobilepos.abc.abilitypos.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;

import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.MyViewHolder> {
    private List<DataEntry> saleList;
  //  private ArrayList<DataEntry> saleList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, qty, price,totalPrice,cross1;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            qty = (TextView) view.findViewById(R.id.qty);
            price = (TextView) view.findViewById(R.id.price);
            totalPrice = (TextView) view.findViewById(R.id.totalprice);
            cross1 =(TextView)view.findViewById(R.id.cross1);
        }
    }
    public VoucherAdapter(List<DataEntry> saleList) {
        this.saleList = saleList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DataEntry sale = saleList.get(position);

        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")) {
            holder.cross1.setVisibility(View.GONE);
            holder.name.setText(sale.getItemName());
            holder.qty.setText("");
            holder.price.setText("");
            holder.totalPrice.setText(sale.getChangePrice() + "");

        } else {
            holder.cross1.setVisibility(View.GONE);
            holder.name.setText(sale.getItemName());
            holder.qty.setText(sale.getItemQty() + "");
            holder.price.setText(sale.getItemSPrice() + "");
            holder.totalPrice.setText(sale.getChangePrice() + "");

        }

    }
    @Override
    public int getItemCount() {
        return saleList.size();

    }

}
