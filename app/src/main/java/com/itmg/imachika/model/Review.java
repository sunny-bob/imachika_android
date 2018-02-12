package com.itmg.imachika.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class Review implements Serializable {

    private String _id;
    private String item_id;
    private String user_id;
    private String user_name;
    private String content;
    private String rating;
    private int is_liked;//1，已点赞 0， 未点赞
    private String like_count;
    private String create_time;
    private List<ReReview> rereviews;
    private int reviewed_count;
    private boolean isHint;

    public List<ReReview> getRereviews() {
        return rereviews;
    }

    public void setRereviews(List<ReReview> rereviews) {
        this.rereviews = rereviews;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(int is_liked) {
        this.is_liked = is_liked;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getReviewed_count() {
        return reviewed_count;
    }

    public void setReviewed_count(int reviewed_count) {
        this.reviewed_count = reviewed_count;
    }

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean hint) {
        isHint = hint;
    }
}
