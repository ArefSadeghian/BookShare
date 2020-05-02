package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends AppCompatActivity implements Runnable{
    boolean AnimationFinished;
    boolean DataLoaded;
    boolean ImageLoaded;

    SharedPreferences sharedPreferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AnimationFinished = false;
        DataLoaded = false;
        ImageLoaded = false;
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
            ImageLoaded = true;
        }
        else {
            queue = Volley.newRequestQueue(this);
            String address = "https://sadbookshare.herokuapp.com/api/account/"+sharedPreferences.getString("username","username")+"/properties";
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, address, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        MainActivity.MyAccount = Account.getInstance(
                                response.getString("username"),
                                response.getString("first_name"),
                                response.getString("last_name"),
                                response.getString("email"),
                                sharedPreferences.getString("token",""));
                        sharedPreferences.edit()
                                .putString("username",MainActivity.MyAccount.Username)
                                .putString("first_name",MainActivity.MyAccount.FirstName)
                                .putString("last_name",MainActivity.MyAccount.LastName)
                                .putString("email",MainActivity.MyAccount.Email).commit();
                        Toast.makeText(SplashActivity.this, "Data fetched successfully", Toast.LENGTH_SHORT).show();
                        if(AnimationFinished && ImageLoaded){
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
                    if(AnimationFinished && ImageLoaded){
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
            String address1 = "https://sadbookshare.herokuapp.com/api/account/"+sharedPreferences.getString("username","username")+"/profile_image";
            ImageRequest request1 = new ImageRequest(address1, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    MainActivity.MyAccount = Account.getInstance(response);
                    Toast.makeText(SplashActivity.this, "Image fetched successfully", Toast.LENGTH_SHORT).show();
                    if(AnimationFinished && DataLoaded){
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    ImageLoaded = true;
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_4444, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(AnimationFinished && DataLoaded){
                        initializeInformation();
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                    }
                    ImageLoaded = true;
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Token "+sharedPreferences.getString("token",""));
                    return headers;
                }
            };
            queue.add(request1);
        }
    }

    private void initializeInformation() {
        if(MainActivity.MyAccount==null){
            MainActivity.MyAccount = Account.getInstance(
                    sharedPreferences.getString("username","username"),
                    sharedPreferences.getString("firstName","firstName"),
                    sharedPreferences.getString("lastName","lastName"),
                    sharedPreferences.getString("email","email"),
                    sharedPreferences.getString("token",""));
        }
    }

    @Override
    public void run() {
        if(DataLoaded&&ImageLoaded){
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
