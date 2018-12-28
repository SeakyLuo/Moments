package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AtMeFragment extends NotificationFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atme, container, false);
        init();
        fragment.setAdapter(new PostAdapter());
        refresh();
        fragment.show(getFragmentManager(), R.id.atme_content);
        return view;
    }

    @Override
    public void refresh(){
        fragment.setData(User.user.getAtMe());
    }
}
