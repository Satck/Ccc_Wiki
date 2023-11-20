package com.jiawa.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.domain.EbookExample;
import com.jiawa.wiki.mapper.EbookMapper;
import com.jiawa.wiki.req.EbookQueryReq;
import com.jiawa.wiki.req.EbookSaveReq;
import com.jiawa.wiki.resp.EbookQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class    EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        EbookExample ebookExample = new EbookExample();
        //相当于是where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // 动态sql
        if(!ObjectUtils.isEmpty(req.getName())){
        criteria.andNameLike("%" + req.getName() + "%");
        }
        // 打印日志
        // 这个pageHelper只对遇到的第一个sql语句 有作用
        PageHelper.startPage(req.getPage(),req.getSize());
        // 持久层返回List<Ebook> 需要转成List<EbookResp> 再返回给controller
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{ }",pageInfo.getTotal());
        LOG.info("总页数：{ }",pageInfo.getPages());
//        List<EbookResp> respList = new ArrayList<>();
////        将 ebookList中的实体转换倒EbookResp当中 再将EbookResp当中的实体转换到 List<EbookResp> 当中
//        for(Ebook ebook : ebookList){
//            EbookResp  ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook,ebookResp);
//            respList.add(ebookResp);
//        }
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return   pageResp;
    }

    /**
     * 保存
     */

    public void save (EbookSaveReq req){
        Ebook ebook  = CopyUtil.copy(req,Ebook.class);
        // 根据id判断是新增还是更新
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增
            ebookMapper.insert(ebook);
        }else{
            // 更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
