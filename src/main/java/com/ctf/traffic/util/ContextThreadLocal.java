package com.ctf.traffic.util;

import com.ctf.traffic.po.sys.SysPerson;

public class ContextThreadLocal {

	/**
     * 当前管理员
     */
    private static final ThreadLocal<SysPerson> personLocal = new ThreadLocal<SysPerson>(); 

	/**
     * 第三方数据urldecode后的值
     */
    private static final ThreadLocal<String> dataLocal = new ThreadLocal<String>();

    /**
     * Clear all cached values
     */
    public static void clearAll(){
    		personLocal.set(null);
    		dataLocal.set(null);
    }
    
    public static void setData(String data) {
    		dataLocal.set(data);
    }
    
    public static String getData() {
    		return dataLocal.get();
    }
    
    public static void setPerson(SysPerson person){
    		personLocal.set(person);
    }
    public static SysPerson getPerson(){
    		return (SysPerson)personLocal.get();
    }
    
}
