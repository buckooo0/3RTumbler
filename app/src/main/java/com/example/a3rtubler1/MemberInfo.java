package com.example.a3rtubler1;

public class MemberInfo {

    private String name;
    private String phone;
    private String birthday;
    private String balance;

    public MemberInfo(String name, String phone, String birthday, String balance){
        this.name = name;
        this.phone= phone;
        this.birthday = birthday;
        this.balance = balance;
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

    public String getBalance(){
        return  this.balance;
    }
    public void setBalance(String balance){
        this.balance = balance;
    }
}
