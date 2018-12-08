package edu.ucsb.cs184.moments.moments;

public class IconHelper {

    public static int followImage(String id){
        return User.user.mutualFollow(id) ? R.drawable.ic_mutual :
                User.user.isFollowing(id) ? R.drawable.ic_unfollow : R.drawable.ic_follow;
    }

    public static void Camera(){

    }

    public static void Gallery(){

    }
}
