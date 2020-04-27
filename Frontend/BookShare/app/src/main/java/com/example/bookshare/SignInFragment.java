package com.example.bookshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;


public class SignInFragment extends Fragment implements View.OnClickListener {

    EntranceActivity activity;
    TextInputEditText username;
    TextInputEditText password;
    MaterialButton signCard;
    TextView retrieveView;
    final String address = "https://sadbookshare.herokuapp.com/api/account/login";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        activity = (EntranceActivity) getActivity();
        username = (TextInputEditText) view.findViewById(R.id.sign_in_username);
        password = (TextInputEditText) view.findViewById(R.id.sign_in_password);
        signCard = (MaterialButton) view.findViewById(R.id.sign_in_sign_button);
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
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username",username.getText().toString());
                jsonObject.put("password",password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, address, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Toast.makeText(activity, response.getString("message"), Toast.LENGTH_LONG).show();
                        activity.entranceCompletion(
                                response.getString("username"),
                                response.getString("first_name"),
                                response.getString("last_name"),
                                response.getString("email"),
                                response.getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity, new String(error.networkResponse.data), Toast.LENGTH_LONG).show();
                }
            });

            activity.queue.add(request);
        }
    }
}
