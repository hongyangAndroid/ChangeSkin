package com.zhy.changeskin.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.app.AppCompatViewInflater;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;

import com.zhy.changeskin.SkinManager;
import com.zhy.changeskin.attr.SkinAttr;
import com.zhy.changeskin.attr.SkinAttrSupport;
import com.zhy.changeskin.attr.SkinView;
import com.zhy.changeskin.callback.ISkinChangedListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on 15/9/22.
 */
public class BaseSkinActivity extends AppCompatActivity implements ISkinChangedListener
{

    static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private AppCompatViewInflater mAppCompatViewInflater;
    private final Object[] mConstructorArgs = new Object[2];

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        View view = null;
        if ("fragment".equals(name))
        {
            view = super.onCreateView(name, context, attrs);
        }
        if (view == null)
            view = getView(name, context, attrs);
        injectSkin(context, attrs, view);
        return view;
    }

    private View getView(String name, Context context, AttributeSet attrs)
    {
        switch (name)
        {
            case "EditText":
                return new AppCompatEditText(context, attrs);
            case "Spinner":
                return new AppCompatSpinner(context, attrs);
            case "CheckBox":
                return new AppCompatCheckBox(context, attrs);
            case "RadioButton":
                return new AppCompatRadioButton(context, attrs);
            case "CheckedTextView":
                return new AppCompatCheckedTextView(context, attrs);
            case "AutoCompleteTextView":
                return new AppCompatAutoCompleteTextView(context, attrs);
            case "MultiAutoCompleteTextView":
                return new AppCompatMultiAutoCompleteTextView(context, attrs);
            case "RatingBar":
                return new AppCompatRatingBar(context, attrs);
            case "Button":
                return new AppCompatButton(context, attrs);
            case "TextView":
                return new AppCompatTextView(context, attrs);
        }

        return createViewFromTag(context, name, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        View view = null;
        if ("fragment".equals(name))
        {
            view = super.onCreateView(name, context, attrs);
        }

        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null)
        {
            mAppCompatViewInflater = new AppCompatViewInflater();
        }

        boolean subDecorInstalled = true;
        final boolean inheritContext = isPre21 && subDecorInstalled && parent != null
                && parent.getId() != android.R.id.content
                && !ViewCompat.isAttachedToWindow(parent);

        view = mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21,
                true);

        if (view == null)
        {
            view = createViewFromTag(context, name, attrs);
        }
        injectSkin(context, attrs, view);
        return view;

    }

    private void injectSkin(Context context, AttributeSet attrs, View view)
    {
        //do some skin inject
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
    }


    private View createViewFromTag(Context context, String name, AttributeSet attrs)
    {
        if (name.equals("view"))
        {
            name = attrs.getAttributeValue(null, "class");
        }

        try
        {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.'))
            {
                // try the android.widget prefix first...
                return createView(context, name, "android.widget.");
            } else
            {
                return createView(context, name, null);
            }
        } catch (Exception e)
        {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally
        {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException
    {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try
        {
            if (constructor == null)
            {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e)
        {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
