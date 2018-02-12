package com.itmg.imachika.model;

import java.io.Serializable;
import java.util.List;

public class Collection implements Serializable{
    private String status;
    private List<Shop.Data.Info> data;

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setData(List<Shop.Data.Info> data){
        this.data = data;
    }
    public List<Shop.Data.Info> getData(){
        return this.data;
    }

}
