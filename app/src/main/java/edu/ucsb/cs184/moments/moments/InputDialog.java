package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputDialog extends DialogFragment {
    private TextView title;
    private EditText text;
    private Button cancel, save;
    private OnClickListener cancelListener, saveListener;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_text,container,false);
        cancel = view.findViewById(R.id.et_cancel);
        save = view.findViewById(R.id.et_save);
        title = view.findViewById(R.id.et_title);
        text = view.findViewById(R.id.et_editText);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onClick(text);
                dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveListener != null)
                    saveListener.onClick(text);
                dismiss();
            }
        });
        return view;
    }

    public void setTitle(String text){
        title.setText(text);
    }
    public void setContent(String content){
        text.setText(content);
    }
    public void setOnCancelListener(OnClickListener listener) { cancelListener = listener; }
    public void setOnSaveListener(OnClickListener listener) { saveListener = listener; }

    public interface OnClickListener{
        void onClick(EditText editText);
    }
}
