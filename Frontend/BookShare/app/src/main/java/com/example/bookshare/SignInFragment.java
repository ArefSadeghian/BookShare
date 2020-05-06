package com.example.bookshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class SignInFragment extends Fragment implements View.OnClickListener {

    EntranceActivity activity;
    TextInputEditText username;
    TextInputEditText password;
    MaterialButton signCard;
    TextView retrieveView;
    final String address = "https://sadbookshare.herokuapp.com/api/account/login";
    String address1;

    String myUsername;
    String myPassword;
    String myFirstName;
    String myLastName;
    String myEmail;
    Bitmap myAvatar;

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
                        loadImage();
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

    private void loadImage(){
        address1 = "https://sadbookshare.herokuapp.com/api/account/"+activity.sharedPreferences.getString("username","username")+"/profile_image";
        ImageRequest request1 = new ImageRequest(address1, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                MainActivity.MyAccount = Account.getInstance(response);
                Toast.makeText(activity, "Image fetched successfully", Toast.LENGTH_SHORT).show();
                activity.logIn();
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Image not fetched", Toast.LENGTH_SHORT).show();
                activity.logIn();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token "+activity.sharedPreferences.getString("token",""));
                return headers;
            }
        };
        activity.queue.add(request1);
    }
}
