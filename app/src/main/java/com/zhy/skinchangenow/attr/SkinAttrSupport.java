package com.zhy.skinchangenow.attr;

import android.content.Context;
import android.util.AttributeSet;

import com.zhy.skinchangenow.utils.L;
import com.zhy.skinchangenow.constant.SkinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 15/9/23.
 */
public class SkinAttrSupport
{
    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context)
    {
        List<SkinAttr> skinAttrs = new ArrayList<SkinAttr>();
        SkinAttr skinAttr = null;
        for (int i = 0; i < attrs.getAttributeCount(); i++)
        {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);


            if (attrName.equals(SkinConfig.ATTR_BACKGROUND))
            {
                if (attrValue.startsWith("@"))
                {
                    int id = Integer.parseInt(attrValue.substring(1));
                    String entryName = context.getResources().getResourceEntryName(id);
                    String typeName = context.getResources().getResourceTypeName(id);

                    if(entryName.startsWith("skin"))
                    {
                        L.e(entryName + " , " + typeName);
                        skinAttr = new BackgroundAttr(entryName, typeName);
                        skinAttrs.add(skinAttr);
                    }


                }
            } else if (attrName.equals(SkinConfig.ATTR_TEXTCOLOR))
            {
                if (attrValue.startsWith("@"))
                {
                    int id = Integer.parseInt(attrValue.substring(1));
                    String entryName = context.getResources().getResourceEntryName(id);
                    String typeName = context.getResources().getResourceTypeName(id);

                    if(entryName.startsWith("skin"))
                    {
                        L.e(entryName+" , " + typeName);
                        skinAttr = new TextColorAttr(entryName, typeName);
                        skinAttrs.add(skinAttr);
                    }

                }
            }
        }
        return skinAttrs;

    }

}
