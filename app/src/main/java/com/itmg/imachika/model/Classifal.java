package com.itmg.imachika.model;

import com.itmg.imachika.application.APP;
import com.itmg.imachika.util.Constant;

import java.util.List;

/**
 * Created by lenovo on 2017/11/14.
 */

public class Classifal {
    private String _id;

    private String name;

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    private String name_cn;

    private String parent_id;

    private String parent_name;

    private String parent_name_cn;

    private String google_keywords;

    private String yahoo_keywords;

    private boolean on_top;

    private int level;

    private int weight;

    private boolean lowest;

    private List<Boolean> need_gps;

    private String top_id;

    private String top_name;

    private String yelp_keywords;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        switch (APP.languageType) {
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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_name() {
        return this.parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_name_cn() {
        return parent_name_cn;
    }

    public void setParent_name_cn(String parent_name_cn) {
        this.parent_name_cn = parent_name_cn;
    }

    public String getGoogle_keywords() {
        return google_keywords;
    }

    public void setGoogle_keywords(String google_keywords) {
        this.google_keywords = google_keywords;
    }

    public String getYahoo_keywords() {
        return yahoo_keywords;
    }

    public void setYahoo_keywords(String yahoo_keywords) {
        this.yahoo_keywords = yahoo_keywords;
    }

    public boolean isOn_top() {
        return on_top;
    }

    public void setOn_top(boolean on_top) {
        this.on_top = on_top;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isLowest() {
        return lowest;
    }

    public void setLowest(boolean lowest) {
        this.lowest = lowest;
    }

    public List<Boolean> getNeed_gps() {
        return need_gps;
    }

    public void setNeed_gps(List<Boolean> need_gps) {
        this.need_gps = need_gps;
    }

    public String getTop_id() {
        return top_id;
    }

    public void setTop_id(String top_id) {
        this.top_id = top_id;
    }

    public String getTop_name() {
        return top_name;
    }

    public void setTop_name(String top_name) {
        this.top_name = top_name;
    }

    public String getYelp_keywords() {
        return yelp_keywords;
    }

    public void setYelp_keywords(String yelp_keywords) {
        this.yelp_keywords = yelp_keywords;
    }

    @Override
    public String toString() {
        return "Classifal{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", parent_name='" + parent_name + '\'' +
                ", google_keywords='" + google_keywords + '\'' +
                ", yahoo_keywords='" + yahoo_keywords + '\'' +
                ", on_top=" + on_top +
                ", level=" + level +
                ", weight=" + weight +
                ", lowest=" + lowest +
                ", need_gps=" + need_gps +
                ", top_id='" + top_id + '\'' +
                ", top_name='" + top_name + '\'' +
                ", yelp_keywords='" + yelp_keywords + '\'' +
                '}';
    }
}
