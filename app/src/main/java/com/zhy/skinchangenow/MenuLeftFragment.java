package com.zhy.skinchangenow;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.skinchangenow.callback.ISkinChangingCallback;

import java.io.File;

public class MenuLeftFragment extends Fragment
{
    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "night_plugin.apk";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.layout_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.id_restore).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SkinManager.getInstance().removeAnySkin();
            }
        });

        view.findViewById(R.id.id_changeskin).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.zhy.plugin", new ISkinChangingCallback()
                {
                    @Override
                    public void onStart()
                    {
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Toast.makeText(getActivity(), "换肤失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete()
                    {
                        Toast.makeText(getActivity(), "换肤成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
