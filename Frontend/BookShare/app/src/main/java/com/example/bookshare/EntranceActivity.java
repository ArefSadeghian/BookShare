package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

public class EntranceActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    EntranceActivityPagerAdapter pagerAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        sharedPreferences = getSharedPreferences("com.example.bookshare", MODE_PRIVATE);
        tabLayout = (TabLayout) findViewById(R.id.entrance_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.entrance_viewPager);
        pagerAdapter = new EntranceActivityPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);
    }

    public void signUpVerification(String u, String p, String f, String l, String e, String a){
        MainActivity.MyAccount = new Account(u,p,f,l,e,"address",1);
        sharedPreferences.edit()
                .putBoolean("app_first",false)
                .putString("username",u)
                .putString("password",p)
                .putString("firstName",f)
                .putString("lastName",l)
                .putString("avatarAddress",a)
                .putInt("id", 1).commit();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent){
        super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                pagerAdapter.signUpFragment.avatarAddress = imageReturnedIntent.getData().toString();
                pagerAdapter.signUpFragment.avatar.setImageURI(imageReturnedIntent.getData());
                pagerAdapter.signUpFragment.avatar.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            else {
                pagerAdapter.signUpFragment.avatarAddress = "";
                pagerAdapter.signUpFragment.avatar.setImageDrawable(getResources().getDrawable(R.drawable.sign_up_avatar));
            }
        }
    }

    public void chooseImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooseIntent = Intent.createChooser(getIntent,"Select Image");
        chooseIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooseIntent,1);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
