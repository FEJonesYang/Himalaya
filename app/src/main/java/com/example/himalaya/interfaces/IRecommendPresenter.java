package com.example.himalaya.interfaces;

/**
 * @author JonesYang
 * @Data 2020-10-20
 * @Function 逻辑层的处理接口
 */
public interface IRecommendPresenter {

    /**
     * 获取推荐内容
     */
    void getRecommendList();

    /**
     * 注册UI的回调
     */
    void registerViewCallBack(IRecommendViewCallBack callBack);

    /**
     * 取消注册UI的回调
     */
    void unRegisterViewCallBack(IRecommendViewCallBack callBack);

}
