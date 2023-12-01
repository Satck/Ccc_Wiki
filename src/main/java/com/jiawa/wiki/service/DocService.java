package com.jiawa.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    @Autowired
    private DocMapper docMapper;



    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return   list;
    }

    /**
     * 保存
     */

    public void save (DocSaveReq req){
        Doc doc  = CopyUtil.copy(req,Doc.class);
        // 根据id判断是新增还是更新
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增

            // 生成id  id的算法 一种最简单的自增 还有一种是uuid  再就是雪花算法
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else{
            // 更新
            docMapper.updateByPrimaryKey(doc);
        }
    }


    /***
     *
     * 删除
     */

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public PageResp<DocQueryResp> list(DocQueryReq req){
        DocExample docExample = new DocExample();
        //相当于是where条件
        DocExample.Criteria criteria = docExample.createCriteria();
        docExample.setOrderByClause("sort asc");

        PageHelper.startPage(req.getPage(),req.getSize());
        // 持久层返回List<Doc> 需要转成List<DocResp> 再返回给controller
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{ }",pageInfo.getTotal());
        LOG.info("总页数：{ }",pageInfo.getPages());
//        List<DocResp> respList = new ArrayList<>();
////        将 docList中的实体转换倒DocResp当中 再将DocResp当中的实体转换到 List<DocResp> 当中
//        for(Doc doc : docList){
//            DocResp  docResp = new DocResp();
//            BeanUtils.copyProperties(doc,docResp);
//            respList.add(docResp);
//        }
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return   pageResp;
    }
}
