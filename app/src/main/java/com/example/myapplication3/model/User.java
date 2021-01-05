package com.example.myapplication3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String name;
    public String hinhdanh;
    public String kinhdo;
    public String vido;
    public String thongtin;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String hinhdanh, String kinhdo, String vido, String thongtin)
    {
        this.name = name;
        this.hinhdanh = hinhdanh;
        this.kinhdo = kinhdo;
        this.vido = vido;
        this.thongtin = thongtin;
    }

    public User(User u){
        this.hinhdanh = u.hinhdanh;
        this.kinhdo = u.kinhdo;
        this.vido = u.vido;
        this.name = u.name;
        this.thongtin = u.name;
    }

    public User(String string)
    {
        List<String> stringList = new ArrayList<String>();
        for(int i = 0; i< string.length();i++){
            int a = 0, b = 0;
            if (string.charAt(i) == '='){
                a = i + 1;
                for (int j = i;j < string.length(); j++){
                    if(string.charAt(j) == ',' || string.charAt(j) == '}'){
                        b = j;
                        i = b;
                        stringList.add(string.substring(a,b));
                        break;
                    }
                }
            }
        }

        //tim thong tin co dau ,
        int dem = 0;
        int vitri = 0;
        while (dem < 4){
            if(string.charAt(vitri) == ','){
                dem++;
            }
            vitri++;
        }
        stringList.add(string.substring(vitri + 10,string.length() - 1));


        this.name = stringList.get(0);
        this.kinhdo = stringList.get(1);
        this.vido = stringList.get(2);
        this.hinhdanh = stringList.get(3);
        this.thongtin = stringList.get(5);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHinhdanh() {
        return hinhdanh;
    }

    public void setHinhdanh(String hinhdanh) {
        this.hinhdanh = hinhdanh;
    }

    public String getKinhdo() {
        return kinhdo;
    }

    public  double getKinhdoD(){
        return Double.parseDouble(kinhdo);
    }

    public  double getVidoD(){
        return Double.parseDouble(vido);
    }

    public void setKinhdo(String kinhdo) {
        this.kinhdo = kinhdo;
    }

    public String getVido() {
        return vido;
    }

    public void setVido(String vido) {
        this.vido = vido;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }
}