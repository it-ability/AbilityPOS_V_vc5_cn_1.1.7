package com.mobilepos.abc.abilitypos.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobilepos.abc.abilitypos.Model.ReportListData;
import com.mobilepos.abc.abilitypos.R;

import java.util.List;

public class VoucherDetailAdapter extends RecyclerView.Adapter<VoucherDetailAdapter.MyViewHolder> {
    private List<ReportListData> voucherlist;

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
    public VoucherDetailAdapter(List<ReportListData> voucherlist) {
        this.voucherlist = voucherlist;
    }
    @Override
    public VoucherDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);

        return new VoucherDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VoucherDetailAdapter.MyViewHolder holder, int position) {
        ReportListData voucherlists = voucherlist.get(position);
       /* if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")) {
            holder.cross1.setVisibility(View.GONE);
            holder.name.setText(voucherlists.getItemName());
            holder.qty.setText("");
            holder.price.setText("");
            holder.totalPrice.setText(voucherlists.getChangePrice() + "");
        } else {*/

            holder.name.setText(voucherlists.getGroup());
            holder.qty.setText(voucherlists.getCode() + "");
//        holder.price.setText(sale.getItemSPrice()+"");
            holder.price.setText(voucherlists.getName() + "");
            holder.totalPrice.setText(voucherlists.getType() + "");
    //    }
    }
    @Override
    public int getItemCount() {
        return voucherlist.size();

    }

}