package com.example.results;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

class PageAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentList;
    ArrayList<String> mFragmentTitleList;

    PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        mFragmentList = new ArrayList<>();
        mFragmentTitleList = new ArrayList<>();
    }

    public Fragment getItem(int position) {
        return  mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }



    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}


