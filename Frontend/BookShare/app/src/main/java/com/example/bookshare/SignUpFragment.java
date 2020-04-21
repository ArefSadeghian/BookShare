package com.example.bookshare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class SignUpFragment extends Fragment implements View.OnClickListener {
    EntranceActivity activity;
    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;
    ImageView avatarEdit;
    ImageView avatar;
    FloatingActionButton fab;
    String avatarAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        username = (EditText) view.findViewById(R.id.sign_up_username);
        password = (EditText) view.findViewById(R.id.sign_up_password);
        firstName = (EditText) view.findViewById(R.id.sign_up_first_name);
        lastName = (EditText) view.findViewById(R.id.sign_up_last_name);
        email = (EditText) view.findViewById(R.id.sign_up_email);
        avatarEdit = (ImageView) view.findViewById(R.id.sign_up_avatar_edit);
        avatar = (ImageView) view.findViewById(R.id.sign_up_avatar);
        fab = (FloatingActionButton) view.findViewById(R.id.sign_up_fab);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        activity = (EntranceActivity) getActivity();
        fab.setOnClickListener(this);
        avatarEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==fab){
            if(okay(username)&&okay(password)&&okay(firstName)&&okay(lastName)&&okay(email)){
                activity.signUpVerification(username.getText().toString(),password.getText().toString(),firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),avatarAddress);
            }
        }
        else if(v==avatarEdit){
            activity.chooseImage();
        }
    }

    private boolean okay(EditText username) {
        if(username.getText().toString().matches("")){
            toastMessage(activity,getString(R.string.sign_up_toast_error));
            return false;
        }
        return true;
    }

    public static void toastMessage(Context context, String message){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/dosis.ttf");
        float TextSize = 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        TextSize = context.getResources().getDimension(R.dimen.toast_text_size);
        TextSize /= displayMetrics.density;
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        TextView textView = (TextView) view.findViewById(android.R.id.message);
        textView.setTypeface(typeface);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,TextSize);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        toast.show();
    }

}
