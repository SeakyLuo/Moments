package edu.ucsb.cs184.moments.moments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Message implements Parcelable {
    private Long time;
    private String content;
    public Message(Long time, String content){
        this.time = time;
        this.content = content;
    }

    protected Message(Parcel in) {
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readLong();
        }
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(time);
        }
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public String toString(){
        return (new Gson()).toJson(this);
    }
    public static Message fromJson(String json){
        return (new Gson()).fromJson(json, Message.class);
    }

}
