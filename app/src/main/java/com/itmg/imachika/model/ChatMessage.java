package com.itmg.imachika.model;

/**
 * Created by Administrator on 2017/7/18 0018.
 * 聊天界面的消息
 */

public class ChatMessage {
    String fromuid;
    String touid;
    String fromu;
    String data;
    boolean isLeft;

    public ChatMessage() {
    }

    public ChatMessage(String fromuid, String touid, String fromu, String data, boolean isLeft) {
        this.fromuid = fromuid;
        this.touid = touid;
        this.fromu = fromu;
        this.data = data;
        this.isLeft = isLeft;
    }


    public String getFromuid() {
        return fromuid;
    }

    public void setFromuid(String fromuid) {
        this.fromuid = fromuid;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getFromu() {
        return fromu;
    }

    public void setFromu(String fromu) {
        this.fromu = fromu;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "fromuid='" + fromuid + '\'' +
                ", touid='" + touid + '\'' +
                ", fromu='" + fromu + '\'' +
                ", data='" + data + '\'' +
                ", isLeft=" + isLeft +
                '}';
    }
}
