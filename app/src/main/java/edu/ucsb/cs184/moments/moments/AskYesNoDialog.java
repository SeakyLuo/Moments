package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AskYesNoDialog extends DialogFragment {

    private TextView message;
    private Button yes;
    private Button no;
    private View.OnClickListener yesListener, noListener;

    public void setOnYesListener(View.OnClickListener listener) { yesListener = listener; }
    public void setOnNoListener(View.OnClickListener listener) { noListener = listener; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ask_yes_no,container,false);
        message = view.findViewById(R.id.ayn_message);
        yes = view.findViewById(R.id.ayn_yes);
        no = view.findViewById(R.id.ayn_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesListener != null) yesListener.onClick(v);
                // Dismiss Manually?
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noListener != null) noListener.onClick(v);
                dismiss();
            }
        });
        return view;
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }
    public void setYesText(String text) { yes.setText(text); }
    public void setNoText(String text) { no.setText(text); }
}
