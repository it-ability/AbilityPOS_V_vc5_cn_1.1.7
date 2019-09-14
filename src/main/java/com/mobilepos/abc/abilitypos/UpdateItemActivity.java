package com.mobilepos.abc.abilitypos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Database.DB_Controller;

import es.dmoral.toasty.Toasty;
import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.Rabbit;

public class UpdateItemActivity extends AppCompatActivity {

    EditText code, name, pprice, sprice, unit;
    Button update;
    DB_Controller controller;
    String codeType,itemcode = "", itemname = "", itemunit = "";
    Double itempprice = 0.0, itemsprice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        controller = new DB_Controller(getApplicationContext());

        code = findViewById(R.id.code_editText);
        name = findViewById(R.id.name_editText);
        pprice = findViewById(R.id.pprice_editText);
        sprice = findViewById(R.id.sprice_editText);
        unit = findViewById(R.id.unit_editText);
        update = findViewById(R.id.edit_item);
        codeType = getIntent().getExtras().getString("codeType");


        if (MDetect.INSTANCE.isUnicode()) {
            if(codeType.equalsIgnoreCase("C") ||
               codeType.equalsIgnoreCase("V") ||
               codeType.equalsIgnoreCase("A")){
                code.setText(Rabbit.zg2uni(getIntent().getExtras().getString("code")));
                code.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemcode)));
                code.setEnabled(false);
                name.setText(Rabbit.zg2uni(getIntent().getExtras().getString("name")));
                name.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemname)));
                pprice.setVisibility(View.GONE);
                sprice.setVisibility(View.GONE);
                unit.setVisibility(View.GONE);
            }else if(codeType.equalsIgnoreCase("I")){
                code.setText(Rabbit.zg2uni(getIntent().getExtras().getString("code")));
                code.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemcode)));
                code.setEnabled(false);
                name.setText(Rabbit.zg2uni(getIntent().getExtras().getString("name")));
                name.setHint(Rabbit.zg2uni(getResources().getString(R.string.itemname)));
                pprice.setText(Rabbit.zg2uni(getIntent().getExtras().getString("pprice")));
                pprice.setHint(Rabbit.zg2uni(getResources().getString(R.string.pprice)));
                sprice.setText(Rabbit.zg2uni(getIntent().getExtras().getString("sprice")));
                sprice.setHint(Rabbit.zg2uni(getResources().getString(R.string.sprice)));
                unit.setText(Rabbit.zg2uni(getIntent().getExtras().getString("unit")));
                unit.setHint(Rabbit.zg2uni(getResources().getString(R.string.unit)));
                update.setText(Rabbit.zg2uni(Rabbit.zg2uni(getResources().getString(R.string.update))));
                this.setTitle(Rabbit.zg2uni(getResources().getString(R.string.update)));
            }
            else {
                code.setEnabled(false);
                code.setText(getIntent().getExtras().getString("code"));
                name.setText(getIntent().getExtras().getString("name"));
                pprice.setText(getIntent().getExtras().getString("pprice"));
                sprice.setText(getIntent().getExtras().getString("sprice"));
                unit.setText(getIntent().getExtras().getString("unit"));
                update.setText(getResources().getString(R.string.update));
                this.setTitle(getResources().getString(R.string.update));
            }
        }
        if(codeType.equalsIgnoreCase("I")){

            code.setText(getIntent().getExtras().getString("code"));
            code.setEnabled(false);
            name.setText(getIntent().getExtras().getString("name"));
            pprice.setText(getIntent().getExtras().getString("pprice"));
            sprice.setText(getIntent().getExtras().getString("sprice"));
            unit.setText(getIntent().getExtras().getString("unit"));
            update.setText(getResources().getString(R.string.update));
            this.setTitle(getResources().getString(R.string.update));
        }else if(codeType.equalsIgnoreCase("C") ||
                codeType.equalsIgnoreCase("V") ||
                codeType.equalsIgnoreCase("A")){
            code.setText(getIntent().getExtras().getString("code"));
            code.setEnabled(false);
            name.setText(getIntent().getExtras().getString("name"));
            pprice.setVisibility(View.GONE);
            sprice.setVisibility(View.GONE);
            unit.setVisibility(View.GONE);
            update.setText(getResources().getString(R.string.update));
            this.setTitle(getResources().getString(R.string.update));
        }
        else{
            System.out.println("Uni code or not+++++++++++++++++++"+codeType.toString());
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeType.equalsIgnoreCase("C") ||
                   codeType.equalsIgnoreCase("V") ||
                   codeType.equalsIgnoreCase("A")){
                    itemname = name.getText().toString();
                    itemcode = code.getText().toString();
                    itempprice = Double.valueOf("0");
                    itemsprice = Double.valueOf("0");
                    itemunit = " ";
                }
                else if(codeType.equalsIgnoreCase("I")){
                    itemname = name.getText().toString();
                    itemcode = code.getText().toString();
                    itempprice = Double.valueOf(pprice.getText().toString());
                    itemsprice = Double.valueOf(sprice.getText().toString());
                    itemunit = unit.getText().toString();
                }
                controller.updateData(itemcode, itemname, itempprice, itemsprice, itemunit);
                finish();
                Toasty.success(UpdateItemActivity.this, "Successfully update", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
