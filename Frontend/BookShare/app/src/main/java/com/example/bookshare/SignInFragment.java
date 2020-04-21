package com.example.bookshare;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


public class SignInFragment extends Fragment implements View.OnClickListener {

    EntranceActivity activity;
    EditText username;
    EditText password;
    CardView signCard;
    TextView retrieveView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        activity = (EntranceActivity) getActivity();
        username = (EditText) view.findViewById(R.id.sign_in_username);
        password = (EditText) view.findViewById(R.id.sign_in_password);
        signCard = (CardView) view.findViewById(R.id.sign_in_sign_button);
        retrieveView = (TextView) view.findViewById(R.id.sign_in_retrieve);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        retrieveView.setOnClickListener(this);
        signCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==retrieveView){
            Intent intent = new Intent(activity,RetrievalActivity.class);
            activity.startActivity(intent);
        }
        else if(v==signCard){
            Intent intent = new Intent(activity,MainActivity.class);
            activity.startActivity(intent);
        }
    }
}
