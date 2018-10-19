package com.qinglan.sdk.android.demo;

import android.content.Context;

/**
 * Created by engine on 15/3/6.
 */
public class ResUtil {
    public static int getId(Context context,String name){
        return  context.getResources().getIdentifier(name,"id",context.getPackageName());
    }
    public static int getDrawable(Context context,String name){
       return context.getResources().getIdentifier(name,"drawable",context.getPackageName()) ;
    }

    public static int getLayout(Context context,String name){
        return  context.getResources().getIdentifier(name,"layout",context.getPackageName());
    }

    public static int getStyle(Context context,String name){
        return  context.getResources().getIdentifier(name,"style",context.getPackageName());
    }
}
