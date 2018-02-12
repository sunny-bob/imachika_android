package com.itmg.imachika.model;

import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Constant;
import com.itmg.imachika.util.PreferencesUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/11/13.
 */

public class All implements Serializable{
    private String _id;
    private String top_id;
    private List<Cat2s> cat2s;
    private String name;
    private String name_cn;

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

    public List<Cat2s> getCat2s() {
        return cat2s;
    }

    public void setCat2s(List<Cat2s> cat2s) {
        this.cat2s = cat2s;
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

    public class Cat2s implements Serializable{
        private String _id;

        private String top_id;

        private String name;

        private String name_cn;

        public void set_id(String _id){
            this._id = _id;
        }
        public String get_id(){
            return this._id;
        }
        public void setTop_id(String top_id){
            this.top_id = top_id;
        }
        public String getTop_id(){
            return this.top_id;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
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

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }
    }
}
