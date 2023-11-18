package com.jiawa.wiki.req;


// 用于请求参数的封装
public class PageReq {
    private  int page ;

    private int size ;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageReq{");
        sb.append("page=").append(page);
        sb.append(",size=").append(size);
        sb.append('}');
        return sb.toString();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}