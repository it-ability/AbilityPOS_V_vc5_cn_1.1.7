package com.mobilepos.abc.abilitypos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class ListViewAdapter extends ArrayAdapter<DataEntry> {

    private LayoutInflater inflater;
    private ArrayList<DataEntry> orders;
    private int mresourceID;

    Activity mContext;

    public ListViewAdapter(Context context, int tresourceID, ArrayList<DataEntry> orders) {
        super(context, tresourceID, orders);
        this.orders = orders;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mresourceID = tresourceID;
    }

    public View getView(final int position, View convertView, ViewGroup parents) {
        convertView = inflater.inflate(mresourceID, null);

        final DataEntry service = orders.get(position);
        System.out.println("position-----"+service.toString());

        if (service != null) {
            TextView name = (TextView) convertView.findViewById(R.id.itemName);
            final EditText itemQty = (EditText) convertView.findViewById(R.id.qty);
            final TextView itemPrice = (TextView) convertView.findViewById(R.id.price);
            final TextView multiply = (TextView) convertView.findViewById(R.id.multiply);
            final TextView itemqtyPrice = (TextView) convertView.findViewById(R.id.qtyPrice);
            final Button add = (Button) convertView.findViewById(R.id.add);
            final ImageView imageEdit= (ImageView)convertView.findViewById(R.id.image_edit);


            if (MDetect.INSTANCE.isUnicode()) {
                add.setText(Rabbit.zg2uni(convertView.getResources().getString(R.string.add)));

            } else {
                add.setText(convertView.getResources().getString(R.string.add));
            }

            if (name != null) {
                name.setText(service.getItemName());
            }

            System.out.println("The item price is*******"+itemPrice);
            if (itemPrice != null) {
                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                    imageEdit.setVisibility(View.VISIBLE);
                    itemQty.setFocusable(false);
                    itemQty.setFocusableInTouchMode(false);
                    itemQty.setClickable(false);

                    imageEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemQty.setFocusable(true);
                            itemQty.setFocusableInTouchMode(true);
                            itemQty.setClickable(true);
                            itemQty.setText(service.getItemName());
                        }
                    });
                    name.setText(service.getItemCode());    // E01
                    itemQty.setText(service.getItemName()); // traveling
                    itemPrice.setHint("Amount");      // Amount Enter
                    itemPrice.setText(itemPrice.getText().toString()+"");
                   // itemqtyPrice.setText(itemqtyPrice.getText().toString());

                }else if (SaleItemFragment.check.equals("1")){
                    imageEdit.setVisibility(View.GONE);
                    itemPrice.setText(service.getItemSPrice()+"");
                    itemqtyPrice.setText(service.getItemSPrice() * 1 + "");
                }else if (SaleItemFragment.check.equals("2")){
                    imageEdit.setVisibility(View.GONE);
                    itemPrice.setText(service.getItemPPrice()+"");
                    itemqtyPrice.setText(service.getItemPPrice() * 1 + "");
                }/*else if (SaleItemFragment.check.equals("3") || SaleItemFragment.check.equals("4")){
                    imageEdit.setVisibility(View.GONE);
                    itemPrice.setText(service.getItemSPrice()+"");
                    itemqtyPrice.setText(service.getItemSPrice() * 1 + "");
                }*/else {
                    imageEdit.setVisibility(View.GONE);
                    itemPrice.setText(service.getItemSPrice()+"");
                    itemqtyPrice.setText(service.getItemSPrice() * 1 + "");
                }

                //for price change
                itemPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (itemQty.getText().toString().length() > 0) {
                                Double i;
                                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                                    itemqtyPrice.setText(itemqtyPrice.getText().toString());
                                }


                                i = Double.valueOf(itemPrice.getText().toString()) * Double.parseDouble(itemQty.getText() + "");
                                itemqtyPrice.setText(String.valueOf(i));
                            } else {
                        //
                               add.setVisibility(View.INVISIBLE);
                                itemqtyPrice.setText("0");

                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                //for qty change
                itemQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (itemQty.getText().toString().length() > 0) {
                                Double i;
                                if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                                    itemqtyPrice.setText(itemqtyPrice.getText().toString());
                                }else {
                                    i = Double.valueOf(itemPrice.getText().toString()) * Double.parseDouble(itemQty.getText() + "");
                                    itemqtyPrice.setText(String.valueOf(i));
                                }
                            }else {
                                itemqtyPrice.setText("0");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

// From Search data add
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      try {

                        DataEntry dataEntry = new DataEntry();
                            dataEntry.setItemCode(orders.get(position).getItemCode());


                          if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
                             // dataEntry.setItemQty(orders.get(position).getItemQty());
                             // dataEntry.setItemName(orders.get(position).getItemName());
                             // dataEntry.setItemName(orders.get(position).getItemName());
                              dataEntry.setItemName(String.valueOf(itemQty.getText()+""));

                          //    dataEntry.setChangePrice(Double.valueOf(itemPrice.getText().toString()));
                              dataEntry.setItemSPrice(Double.valueOf(itemPrice.getText().toString()));
                              SaleItemFragment.totalPayment += Double.parseDouble(itemPrice.getText().toString());
                          }else{
                              dataEntry.setItemName(orders.get(position).getItemName());
                              dataEntry.setItemQty(Double.valueOf(itemQty.getText() + ""));
                              dataEntry.setItemPPrice(orders.get(position).getItemPPrice());
                              dataEntry.setItemSPrice(orders.get(position).getItemSPrice());
                             /* dataEntry.setChangePrice(Double.valueOf(itemPrice.getText().toString()));
                              dataEntry.setItemTotalPrice(Double.valueOf(itemqtyPrice.getText() + ""));*/
                              dataEntry.setItemSPrice(Double.valueOf(itemPrice.getText().toString()));
                              dataEntry.setChangePrice(Double.valueOf(itemqtyPrice.getText() + ""));
                              SaleItemFragment.totalPayment += Double.parseDouble(itemqtyPrice.getText().toString());
                              }
                            Toasty.success(getContext(),"Add Item Success",Toast.LENGTH_LONG).show();
                            SaleItemFragment.addItemEntry.add(dataEntry);

                        if (SaleItemFragment.check.equals("7")){
                            if (Double.parseDouble(itemqtyPrice.getText().toString()) > 0){
                                SaleItemFragment.customTotal+=Double.parseDouble(itemqtyPrice.getText().toString());
                               }}
                        SaleItemFragment.prepareData(mContext);
                      }catch (Exception e){
                          Toast.makeText(getContext(),"Invalid data ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        return convertView;
    }
}
