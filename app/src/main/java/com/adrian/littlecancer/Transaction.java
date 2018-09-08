package com.adrian.littlecancer;

/**
 * Created by adrian on 25.09.17.
 */

public class Transaction {
    private float value;
    private String note;

    public Transaction(){

    }

    public Transaction(float value, String note){
        this.value = value;
        this.note = note;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
