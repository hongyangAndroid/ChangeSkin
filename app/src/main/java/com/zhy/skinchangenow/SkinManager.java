package com.zhy.skinchangenow;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhy.skinchangenow.attr.SkinView;
import com.zhy.skinchangenow.callback.ISkinChangedListener;
import com.zhy.skinchangenow.callback.ISkinChangingCallback;
import com.zhy.skinchangenow.utils.PrefUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/9/22.
 */
public class SkinManager
{
    private Context mContext;
    private Resources mResources;
    private ResourceManager mResourceManager;
    private PrefUtils mPrefUtils;

    private String mCurPluginPath;
    private String mCurPluginPkg;
    private boolean hasPlugin;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<ISkinChangedListener, List<SkinView>>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private SkinManager()
    {

    }

    private static SkinManager sInstance;

    public static SkinManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (SkinManager.class)
            {
                sInstance = new SkinManager();
            }
        }
        return sInstance;
    }


    public void init(Context context)
    {
        mContext = context.getApplicationContext();
        mPrefUtils = new PrefUtils(mContext);
        String skinPluginPath = mPrefUtils.getPluginPath();
        String skinPluginPkg = mPrefUtils.getPluginPkgName();
        if (TextUtils.isEmpty(skinPluginPath))
            return;
        File file = new File(skinPluginPath);
        if (!file.exists()) return;
        try
        {
            loadPlugin(skinPluginPath, skinPluginPkg);
            mCurPluginPath = skinPluginPath;
            mCurPluginPkg = skinPluginPkg;
        } catch (Exception e)
        {
            mPrefUtils.clear();
            e.printStackTrace();
        }
    }


    private void loadPlugin(String skinPath, String skinPkgName) throws Exception
    {
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);

        Resources superRes = mContext.getResources();
        mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResourceManager = new ResourceManager(mResources, skinPkgName);
        hasPlugin = true;
    }

    public void removeAnySkin()
    {
        clearPluginInfo();
        notifyChangedListeners();

    }

    private void clearPluginInfo()
    {
        mCurPluginPath = null;
        mCurPluginPkg = null;
        hasPlugin = false;
        mPrefUtils.clear();
    }


    public boolean hasSkinPlugin()
    {
        return hasPlugin;
    }


    public ResourceManager getResourceManager()
    {
        if (!hasPlugin)
        {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName());
        }
        return mResourceManager;
    }


    public void changeSkin(final String skinPluginPath, final String pkgName, final ISkinChangingCallback callback)
    {

        callback.onStart();
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    loadPlugin(skinPluginPath, pkgName);
                } catch (Exception e)
                {
                    e.printStackTrace();
                    callback.onError(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                try
                {
                    updatePluginInfo(skinPluginPath, pkgName);
                    notifyChangedListeners();
                    callback.onComplete();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    callback.onError(e);
                }

            }
        }.execute();


    }

    private void updatePluginInfo(String skinPluginPath, String pkgName)
    {
        mPrefUtils.putPluginPath(skinPluginPath);
        mPrefUtils.putPluginPkg(pkgName);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
    }


    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews)
    {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener)
    {
        return mSkinViewMaps.get(listener);
    }


    public void apply(ISkinChangedListener listener)
    {
        List<SkinView> skinViews = getSkinViews(listener);
        if (skinViews == null) return;
        for (SkinView skinView : skinViews)
        {
            skinView.apply();
        }
    }

    public void addChangedListener(ISkinChangedListener listener)
    {
        mSkinChangedListeners.add(listener);
    }


    public void removeChangedListener(ISkinChangedListener listener)
    {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }


    public void notifyChangedListeners()
    {
        for (ISkinChangedListener listener : mSkinChangedListeners)
        {
            listener.onSkinChanged();
        }
    }

}
