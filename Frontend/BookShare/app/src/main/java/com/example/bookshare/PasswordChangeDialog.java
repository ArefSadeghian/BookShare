package com.example.bookshare;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordChangeDialog extends DialogFragment implements View.OnClickListener {
    AccountActivity activity;
    TextInputEditText old;
    TextInputEditText recent;
    TextInputEditText recentRepeat;
    MaterialButton button;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = (View) inflater.inflate(R.layout.password_change_dialog, null);
        activity = (AccountActivity) getActivity();
        old = (TextInputEditText) view.findViewById(R.id.password_change_old);
        recent = (TextInputEditText) view.findViewById(R.id.password_change_new);
        recentRepeat = (TextInputEditText) view.findViewById(R.id.password_change_new_repeat);
        button = (MaterialButton) view.findViewById(R.id.password_change_email_button);
        button.setOnClickListener(this);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if(old.getText().toString().matches("")){
            old.setError(getString(R.string.password_change_error));
        }
        else if(recent.getText().toString().matches("")){
            recent.setError(getString(R.string.sign_up_password_error1));
        }
        else if(!recentRepeat.getText().toString().matches(recent.getText().toString())){
            recentRepeat.setError(getString(R.string.sign_up_password_error2));
        }
        else {
            activity.sendRequest(old.getText().toString(), recent.getText().toString(), recentRepeat.getText().toString());
            dismiss();
        }
    }
}
