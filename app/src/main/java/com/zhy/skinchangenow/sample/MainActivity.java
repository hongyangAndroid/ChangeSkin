package com.zhy.skinchangenow.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.skinchangenow.R;
import com.zhy.skinchangenow.SkinManager;
import com.zhy.skinchangenow.base.BaseSkinActivity;
import com.zhy.skinchangenow.callback.ISkinChangingCallback;

import java.io.File;

public class MainActivity extends BaseSkinActivity
{
    private ListView mListView;
    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "night_plugin.apk";
    private String[] mDatas = new String[]{"OpenFragment", "Service"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setAdapter(new ArrayAdapter<String>(this, -1, mDatas)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent
                            , false);
                }

                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_title);
                tv.setText(getItem(position));
                return convertView;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        Intent intent = new Intent(MainActivity.this,FragmentTestActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.zhy.plugin", new ISkinChangingCallback()
                        {
                            @Override
                            public void onStart()
                            {
                            }

                            @Override
                            public void onError(Exception e)
                            {
                                Toast.makeText(MainActivity.this, "换肤失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onComplete()
                            {
                                Toast.makeText(MainActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.id_action_plugin_skinchange:
                SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.zhy.plugin", new ISkinChangingCallback()
                {
                    @Override
                    public void onStart()
                    {
                    }

                    @Override
                    public void onError(Exception e)
                    {
                        Toast.makeText(MainActivity.this, "换肤失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete()
                    {
                        Toast.makeText(MainActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.id_action_remove_any_skin:
                SkinManager.getInstance().removeAnySkin();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
