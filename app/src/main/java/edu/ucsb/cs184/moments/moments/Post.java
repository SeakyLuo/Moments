package edu.ucsb.cs184.moments.moments;
import android.support.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;

public class Post {
    private int userid;
    private String content;
    private Time time;

    private ArrayList<Rating> ratings = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    public Post(int userid, String content, Time time){
        this.userid = userid;
        this.content = content;
        this.time = time;
    }

    public int getUserid() { return userid; }
    public String getContent() { return content; }
    public Time getTime() { return time; }
    public int comments_received() { return comments.size(); }
    public ArrayList<Comment> getComments() { return  comments; }
    public int ratings_received() { return ratings.size(); }
    public float ratings_avg(){
        float sum = 0;
        int count = ratings.size();
        if (count == 0) return 0f;
        for (int i = 0; i < count; i++)
            sum += ratings.get(i).getRating();
        return sum / count;
    }
    public Boolean isAnonymous() { return userid == User.anonymous; }
    public Boolean isCollected(int userid) {
        User user = User.findUser(userid);
        ArrayList<Post> posts = user.getCollections();
        for (int i = 0; i < posts.size(); i++) if (this.equals(posts.get(i))) return true;
        return false;
    }
    public void addComment(Comment comment) { comments.add(comment); }
    public void addRating(Rating rating) { ratings.add(rating); }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Post))
            return false;
        Post p = (Post) obj;
        return time == p.time && userid == p.userid;
    }
}
