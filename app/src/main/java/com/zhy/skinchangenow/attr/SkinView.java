package com.zhy.skinchangenow.attr;

import android.view.View;

import com.zhy.skinchangenow.utils.L;

import java.util.List;

/**
 * Created by zhy on 15/9/22.
 */
public class SkinView
{
//    SoftReference<View> viewRef;
    View view ;
    List<SkinAttr> attrs;

    public SkinView(View view, List<SkinAttr> skinAttrs)
    {
        this.view = view;
        this.attrs = skinAttrs;
    }

    public void apply()
    {
       // View view = viewRef.get();
        if (view == null) return;

        for (SkinAttr attr : attrs)
        {
            attr.apply(view);
        }
    }
}
