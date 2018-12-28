package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchHistoryAdapter extends CustomAdapter<SearchPair> {
    private SearchActivity searchActivity;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_history, parent, false));
    }

    public void setSearchActivity(SearchActivity activity){
        searchActivity = activity;
    }

    public class ViewHolder extends CustomViewHolder {
        TextView keyword;
        ImageButton delete;

        ViewHolder(View view) {
            super(view);
            keyword = view.findViewById(R.id.sh_text);
            delete = view.findViewById(R.id.sh_delete);
        }

        @Override
        void setData() {
            keyword.setText(data.keyword);
            keyword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchActivity.searchHistory(data);
                }
            });
            keyword.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AskYesNoDialog dialog = new AskYesNoDialog();
                    dialog.showNow(((AppCompatActivity) context).getSupportFragmentManager(), "Clear All");
                    dialog.setMessage("Do you want to clear all the history?");
                    dialog.setOnYesListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clear();
                            User.user.clearHistory();
                        }
                    });
                    return true;
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.user.removeHistory(data.keyword);
                    remove(data);
                }
            });
        }
    }
}
