package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AtMeFragment extends Fragment {

    private Context context;
    private RecyclerViewFragment fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atme, container, false);
        context = getContext();
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostAdapter());
        refresh();
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fragment.show(getFragmentManager(), R.id.atme_content);
        return view;
    }

    private void refresh(){
        try {
            fragment.setData(User.user.getAtMe());
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
    }
}
