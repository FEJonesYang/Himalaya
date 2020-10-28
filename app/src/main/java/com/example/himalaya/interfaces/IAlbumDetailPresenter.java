package com.example.himalaya.interfaces;

/**
 * @author JonesYang
 * @Data 2020-10-26
 * @Function 进入某个专辑内，实现刷新加载的功能
 */
public interface IAlbumDetailPresenter {
    /**
     * 下拉刷新更多内容
     */
    void pullRefreshMore();

    /**
     * 下拉加载更多内容
     */
    void loadMore();

    /**
     * 获取专辑详情
     *
     * @param albumId
     * @param page
     */
    void getAlbumDetail(int albumId, int page);

    /**
     * 注册UI的回调
     */
    void registerViewCallBack(IAlbumDetailViewCallback iAlbumDetailViewCallback);

    /**
     * 取消注册UI的回调
     */
    void unregisterViewCallBack(IAlbumDetailViewCallback iAlbumDetailViewCallback);

}
