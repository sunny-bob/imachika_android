package com.itmg.imachika.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/11/14.
 */

public class Shop implements Serializable{
    private String status;
    private String msg;
    private Data data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return this.status;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }

    public class Data implements Serializable{
        private String location;
        private int span_time;
        private String sort;
        private Resultinfo resultinfo;
        private List<Info> data;
        private int is_fulfil;
        public void setLocation(String location){
            this.location = location;
        }
        public String getLocation(){
            return this.location;
        }
        public void setSpan_time(int span_time){
            this.span_time = span_time;
        }
        public int getSpan_time(){
            return this.span_time;
        }
        public void setSort(String sort){
            this.sort = sort;
        }
        public String getSort(){
            return this.sort;
        }
        public void setResultinfo(Resultinfo resultinfo){
            this.resultinfo = resultinfo;
        }
        public Resultinfo getResultinfo(){
            return this.resultinfo;
        }
        public void setData(List<Info> data){
            this.data = data;
        }
        public List<Info> getData(){
            return this.data;
        }
        public int getIs_fulfil() {
            return is_fulfil;
        }

        public void setIs_fulfil(int is_fulfil) {
            this.is_fulfil = is_fulfil;
        }

        public class Info implements Serializable{
            private String _id;
            private int from;
            private String address;
            private String phone;
            private double rating;
            private String location;
            private String g_id;
            private String y_id;
            private String content;
            private List<String> g_cat;
            private String big_img;
            private List<String> opening_weekday;
            private String zip_code;
            private String area;
            private String origin_end_time_format;
            private String origin_start_time_format;
            private String web_url;//活动链接
            private String small_img;
            private int create_time;
            private int review_count;
            private int rating_count;
            private int area_id;
            private int index;
            private int gtexrating_count;
            private List<String> category;
            private double dv;
            private String distance;
            private double rating_tmp;
            private String desc;//描述
            private ArrayList<String> tag;//标签

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public List<String> getReviews() {
                return reviews;
            }

            public void setReviews(List<String> reviews) {
                this.reviews = reviews;
            }

            private List<String> imgs;
            private List<String> reviews;

            public String getOrigin_end_time_format() {
                return origin_end_time_format;
            }

            public void setOrigin_end_time_format(String origin_end_time_format) {
                this.origin_end_time_format = origin_end_time_format;
            }

            public String getOrigin_start_time_format() {
                return origin_start_time_format;
            }

            public void setOrigin_start_time_format(String origin_start_time_format) {
                this.origin_start_time_format = origin_start_time_format;
            }

            public void set_id(String _id){
                this._id = _id;
            }
            public String get_id(){
                return this._id;
            }
            public void setFrom(int from){
                this.from = from;
            }
            public int getFrom(){
                return this.from;
            }
            public void setAddress(String address){
                this.address = address;
            }
            public String getAddress(){
                return this.address;
            }
            public void setPhone(String phone){
                this.phone = phone;
            }
            public String getPhone(){
                return this.phone;
            }
            public void setRating(double rating){
                this.rating = rating;
            }
            public double getRating(){
                return this.rating;
            }
            public void setLocation(String location){
                this.location = location;
            }
            public String getLocation(){
                return this.location;
            }
            public void setG_id(String g_id){
                this.g_id = g_id;
            }
            public String getG_id(){
                return this.g_id;
            }
            public String getY_id() {
                return y_id;
            }

            public void setY_id(String y_id) {
                this.y_id = y_id;
            }

            public void setContent(String content){
                this.content = content;
            }
            public String getContent(){
                return this.content;
            }
            public void setG_cat(List<String> g_cat){
                this.g_cat = g_cat;
            }
            public List<String> getG_cat(){
                return this.g_cat;
            }
            public void setBig_img(String big_img){
                this.big_img = big_img;
            }
            public String getBig_img(){
                return this.big_img;
            }
            public void setOpening_weekday(List<String> opening_weekday){
                this.opening_weekday = opening_weekday;
            }
            public List<String> getOpening_weekday(){
                return this.opening_weekday;
            }
            public void setZip_code(String zip_code){
                this.zip_code = zip_code;
            }
            public String getZip_code(){
                return this.zip_code;
            }
            public void setArea(String area){
                this.area = area;
            }
            public String getArea(){
                return this.area;
            }
            public void setWeb_url(String web_url){
                this.web_url = web_url;
            }
            public String getWeb_url(){
                return this.web_url;
            }
            public void setSmall_img(String small_img){
                this.small_img = small_img;
            }
            public String getSmall_img(){
                return this.small_img;
            }
            public void setCreate_time(int create_time){
                this.create_time = create_time;
            }
            public int getCreate_time(){
                return this.create_time;
            }
            public void setReview_count(int review_count){
                this.review_count = review_count;
            }
            public int getReview_count(){
                return this.review_count;
            }
            public void setRating_count(int rating_count){
                this.rating_count = rating_count;
            }
            public int getRating_count(){
                return this.rating_count;
            }
            public void setArea_id(int area_id){
                this.area_id = area_id;
            }
            public int getArea_id(){
                return this.area_id;
            }
            public void setIndex(int index){
                this.index = index;
            }
            public int getIndex(){
                return this.index;
            }
            public void setGtexrating_count(int gtexrating_count){
                this.gtexrating_count = gtexrating_count;
            }
            public int getGtexrating_count(){
                return this.gtexrating_count;
            }
            public void setCategory(List<String> category){
                this.category = category;
            }
            public List<String> getCategory(){
                return this.category;
            }
            public void setDv(double dv){
                this.dv = dv;
            }
            public double getDv(){
                return this.dv;
            }
            public void setDistance(String distance){
                this.distance = distance;
            }
            public String getDistance(){
                return this.distance;
            }
            public void setRating_tmp(double rating_tmp){
                this.rating_tmp = rating_tmp;
            }
            public double getRating_tmp(){
                return this.rating_tmp;
            }
            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public ArrayList<String> getTag() {
                return tag;
            }

            public void setTag(ArrayList<String> tag) {
                this.tag = tag;
            }

        }

        public class Resultinfo implements Serializable {
            private int es;
            private int g;
            private int y;
            private int yp;
            private String g_next_page_token;

            public void setEs(int es) {
                this.es = es;
            }

            public int getEs() {
                return this.es;
            }

            public void setG(int g) {
                this.g = g;
            }

            public int getG() {
                return this.g;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getY() {
                return this.y;
            }

            public void setYp(int yp) {
                this.yp = yp;
            }

            public int getYp() {
                return this.yp;
            }

            public void setG_next_page_token(String g_next_page_token) {
                this.g_next_page_token = g_next_page_token;
            }

            public String getG_next_page_token() {
                return this.g_next_page_token;
            }
        }

    }
}
