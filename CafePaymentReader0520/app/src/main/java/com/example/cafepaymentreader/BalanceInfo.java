package com.example.cafepaymentreader;

public class BalanceInfo {

      private String balanceamount;

    public BalanceInfo(String balanceamount){
        this.balanceamount = balanceamount;
    }

    public String getBalanceamount(){  return  this.balanceamount;    }
    public void setBalanceamount(String balanceamount){
        this.balanceamount = balanceamount;
    }

}
