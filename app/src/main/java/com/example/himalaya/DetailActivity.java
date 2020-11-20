package com.example.himalaya;


import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.himalaya.adapters.DetailAlbumAdapter;
import com.example.himalaya.base.BaseActivity;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.presenters.AlbumDetailPresenter;
import com.example.himalaya.utils.ImageBlur;
import com.example.himalaya.utils.LogUtil;
import com.example.himalaya.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;


public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private static final String TAG = "DetailActivity";
    //顶部的专辑信息
    private ImageView mLargeCover;
    private ImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage = 1;
    //RecyclerView
    private RecyclerView recyclerView;
    private DetailAlbumAdapter detailAlbumAdapter;
    //UI 界面 UILoader
    private UILoader uiLoader;
    private FrameLayout mDetailListContainer;
    private long mCurrentId;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        //初始化界面
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getAlbumDetailPresenter();
        mAlbumDetailPresenter.registerViewCallBack(this);
    }


    private void initView() {
        // 列表 UI 布局的 Container --> FrameLayout
        mDetailListContainer = findViewById(R.id.detail_list_container);

        //获取数据成功则加载 RecyclerView 布局
        if (uiLoader == null) {
            uiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup viewGroup) {
                    return createSuccessView(viewGroup);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(uiLoader);
            //如果网络发生错误
            uiLoader.setOnRetryClickListener(new UILoader.OnRetryClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onRetryClick() {
                    mAlbumDetailPresenter.getAlbumDetail((int) mCurrentId,mCurrentPage);
                }
            });
        }
        //显示专辑信息的控件
        mLargeCover = findViewById(R.id.iv_larger_cover);
        mSmallCover = findViewById(R.id.iv_small_cover);
        mAlbumTitle = findViewById(R.id.tv_album_title);
        mAlbumAuthor = findViewById(R.id.tv_album_author);

    }

    /**
     * 数据获取成功，设置 RecyclerView 布局
     */
    private View createSuccessView(ViewGroup viewGroup) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_detail_list, viewGroup, false);
        //设置 DetailAlbumAdapter 的适配器
        recyclerView = view.findViewById(R.id.detail_rv);
        //1、 布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //2、适配器
        detailAlbumAdapter = new DetailAlbumAdapter();
        recyclerView.setAdapter(detailAlbumAdapter);
        //3、设置分割线
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                //用到了一个工具类 UIUtil ：dp ---> px;px ---> dp
                outRect.top = UIUtil.dip2px(view.getContext(), 2);
                outRect.bottom = UIUtil.dip2px(view.getContext(), 2);
                outRect.left = UIUtil.dip2px(view.getContext(), 2);
                outRect.right = UIUtil.dip2px(view.getContext(), 2);
            }
        });
        return view;
    }

    /**
     * 详情列表的数据获取成功
     */
    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //判断数据的结果，根据结果显示UI
        if (tracks == null || tracks.size() == 0) {
            onEmpty();
        }

        if (uiLoader != null) {
            uiLoader.updateUI(UILoader.UIStatus.SUCCESS);
        }
        //加载详情界面的 item 的UI 数据
        detailAlbumAdapter.setData(tracks);
    }

    /**
     * 设置UI
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onAlbumLoaded(Album album) {
        //获取专辑的详情内容
        long id = album.getId();
        mCurrentId = id;
        mAlbumDetailPresenter.getAlbumDetail((int) id, mCurrentPage);
        //正在加载UI Loading 的状态
        onLoading();

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        //大图片实现毛玻璃效果
        if (mLargeCover != null && null != mLargeCover) {
            final Drawable drawable = mLargeCover.getDrawable();
            //Picasso 实现异步回调
            Picasso.get().load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    if (drawable != null) {
                        ImageBlur.makeBlur(mLargeCover, DetailActivity.this);
                    }
                }

                @Override
                public void onError(Exception e) {
                    LogUtil.d(TAG, "图片加载失败");
                }
            });
        }
        if (mSmallCover != null) {
            Glide.with(this).load(album.getCoverUrlSmall()).into(mSmallCover);
        }
    }

    @Override
    public void onNetworkError(int errorCode, String errorMessage) {
        LogUtil.d(TAG, "Network error...");
        uiLoader.updateUI(UILoader.UIStatus.NETWORK_ERROR);
    }


    public void onEmpty() {
        LogUtil.d(TAG, "Data is empty...");
        uiLoader.updateUI(UILoader.UIStatus.EMPTY);
    }

    public void onLoading() {
        LogUtil.d(TAG, "Loading...");
        uiLoader.updateUI(UILoader.UIStatus.LOADING);
    }
}
