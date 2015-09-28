package com.zhy.skinchangenow.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zhy.skinchangenow.SkinManager;
import com.zhy.skinchangenow.SkinParseFactoryBak;
import com.zhy.skinchangenow.attr.SkinAttr;
import com.zhy.skinchangenow.attr.SkinAttrSupport;
import com.zhy.skinchangenow.attr.SkinView;
import com.zhy.skinchangenow.callback.ISkinChangedListener;
import com.zhy.skinchangenow.utils.L;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 15/9/22.
 */
public class BaseSkinActivityBak extends AppCompatActivity implements ISkinChangedListener
{
    private SkinParseFactoryBak mSkinParseFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        L.e("TAG" + getLayoutInflater().getFilter() + " FILTER");

        L.e("TAG" + (getLayoutInflater().getFactory())+" getFactory");


        L.e("TAG" + (getLayoutInflater().getFactory2())+" getFactory2");

//        mSkinParseFactory = new SkinParseFactory();
//        getLayoutInflater().setFactory(mSkinParseFactory);
        super.onCreate(savedInstanceState);




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
                    view = getLayoutInflater().createView("android.view." + name, null, attrs);
                    L.e("android.view.");
                }
                if (view == null)
                {
                    try
                    {
                        L.e("TAG" + getLayoutInflater().getFilter()+" FILTER");
                        view = getLayoutInflater().createView("android.widget." + name, null, attrs);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    L.e("android.widget.");
                    if (view == null)
                    {
                        Class clazz = getClassLoader().loadClass(
                                "android.widget." + name).asSubclass(View.class);
                        final Class<?>[] mConstructorSignature = new Class[]{
                                Context.class, AttributeSet.class};
                        Constructor constructor = clazz.getConstructor(mConstructorSignature);
                        constructor.setAccessible(true);
                        Object[] args = new Object[2];
                        args[0] = this;
                        args[1] = attrs;
                        view = (View) constructor.newInstance(args);

                        L.e("android.widget. view=" + view);
                    }
                }
                if (view == null)
                {
                    view = getLayoutInflater().createView(name, "android.webkit.", attrs);
                    L.e("android.webkit.");
                }

            } else
            {
                try
                {
                    view = LayoutInflater.from(context).createView(name, null, attrs);
                }catch (Exception e)
                {

                }



                L.e("null ns");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        View view = super.onCreateView(parent, name, context, attrs);
        if (view == null)
            view = createView(this, name, attrs);
        // if (view == null)
        //   view = getDelegate().createView(parent, name, this, attrs);

        L.e("act onCreateView view parent " + view);
        List<SkinAttr> skinAttrList = SkinAttrSupport.getSkinAttrs(attrs, context);
        if (skinAttrList.size() != 0)
        {
            List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
            if (skinViews == null)
            {
                skinViews = new ArrayList<SkinView>();
            }
            SkinManager.getInstance().addSkinView(this, skinViews);
            skinViews.add(new SkinView(view, skinAttrList));

            if (SkinManager.getInstance().hasSkinPlugin())
            {
                SkinManager.getInstance().apply(this);
            }
        }

        return view;
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        SkinManager.getInstance().addChangedListener(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        SkinManager.getInstance().removeChangedListener(this);
    }


    @Override
    public void onSkinChanged()
    {
        SkinManager.getInstance().apply(this);
    }
}
