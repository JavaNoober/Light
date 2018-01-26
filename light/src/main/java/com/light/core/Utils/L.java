package com.light.core.Utils;

import android.util.Log;

import com.light.body.LightConfig;

import java.util.ArrayList;

public class L {
    private static final String TAG = "Light";
	private static final String LINE="-------------------------------";

	private static final boolean isDebug = LightConfig.isDebug;
    public static void e(String tag, String msg){
        if(isDebug){
            Log.e(tag, msg);
        }
    }
    public static void d(String tag, String msg){
	    if(isDebug){
		    Log.d(tag, msg);
	    }
    }
    public static void v(String tag, String msg){
	    if(isDebug){
		    Log.v(tag, msg);
	    }
    }
    public static void w(String tag, String msg){
	    if(isDebug){
		    Log.w(tag, msg);
	    }
    }
    public static void i(String tag, String msg){
	    if(isDebug){
		    Log.i(tag, msg);
	    }
    }

    public static void e(String msg){
	    if(isDebug){
		    Log.e(TAG, msg);
	    }
    }
    public static void d(String msg){
	    if(isDebug){
		    Log.d(TAG, msg);
	    }
    }
    public static void v(String msg){
	    if(isDebug){
		    Log.v(TAG, msg);
	    }
    }
    public static void w(String msg){
	    if(isDebug){
		    Log.w(TAG, msg);
	    }
    }
    public static void i(String msg){
	    if(isDebug){
		    Log.i(TAG, msg);
	    }
    }
    public static void logString(Object oj,String msg){
        if(!isDebug)
            return ;
        if(oj==null)Log.e("NULL",LINE);
        else{
            String cN=oj.getClass().getName();
            String[] cNs=null;
            if(cN==null||cN.equals(""))Log.e("Name",LINE);
            else{
                if(cN.contains(".")){
                    cNs=cN.split("\\.");
                    cN=cNs[cNs.length-1];
                }
                if(cN.contains("\\$")){
                    cNs=cN.split("\\$");
                    cN=cNs[0];
                }
                if (cN.equals("String")) {
            		Log.e(oj.toString(), msg);
            		return;
				}
                Log.e(cN, msg);
            }
        }
    }
    public static void logArray(Object oj,String msg,ArrayList<String> aList){
    	if(oj==null)Log.i("NULL",LINE);
    	else{
    		 String cN=oj.getClass().getName();
    		  String[] cNs=null;
             if(cN==null||cN.equals(""))Log.i("Name",LINE);
             else{
            	 if(cN.contains(".")){
                     cNs=cN.split("\\.");
                     cN=cNs[cNs.length-1];
                 }
                 if(cN.contains("\\$")){
                     cNs=cN.split("\\$");
                     cN=cNs[0];
                 }
                 if(aList==null){
                	 Log.e(cN, msg+".List=NULL"); 
                 }else{
                	
                	 Log.e(cN, msg+"--------List!=null-----start");
                	 if(aList.size()==0){
                		 Log.e(cN,"--List.size="+0+"------end ");
                		 return;
                	 }
                	 for(int i=0;i<aList.size();i++){
                		 String s=aList.get(i);
                		 Log.e(cN,"list"+"_"+i+s);
                	 }
                	 Log.e(cN,"--List.size="+aList.size()+"------end ");
                 }
             }
    	}
    }
    public static void logString(Class c,String msg){
    	if(!isDebug)return;
    	if(c==null)Log.i("NULL",LINE);
    	else 
    	Log.e(c.getName(), msg);
    }
}
