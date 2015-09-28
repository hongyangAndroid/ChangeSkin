package com.zhy.skinchangenow.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.skinchangenow.R;
import com.zhy.skinchangenow.utils.L;

/**
 * Created by zhy on 15/9/23.
 */
public class TestFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        L.e("inflater in fra = " + inflater);
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

}
