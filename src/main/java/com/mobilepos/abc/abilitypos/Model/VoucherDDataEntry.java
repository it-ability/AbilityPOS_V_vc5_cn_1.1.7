package com.mobilepos.abc.abilitypos.Model;

public class VoucherDDataEntry {

    String VoucherNo, Name, DetailsCode, CodeType, CompanyId, VDate, FlatD;
    Double Pprice, SPrice, Qty, Amt;

    public VoucherDDataEntry() {

    }


    public String getVoucherNo() {
        return VoucherNo;
    }

    public String setVoucherNo(String VoucherNo) {
        this.VoucherNo = VoucherNo;
        return VoucherNo;
    }

    public String getName() {
        return Name;
    }

    public String setName(String Name) {
        this.Name = Name;
        return Name;
    }

    public String getDetailsCode() {
        return DetailsCode;
    }

    public String setDetailsCode(String DetailsCode)
    {
        this.DetailsCode=DetailsCode;
        return DetailsCode;
    }

    public Double getPprice()
    {
        return Pprice;
    }
    public Double setPprice(Double Pprice)
    {
        this.Pprice=Pprice;
        return Pprice;
    }

    public Double getSPrice()
    {
        return SPrice;
    }
    public Double setSPrice(Double SPrice)
    {
        this.SPrice=SPrice;
        return SPrice;
    }

    public Double getQty()
    {
        return Qty;
    }
    public Double setQty(Double Qty)
    {
        this.Qty=Qty;
        return Qty;
    }

    public Double getAmt()
    {
        return Amt;
    }

    public Double setAmt(Double Amt)
    {
        this.Amt=Amt;

        return Amt;
    }

    public String getCodeType()
    {
        return CodeType;
    }
    public String setCodeType(String CodeType)
    {
        this.CodeType=CodeType;
        return CodeType;
    }
    public String getCompanyId()
    {
        return CompanyId;
    }

    public String setCompanyId(String CompanyId)
    {
        this.CompanyId= CompanyId;
        return CompanyId;
    }
    public String getVDate()
    {
        return VDate;
    }
    public String setVDate(String VDate)
    {
        this.VDate=VDate;
        return VDate;
    }
    public String getFlatD()
    {
        return FlatD;
    }

    public String setFlatD(String flatD) {
        this.FlatD = flatD;
        return flatD;
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


