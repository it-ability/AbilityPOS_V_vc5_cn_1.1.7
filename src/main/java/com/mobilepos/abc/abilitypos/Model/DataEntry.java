package com.mobilepos.abc.abilitypos.Model;

public class DataEntry {
    private String codeType;
    private String itemName;
    private String itemCode;
    private Double itemQty;
    private Double itemPPrice;
    private Double itemSPrice;
    private Double itemTotalPrice;
    private String itemUnit;
    private String voucherNo;
    private Double Pay;
    private Double Credits;
    private Double changePrice;
    private String cusName;
    private String flat;

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public DataEntry() {
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getItemQty() {
        return itemQty;
    }

    public void setItemQty(Double itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public Double getItemPPrice() {
        return itemPPrice;
    }

    public void setItemPPrice(Double itemPPrice) {
        this.itemPPrice = itemPPrice;
    }

    public Double getItemSPrice() {
        return itemSPrice;
    }

    public void setItemSPrice(Double itemSPrice) {
        this.itemSPrice = itemSPrice;
    }

    public Double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(Double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public Double getPay() {
        return Pay;
    }

    public void setPay(Double pay) {
        Pay = pay;
    }

    public Double getCredits() {
        return Credits;
    }

    public void setCredits(Double credits) {
        Credits = credits;
    }

    public Double getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(Double changePrice) {
        this.changePrice = changePrice;
    }



    @Override
    public String toString() {
        return "DataEntry{" +
                "codeType='" + codeType + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemQty=" + itemQty +
                ", itemPPrice=" + itemPPrice +
                ", itemSPrice=" + itemSPrice +
                ", itemTotalPrice=" + itemTotalPrice +
                ", itemUnit='" + itemUnit + '\'' +
                ", voucherNo='" + voucherNo + '\'' +
                ", Pay=" + Pay +
                ", Credits=" + Credits +
                ", changePrice=" + changePrice +
                '}';
    }
}
