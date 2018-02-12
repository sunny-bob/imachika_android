package com.itmg.imachika.model;

import java.util.List;

/**
 * Created by lenovo on 2017/11/20.
 */

public class UserInfo {
    private String status;
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String _id;
        private String email;
        private String pwd;
        private String user_name;
        private String address;
        private List<String> address_pub;
        private int last_date;
        private int create_time;
        private int index;
        private String image;
        private String location;
        private String gender;
        private String signature;
        private String address_location;
        private String fb_user_id;
        private String fb_access_token;
        private boolean isblack;
        private boolean is_follow;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<String> getAddress_pub() {
            return address_pub;
        }

        public void setAddress_pub(List<String> address_pub) {
            this.address_pub = address_pub;
        }

        public int getLast_date() {
            return last_date;
        }

        public void setLast_date(int last_date) {
            this.last_date = last_date;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getAddress_location() {
            return address_location;
        }

        public void setAddress_location(String address_location) {
            this.address_location = address_location;
        }

        public String getFb_user_id() {
            return fb_user_id;
        }

        public void setFb_user_id(String fb_user_id) {
            this.fb_user_id = fb_user_id;
        }

        public String getFb_access_token() {
            return fb_access_token;
        }

        public void setFb_access_token(String fb_access_token) {
            this.fb_access_token = fb_access_token;
        }

        public boolean isIsblack() {
            return isblack;
        }

        public void setIsblack(boolean isblack) {
            this.isblack = isblack;
        }

        public boolean isIs_follow() {
            return is_follow;
        }

        public void setIs_follow(boolean is_follow) {
            this.is_follow = is_follow;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "_id='" + _id + '\'' +
                    ", email='" + email + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", address='" + address + '\'' +
                    ", address_pub=" + address_pub +
                    ", last_date=" + last_date +
                    ", create_time=" + create_time +
                    ", index=" + index +
                    ", image='" + image + '\'' +
                    ", location='" + location + '\'' +
                    ", gender='" + gender + '\'' +
                    ", signature='" + signature + '\'' +
                    ", address_location='" + address_location + '\'' +
                    ", fb_user_id='" + fb_user_id + '\'' +
                    ", fb_access_token='" + fb_access_token + '\'' +
                    ", isblack=" + isblack +
                    ", is_follow=" + is_follow +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
