package com.mobilepos.abc.abilitypos.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobilepos.abc.abilitypos.R;

public class EditItemFragment extends Fragment {

    EditText name, code, pprice, sprice, unit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        name = view.findViewById(R.id.name_editText);
        code = view.findViewById(R.id.code_editText);
        pprice = view.findViewById(R.id.pprice_editText);
        sprice = view.findViewById(R.id.sprice_editText);
        unit = view.findViewById(R.id.unit_editText);


        return view;
    }

}
