package com.example.himalaya.utils;

import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.fragments.HistoryFragment;
import com.example.himalaya.fragments.RecommendFragment;
import com.example.himalaya.fragments.SubscriptionFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JonesYang
 * @Data 2020-10-17
 * @Function 创建Fragment
 */
public class FragmentCreator {

    public static final int INDEX_RECOMMEND = 0;
    public static final int INDEX_SUBSCRIPTION = 1;
    public static final int INDEX_HISTORY = 2;

    public static final int PAGE_COUNT = 3;
    private static Map<Integer, BaseFragment> sCache = new HashMap<>();

    public static BaseFragment getFragment(int index) {
        //如果缓存中存在
        BaseFragment fragment = sCache.get(index);
        if (fragment != null) {
            return fragment;
        }

        //如果缓存不存在相应的Fragment
        switch (index) {
            case INDEX_RECOMMEND:
                fragment = new RecommendFragment();
                break;
            case INDEX_SUBSCRIPTION:
                fragment = new SubscriptionFragment();
                break;
            case INDEX_HISTORY:
                fragment = new HistoryFragment();
                break;
        }
        //把创建的Fragment加入到Map缓存中
        sCache.put(index, fragment);
        return fragment;
    }

}
