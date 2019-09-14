package com.mobilepos.abc.abilitypos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.mobilepos.abc.abilitypos.Fragment.SaleItemFragment;
import com.mobilepos.abc.abilitypos.ReportDetailActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class DB_Controller extends SQLiteOpenHelper {

    public String date;
    Context mContext;


    public DB_Controller(Context context) {
        super(context, "ABILITY.db", null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table USER(UserId Integer Primary Key Autoincrement, UserName Text, Contact Text, Gender Text, Address Text, Email Text, Password Text, ConcatRole Text, CompanyId Text,CreateDate Date, Active Integer);");
        sqLiteDatabase.execSQL("create table ITEM(Id Integer Primary Key Autoincrement, GroupCode Text,DetailCode Text UNIQUE, Name Text, PPrice Double, SPrice Double, Unit Text, CompanyId Text, CodeType Text);");
        sqLiteDatabase.execSQL("CREATE TABLE VoucherH(VoucherNo Text, UserId Integer, CusName Text,VrType Text, Discount Double, Tax Double, CompanyId Text, Amount Double, Paid Double, Balance Double, CurrentBal Double, Remark Text, VDate Date, FlatH String);");
        sqLiteDatabase.execSQL("CREATE TABLE VoucherD(VoucherNo Text , Name Text, DetailCode Text,Pprice Double, SPrice Double, Qty Double, Amt Double, CodeType Text, CompanyId Text, VDate Date, FlatD String);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USER;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ITEM;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS VoucherH;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS VoucherD;");
    }

    public void activateAccount(String username, String contact, String gender, String address, String email, String password, String role, String cmdId, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", username);
        contentValues.put("Contact", contact);
        contentValues.put("Gender", gender);
        contentValues.put("Address", address);
        contentValues.put("Email", email);
        contentValues.put("Password", password);
        contentValues.put("ConcatRole", role);
        contentValues.put("CompanyId", cmdId);
        contentValues.put("CreateDate", date);
        contentValues.put("Active", 1);
        this.getWritableDatabase().insertOrThrow("USER", "", contentValues);
    }

    public boolean
    add_item(String groupCode, String itemcode, String name, double pprice, double sprice, String unit, String companyId, String codeType) {
        boolean success = true;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("GroupCode", groupCode);
            contentValues.put("DetailCode", itemcode);   // GET
            contentValues.put("Name", name);             //GET
            contentValues.put("PPrice", pprice);
            contentValues.put("SPrice", sprice);
            contentValues.put("Unit", unit);             //GET
            contentValues.put("CompanyId", companyId);
            contentValues.put("CodeType", codeType);     //GET
            db.insertOrThrow("ITEM", "", contentValues);
            db.close();
            success = true;
        } catch (SQLiteConstraintException e) {
            success = false;
            Log.e("<<System === >>", "<<Exception ===>>" + e.toString());
        }
        return success;
    }

    public void insert_voucherH(String vno, Integer userId, String CusName, String vrType, double discount, double tax, String companyId, Double Amount, Double Paid, Double Balance, Double cureentBalance, String Remark, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("VoucherNo", vno);             // 0
        contentValues.put("UserId", userId);             // 1
        contentValues.put("CusName", CusName);           // 2
        contentValues.put("VrType", vrType);
        contentValues.put("Discount", discount);
        contentValues.put("Tax", tax);
        contentValues.put("CompanyId", companyId);
        contentValues.put("Amount", Amount);
        contentValues.put("Paid", Paid);
        contentValues.put("Balance", Balance);
        contentValues.put("CurrentBal", cureentBalance);
        contentValues.put("Remark", Remark);
        contentValues.put("VDate", date);
        contentValues.put("FlatH","N");
        this.getWritableDatabase().insertOrThrow("VoucherH", "", contentValues);
    }

    public void insert_vourherD(String vno, String itemName, String itemCode, Double pPrice, Double sPirce, Double qty, Double amt, String codeType, String companyId, String date) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("VoucherNo", vno);
        contentValues.put("Name", itemName);
        contentValues.put("DetailCode", itemCode);
        contentValues.put("Pprice", pPrice);
        contentValues.put("SPrice", sPirce);
        contentValues.put("Qty", qty);
        contentValues.put("Amt", amt);
        //   contentValues.put("Profit",profit);
        contentValues.put("CodeType", codeType);
        contentValues.put("CompanyId", companyId);
        contentValues.put("VDate", date);
        contentValues.put("FlatD","N");
        this.getWritableDatabase().insertOrThrow("VoucherD", "", contentValues);
    }

    public Cursor signin_owner(String username, String passw) {

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USER WHERE Email='" + username + "'" + "AND Password='" + passw + "'", null);
        return cursor;
    }

    public boolean defaultUser() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM USER WHERE ConcatRole='O'", null);

        boolean hasTables = false;
        if (cursor.getCount() == 0) {
            hasTables = false;
        } else if (cursor.getCount() > 0) {
            hasTables = true;
        }

        cursor.close();

        return hasTables;
    }

    public boolean defaultCustomer() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ITEM WHERE CodeType='C'", null);

        boolean hasTables = false;
        if (cursor.getCount() == 0) {
            hasTables = false;
        } else if (cursor.getCount() > 0) {
            hasTables = true;
        }

        cursor.close();

        return hasTables;
    }

    public boolean defaultVender() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM ITEM WHERE CodeType='V'", null);

        boolean hasTables = false;
        if (cursor.getCount() == 0) {
            hasTables = false;
        } else if (cursor.getCount() > 0) {
            hasTables = true;
        }

        cursor.close();

        return hasTables;
    }

    public Cursor getUserData(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USER WHERE UserName='" + email + "'", null);
        return cursor;
    }

    public Cursor getCustomerCombo() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEM WHERE CodeType='C'", null);
        return cursor;
    }

    //for export userdata
    public Cursor getUserDatas() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM USER", null);
        return cursor;
    }

    //for export itemdata
    public Cursor getItemDatas() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM ITEM", null);
        return cursor;
    }


    //repair code VrD 1
    //for export VoucherD
    public Cursor getVoucherDDatas() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM VoucherD", null);
        return cursor;
    }

    //repair code VrH 1
    //for export VoucherH
    public Cursor getVoucherHDatas() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM VoucherH", null);
        return cursor;
    }


    //repair code VrH 2
    public Cursor getVoucherHDatasForCloud()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VoucherH WHERE FlatH = 'N' ", null);
        return cursor;
    }


    //repair code VrD 2
    public Cursor getVoucherDDatasForCloud()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM VoucherD WHERE FlatD = 'N' ", null);
        return cursor;
    }


    //repair code all data from API
    //delete User Table
    public void deleteUser() {
        this.getWritableDatabase().delete("USER", "", null);
    }

    //delete Item Table
    public void deleteItem() {
        this.getWritableDatabase().delete("ITEM", "", null);
    }

    //delete VoucherD Table
    public void deleteVoucherD() {
        this.getWritableDatabase().delete("VoucherD", "", null);
    }

    //delete VoucherH Table
    public void deleteVoucherH() {
        this.getWritableDatabase().delete("VoucherH", "", null);
    }

    public Cursor all_data(String cType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT DISTINCT GroupCode FROM ITEM WHERE CodeType='" + cType + "'", null);
        return data;
    }

    public Cursor all_cus(String codeType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT DISTINCT Name FROM ITEM WHERE CodeType='" + codeType + "'", null);
        return data;
    }

    public Cursor search_data(String code ) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            Cursor data = db.rawQuery("SELECT * FROM ITEM where (DetailCode like '" + code + "%' or Name like '" + code + "%') and CodeType='A'", null);
            return data;
        }
        Cursor data = db.rawQuery("SELECT * FROM ITEM where (DetailCode like '" + code + "%' or Name like '" + code + "%') and CodeType='I'", null);

        return data;
    }

    public Cursor search_custom_data(String customdata ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM ITEM where Name like '" + customdata + "%' and CodeType='A'", null);
        return data;
    }


    //repair code VrH 3
    public Cursor search_customer(String cusname, String vrType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM VoucherH where CusName like '" + cusname + "%' and CurrentBal>0 and VrType='" + vrType + "'", null);
        return data;
    }

    public Cursor update_data(String codeType, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM ITEM where (DetailCode like '" + code + "%' or Name like '" + code + "%') and CodeType='" + codeType + "'", null);
        return data;
    }

    //repair code VrH 4

    public void update_voucherHFlatH(String flatH)
    {

       this.getWritableDatabase().execSQL("Update VoucherH Set FlatH='" + flatH + "' WHERE FlatH='N' ");

    }

    //repair code VrD 3

    public void update_voucherDFlatD(String flatD)
    {

        this.getWritableDatabase().execSQL("Update VoucherD Set FlatD='" + flatD + "' WHERE FlatD='N' ");

    }

    //repair code VrH 5
    public Cursor all_vrdata(String vrtype) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT VoucherNo FROM VoucherH WHERE VrType='" + vrtype + "'", null);
        return data;
    }

    //repair code VrH 6
    /*MTGS*/
    public Cursor allVoucher() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT VoucherNo FROM VoucherH", null);
        return data;
    }

    //repair code VrD 4
    public Cursor all_detailVr(String vrno) {
        SQLiteDatabase database = this.getWritableDatabase();
        if (SaleItemFragment.check.equals("0") || SaleItemFragment.check.equals("3")){
            Cursor data = database.rawQuery("SELECT * FROM VoucherD WHERE CodeType='A' AND VoucherNo='" + vrno + "'", null);
            return data;
        }
        Cursor data = database.rawQuery("SELECT * FROM VoucherD WHERE CodeType='I' AND VoucherNo='" + vrno + "'", null);
        return data;
    }

    //repair code VrH 7
    public Cursor all_detailH(String vrno) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM VoucherH WHERE VoucherNo='" + vrno + "'", null);
        return data;

    }
    public Cursor select_distinct(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("select detailcode ,  Name  from ITEM where codetype ='A'",null);
        return data;
    }

    public Cursor select_detailH(String vType) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM VoucherH WHERE VrType='" + vType + "'", null);
        return data;
    }

    public Cursor select_DV_detailH(String date, String vType) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate='" + date + "' AND VrType='" + vType + "'", null);
        return data;
    }

    public Cursor select_DV_detailH(String startDate, String endDate, String vType) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "'", null);
        //VDate BETWEEN '"+startDate+"' and '"+endDate+"'
        return data;
    }
    //repair code VrH 8

    public Cursor select_DV_detailHBy(String startDate, String endDate, String vType, String cusName,String today) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = null;
         /* if (cusName.equals("All") || cusName.equals("")) {
              data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "'", null);
          }*/
        if (SaleItemFragment.check.equals("1")) {
            if (cusName.equalsIgnoreCase("All") || cusName.equalsIgnoreCase("")){
             //   data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "'", null);
                data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='1'", null);
            }else
            {
                data = database.rawQuery("SELECT * FROM VoucherH WHERE CusName='" + cusName + "' AND VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='1'", null);
            }
          }
        if (SaleItemFragment.check.equals("2")) {
            if (cusName.equalsIgnoreCase("All") || cusName.equalsIgnoreCase("")){
                data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='2'", null);
            }else
            {
                data = database.rawQuery("SELECT * FROM VoucherH WHERE CusName='" + cusName + "' AND VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='2'", null);
            }
        }
        if (SaleItemFragment.check.equals("0")){
            data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='0'", null);
        }
        if (SaleItemFragment.check.equals("3")){
            data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='3'", null);
        }

        /*    //System.out.println("&&&&&&&&&&&&&&&&" + data.toString());
            System.out.println("&&&&&&&&&&&&&&&&" + "SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "'");
        } else {

            data = database.rawQuery("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "' AND CusName='" + cusName + "' ", null);
            System.out.println("^^^^^^^^^" + data.toString());


        }
        //VDate BETWEEN '"+startDate+"' and '"+endDate+"'
        System.out.println("SELECT * FROM VoucherH WHERE VDate BETWEEN '" + startDate + "' AND '" + endDate + "' AND VrType='" + vType + "' AND CusName='" + cusName + "' ");*/
        return data;
    }

    public Cursor select_Customer() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = null;
        if (SaleItemFragment.check.equals("1")){
             data = database.rawQuery("SELECT DISTINCT CusName FROM VoucherH WHERE VrType='1'", null);

        }
        else if (SaleItemFragment.check.equals("2")){
             data = database.rawQuery("SELECT DISTINCT CusName FROM VoucherH WHERE VrType='2'", null);

           
        }
       // Cursor data = database.rawQuery("SELECT DISTINCT CusName FROM VoucherH ", null);
        //VDate BETWEEN '"+startDate+"' and '"+endDate+"'
      //  return data;
        return data;
    }
    


    public void deleteRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ITEM WHERE DetailCode='" + value + "'");
        db.close();
    }

    public void deleteUser(String userId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM USER WHERE UserId='" + userId + "'");
    }

    //repair code VrH and VrD
    public void deleteVoucher(String vrno) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM VoucherD WHERE VoucherNo='" + vrno + "'");
        database.execSQL("DELETE FROM VoucherH WHERE VoucherNo='" + vrno + "'");
    }


    /*public void deleteVoucherR() {
        this.getWritableDatabase().delete("VoucherD", "VoucherNo", null);
        this.getWritableDatabase().delete("VoucherH", "VoucherNo", null);
    }*/





    /*public void deleteVoucher(String voucherNo){
        this.getWritableDatabase().delete("VoucherD","VoucherNo='"+voucherNo+"'",null);

    }*/

    public void updateData(String itemcode, String name, double pprice, double sprice, String unit) {
        /*SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DetailCode",itemcode);
        contentValues.put("Name",name);
        contentValues.put("PPrice",pprice);
        contentValues.put("SPrice",sprice);
        contentValues.put("Unit",unit);
        db.update("ITEM", contentValues, "DetailCode = ?",new String[] { itemcode });*/
        this.getWritableDatabase().execSQL("update ITEM SET Name='" + name + "', PPrice='" + pprice + "', SPrice='" + sprice + "', Unit='" + unit + "' WHERE DetailCode='" + itemcode + "'");
        /*return true;*/
    }

    public void updateUser(String id, String name, String contact, String gender, String email, String address, String password, String active) {

        this.getWritableDatabase().execSQL("update USER SET UserName='" + name + "', Contact='" + contact + "', Gender='" + gender + "', Address='" + address + "', Email='" + email + "', Password='" + password + "', Active='" + active + "' WHERE UserId='" + id + "'");

    }

    //for backup database
    public void backupDB(String outFileName) {
        //database path
        final String inFileName = mContext.getDatabasePath("ABILITY.db").toString();
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            // Close the streams
            output.flush();
            output.close();
            fis.close();
            Toasty.success(mContext, "Backup Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toasty.error(mContext, "Unable to Backup Database !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    //for restore database
    public void importDB(String inFileName) {
        final String outFileName = mContext.getDatabasePath("ABILITY.db").toString();
        try {
            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);
            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            // Close the streams
            output.flush();
            output.close();
            fis.close();
            Toasty.success(mContext, "Import Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toasty.error(mContext, "Unable to Import Database !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Cursor selectReportByCodeType(String codeType, String detailCodeFrom, String detailCodeTo, String StartDate, String EndDate)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = null;
        String strsql = "";

        if ( codeType == "C")
        {
            strsql = " SELECT GroupCode,DetailCode,Name,Unit FROM ITEM WHERE CodeType='" + codeType + "'";
            if (detailCodeFrom != null && detailCodeTo != null)
            {
                strsql = strsql+ " and DetailCode between " + "'" + detailCodeFrom + "'" + "and" + " " + "'" + detailCodeTo + "'";
            }
            strsql = strsql + "Group by DetailCode";
        }


        else if ( codeType == "V")
        {
            strsql = " SELECT GroupCode,DetailCode,Name,Unit FROM ITEM WHERE CodeType='" + codeType + "'";
            if (detailCodeFrom != null && detailCodeTo != null)
            {
                strsql = strsql+ " and DetailCode between " + "'" + detailCodeFrom + "'" + "and" + " " + "'" + detailCodeTo + "'";
            }
            strsql = strsql + "Group by DetailCode";
        }


        else if (codeType == "I")
        {

            strsql = " select item.name as GroupCode, Item.GroupCode as DetailCode,voucherd.DetailCode as Name, sum(VoucherD.qty) as Unit from VoucherD"  ;
            strsql = strsql + " inner join item  on item.DetailCode = Voucherd.Detailcode";

            if ( detailCodeFrom != null && detailCodeTo != null)
            {
                strsql = strsql + " where voucherd.DetailCode  between " + "'" + detailCodeFrom + "'" + "and" + " " + "'" + detailCodeTo + "'";
            }
            strsql = strsql + " group by Voucherd.DetailCode order by Item.GroupCode, Item.DetailCode";
        }



        else if (codeType == "Pv" ){
            {//Sayar + h.Discount - h.Tax  + h.Discount - h.Tax)
                strsql = "select h.voucherno as GroupCode , (h.Amount - h.Tax + h.Discount)   as DetailCode,h.Discount  as Name,";
                strsql = strsql + " (select (sum(sprice * abs(Qty)) - sum(Pprice * abs(Qty)))  from voucherD d where d.VoucherNo=h.VoucherNo and codetype='I' group by voucherno) as Unit";
                strsql = strsql + " from voucherH h";
            }
            if  (StartDate != null && EndDate != null)
            {
                strsql = strsql + " where VrType='1' and VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "' ORDER BY VDate ASC";
            }
        }



        else if (codeType == "PR")
        {// strsql = " SELECT VoucherNo as GroupCode,VrType/2 as DetailCode,Amount as Name,Amount-Paid  as Unit FROM VoucherH" ;
            strsql = " SELECT VoucherNo as GroupCode,VrType/2 as DetailCode,Amount as Name,Paid  as Unit FROM VoucherH" ;
            strsql = strsql + " WHERE  VrType='2'";
            if (StartDate !=null && EndDate != null)
            {
                strsql = strsql + "and VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "' ORDER BY VDate ASC " ;
            }
        }



        else if (codeType == "AmtAZ")
        {
            strsql = " SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit";
            strsql = strsql + " FROM VoucherD inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";
            if (StartDate != null && EndDate != null)
            {
                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by DetailCode,Name order by Name";
            }
        }


        else if (codeType =="AmtZA") {
            strsql = " SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit";
            strsql = strsql + " FROM VoucherD inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";
            if (StartDate != null && EndDate != null)
            {
                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by DetailCode,Name order by Name desc";
            }
        }


        else if (codeType == "QtyAZ" ) {

            strsql = "  SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit  FROM VoucherD";
            strsql = strsql + " inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";


            if (StartDate != null && EndDate != null) {

                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by Detailcode,Name order by DetailCode asc";
            }
        }


        else if (codeType== "QtyZA") {

            strsql = "  SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit  FROM VoucherD";
            strsql = strsql + " inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";


            if (StartDate != null && EndDate != null) {

                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by Detailcode,Name order by DetailCode desc";
            }
        }


        else if (codeType== "ProfitAZ") {

            strsql = "  SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit  FROM VoucherD";
            strsql = strsql + " inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";


            if (StartDate != null && EndDate != null) {

                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by Detailcode,Name order by Unit";
            }
        }


        else if (codeType== "ProfitZA") {

            strsql = "  SELECT  Name as GroupCode,count(Detailcode) as DetailCode ,sum(sprice * abs(Qty)) as Name,";
            strsql = strsql + "(sum(sprice * abs(Qty)) - sum(pprice * abs(Qty))) as Unit  FROM VoucherD";
            strsql = strsql + " inner join VoucherH on VoucherH.VoucherNo = VoucherD.voucherno";
            strsql = strsql + " where codetype='I' and VrType='1'";


            if (StartDate != null && EndDate != null) {

                strsql = strsql + " AND VoucherH.VDate BETWEEN '" + StartDate +  "' AND '" + EndDate + "'";
                strsql = strsql + " group by Detailcode,Name order by Unit desc";
            }
        }


        else {
            strsql = " select 1 as No, '' as GroupCode,'' as DetailCode ,'Opening Balance' as Name , sum(Amt) as Unit   from VoucherD ";
            strsql = strsql+ " where detailcode = '"+codeType+"' and codeType='A' and Vdate < '" + StartDate + "' union";
            strsql = strsql+ " select 2 as No, Vdate as GroupCode,VoucherNo as DetailCode,Name as Name,Amt as Unit  from VoucherD";
            strsql = strsql+ " where detailcode = '"+codeType+"' and codeType='A' and Vdate between '" + StartDate + "' and '" + EndDate+"' union";
            strsql = strsql+ " select 3 as No,'' as GroupCode,'' as DetailCode ,'Closing Balance' as Name , sum(Amt) as Unit   from VoucherD";
            strsql = strsql+ " where detailcode = '"+codeType+"' and codeType='A' and Vdate <= '" + StartDate + "'";

        }
        data = database.rawQuery(strsql, null);
        return data;
    }

        

}


