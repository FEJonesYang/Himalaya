package com.example.himalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * @author JonesYang
 * @Data 2020-10-26
 * @Function
 */
public interface IAlbumDetailViewCallback {

    /**
     * 专辑详情内容加载
     *
     * @param tracks
     */
    void onDetailListLoaded(List<Track> tracks);

    /**
     * 把 Album 传给 UI
     *
     * @param album
     */
    void onAlbumLoaded(Album album);

}
