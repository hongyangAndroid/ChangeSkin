package com.zhy.skinchangenow.attr;

import android.view.View;

import com.zhy.skinchangenow.utils.L;

/**
 * Created by zhy on 15/9/22.
 */
public abstract class SkinAttr
{
    String attrName;
    String attrType;


    public SkinAttr(String attrName, String attrType)
    {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public void apply(View view)
    {
        L.e("SkinAttr : " + attrName + " , " + attrType);
    }
}
