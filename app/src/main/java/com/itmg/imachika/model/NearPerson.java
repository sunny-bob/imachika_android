package com.itmg.imachika.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/11/17.
 */

public class NearPerson implements Serializable{
    private String status;
    private String msg;
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NearPerson{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable{
        private String _id;
        private String gg_user_id;
        private String gg_access_token;
        private int last_date;
        private String email;
        private String user_name;
        private String image;
        private String gender;
        private String signature;
        private String tel;
        private int create_time;
        private int index;
        private Location location;
        private int dv;
        private String distance;
        private List<String> address_pub;

        public List<String> getAddress_pub() {
            return address_pub;
        }

        public void setAddress_pub(List<String> address_pub) {
            this.address_pub = address_pub;
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

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getGg_user_id() {
            return gg_user_id;
        }

        public void setGg_user_id(String gg_user_id) {
            this.gg_user_id = gg_user_id;
        }

        public String getGg_access_token() {
            return gg_access_token;
        }

        public void setGg_access_token(String gg_access_token) {
            this.gg_access_token = gg_access_token;
        }

        public int getLast_date() {
            return last_date;
        }

        public void setLast_date(int last_date) {
            this.last_date = last_date;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public int getDv() {
            return dv;
        }

        public void setDv(int dv) {
            this.dv = dv;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "_id='" + _id + '\'' +
                    ", gg_user_id='" + gg_user_id + '\'' +
                    ", gg_access_token='" + gg_access_token + '\'' +
                    ", last_date=" + last_date +
                    ", email='" + email + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", image='" + image + '\'' +
                    ", gender='" + gender + '\'' +
                    ", signature='" + signature + '\'' +
                    ", tel='" + tel + '\'' +
                    ", create_time=" + create_time +
                    ", index=" + index +
                    ", location=" + location +
                    ", dv=" + dv +
                    ", distance='" + distance + '\'' +
                    ", address_pub=" + address_pub +
                    '}';
        }

        public class Location implements Serializable{
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            @Override
            public String toString() {
                return "Location{" +
                        "lat=" + lat +
                        ", lng=" + lng +
                        '}';
            }
        }
    }
}
