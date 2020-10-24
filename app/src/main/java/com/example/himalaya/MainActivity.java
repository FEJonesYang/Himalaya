package com.example.himalaya;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


import com.example.himalaya.adapters.IndicatorAdapter;
import com.example.himalaya.adapters.MainContentAdapter;


import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;


public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private IndicatorAdapter indicatorAdapter;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    /**
     *  table 栏的点击事件
     * */
    private void initEvent() {
        indicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.onIndicatorTapClickListener() {
            @Override
            public void onTapClick(int index) {
                //始终都需要调用 viewPager.setCurrentItem(index); ，如何在 Activity 获取 index？
                //这时候就需要把 IndicatorAdapter 适配器里面的 index 传过来，这时候可以在 IndicatorAdapter 设置
                //接口进行回调，这就是面向接口编程
                viewPager.setCurrentItem(index);
            }
        });
    }

    private void initView() {
        magicIndicator = findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));

        //ViewPager
        viewPager = findViewById(R.id.content_pager);

        //创建内容适配器
        manager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(manager);
        viewPager.setAdapter(mainContentAdapter);

        //创建indicator的适配器，由于点击事件需要把 indicatorAdapter 提升成类变量
        indicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(indicatorAdapter);

        //把ViewPager和indicator绑定到一起
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }
}
