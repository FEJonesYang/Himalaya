package com.example.himalaya.presenters;

import com.example.himalaya.interfaces.IRecommendPresenter;
import com.example.himalaya.interfaces.IRecommendViewCallBack;
import com.example.himalaya.utils.Constants;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JonesYang
 * @Data 2020-10-20
 * @Function
 */
public class RecommendPresenter implements IRecommendPresenter {


    private List<IRecommendViewCallBack> mCallBacks = new ArrayList<>();
    private List<Album> albumList;
    public static final String TAG = "RecommendPresenter";

    /**
     * 构造一个单例
     */
    private volatile static RecommendPresenter recommendPresenter;

    //双重锁检测
    public static RecommendPresenter getRecommendPresenter() {
        if (recommendPresenter == null) {
            synchronized (RecommendPresenter.class) {
                if (recommendPresenter == null) {
                    recommendPresenter = new RecommendPresenter();
                }
            }
        }
        return recommendPresenter;
    }


    /**
     * 获取推荐数据: 获取猜你喜欢专辑
     * 接口：3.10.6
     */

    @Override
    public void getRecommendList() {
        updateLoading();
        //封装参数
        Map<String, String> map = new HashMap<>();
        //参数表示一页数据返回多少条
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT + "");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG, "Thread name ---> " + Thread.currentThread().getName());
                //获取数据成功
                if (gussLikeAlbumList != null) {
                    albumList = gussLikeAlbumList.getAlbumList();
                    //获取数据之后更新UI
                    LogUtil.d(TAG, "推荐栏目获取的数据 --->" + albumList.size());
                    //获得数据后 更新UI
                    handlerRecommendResult();
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取数据失败
                LogUtil.d(TAG, "error code ---> " + i);
                LogUtil.d(TAG, "error message --->" + s);
                //错误处理
                handlerError();
            }
        });
    }

    /**
     * 接口回调，数据还没有展示出来
     */
    private void updateLoading() {
        for (IRecommendViewCallBack callBack : mCallBacks) {
            callBack.onLoading();
        }
    }

    /**
     * 获取数据发生错误
     */
    private void handlerError() {
        if (mCallBacks != null) {
            for (IRecommendViewCallBack callBack : mCallBacks) {
                callBack.onNetworkError();
            }
        }
    }

    /**
     * 接口进行注册
     */
    @Override
    public void registerViewCallBack(IRecommendViewCallBack callBack) {
        if (mCallBacks != null && !mCallBacks.contains(callBack)) {
            mCallBacks.add(callBack);
        }
    }

    /**
     * 接口取消注册
     */
    @Override
    public void unRegisterViewCallBack(IRecommendViewCallBack callBack) {
        if (mCallBacks != null) {
            mCallBacks.remove(callBack);
        }
    }

    /***
     * 数据获取成功，进行接口回调
     * */
    private void handlerRecommendResult() {
        //通知更新UI
        if (albumList != null) {
            if (albumList.size() == 0) {
                for (IRecommendViewCallBack callBack : mCallBacks) {
                    callBack.onEmpty();
                }
            } else {
                for (IRecommendViewCallBack callBack : mCallBacks) {
                    callBack.onRecommendListLoaded(albumList);
                }
            }
        }
    }


}
