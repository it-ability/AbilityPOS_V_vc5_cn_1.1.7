package com.mobilepos.abc.abilitypos.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.Fragment.ManageItemFragment;
import com.mobilepos.abc.abilitypos.ManagementActivity;
import com.mobilepos.abc.abilitypos.Model.DataEntry;
import com.mobilepos.abc.abilitypos.R;
import com.mobilepos.abc.abilitypos.UpdateItemActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemManageAdapter extends ArrayAdapter<DataEntry> {
    private LayoutInflater inflater;
    private ArrayList<DataEntry> manageItem;
    private int mresourceID;
    public static TextView name, unit,price;
    ImageButton delete, update;
    DB_Controller controller;

    public static String code = "", itemname = "", pprice = "", sprice = "", itemunit = "";
    private String codeType;


    public ItemManageAdapter(Context context, int tresourceID, ArrayList<DataEntry> manageItem) {
        super(context, tresourceID, manageItem);
        System.out.println(manageItem.size());
        this.manageItem = manageItem;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mresourceID = tresourceID;
        System.out.println("Manage Item ===>>>"+manageItem);
    }

    public View getView(final int position, View convertView, ViewGroup parents) {
        convertView = inflater.inflate(mresourceID, null);


        name = (TextView) convertView.findViewById(R.id.name);
        price = (TextView) convertView.findViewById(R.id.price);
        unit = (TextView) convertView.findViewById(R.id.unit);
        delete = (ImageButton) convertView.findViewById(R.id.delete);
        update = (ImageButton) convertView.findViewById(R.id.update);

        controller = new DB_Controller(getContext());

        final DataEntry udList = manageItem.get(position);

        if (ManageItemFragment.checked.equals(getContext().getResources().getString(R.string.Customer)) || ManageItemFragment.checked.equals(getContext().getResources().getString(R.string.Supplier))){
            price.setVisibility(View.GONE);
            unit.setVisibility(View.GONE);
        }

        if (udList != null) {

            if (name != null) {
                name.setText(udList.getItemName());
            }
            if (price != null) {
                price.setText("PPrice : " + udList.getItemPPrice() + "\n" + "SPrice : " + udList.getItemSPrice());
            }
            if (unit != null) {
                unit.setText("Unit : " + udList.getItemUnit());
            }




//Delete ListView Button Click
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String code = udList.getItemCode();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(R.string.delete);
                    alertDialogBuilder.setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    controller.deleteRow(code);
                                    for (int i = 0; i < manageItem.size(); i++) {
                                        if (manageItem.get(i).getItemCode().equals(code)) {
                                            manageItem.remove(i);
                                            notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                    alertDialogBuilder.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

// ListView Update Button CLick

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //System.out.println("##########"+udList.toString());
                    //System.out.println("##########"+udList.getItemCode());
                    final String codes = udList.getItemCode();
                    for (int i = 0; i < manageItem.size(); i++) {
                        if (manageItem.get(i).getItemCode().equals(codes)) {
                            System.out.println("######%%%%%%%%%%%%%"+manageItem.get(i).getItemCode());
                            codeType = manageItem.get(i).getCodeType();
                            code = manageItem.get(i).getItemCode();
                            itemname = manageItem.get(i).getItemName();
                            pprice = manageItem.get(i).getItemPPrice() + "";
                            sprice = manageItem.get(i).getItemSPrice() + "";
                            itemunit = manageItem.get(i).getItemUnit();
                        }
                    }
                    //System.out.println("mangeItem ====>>>"+code);
                    //System.out.println("mangeItem ====>>>"+itemname);
                    // System.out.println("mangeItem ====>>>"+pprice);
                    // System.out.println("mangeItem ====>>>"+codeType);
                    // System.out.println("mangeItem ====>>>"+manageItem.toString());
                    Intent intent = new Intent(getContext(), UpdateItemActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("codeType", codeType);
                    mBundle.putString("code", code);
                    mBundle.putString("name", itemname);
                    mBundle.putString("pprice", pprice);
                    mBundle.putString("sprice", sprice);
                    mBundle.putString("unit", itemunit);
                    intent.putExtras(mBundle);
                    getContext().startActivity(intent);
                }
            });
        }
        return convertView;
    }


}
