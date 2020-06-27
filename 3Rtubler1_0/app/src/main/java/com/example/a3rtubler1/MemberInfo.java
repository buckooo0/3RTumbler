package com.example.a3rtubler1;

public class MemberInfo {

    private String name;
    private String phone;
    private String birthday;
    private String tumPassword;
    private int paycount;

    public MemberInfo(String name, String phone, String birthday, String tumPassword, int paycount){
        this.name = name;
        this.phone= phone;
        this.birthday = birthday;
        this.tumPassword = tumPassword;
        this.paycount = paycount;
    }

    public String getName(){
        return  this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhone(){
        return  this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getBirthday(){
        return  this.birthday;
    }
    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getTumPassword() { return this.tumPassword; }
    public void setTumPassword(String tumPassword) { this.tumPassword = tumPassword; }

    public int getpaycount() { return this.paycount; }
    public void setPaycount(int paycount) { this.paycount = paycount; }
}
