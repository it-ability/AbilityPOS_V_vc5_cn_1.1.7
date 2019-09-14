package com.mobilepos.abc.abilitypos.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

public class VoucherH implements Serializable {
//    Double Discount,Tax,Amount,Paid,Balance,CurBalance;
//    String VoucherNo,CusName,VrType,CompanyId,Remark,FlatH;
//    Integer UserId;
//    String VDate;

      @SerializedName("Discount")
      private Double Discount;

      @SerializedName("Tax")
      private Double Tax;

      @SerializedName("Amount")
      private Double Amount;

      @SerializedName("Paid")
      private Double Paid;

      @SerializedName("Balance")
      private Double Balance;

      @SerializedName("CurBalance")
      private Double CurBalance;

      @SerializedName("VoucherNo")
      private String VoucherNo;

      @SerializedName("CusName")
      private String CusName;

      @SerializedName("VrType")
      private String VrType;

      @SerializedName("ComapanyId")
      private String CompanyId;

      @SerializedName("Remark")
      private String Remark;

      @SerializedName("FlatH")
      private String FlatH;

      @SerializedName("VDate")
      private String VDate;

      @SerializedName("UserId")
      private Integer UserId;


    public VoucherH(String VoucherNo, Integer UserId, String CusName, String VrType,
                    Double Discount, Double Tax, String CompanyId, Double Amount,
                    Double Paid, Double Balance, Double CurrentBal, String Remark, String VDate, String FlatH)
    {
        this.VoucherNo=VoucherNo;
        this.UserId=UserId;
        this.CusName=CusName;
        this.VrType=VrType;
        this.Discount=Discount;
        this.Tax=Tax;
        this.CompanyId= CompanyId;
        this.Amount=Amount;
        this.Paid=Paid;
        this.Balance=Balance;
        this.CurBalance=CurrentBal;
        this.Remark=Remark;
        this.VDate=VDate;
        this.FlatH=FlatH;
    }
    public VoucherH()
    {

    }
    public Double getDiscount(){return Discount;}
    public void setDiscount(Double Discount){this.Discount=Discount;}

    public Double getTax(){return Tax;}
    public void setTax(Double Tax){this.Tax=Tax;}

    public Double getAmount(){return Amount;}
    public void setAmount(Double Amount){this.Amount=Amount;}

    public Double getPaid(){return Paid;}
    public void setPaid(Double Paid){this.Paid=Paid;}

    public Double getBalance(){return Balance;}
    public void setBalance(Double Balance){this.Balance=Balance;}

    public Double getCurBalance(){return CurBalance;}
    public void setCurBalance(Double CurBalance){this.CurBalance=CurBalance;}

    public String getVoucherNo(){return VoucherNo;}
    public void setVoucherNo(String VoucherNo){this.VoucherNo=VoucherNo; }

    public String getCusName(){return CusName;}
    public void setCusName(String CusName){this.CusName=CusName;}

    public String getVrType(){return VrType;}
    public void setVrType(String VrType){this.VrType=VrType;}

    public String getCompanyId(){return CompanyId;}
    public void setCompanyId(String CompanyId){this.CompanyId=CompanyId;}

    public String getRemark(){return Remark;}
    public void setRemark(String Remark){this.Remark=Remark;}

    public String getFlatH(){return FlatH;}
    public void setFlatH(String FlatH){this.FlatH=FlatH;}

    public Integer getUserId(){return UserId;}
    public void setUserId(Integer UserId){this.UserId=UserId;}

    public String getVDate(){return VDate;}
    public void setVDate(String VDate){this.VDate=VDate;}


    public String toString()
    {
        return
                        "VoucherNo='" + VoucherNo + '\'' +
                        ", UserId='" + UserId + '\'' +
                        ", CusName='" + CusName + '\'' +
                        ", VrType=" + VrType +
                        ", Discount=" + Discount +
                        ", Tax=" + Tax +
                        ", CompanyId=" + CompanyId +
                        ", Amount='" + Amount + '\'' +
                        ", Paid='" + Paid + '\'' +
                        ", Balanece=" + Balance +
                        ", CurBalance=" + CurBalance +
                        ", Remark=" + Remark +
                        ", FlatH=" + FlatH +
                        ", VDate=" + VDate ;
    }


}
