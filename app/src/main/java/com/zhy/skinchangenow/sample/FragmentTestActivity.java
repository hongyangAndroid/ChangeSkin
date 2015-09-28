package com.zhy.skinchangenow.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhy.skinchangenow.R;
import com.zhy.skinchangenow.base.BaseSkinActivity;

/**
 * Created by zhy on 15/9/23.
 */
public class FragmentTestActivity extends BaseSkinActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.id_fragment_container);
        if (fragment == null)
        {
            fragment = new TestFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.id_fragment_container, fragment).commit();
        }

    }
}
