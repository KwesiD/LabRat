package com.example.kwesi.labrat;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "mouse")
public class Mouse {
    @PrimaryKey
    @NonNull
    protected String mid; //mouse id
    @ColumnInfo(name = "parent_description")
    protected String parentDesc;
    @ColumnInfo(name = "paired_date")
    protected Date pairedDate;
    @ColumnInfo(name = "color")
    protected String color;
    @ColumnInfo(name = "date_of_birth")
    protected Date dob;

    Mouse(String parentDesc, Date pairedDate,Date dob,String color){
        this.parentDesc = parentDesc;
        this.pairedDate = pairedDate;
        this.dob = dob;
        this.color = color;
        this.mid = parentDesc+color+dob.toString();
    }


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getParentDesc() {
        return parentDesc;
    }

    public void setParentDesc(String parentDesc) {
        this.parentDesc = parentDesc;
    }

    public Date getPairedDate() {
        return pairedDate;
    }

    public void setPairedDate(Date pairedDate) {
        this.pairedDate = pairedDate;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
