package edu.ucsb.cs184.moments.moments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FullPostRatingsFragment extends Fragment {

    private RatingBar ratingBar;
    private TextView avg_rating;
    private TextView reviews;
    private ArrayList<ProgressBar> progressBars = new ArrayList<>();
    private Post post;
    private boolean shown = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_post_ratings, container, false);
        ratingBar = view.findViewById(R.id.fpr_avg_rating);
        avg_rating = view.findViewById(R.id.fpr_rating_text);
        reviews = view.findViewById(R.id.fpr_reviews);
        progressBars.add((ProgressBar) view.findViewById(R.id.fp1starprogressBar));
        progressBars.add((ProgressBar) view.findViewById(R.id.fp2starprogressBar));
        progressBars.add((ProgressBar) view.findViewById(R.id.fp3starprogressBar));
        progressBars.add((ProgressBar) view.findViewById(R.id.fp4starprogressBar));
        progressBars.add((ProgressBar) view.findViewById(R.id.fp5starprogressBar));
        shown = true;
        if (post != null) setRating(post);
        return view;
    }

    public void setRating(Post post){
        this.post = post;
        if (!shown) return;
        float rating = post.ratings_avg();
        int review = post.ratings_received();
        ratingBar.setRating(rating);
        avg_rating.setText("Rating: " + rating + "/5.0");
        reviews.setText("Reviews: " + review);
        for (int i = 0; i < progressBars.size(); i++){
            ProgressBar progressBar = progressBars.get(i);
            progressBar.setProgress((review == 0) ? 50 : post.counting_star(i) / review * 100);
        }
    }

}
