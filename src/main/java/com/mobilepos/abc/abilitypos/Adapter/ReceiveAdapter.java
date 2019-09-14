package com.mobilepos.abc.abilitypos.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.SaleReceiveActivity;

import java.util.List;

public class ReceiveAdapter extends RecyclerView.Adapter<ReceiveAdapter.MyViewHolder>{

    private List<DataEntry> voucherlist;
    public String selectedCode;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name,price,totalPrice;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.receive);
            totalPrice = (TextView) view.findViewById(R.id.totalprice);
            delete=(ImageView)view.findViewById(R.id.close);
        }
    }

    public ReceiveAdapter(List<DataEntry> voucherlist) {
        this.voucherlist = voucherlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_voucher_list, parent, false);

        return new ReceiveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DataEntry movie = voucherlist.get(position);
        holder.name.setText(movie.getItemName());
        holder.price.setText("Receive  :  "+movie.getItemSPrice()+"");
        holder.totalPrice.setText("       Amt  :  "+movie.getChangePrice()+"");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCode = voucherlist.get(position).getItemName();
                try {
                    for(int i=0;i<voucherlist.size();i++){
                        if(voucherlist.get(position).getItemName().equals(selectedCode)){
                            SaleReceiveActivity.totalPayment-=Double.valueOf(voucherlist.get(position).getItemSPrice());
                            SaleReceiveActivity.totalReceive-=Double.valueOf(voucherlist.get(position).getChangePrice());
                            voucherlist.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                }catch (IndexOutOfBoundsException ie){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return voucherlist.size();
    }

}
