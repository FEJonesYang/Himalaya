package com.example.himalaya.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.example.himalaya.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * @author JonesYang
 * @Data 2020-10-12
 * @Function 导航栏的适配器
 */
public class IndicatorAdapter extends CommonNavigatorAdapter {

    //标题数组
    private final String[] mTitles;
    //点击事件的接口成员变量
    public onIndicatorTapClickListener onIndicatorTapClickListener;

    public IndicatorAdapter(Context context) {
        //把标题放入定义的 mTitles 数组里
        mTitles = context.getResources().getStringArray(R.array.indicator_title);
    }

    @Override
    public int getCount() {
        if (mTitles != null) {
            return mTitles.length;
        }
        return 0;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        //创建View
        final SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
        //一般情况下设置为灰色
        simplePagerTitleView.setNormalColor(Color.GRAY);
        //被选择后设置的颜色
        simplePagerTitleView.setSelectedColor(Color.WHITE);
        //设置文本
        simplePagerTitleView.setText(mTitles[index]);
        //被点击之后，切换到相应的ViewPager之下
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果事件监听不为空
                if (onIndicatorTapClickListener != null) {
                    onIndicatorTapClickListener.onTapClick(index);
                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
        linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        linePagerIndicator.setColors(Color.WHITE);
        return linePagerIndicator;
    }

    // 设置接口成员变量
    public void setOnIndicatorTapClickListener(IndicatorAdapter.onIndicatorTapClickListener onIndicatorTapClickListener) {
        this.onIndicatorTapClickListener = onIndicatorTapClickListener;
    }

    //table 的点击事件的接口
    public interface onIndicatorTapClickListener {
        void onTapClick(int index);
    }
}
