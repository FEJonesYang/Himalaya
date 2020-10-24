package com.example.himalaya.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.himalaya.utils.FragmentCreator;

/**
 * @author JonesYang
 * @Data 2020-10-17
 * @Function  ViewPager + Fragment 的适配器
 */
public class MainContentAdapter extends FragmentPagerAdapter {

    public MainContentAdapter(FragmentManager manager) {
        super(manager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        //返回 Fragment
        return FragmentCreator.getFragment(position);
    }

    @Override
    public int getCount() {
        //返回 Fragment 的数量
        return FragmentCreator.PAGE_COUNT;
    }
}
