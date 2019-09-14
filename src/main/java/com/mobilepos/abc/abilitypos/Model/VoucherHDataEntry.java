package com.mobilepos.abc.abilitypos.Model;

public class VoucherHDataEntry {
    Double Discount,Tax,Amount,Paid,Balance,CurBalance;
    String VoucherNo,CusName,VrType,CompanyId,Remark,FlatH;
    Integer UserId;
    String VDate;


    public Double getDiscount(){return Discount;}
    public Double setDiscount(Double Discount){this.Discount=Discount;
        return Discount;
    }

    public Double getTax(){return Tax;}
    public Double setTax(Double Tax){this.Tax=Tax;
        return Tax;
    }

    public Double getAmount(){return Amount;}
    public Double setAmount(Double Amount){this.Amount=Amount;
        return Amount;
    }

    public Double getPaid(){return Paid;}
    public Double setPaid(Double Paid){this.Paid=Paid;
        return Paid;
    }

    public Double getBalance(){return Balance;}
    public Double setBalance(Double Balance){this.Balance=Balance;
        return Balance;
    }

    public Double getCurBalance(){return CurBalance;}
    public Double setCurBalance(Double CurBalance){this.CurBalance=CurBalance;
        return CurBalance;
    }

    public String getVoucherNo(){return VoucherNo;}
    public String setVoucherNo(String VoucherNo){this.VoucherNo=VoucherNo;
        return VoucherNo;
    }

    public String getCusName(){return CusName;}
    public String setCusName(String CusName){this.CusName=CusName;
        return CusName;
    }

    public String getVrType(){return VrType;}
    public String setVrType(String VrType){this.VrType=VrType;
        return VrType;
    }

    public String getCompanyId(){return CompanyId;}
    public String setCompanyId(String CompanyId){this.CompanyId=CompanyId;
        return CompanyId;
    }

    public String getRemark(){return Remark;}
    public String setRemark(String Remark){this.Remark=Remark;
        return Remark;
    }

    public String getFlatH(){return FlatH;}
    public String setFlatH(String FlatH){this.FlatH=FlatH;
        return FlatH;
    }

    public Integer getUserId(){return UserId;}
    public Integer setUserId(Integer UserId){this.UserId=UserId;
        return UserId;
    }

    public String getVDate(){return VDate;}
    public String setVDate(String VDate){this.VDate=VDate;
        return VDate;
    }

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
