package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GenderDialog extends DialogFragment {
    private String MALE;
    private String FEMALE;
    private String UNKNOWN;
    private RadioGroup radioGroup;
    private Button cancel, confirm;
    private OnConfirmListener confirmListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_gender,container,false);
        MALE = getString(R.string.male);
        FEMALE = getString(R.string.female);
        UNKNOWN = getString(R.string.unknown);
        radioGroup = view.findViewById(R.id.sg_radio_group);
        cancel = view.findViewById(R.id.sg_cancel);
        confirm = view.findViewById(R.id.sg_confirm);
        if (User.user.getGender().equals(MALE)){
            ((RadioButton) view.findViewById(R.id.sg_rb_male)).setChecked(true);
        }else if (User.user.getGender().equals(FEMALE)){
            ((RadioButton) view.findViewById(R.id.sg_rb_female)).setChecked(true);
        }else if (User.user.getGender().equals(UNKNOWN)){
            ((RadioButton) view.findViewById(R.id.sg_rb_unknown)).setChecked(true);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.sg_rb_male:
                        if (confirmListener != null)
                            confirmListener.onConfirm(MALE);
                        dismiss();
                        break;
                    case R.id.sg_rb_female:
                        if (confirmListener != null)
                            confirmListener.onConfirm(FEMALE);
                        dismiss();
                        break;
                    case R.id.sg_rb_unknown:
                        if (confirmListener != null)
                            confirmListener.onConfirm(UNKNOWN);
                        dismiss();
                        break;
                }
            }
        });
        return view;
    }

    public void setOnConfirmListener(OnConfirmListener listener) { confirmListener = listener; }

    public interface OnConfirmListener{
        void onConfirm(String selection);
    }
}