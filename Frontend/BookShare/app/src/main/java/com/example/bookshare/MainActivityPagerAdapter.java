package com.example.bookshare;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {
    SignInFragment signInFragment;
    SignUpFragment signUpFragment;
    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result;
        if(position==0){
            signInFragment = new SignInFragment();
            return signInFragment;
        }
        signUpFragment = new SignUpFragment();
        return signUpFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
