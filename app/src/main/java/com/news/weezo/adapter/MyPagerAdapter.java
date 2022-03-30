package com.news.weezo.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.news.weezo.activity.TextNewsFragment;
import com.news.weezo.activity.VideoNewsFragment;

import org.jetbrains.annotations.NotNull;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public MyPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 1:
                VideoNewsFragment videoNewsFragment = new VideoNewsFragment();
                return  videoNewsFragment;
            case 0:
                TextNewsFragment textNewsFragment = new TextNewsFragment();
                return  textNewsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
