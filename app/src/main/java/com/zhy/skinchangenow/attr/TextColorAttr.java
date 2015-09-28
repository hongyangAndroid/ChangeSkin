package com.zhy.skinchangenow.attr;

import android.view.View;
import android.widget.TextView;

import com.zhy.skinchangenow.ResourceManager;
import com.zhy.skinchangenow.SkinManager;

/**
 * Created by zhy on 15/9/22.
 */
public class TextColorAttr extends SkinAttr
{
    public TextColorAttr(String attrName, String attrType)
    {
        super(attrName, attrType);
    }

    @Override
    public void apply(View view)
    {
        super.apply(view);
        ResourceManager resourceManager = SkinManager.getInstance().getResourceManager();
        if (attrType.equals("color"))
        {
            if(view instanceof TextView)
            {
                ((TextView)view).setTextColor(resourceManager.getColor(attrName));
            }
        }
    }
}
