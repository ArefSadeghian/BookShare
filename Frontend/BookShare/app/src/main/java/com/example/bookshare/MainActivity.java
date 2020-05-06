package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Account MyAccount;

    DrawerLayout drawerLayout;
    ImageView MenuIcon;
    ImageView NavigationAccountSetting;
    TextView Username;
    ImageView Avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawerLayout);
        NavigationAccountSetting = (ImageView) findViewById(R.id.main_nav_haeder_setting);
        MenuIcon = (ImageView) findViewById(R.id.main_menu);
        MenuIcon.setOnClickListener(this);
        NavigationAccountSetting.setOnClickListener(this);
        Username = (TextView) findViewById(R.id.main_nav_header_username);
        Avatar = (ImageView) findViewById(R.id.main_nav_haeder_avatar);

    }

    @Override
    public void onResume(){
        super.onResume();
        if(MyAccount!=null){
            Username.setText(MyAccount.Username);
        }
        if(MyAccount.Avatar!=null){
            Avatar.setImageBitmap(MainActivity.MyAccount.Avatar);
        }
    }
    @Override
    public void onClick(View v) {
        if(v==MenuIcon){
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
        else if(v==NavigationAccountSetting){
            Intent intent = new Intent(this, AccountActivity.class);
            intent.putExtra("username",MyAccount.Username);
            intent.putExtra("firstName", MyAccount.FirstName);
            intent.putExtra("lastName", MyAccount.LastName);
            intent.putExtra("email", MyAccount.Email);
            startActivity(intent);
        }
    }
}
