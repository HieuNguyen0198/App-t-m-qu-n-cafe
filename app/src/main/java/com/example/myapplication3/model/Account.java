package com.example.myapplication3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    public String username;
    public String password;
    public String phone;


    public Account()
    {

    }

    public Account(Account account)
    {
        username = account.username;
        password = account.password;
        phone = account.phone;
    }

    public Account(String username, String password, String phone)
    {
        this.password = password;
        this.username = username;
        this.phone = phone;
    }

    public Account(String string)
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

        this.username = stringList.get(0);
        this.phone = stringList.get(1);
        this.password = stringList.get(2);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
