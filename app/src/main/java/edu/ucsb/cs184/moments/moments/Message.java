package edu.ucsb.cs184.moments.moments;

import java.util.Date;

public class Message {
    private int senderid;
    private int receiverid;
    private Date date;
    private String content;
    public Message(int sender, int receiver, Date date, String content){
        senderid = sender;
        receiverid = receiver;
        this.date = date;
        this.content = content;
    }

}
