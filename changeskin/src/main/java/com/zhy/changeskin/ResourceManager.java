package com.zhy.changeskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.zhy.changeskin.utils.L;

/**
 * Created by zhy on 15/9/22.
 */
public class ResourceManager
{
    private Resources mResources;
    private String mPluginPackageName;
    private String mSuffix;


    public ResourceManager(Resources res, String pluginPackageName, String suffix)
    {
        mResources = res;
        mPluginPackageName = pluginPackageName;

        if (suffix == null)
        {
            suffix = "";
        }
        mSuffix = suffix;

    }

    public Drawable getDrawableByName(String name)
    {
        try
        {
            name = appendSuffix(name);
            L.e("name = " + name);
            return mResources.getDrawable(mResources.getIdentifier(name, "drawable", mPluginPackageName));

        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int getColor(String name)
    {
        try
        {
            name = appendSuffix(name);
            L.e("name = " + name);
            return mResources.getColor(mResources.getIdentifier(name, "color", mPluginPackageName));

        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public ColorStateList getColorStateList(String name) {
        try
        {
            name = appendSuffix(name);
            L.e("name = " + name);
            return mResources.getColorStateList(mResources.getIdentifier(name, "color", mPluginPackageName));

        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

    }

    private String appendSuffix(String name)
    {
        if (!TextUtils.isEmpty(mSuffix))
            return name += "_" + mSuffix;
        return name ;
    }

}
