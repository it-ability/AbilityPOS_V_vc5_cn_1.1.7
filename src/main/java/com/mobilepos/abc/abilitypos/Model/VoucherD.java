package com.mobilepos.abc.abilitypos.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoucherD implements Serializable {
//
//    String VoucherNo, Name, DetailsCode, CodeType, CompanyId, VDate, FlatD;
//    Double Pprice, SPrice, Qty, Amt;
    @SerializedName("VoucherNo")
    private String VoucherNo;

    @SerializedName("Name")
    private String Name;

    @SerializedName("DetailsCode")
    private String DetailsCode;

    @SerializedName("CodeType")
    private String CodeType;

    @SerializedName("CompanyId")
    private String CompanyId;

    @SerializedName("VDate")
    private String VDate;

    @SerializedName("FlatD")
    private String FlatD;

    @SerializedName("PPrice")
    private Double Pprice;

    @SerializedName("SPrice")
    private Double SPrice;

    @SerializedName("Qty")
    private Double Qty;

    @SerializedName("Amt")
    private Double Amt;

    public VoucherD( String VoucherNo, String Name, String DetailsCode, Double Pprice,
                     Double SPrice, Double Qty, Double Amt, String CodeType, String CompanyId,
                     String VDate, String FlatD )
    {
        this.VoucherNo=VoucherNo;
        this.Name=Name;
        this.DetailsCode=DetailsCode;
        this.SPrice=SPrice;
        this.Pprice=Pprice;
        this.Qty=Qty;
        this.Amt=Amt;
        this.CodeType=CodeType;
        this.CompanyId=CompanyId;
        this.VDate=VDate;
        this.FlatD=FlatD;


    }
    public VoucherD() {

    }


    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String VoucherNo) {
        this.VoucherNo = VoucherNo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDetailsCode() {
        return DetailsCode;
    }

    public void setDetailsCode(String DetailsCode)
    {
        this.DetailsCode=DetailsCode;
    }

    public Double getPprice()
    {
        return Pprice;
    }
    public void setPprice(Double Pprice)
    {
        this.Pprice=Pprice;
    }

    public Double getSPrice()
    {
        return SPrice;
    }
    public void setSPrice(Double SPrice)
    {
        this.SPrice=SPrice;
    }

    public Double getQty()
    {
        return Qty;
    }
    public void setQty(Double Qty)
    {
        this.Qty=Qty;
    }

    public Double getAmt()
    {
        return Amt;
    }

    public void setAmt(Double Amt)
    {
        this.Amt=Amt;

    }

    public String getCodeType()
    {
        return CodeType;
    }
    public void setCodeType(String CodeType)
    {
        this.CodeType=CodeType;
    }
    public String getCompanyId()
    {
        return CompanyId;
    }

    public void setCompanyId(String CompanyId)
    {
        this.CompanyId= CompanyId;
    }
    public String getVDate()
    {
        return VDate;
    }
    public void setVDate(String VDate)
    {
        this.VDate=VDate;
    }
    public String getFlatD()
    {
        return FlatD;
    }

    public void setFlatD(String flatD) {
        this.FlatD = flatD;
    }

    public String toString()
    {
        return
                "VoucherNo='" + VoucherNo + '\'' +
                        ", Name='" + Name + '\'' +
                        ", DetailsCode='" + DetailsCode + '\'' +
                        ", Pprice=" + Pprice +
                        ", SPrice=" + SPrice +
                        ", Qty=" + Qty +
                        ", Amt=" + Amt +
                        ", CodeType='" + CodeType + '\'' +
                        ", CompanyId='" + CompanyId + '\'' +
                        ", VDate=" + VDate +
                        ", FlatD=" + FlatD;
    }

}


