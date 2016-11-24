package com.example.jiji.dataacquisition.Model;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Created by lam on 11/23/2016.
 */

public abstract class DataPoint {
    private Vector data;
    private Timestamp timestamp = new Timestamp(Long.parseLong(DateFormat.getDateTimeInstance().format(new Date())));

    public DataPoint(Vector data){
        this.data = null;
    }

    public String toJSONString(){
        return null;
    }
}