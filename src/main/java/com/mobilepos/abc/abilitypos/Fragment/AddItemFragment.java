package com.mobilepos.abc.abilitypos.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;
import com.mobilepos.abc.abilitypos.MainActivity;
import com.mobilepos.abc.abilitypos.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

import static android.content.Context.MODE_PRIVATE;


public class AddItemFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    public static List<String> groupType;
    public static List<String> cType;
    public static String gType = "", item_code = "", item_name = "", item_unit = "";
    public static String codeType;
    public static double p_price = 0.0, s_price = 0.0;
    public static EditText ItemCode;
    Button add_item;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(1)
    public static EditText itemCode;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(2)
    public EditText itemName;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(3)
    public EditText pPrice;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(4)
    public EditText sPrice;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(5)
    public EditText unit;

    @NotEmpty(sequence = 1, messageResId = R.string.validation_empty)
    @Order(6)
    EditText newItem;

    ImageButton add;
    AutoCompleteTextView spin;

    private Validator mValidator;

    TextInputLayout mCodeInputLayout;
    TextInputLayout mNameInputLayout;
    TextInputLayout mPPriceInputLayout;
    TextInputLayout mSPriceInputLayout;
    TextInputLayout mUnitInputLayout;
    TextInputLayout mNewItemInputLayout;

    Spinner groupcode;
    public static Spinner codeTypeSpinner;

    DB_Controller controller;

    int numRow;
    Cursor data;
    public static String Orid = "";

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        controller = new DB_Controller(getActivity());



        cType = new ArrayList<String>();
        cType.add(getResources().getString(R.string.codetype));
        cType.add(getResources().getString(R.string.Customer));
        cType.add(getResources().getString(R.string.Item));
        cType.add(getResources().getString(R.string.Supplier));
      //  cType.add(getResources().getString(R.string.Account));


        codeTypeSpinner = view.findViewById(R.id.codeType);
        codeTypeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> aac = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cType);
        aac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        codeTypeSpinner.setAdapter(aac);

        boolean cus = controller.defaultCustomer();
        if (!cus == true) {
            controller.add_item("customer", "cash", "cash", 0.0, 0.0, "cash", MainActivity.companyId, "C");
        }

        boolean ven = controller.defaultVender();
        if (!ven == true) {
            controller.add_item("vender", "vender", "vender", 0.0, 0.0, "vender", MainActivity.companyId, "V");
        }


        add = (ImageButton) view.findViewById(R.id.hideComplete);

        /*ArrayList<String> unique = removeDuplicates(groupType);
        for (String element : unique) {
            System.out.println("aaaa");
            System.out.println(element);
        }*/

        /*spin = (AutoCompleteTextView) view.findViewById(R.id.groupcode);
        spin.setOnItemSelectedListener(this);
        *//*ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,groupType);
        spin.setAdapter(aa);*//*
        spin.setOnItemClickListener(this);*/

        groupType = new ArrayList<>();
        groupType.add("Select group code");
        groupcode = (Spinner) view.findViewById(R.id.groupcode);
        groupcode.setOnItemSelectedListener(this);
        ArrayAdapter group = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, groupType);
        group.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupcode.setAdapter(group);
        //for validation

        mCodeInputLayout = view.findViewById(R.id.code);
        mNameInputLayout = view.findViewById(R.id.name);
        mPPriceInputLayout = view.findViewById(R.id.pprice);
        mSPriceInputLayout = view.findViewById(R.id.sprice);
        mUnitInputLayout = view.findViewById(R.id.unit);
        mNewItemInputLayout = view.findViewById(R.id.newItem);

        itemCode = view.findViewById(R.id.code_editText);
        itemName = view.findViewById(R.id.name_editText);
        pPrice = view.findViewById(R.id.pprice_editText);
        sPrice = view.findViewById(R.id.sprice_editText);
        unit = view.findViewById(R.id.unit_editText);
        newItem = view.findViewById(R.id.newItem_editText);

        mValidator = new Validator(this);

        groupcode.setVisibility(View.VISIBLE);
        newItem.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupcode.setVisibility(View.GONE);
                newItem.setVisibility(View.VISIBLE);
                newItem.setFocusableInTouchMode(true);
                newItem.requestFocus();
                newItem.setText(null);
                mNewItemInputLayout.setVisibility(View.VISIBLE);
            }
        });

        add_item = view.findViewById(R.id.add_item);

        if (MDetect.INSTANCE.isUnicode()) {


            itemCode.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemcode)));
            itemName.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemname)));
            pPrice.setHint(Rabbit.zg2uni(getResources().getString(R.string.pprice)));
            sPrice.setHint(Rabbit.zg2uni(getResources().getString(R.string.sprice)));
            unit.setHint(Rabbit.zg2uni(getResources().getString(R.string.unit)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.codetype)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Customer)));
            cType.add(Rabbit.zg2uni(getResources().getString(R.string.Item)));
            if (codeTypeSpinner.getSelectedItemPosition() == 1) {
                add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.addcustomer)));
            }
            if (codeTypeSpinner.getSelectedItemPosition() == 2) {
                add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.additem)));
            }
        } else {
            itemCode.setHint(getResources().getString(R.string.itemcode));
            itemName.setHint(getResources().getString(R.string.itemname));
            pPrice.setHint(getResources().getString(R.string.pprice));
            sPrice.setHint(getResources().getString(R.string.sprice));
            unit.setHint(getResources().getString(R.string.unit));
            /*cType.add(getResources().getString(R.string.codetype));
            cType.add(getResources().getString(R.string.Customer));
            cType.add(getResources().getString(R.string.Item));*/
        }

        add_item.setEnabled(false);

        /*MTGS*/
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        mValidator.setValidationListener(new Validator.ValidationListener() {

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                clearErrors();

                for (int i = 0; i < errors.size(); i++) {
                    ValidationError error = errors.get(i);
                    View view = error.getView();
                    String message = error.getFailedRules().get(0).getMessage(getActivity());

                    if (view instanceof EditText) {
                        // Only request focus in the first view that messes up!
                        if (i == 0)
                            view.requestFocus();

                        //  view.setContentDescription("Error");

                        System.out.println("viewid==="+view.getId());
                        switch (view.getId()) {

                            case R.id.code_editText:
                                //  mCodeInputLayout.setErrorEnabled(true);
                                //   mCodeInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mCodeInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mCodeInputLayout.setError(message);
                                }
                                break;

                            case R.id.name_editText:
                                mNameInputLayout.setErrorEnabled(true);
                                mNameInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mNameInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mNameInputLayout.setError(message);
                                }
                                break;

                            case R.id.pprice_editText:
                                mPPriceInputLayout.setErrorEnabled(true);
                                mPPriceInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mPPriceInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mPPriceInputLayout.setError(message);
                                }
                                break;

                            case R.id.sprice_editText:
                                mSPriceInputLayout.setErrorEnabled(true);
                                mSPriceInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mSPriceInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mSPriceInputLayout.setError(message);
                                }
                                break;

                            case R.id.unit_editText:
                                mUnitInputLayout.setErrorEnabled(true);
                                mUnitInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mUnitInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mUnitInputLayout.setError(message);
                                }
                                break;

                            case R.id.newItem_editText:
                                mNewItemInputLayout.setErrorEnabled(true);
                                mNewItemInputLayout.setContentDescription("Error");
                                if (MDetect.INSTANCE.isUnicode()) {
                                    mNewItemInputLayout.setError(Rabbit.zg2uni(message));
                                } else {
                                    mNewItemInputLayout.setError(message);
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void onValidationSucceeded() {
                ItemCode = view.findViewById(R.id.code_editText);
                EditText itemName = view.findViewById(R.id.name_editText);
                EditText pPrice = view.findViewById(R.id.pprice_editText);
                EditText sPrice = view.findViewById(R.id.sprice_editText);
                EditText unit = view.findViewById(R.id.unit_editText);
                EditText newItem = view.findViewById(R.id.newItem_editText);

                /*hsulattaung*/
                if (codeType.equalsIgnoreCase("I")) {
                    //item_code = MainActivity.barcode;
                    item_name = itemName.getText().toString();
                    p_price = Double.parseDouble(pPrice.getText().toString());
                    s_price = Double.parseDouble(sPrice.getText().toString());
                    item_unit = unit.getText().toString();
                    gType = newItem.getText().toString();
                    controller.add_item(gType, MainActivity.barcode,item_name, p_price, s_price, item_unit, MainActivity.companyId, codeType);
                    clear();
                }

                else if(codeType.equalsIgnoreCase("C") ||
                        codeType.equalsIgnoreCase("V") ||
                        codeType.equalsIgnoreCase("A")){
                    item_code = ItemCode.getText().toString();
                    item_name = itemName.getText().toString();
                    item_unit = unit.getText().toString();
                    gType = newItem.getText().toString();
                    controller.add_item(gType, item_code, item_name, p_price, s_price, item_unit, MainActivity.companyId, codeType);

                }

            }
        });
        return view;
    }

    /*MTGS*/
    public void addItem() {
        boolean success = true;
        if (validate()) {
            resetError();
            if (codeType.equalsIgnoreCase("I")) {
                item_code = itemCode.getText().toString();
                //item_code = MainActivity.barcode;
                item_name = itemName.getText().toString();
                p_price = Double.parseDouble(pPrice.getText().toString());
                s_price = Double.parseDouble(sPrice.getText().toString());
                item_unit = unit.getText().toString();
                gType = newItem.getText().toString();
                success = controller.add_item(gType, item_code, item_name, p_price, s_price, item_unit, MainActivity.companyId, codeType);
            }
            else if(codeType.equalsIgnoreCase("C") ||
                    codeType.equalsIgnoreCase("V") ||
                    codeType.equalsIgnoreCase("A")){
                item_code = itemCode.getText().toString();
                item_name = itemName.getText().toString();
                item_unit = unit.getText().toString();
                gType = newItem.getText().toString();
               // success = controller.add_item(gType, item_code, item_name, p_price, s_price, item_unit, MainActivity.companyId, codeType);
                success = controller.add_item(gType, item_code, item_name, 0.0, 0.0, "", MainActivity.companyId, codeType);
            }

            if (success) {
                Toasty.success(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                clear();
            } else if (!success) {
                if (MDetect.INSTANCE.isUnicode()) {
                    itemCode.setError(Rabbit.zg2uni(getResources().getString(R.string.code_already_exit)));
                    itemCode.setBackgroundResource(R.drawable.err_background);
                } else {
                    itemCode.setError(getResources().getString(R.string.code_already_exit));
                }
            }
        }

    }

    /*MTGS*/
    private void clear() {
        //itemCode.setText(null);
        itemName.setText(null);
        pPrice.setText(null);
        sPrice.setText(null);
        unit.setText(null);
    }

    private void clearErrors() {
        mCodeInputLayout.setErrorEnabled(false);
        mNameInputLayout.setErrorEnabled(false);
        mPPriceInputLayout.setErrorEnabled(false);
        mSPriceInputLayout.setErrorEnabled(false);
        mUnitInputLayout.setErrorEnabled(false);

        mCodeInputLayout.setContentDescription("");
        mNameInputLayout.setContentDescription("");
        mPPriceInputLayout.setContentDescription("");
        mSPriceInputLayout.setContentDescription("");
        mUnitInputLayout.setContentDescription("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        clearErrors();
        resetError();
        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.codeType) {
            if (position == 0) {
                codeType = "";
                add_item.setEnabled(false);
                mCodeInputLayout.setVisibility(View.GONE);
                mNameInputLayout.setVisibility(View.GONE);
                mNewItemInputLayout.setVisibility(View.GONE);
                mPPriceInputLayout.setVisibility(View.GONE);
                mSPriceInputLayout.setVisibility(View.GONE);
                mUnitInputLayout.setVisibility(View.GONE);
                add_item.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                groupcode.setVisibility(View.GONE);
                newItem.setVisibility(View.GONE);
            } else if (position == 1) {
                codeType = "C";
                add_item.setEnabled(true);
                mCodeInputLayout.setVisibility(View.VISIBLE);
                mNameInputLayout.setVisibility(View.VISIBLE);
                mNewItemInputLayout.setVisibility(View.GONE);
                mPPriceInputLayout.setVisibility(View.GONE);
                mSPriceInputLayout.setVisibility(View.GONE);
                mUnitInputLayout.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                groupcode.setVisibility(View.VISIBLE);
                add_item.setVisibility(View.VISIBLE);
                newItem.setVisibility(View.GONE);
                if (MDetect.INSTANCE.isUnicode()) {
                    add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.addcustomer)));
                } else{
                    add_item.setText(getResources().getString(R.string.addcustomer));
                }
            } else if (position == 2) {
                codeType = "I";
                add_item.setEnabled(true);
                mCodeInputLayout.setVisibility(View.VISIBLE);
                mNameInputLayout.setVisibility(View.VISIBLE);
                mNewItemInputLayout.setVisibility(View.VISIBLE);
                mPPriceInputLayout.setVisibility(View.VISIBLE);
                mSPriceInputLayout.setVisibility(View.VISIBLE);
                mUnitInputLayout.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                groupcode.setVisibility(View.VISIBLE);
                add_item.setVisibility(View.VISIBLE);
                newItem.setVisibility(View.GONE);
                if (MDetect.INSTANCE.isUnicode()) {
                    add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.additem)));
                } else{
                    add_item.setText(getResources().getString(R.string.additem));
                }
            }

            else if (position == 3) {
                codeType = "V";
                add_item.setEnabled(true);
                mCodeInputLayout.setVisibility(View.VISIBLE);
                mNameInputLayout.setVisibility(View.VISIBLE);
                mNewItemInputLayout.setVisibility(View.GONE);
                mPPriceInputLayout.setVisibility(View.GONE);
                mSPriceInputLayout.setVisibility(View.GONE);
                mUnitInputLayout.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                groupcode.setVisibility(View.VISIBLE);
                add_item.setVisibility(View.VISIBLE);
                newItem.setVisibility(View.GONE);
                if (MDetect.INSTANCE.isUnicode()) {
                    add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.addsupplier)));
                } else {
                    add_item.setText(getResources().getString(R.string.addsupplier));
                }
            }
                else if (position == 4) {
                    codeType = "A";
                    add_item.setEnabled(true);
                    mCodeInputLayout.setVisibility(View.VISIBLE);
                    mNameInputLayout.setVisibility(View.VISIBLE);
                    mNewItemInputLayout.setVisibility(View.GONE);
                    mPPriceInputLayout.setVisibility(View.GONE);
                    mSPriceInputLayout.setVisibility(View.GONE);
                    mUnitInputLayout.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    groupcode.setVisibility(View.VISIBLE);
                    add_item.setVisibility(View.VISIBLE);
                    newItem.setVisibility(View.GONE);
                    if (MDetect.INSTANCE.isUnicode()) {
                        add_item.setText(Rabbit.zg2uni(getResources().getString(R.string.addAccount)));
                    } else{
                        add_item.setText(getResources().getString(R.string.addAccount));
                    }
            }

            //for change groupcode from code type
            groupType = new ArrayList<>();
            Cursor data = controller.all_data(codeType);
            int numRow = data.getCount();

            groupType.add("Select group code");
            if (numRow > 0) {
                while (data.moveToNext()) {
                    groupType.add(data.getString(0));
                }
            }
            codeTypeSpinner.setOnItemSelectedListener(this);
            ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, groupType);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupcode.setAdapter(aa);
        } else {
            if (position != 0) {
                Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                newItem.setText(parent.getItemAtPosition(position).toString());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        newItem.setText(parent.getItemAtPosition(position).toString());
    }


    //MTGS//
    private boolean validate() {
        boolean validate = true;
        if (groupcode.getVisibility() == View.VISIBLE) {
            if (groupcode.getSelectedItemPosition() == 0) {
                if (MDetect.INSTANCE.isUnicode()) {
                    Toast.makeText(getActivity(), Rabbit.zg2uni(getResources().getString(R.string.group_code)), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.group_code), Toast.LENGTH_LONG).show();
                }
                validate = false;
            }
        }

        if (newItem.getVisibility() == View.VISIBLE) {
            if (newItem.getText().toString().isEmpty() || newItem.getText().toString() == null) {
                if (MDetect.INSTANCE.isUnicode()) {
                    newItem.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
                } else {
                    newItem.setError(getResources().getString(R.string.required));
                }
                validate = false;
            }
        }
        if (itemName.getText().toString().isEmpty() || itemName.getText().toString() == null) {
            if (MDetect.INSTANCE.isUnicode()) {
                itemName.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
            } else {
                itemName.setError(getResources().getString(R.string.required));
            }
            validate = false;
        }

        if (itemCode.getText().toString().isEmpty() || itemCode.getText().toString() == null) {
            if (MDetect.INSTANCE.isUnicode()) {
                itemCode.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
            } else {
                itemCode.setError(getResources().getString(R.string.required));
            }
            validate = false;
        }

        if (codeTypeSpinner.getSelectedItemPosition() == 2) {
            if (pPrice.getText().toString().isEmpty() || pPrice.getText().toString() == null) {
                if (MDetect.INSTANCE.isUnicode()) {
                    pPrice.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
                } else {
                    pPrice.setError(getResources().getString(R.string.required));
                }
                validate = false;
            }
            if (sPrice.getText().toString().isEmpty() || sPrice.getText().toString() == null) {
                if (MDetect.INSTANCE.isUnicode()) {
                    sPrice.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
                } else {
                    sPrice.setError(getResources().getString(R.string.required));
                }
                validate = false;
            }
            if (unit.getText().toString().isEmpty() || unit.getText().toString() == null) {
                if (MDetect.INSTANCE.isUnicode()) {
                    unit.setError(Rabbit.zg2uni(getResources().getString(R.string.required)));
                } else {
                    unit.setError(getResources().getString(R.string.required));
                }
                validate = false;
            }
        }
        return validate;
    }

    private void resetError() {
        newItem.setError(null);
        itemName.setError(null);
        itemCode.setError(null);
        itemCode.setBackgroundResource(R.drawable.border);
        pPrice.setError(null);
        sPrice.setError(null);
        unit.setError(null);
    }
}