package edu.ucsb.cs184.moments.moments;

import java.sql.Time;

public class Comment {
    private int userid;
    private String content;
    private Time time;
    private Comment parent;
    public Comment(int userid, String content, Time time){
        this.userid = userid;
        this.content = content;
        this.time = time;
    }
    public Comment(int userid, String content, Time time, Comment parent){
        this.userid = userid;
        this.content = content;
        this.time = time;
        this.parent = parent;
    }

    public int getUserid() { return userid; }
    public String getContent() { return content; }
    public Time getTime() { return time; }
    public Comment getParent() { return parent; }
}
