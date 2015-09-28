package com.zhy.skinchangenow.attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.skinchangenow.ResourceManager;
import com.zhy.skinchangenow.SkinManager;

/**
 * Created by zhy on 15/9/28.
 */
public enum SkinAttrType
{
    BACKGROUD("background")
            {
                @Override
                public void apply(View view, String resName)
                {
                    Drawable drawable = getResourceManager().getDrawableByName(resName);
                    if (drawable == null) return;
                    view.setBackgroundDrawable(drawable);
                }
            }, COLOR("textColor")
        {
            @Override
            public void apply(View view, String resName)
            {
                ((TextView) view).setTextColor(getResourceManager().getColor(resName));

            }
        }, SRC("src")
        {
            @Override
            public void apply(View view, String resName)
            {
                if (view instanceof ImageView)
                {
                    Drawable drawable = getResourceManager().getDrawableByName(resName);
                    if (drawable == null) return;
                    ((ImageView) view).setImageDrawable(drawable);
                }

            }
        };

    String attrType;

    SkinAttrType(String attrType)
    {
        this.attrType = attrType;
    }

    public String getAttrType()
    {
        return attrType;
    }


    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager()
    {
        return SkinManager.getInstance().getResourceManager();
    }

}
