package com.itmg.imachika.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/11/13.
 */

public class Cat implements Serializable{
    String status;
    Type data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Type getData() {
        return data;
    }

    public void setData(Type data) {
        this.data = data;
    }

    public class Type{
       String rank_cats;
       String all_cats;
       String all;

        public String getRank_cats() {
            return rank_cats;
        }

        public void setRank_cats(String rank_cats) {
            this.rank_cats = rank_cats;
        }

        public String getAll_cats() {
            return all_cats;
        }

        public void setAll_cats(String all_cats) {
            this.all_cats = all_cats;
        }

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }
    }
}
