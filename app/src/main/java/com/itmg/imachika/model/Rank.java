package com.itmg.imachika.model;

import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Constant;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/11/13.
 */

public class Rank implements Serializable{
    String _id;
    String top_id;
    String name;
    String name_cn;
    String parent_name;
    String parent_id;
    int level;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTop_id() {
        return top_id;
    }

    public void setTop_id(String top_id) {
        this.top_id = top_id;
    }

    public String getName() {
        switch (APP.languageType){
            case Constant.LANGUAGE_TYPE_JP:
                return this.name;
            case Constant.LANGUAGE_TYPE_CN:
                return this.name_cn;
            case Constant.LANGUAGE_TYPE_EN:
                if(this._id.contains("_")){
                    return this._id.replace("_"," ");
                }
                return this._id;
            default:
                return this.name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
