package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends AppCompatActivity implements Runnable{
    boolean AnimationFinished;
    boolean DataLoaded;
    SharedPreferences sharedPreferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AnimationFinished = false;
        DataLoaded = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("com.example.bookshare", MODE_PRIVATE);
        new Handler().postDelayed(this,3000);

        if(sharedPreferences.getBoolean("app_first",true)){
            if(AnimationFinished){
                Intent intent = new Intent(SplashActivity.this,EntranceActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
            DataLoaded = true;
        }
        else {
            queue = Volley.newRequestQueue(this);
            String address = "https://sadbookshare.herokuapp.com/api/account/";
            address = address.concat(sharedPreferences.getString("username","username")+"/properties");
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, address, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        MainActivity.MyAccount = new Account(
                                response.getString("username"),
                                response.getString("first_name"),
                                response.getString("last_name"),
                                response.getString("email"),
                                sharedPreferences.getString("token",""));
                        sharedPreferences.edit()
                                .putString("username",MainActivity.MyAccount.Username)
                                .putString("first_name",MainActivity.MyAccount.FirstName)
                                .putString("last_name",MainActivity.MyAccount.LastName)
                                .putString("email",MainActivity.MyAccount.Email).apply();
                        Toast.makeText(SplashActivity.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
                        if(AnimationFinished){
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            SplashActivity.this.startActivity(intent);
                            SplashActivity.this.finish();
                        }
                        DataLoaded = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SplashActivity.this, new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
                    if(AnimationFinished){
                        initializeInformation();
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    DataLoaded = true;
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token "+sharedPreferences.getString("token",""));
                    return headers;
                }
            };
            queue.add(request);
        }
    }

    private void initializeInformation() {
        if(MainActivity.MyAccount==null){
            MainActivity.MyAccount = new Account(
                    sharedPreferences.getString("username","username"),
                    sharedPreferences.getString("firstName","firstName"),
                    sharedPreferences.getString("lastName","lastName"),
                    sharedPreferences.getString("email","email"),
                    sharedPreferences.getString("token",""));
        }
    }

    @Override
    public void run() {
        if(DataLoaded){
            if(sharedPreferences.getBoolean("app_first",true)){
                Intent intent = new Intent(SplashActivity.this,EntranceActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
            else {
                initializeInformation();
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(intent);
                SplashActivity.this.finish();
            }
        }
        AnimationFinished = true;
    }
}
