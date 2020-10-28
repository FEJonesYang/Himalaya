package com.example.himalaya.presenters;

import android.util.Log;

import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-10-26
 * @Function
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {


    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    private Album mTargetAlbum;
    /**
     * 构造一个单例
     */
    private static volatile AlbumDetailPresenter albumDetailPresenter;

    private AlbumDetailPresenter() {
    }

    //Double Lock Check
    public static AlbumDetailPresenter getAlbumDetailPresenter() {
        if (albumDetailPresenter == null) {
            synchronized (AlbumDetailPresenter.class) {
                if (albumDetailPresenter == null) {
                    albumDetailPresenter = new AlbumDetailPresenter();
                }
            }
        }
        return albumDetailPresenter;
    }

    @Override
    public void pullRefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {

    }

    /**
     * 注册回调
     */
    @Override
    public void registerViewCallBack(IAlbumDetailViewCallback iAlbumDetailViewCallback) {
        if (!mCallbacks.contains(iAlbumDetailViewCallback)) {
            mCallbacks.add(iAlbumDetailViewCallback);
            if (mTargetAlbum != null) {
                iAlbumDetailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    /**
     * 取消注册
     */
    @Override
    public void unregisterViewCallBack(IAlbumDetailViewCallback iAlbumDetailViewCallback) {
        if (mCallbacks != null) {
            mCallbacks.remove(iAlbumDetailViewCallback);
        }
    }

    /**
     * 为 mTargetAlbum 赋值，在接口回调中使用
     */
    public void setTargetAlbum(Album targetAlbum) {
        this.mTargetAlbum = targetAlbum;
    }
}
