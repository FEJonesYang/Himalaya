package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-10-20
 * @Function 获取数据之后，调用该接口
 */
public interface IRecommendViewCallBack {

    /**
     * 获取推荐内容的接口
     *
     * @param albums
     */
    void onRecommendListLoaded(List<Album> albums);

    /**
     * 网络错误
     */
    void onNetworkError();

    /**
     *  获取的数据为空
     * */
    void onEmpty();

    /**
     *  正在加载
     * */
    void onLoading();

}
