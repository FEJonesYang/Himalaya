package com.example.himalaya.presenters;


import com.example.himalaya.interfaces.IAlbumDetailPresenter;
import com.example.himalaya.interfaces.IAlbumDetailViewCallback;
import com.example.himalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JonesYang
 * @Data 2020-10-26
 * @Function 获取详情界面的数据
 */
public class AlbumDetailPresenter implements IAlbumDetailPresenter {


    private static final String TAG = "AlbumDetailPresenter";
    // 实现了 IAlbumDetailViewCallback 接口的回调队列
    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();
    //获取的数据源
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


    /**
     * 获取详情列表的数据
     * */
    @Override
    public void getAlbumDetail(int albumId, int page) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.ALBUM_ID, albumId + "");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page + "");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG, "Track Size -- > " + tracks.size());
                    //如果获取成功
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                LogUtil.d(TAG, "errorCode -- > " + errorCode);
                LogUtil.d(TAG, "errorMessage -- > " + errorMessage);
                //如果没有获取成功
                handlerError(errorCode,errorMessage);
            }
        });
    }

    /**
     * 获取详情界面时网络发生错误
     * @param errorCode
     * @param errorMessage
     */
    private void handlerError(int errorCode, String errorMessage) {
        if (mCallbacks != null) {
            for (IAlbumDetailViewCallback callback : mCallbacks) {
                callback.onNetworkError(errorCode,errorMessage);
            }
        }
    }

    /**
     * 获取数据成功
     */
    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onDetailListLoaded(tracks);
        }
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
