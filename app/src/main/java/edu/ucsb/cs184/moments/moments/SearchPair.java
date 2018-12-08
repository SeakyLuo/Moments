package edu.ucsb.cs184.moments.moments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class SearchPair implements Parcelable {
    public static final String POSTS = "Posts";
    public static final String USERS = "Users";
    public static final String GROUPS = "Groups";
    public static final String HISTORY = "History";
    public static final String[] types = { POSTS, USERS, GROUPS, HISTORY };
    public String keyword;
    public String type;

    public SearchPair() {}

    public SearchPair(String keyword, String type){
        this.keyword = keyword;
        this.type = type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SearchPair))
            return false;
        SearchPair o = (SearchPair) obj;
        return keyword.equals(o.keyword);
    }

    protected SearchPair(Parcel in) {
        keyword = in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(keyword);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchPair> CREATOR = new Creator<SearchPair>() {
        @Override
        public SearchPair createFromParcel(Parcel in) {
            return new SearchPair(in);
        }

        @Override
        public SearchPair[] newArray(int size) {
            return new SearchPair[size];
        }
    };
}
