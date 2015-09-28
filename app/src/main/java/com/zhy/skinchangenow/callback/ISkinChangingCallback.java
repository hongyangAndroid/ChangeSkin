package com.zhy.skinchangenow.callback;

/**
 * Created by zhy on 15/9/22.
 */
public interface ISkinChangingCallback
{
    void onStart();
    void onError(Exception e);
    void onComplete();
}
