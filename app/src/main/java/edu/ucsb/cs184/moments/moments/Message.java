package edu.ucsb.cs184.moments.moments;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Message implements Parcelable {
    private String content;
    private Long time;
    public Message(String content, Long time){
        this.content = content;
        this.time = time;
    }
    public String getContent() { return content; }
    public Long getTime() { return time; }

    protected Message(Parcel in) {
        content = in.readString();
        if (in.readByte() == 0) {
            time = null;
        } else {
            time = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        if (time == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(time);
        }
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Message)) return false;
        Message m = (Message) obj;
        return content.equals(m.content) && time.equals(m.time);
    }

}
