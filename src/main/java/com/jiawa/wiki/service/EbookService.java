package com.jiawa.wiki.service;


import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookReq;
import com.jiawa.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class    EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        //相当于是where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 模糊查询
        criteria.andNameLike("%" + req.getName() + "%");
        // 持久层返回List<Ebook> 需要转成List<EbookResp> 再返回给controller
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> respList = new ArrayList<>();
//        将 ebookList中的实体转换倒EbookResp当中 再将EbookResp当中的实体转换到 List<EbookResp> 当中
        for(Ebook ebook : ebookList){
            EbookResp  ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return  respList;
    }

}
