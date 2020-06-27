package com.example.cafepaymentreader;

public class MemberInfo {

    private String name;
    private String phone;
    private String birthday;


    public MemberInfo(String name, String phone, String birthday){
        this.name = name;
        this.phone= phone;
        this.birthday = birthday;

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


}
