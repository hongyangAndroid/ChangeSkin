package com.zhy.skinchangenow.attr;

import android.view.View;

import com.zhy.skinchangenow.ResourceManager;
import com.zhy.skinchangenow.SkinManager;

/**
 * Created by zhy on 15/9/22.
 */
public class BackgroundAttr extends SkinAttr
{
    public BackgroundAttr(String attrName, String attrType)
    {
        super(attrName, attrType);
    }

    @Override
    public void apply(View view)
    {
        super.apply(view);
        ResourceManager resourceManager = SkinManager.getInstance().getResourceManager();

        if (attrType.equals("drawable"))
        {
            view.setBackgroundDrawable(resourceManager.getDrawableByName(attrName));
        }
    }
}
