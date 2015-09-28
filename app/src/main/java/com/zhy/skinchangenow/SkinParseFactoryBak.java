package com.zhy.skinchangenow;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

import com.zhy.skinchangenow.attr.SkinAttr;
import com.zhy.skinchangenow.attr.SkinAttrSupport;
import com.zhy.skinchangenow.attr.SkinView;
import com.zhy.skinchangenow.callback.ISkinChangedListener;
import com.zhy.skinchangenow.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 15/9/22.
 */
public class SkinParseFactoryBak implements Factory
{

    private AppCompatDelegate appCompatDelegate;

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {

        L.e("onCreateView ===" + name + " , context =====" + context);
        if (!(context instanceof ISkinChangedListener)) return null;


        ISkinChangedListener listener = (ISkinChangedListener) context;
        List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(attrs, context);
        if (skinAttrList.size() == 0) return null;

        View view = createView(context, name, attrs);

        L.e(" view in factory :" + view);
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(listener);
        if (skinViews == null)
        {
            skinViews = new ArrayList<SkinView>();
        }
        SkinManager.getInstance().addSkinView(listener, skinViews);

        skinViews.add(new SkinView(view, skinAttrList));

        if (SkinManager.getInstance().hasSkinPlugin())
        {
            L.e("apply");
            SkinManager.getInstance().apply(listener);
        }

        return view;
    }



    private View createView(Context context, String name, AttributeSet attrs)
    {
        L.e("createView " + name + " " + attrs);
        View view = null;
        try
        {
            if (-1 == name.indexOf('.'))
            {
                if ("View".equals(name))
                {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                    L.e("android.view.");
                }
                if (view == null)
                {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                    L.e("android.widget.");
                }
                if (view == null)
                {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                    L.e("android.webkit.");
                }
            } else
            {
                view = LayoutInflater.from(context).createView(name, null, attrs);
                L.e("null ns");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }


}
