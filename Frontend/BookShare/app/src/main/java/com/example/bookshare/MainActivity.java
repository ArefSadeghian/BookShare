package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

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
        if(MyAccount!=null){
            Username.setText(MyAccount.Username);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==MenuIcon){
            drawerLayout.openDrawer(Gravity.RIGHT);
        }
        else if(v==NavigationAccountSetting){
            DialogFragment dialogFragment = new AccountDialog();
            dialogFragment.show(getSupportFragmentManager(),"AccountDialog");
        }
    }
}
