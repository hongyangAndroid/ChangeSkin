package com.zhy.skinchangenow.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.zhy.skinchangenow.SkinManager;
import com.zhy.skinchangenow.SkinParseFactoryBak;
import com.zhy.skinchangenow.callback.ISkinChangedListener;

import java.lang.reflect.Field;

/**
 * Created by zhy on 15/9/22.
 */
public class BaseSkinFragmentActivityBak extends AppCompatActivity implements ISkinChangedListener
{
    private SkinParseFactoryBak mSkinParseFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Field field = null;
        try
        {
            field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(getLayoutInflater(), false);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }


        mSkinParseFactory = new SkinParseFactoryBak();
        getLayoutInflater().setFactory(mSkinParseFactory);
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
