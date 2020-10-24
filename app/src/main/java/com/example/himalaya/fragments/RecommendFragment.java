package com.example.himalaya.fragments;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.himalaya.R;
import com.example.himalaya.adapters.RecommendListAdapter;
import com.example.himalaya.base.BaseFragment;
import com.example.himalaya.interfaces.IRecommendViewCallBack;
import com.example.himalaya.presenters.RecommendPresenter;

import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-10-17
 * @Function 推荐列表的 Fragment
 */
public class RecommendFragment extends BaseFragment implements IRecommendViewCallBack, UILoader.OnRetryClickListener {

    public static final String TAG = "RecommendFragment";
    private View view;
    private RecyclerView recommendRecyclerView;
    private RecommendListAdapter recommendListAdapter;  //RecyclerView 的适配器
    private RecommendPresenter recommendPresenter; //逻辑层的对象变量
    private UILoader uiLoader;

    @Override
    protected View onSubViewLoaded(final LayoutInflater layoutInflater, final ViewGroup container) {

        uiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup container) {
                return createSuccessView(layoutInflater, container);
            }
        };

        //获取逻辑层的对象
        recommendPresenter = RecommendPresenter.getRecommendPresenter();
        //设置通知接口的注册
        recommendPresenter.registerViewCallBack(this);
        //获取推荐列表
        recommendPresenter.getRecommendList();

        //与父布局进行解绑
        if (uiLoader.getParent() instanceof ViewGroup) {
            ((ViewGroup) uiLoader.getParent()).removeView(uiLoader);
        }

        uiLoader.setOnRetryClickListener(this);

        //返回View
        return uiLoader;
    }

    /**
     * 获取数据
     */
    private View createSuccessView(LayoutInflater layoutInflater, ViewGroup container) {

        //加载View
        view = layoutInflater.inflate(R.layout.fragement_recommend, container, false);

        /*
         * RecycleView 的使用,显示列表数据
         * */

        //1、获取对应的控件
        recommendRecyclerView = view.findViewById(R.id.recommend_list);

        //2、设置布局管理器，分割线
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recommendRecyclerView.setLayoutManager(linearLayoutManager);
        recommendRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //用到了一个工具类 UIUtil ：dp ---> px;px ---> dp
                outRect.top = UIUtil.dip2px(view.getContext(), 5);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 5);
                outRect.left = UIUtil.dip2px(view.getContext(), 5);
                outRect.right = UIUtil.dip2px(view.getContext(), 5);
            }
        });

        //3、设置适配器
        recommendListAdapter = new RecommendListAdapter();
        recommendRecyclerView.setAdapter(recommendListAdapter);

        return view;
    }

    /**
     * 更新UI
     */
    @Override
    public void onRecommendListLoaded(List<Album> albums) {
        recommendListAdapter.setData(albums);
        uiLoader.updateUI(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        LogUtil.d(TAG, "网络出现错误.");
        uiLoader.updateUI(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onEmpty() {
        LogUtil.d(TAG, "获取数据为空.");
        uiLoader.updateUI(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onLoading() {
        LogUtil.d(TAG, "正在加载...");
        uiLoader.updateUI(UILoader.UIStatus.LOADING);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消注册，避免泄漏
        if (recommendPresenter != null) {
            recommendPresenter.unRegisterViewCallBack(this);
        }
    }


    /**
     * 接口回调，点击之后会重新加载数据
     */
    @Override
    public void onRetryClick() {
        if (recommendPresenter != null) {
            recommendPresenter.getRecommendList();
        }
    }
}
