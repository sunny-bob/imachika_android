package com.itmg.imachika.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class ReReview implements Serializable {
    private String _id;
    private String reviewed_id;
    private String user_id;
    private String user_name;
    private String content;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getReviewed_id() {
        return reviewed_id;
    }

    public void setReviewed_id(String reviewed_id) {
        this.reviewed_id = reviewed_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
