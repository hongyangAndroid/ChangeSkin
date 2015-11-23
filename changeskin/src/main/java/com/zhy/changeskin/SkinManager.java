package com.zhy.changeskin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.zhy.changeskin.attr.SkinView;
import com.zhy.changeskin.callback.ISkinChangedListener;
import com.zhy.changeskin.callback.ISkinChangingCallback;
import com.zhy.changeskin.utils.PrefUtils;

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

    private boolean usePlugin;
    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<ISkinChangedListener, List<SkinView>>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private SkinManager()
    {
    }

    private static class SingletonHolder
    {
        static SkinManager sInstance = new SkinManager();
    }

    public static SkinManager getInstance()
    {
        return SingletonHolder.sInstance;
    }


    public void init(Context context)
    {
        mContext = context.getApplicationContext();
        mPrefUtils = new PrefUtils(mContext);

        String skinPluginPath = mPrefUtils.getPluginPath();
        String skinPluginPkg = mPrefUtils.getPluginPkgName();
        mSuffix = mPrefUtils.getSuffix();
        if (TextUtils.isEmpty(skinPluginPath))
            return;
        File file = new File(skinPluginPath);
        if (!file.exists()) return;
        try
        {
            loadPlugin(skinPluginPath, skinPluginPkg, mSuffix);
            mCurPluginPath = skinPluginPath;
            mCurPluginPkg = skinPluginPkg;
        } catch (Exception e)
        {
            mPrefUtils.clear();
            e.printStackTrace();
        }
    }


    private void loadPlugin(String skinPath, String skinPkgName, String suffix) throws Exception
    {
       //checkPluginParams(skinPath, skinPkgName);
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);

        Resources superRes = mContext.getResources();
        mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResourceManager = new ResourceManager(mResources, skinPkgName, suffix);
        usePlugin = true;
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName)
    {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName))
        {
            return false;
        }
        return true;
    }

    private void checkPluginParamsThrow(String skinPath, String skinPkgName)
    {
        if (!checkPluginParams(skinPath, skinPkgName))
        {
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty ! ");
        }
    }


    public void removeAnySkin()
    {
        clearPluginInfo();
        notifyChangedListeners();
    }




    public boolean needChangeSkin()
    {
        return usePlugin || !TextUtils.isEmpty(mSuffix);
    }


    public ResourceManager getResourceManager()
    {
        if (!usePlugin)
        {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }


    /**
     * 应用内换肤，传入资源区别的后缀
     *
     * @param suffix
     */
    public void changeSkin(String suffix)
    {
        clearPluginInfo();//clear before
        mSuffix = suffix;
        mPrefUtils.putPluginSuffix(suffix);
        notifyChangedListeners();
    }

    private void clearPluginInfo()
    {
        mCurPluginPath = null;
        mCurPluginPkg = null;
        usePlugin = false;
        mSuffix = null;
        mPrefUtils.clear();
    }

    private void updatePluginInfo(String skinPluginPath, String pkgName, String suffix)
    {
        mPrefUtils.putPluginPath(skinPluginPath);
        mPrefUtils.putPluginPkg(pkgName);
        mPrefUtils.putPluginSuffix(suffix);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
        mSuffix = suffix;
    }


    public void changeSkin(final String skinPluginPath, final String pkgName, ISkinChangingCallback callback)
    {
        changeSkin(skinPluginPath, pkgName, "", callback);
    }


    /**
     * 根据suffix选择插件内某套皮肤，默认为""
     *
     * @param skinPluginPath
     * @param pkgName
     * @param suffix
     * @param callback
     */
    public void changeSkin(final String skinPluginPath, final String pkgName, final String suffix, ISkinChangingCallback callback)
    {
        if (callback == null)
            callback = ISkinChangingCallback.DEFAULT_SKIN_CHANGING_CALLBACK;
        final ISkinChangingCallback skinChangingCallback = callback;

        skinChangingCallback.onStart();
        checkPluginParamsThrow(skinPluginPath, pkgName);

        if (skinPluginPath.equals(mCurPluginPath) && pkgName.equals(mCurPluginPkg))
        {
            return;
        }

        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                } catch (Exception e)
                {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                try
                {
                    updatePluginInfo(skinPluginPath, pkgName, suffix);
                    notifyChangedListeners();
                    skinChangingCallback.onComplete();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

            }
        }.execute();
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
