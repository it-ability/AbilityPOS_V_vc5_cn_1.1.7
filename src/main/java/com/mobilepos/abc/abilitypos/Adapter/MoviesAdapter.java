package com.mobilepos.abc.abilitypos.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;

import org.w3c.dom.Text;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private List<DataEntry> moviesList;
    public String selectedCode;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, qty, price,totalPrice,cross,equal;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            name       = (TextView) view.findViewById(R.id.name);
            qty        = (TextView) view.findViewById(R.id.qty);
            price      = (TextView) view.findViewById(R.id.price);
            totalPrice = (TextView) view.findViewById(R.id.totalprice);
            delete     =(ImageView)view.findViewById(R.id.close);
            cross      =(TextView)view.findViewById(R.id.cross);
            equal     =(TextView)view.findViewById(R.id.equal);

        }
    }


    public MoviesAdapter(List<DataEntry> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final DataEntry movie = moviesList.get(position);

        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            holder.qty.setVisibility(View.GONE);
            holder.equal.setVisibility(View.GONE);
            holder.cross.setVisibility(View.GONE);
            holder.totalPrice.setVisibility(View.GONE);

            holder.name.setText(movie.getItemName());
           // holder.price.setText("");
            holder.price.setText(movie.getChangePrice()+"");
           // holder.totalPrice.setText(movie.getItemTotalPrice() + "");

        }else {

            holder.name.setText(movie.getItemName());
            holder.qty.setText(movie.getItemQty() + "");
            /*holder.price.setText(movie.getChangePrice() + "");
            holder.totalPrice.setText(movie.getItemTotalPrice() + "");*/
            holder.price.setText(movie.getItemSPrice() + "");
            holder.totalPrice.setText(movie.getChangePrice() + "");
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCode = moviesList.get(position).getItemName();
                try {
                    for(int i=0;i<moviesList.size();i++){
                        if(moviesList.get(position).getItemName().equals(selectedCode)){
                            if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                            //    SaleItemFragment.totalPayment-=Double.valueOf(moviesList.get(position).getChangePrice());
                                SaleItemFragment.totalPayment-=Double.valueOf(moviesList.get(position).getItemSPrice());
                            }else {
                             //   SaleItemFragment.totalPayment-=Double.valueOf(moviesList.get(position).getItemTotalPrice());
                                SaleItemFragment.totalPayment-=Double.valueOf(moviesList.get(position).getChangePrice());
                            }
                            moviesList.remove(position);
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
        return moviesList.size();
    }
}