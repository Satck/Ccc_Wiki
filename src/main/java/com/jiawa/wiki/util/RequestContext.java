package com.jiawa.wiki.util;


import java.io.Serializable;

public class RequestContext implements Serializable {
    // 线程本地变量
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContext.remoteAddr.set(remoteAddr);
    }

}
