package com.zhy.skinchangenow;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by zhy on 15/9/22.
 */
public class ResourceManager
{
    private Resources mResources;
    private String mPluginPackageName;

    public ResourceManager(Resources res,String pluginPackageName)
    {
        mResources = res;
        mPluginPackageName = pluginPackageName;
    }

    public Drawable getDrawableByName(String name)
    {
        return mResources.getDrawable(mResources.getIdentifier(name, "drawable", mPluginPackageName));
    }

    public int getColor(String name)
    {
        return mResources.getColor(mResources.getIdentifier(name, "color", mPluginPackageName));

    }

}
