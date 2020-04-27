package com.example.bookshare;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;


public class NameChangeDialog extends DialogFragment implements View.OnClickListener {

    AccountActivity activity;
    TextView titleView;
    TextInputEditText nameEditText;
    MaterialButton confirmButton;
    int code;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = (View) inflater.inflate(R.layout.name_change_dialog, null);
        activity = (AccountActivity) getActivity();
        titleView = (TextView) view.findViewById(R.id.name_change_title);
        nameEditText = (TextInputEditText) view.findViewById(R.id.name_change_name);
        confirmButton = (MaterialButton) view.findViewById(R.id.name_change_confirm);
        code = getArguments().getInt("code");
        switch (code){
            case 1:
                titleView.setText(getString(R.string.name_change_title1));
                nameEditText.setHint(getString(R.string.sign_in_username));
                break;
            case 2:
                titleView.setText(getString(R.string.name_change_title2));
                nameEditText.setHint(getString(R.string.sign_up_first_name));
                break;
            case 3:
                titleView.setText(getString(R.string.name_change_title3));
                nameEditText.setHint(getString(R.string.sign_up_last_name));
                break;
        }
        confirmButton.setOnClickListener(this);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (code){
            case 1:
                if(nameEditText.getText().toString().length()>30||nameEditText.getText().toString().length()<8){
                    nameEditText.setError(getString(R.string.sign_up_username_error1));
                }
                else if(nameEditText.getText().toString().matches("")){
                    nameEditText.setError(getString(R.string.sign_up_username_error2));
                }
                else {
                    activity.sendRequest("username",nameEditText.getText().toString());
                    dismiss();
                }
                break;
            case 2:
                if(nameEditText.getText().toString().length()>30){
                    nameEditText.setError(getString(R.string.sign_up_first_error));
                }
                else {
                    activity.sendRequest("first_name", nameEditText.getText().toString());
                    dismiss();
                }
                break;
            case 3:
                if(nameEditText.getText().toString().length()>40){
                    nameEditText.setError(getString(R.string.sign_up_last_error));
                }
                else {
                    activity.sendRequest("last_name", nameEditText.getText().toString());
                    dismiss();
                }
                break;
        }

    }
}
