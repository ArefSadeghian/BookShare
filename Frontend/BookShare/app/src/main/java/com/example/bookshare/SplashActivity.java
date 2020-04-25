package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashActivity extends AppCompatActivity implements Runnable{
    boolean AnimationFinished;
    boolean DataLoaded;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AnimationFinished = false;
        DataLoaded = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("com.example.bookshare", MODE_PRIVATE);
        new Handler().postDelayed(this,3000);
        if(AnimationFinished){
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
        DataLoaded = true;
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
