package com.jiawa.wiki.resp;


import java.util.List;

// 用于请求参数的封装
public class PageResp<T> {
    private  long  total ;
    // 对于不确定的类型使用泛型
    private List<T>  list  ;


    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}