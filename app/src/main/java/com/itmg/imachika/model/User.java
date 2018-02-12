package com.itmg.imachika.model;

import java.util.List;

/**
 * Created by lenovo on 2017/11/15.
 */

public class User {
    private String status;
    private String msg;
    private String user_id;
    private UserInfo user;
    private UserInfo data;
    private int type;// 登录 ： 1 登录密码不一致   2 Email 未注册  3 email格式错误 ；
                     // 注册 ： 4 email用户已经存在  5 email格式错误
    private boolean is_signup;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIs_signup() {
        return is_signup;
    }

    public void setIs_signup(boolean is_signup) {
        this.is_signup = is_signup;
    }

    public class UserInfo {
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
        private String tel;
        private String gender;
        private String signature;

        public void set_id(String _id){
            this._id = _id;
        }
        public String get_id(){
            return this._id;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public String getEmail(){
            return this.email;
        }
        public void setPwd(String pwd){
            this.pwd = pwd;
        }
        public String getPwd(){
            return this.pwd;
        }
        public void setUser_name(String user_name){
            this.user_name = user_name;
        }
        public String getUser_name(){
            return this.user_name;
        }
        public void setAddress(String address){
            this.address = address;
        }
        public String getAddress(){
            return this.address;
        }
        public void setAddress_pub(List<String> address_pub){
            this.address_pub = address_pub;
        }
        public List<String> getAddress_pub(){
            return this.address_pub;
        }
        public void setLast_date(int last_date){
            this.last_date = last_date;
        }
        public int getLast_date(){
            return this.last_date;
        }
        public void setCreate_time(int create_time){
            this.create_time = create_time;
        }
        public int getCreate_time(){
            return this.create_time;
        }
        public void setIndex(int index){
            this.index = index;
        }
        public int getIndex(){
            return this.index;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
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


        @Override
        public String toString() {
            return "UserInfo{" +
                    "_id='" + _id + '\'' +
                    ", email='" + email + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", address='" + address + '\'' +
                    ", address_pub='" + address_pub + '\'' +
                    ", last_date=" + last_date +
                    ", create_time=" + create_time +
                    ", index=" + index +
                    ", image='" + image + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user=" + user +
                ", is_signup=" + is_signup +
                '}';
    }
}
