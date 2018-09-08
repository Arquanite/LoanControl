package com.adrian.littlecancer;

import java.security.PublicKey;

/**
 * Created by adrian on 22.09.17.
 */

public class Friend {
    private String name;
    private float balance;

    public Friend(){

    }

    public Friend(String name, float balance){
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
