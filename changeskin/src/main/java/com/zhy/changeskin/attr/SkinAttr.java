package com.zhy.changeskin.attr;

import android.view.View;

/**
 * Created by zhy on 15/9/22.
 */
public class SkinAttr
{
    String resName;
    SkinAttrType attrType;


    public SkinAttr(SkinAttrType attrType, String resName)
    {
        this.resName = resName;
        this.attrType = attrType;
    }

    public void apply(View view)
    {
        attrType.apply(view, resName);
    }
}
