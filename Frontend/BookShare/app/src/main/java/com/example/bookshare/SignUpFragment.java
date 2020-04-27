package com.example.bookshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignUpFragment extends Fragment implements View.OnClickListener {
    EntranceActivity activity;
    TextInputEditText username;
    TextInputEditText password;
    TextInputEditText passwordRepeat;
    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    ImageView avatar;
    FloatingActionButton fab;
    String avatarAddress;
    FrameLayout frameLayout;

    final String address = "https://sadbookshare.herokuapp.com/api/account/register";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        username = (TextInputEditText) view.findViewById(R.id.sign_up_username);
        password = (TextInputEditText) view.findViewById(R.id.sign_up_password);
        passwordRepeat = (TextInputEditText) view.findViewById(R.id.sign_up_password_repeat);
        firstName = (TextInputEditText) view.findViewById(R.id.sign_up_first_name);
        lastName = (TextInputEditText) view.findViewById(R.id.sign_up_last_name);
        email = (TextInputEditText) view.findViewById(R.id.sign_up_email);
        avatar = (ImageView) view.findViewById(R.id.sign_up_avatar);
        fab = (FloatingActionButton) view.findViewById(R.id.sign_up_fab);
        frameLayout = (FrameLayout) view.findViewById(R.id.sign_up_top_frameLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        activity = (EntranceActivity) getActivity();
        fab.setOnClickListener(this);
        frameLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==fab){
            if(okay(username)&&okay(password)&&okay(passwordRepeat)&&okay(firstName)&&okay(lastName)&&okay(email)){
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username",username.getText().toString());
                    jsonObject.put("password",password.getText().toString());
                    jsonObject.put("password_confirmation",passwordRepeat.getText().toString());
                    jsonObject.put("email",email.getText().toString());
                    jsonObject.put("first_name",firstName.getText().toString());
                    jsonObject.put("last_name",lastName.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, address, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(activity, "Registration was successful", Toast.LENGTH_LONG).show();
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
        else if(v==frameLayout){

        }
    }

    private boolean okay(TextInputEditText inputEditText) {
        switch (inputEditText.getId()){
            case R.id.sign_up_username:
                if(username.getText().toString().length()>30||username.getText().toString().length()<8){
                    username.setError(getString(R.string.sign_up_username_error1));
                    return false;
                }
                else if(username.getText().toString().matches("")){
                    username.setError(getString(R.string.sign_up_username_error2));
                    return false;
                }
                else {
                    return true;
                }
            case R.id.sign_up_password:
                if(password.getText().toString().matches("")){
                    password.setError(getString(R.string.sign_up_password_error1));
                    return false;
                }
                else {
                    return true;
                }
            case R.id.sign_up_password_repeat:
                if(!passwordRepeat.getText().toString().matches(password.getText().toString())){
                    passwordRepeat.setError(getString(R.string.sign_up_password_error2));
                    return false;
                }
                else {
                    return true;
                }
            case R.id.sign_up_first_name:
                if(firstName.getText().toString().length()>30){
                    firstName.setError(getString(R.string.sign_up_first_error));
                    return false;
                }
                else {
                    return true;
                }
            case R.id.sign_up_last_name:
                if(lastName.getText().toString().length()>40){
                    lastName.setError(getString(R.string.sign_up_last_error));
                    return false;
                }
                else {
                    return true;
                }
            case R.id.sign_up_email:
                if(email.getText().toString().length()>254){
                    email.setError(getString(R.string.sign_up_email_error1));
                    return false;
                }
                else if(email.getText().toString().matches("")){
                    email.setError(getString(R.string.sign_up_email_error2));
                    return false;
                }
                else {
                    return true;
                }
            default:
                return true;
        }
    }

}
