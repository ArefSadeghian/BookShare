package com.example.bookshare;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class EntranceActivityPagerAdapter extends FragmentStatePagerAdapter {
    public EntranceActivityPagerAdapter(FragmentManager fm) {
        super(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result;
        if(position==0){
            result = new SignInFragment();
            return result;
        }
        result = new SignUpFragment();
        return result;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
