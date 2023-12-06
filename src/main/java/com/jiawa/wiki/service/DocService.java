package com.jiawa.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Content;
import com.jiawa.wiki.domain.Doc;
import com.jiawa.wiki.domain.DocExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.ContentMapper;
import com.jiawa.wiki.mapper.DocMapper;
import com.jiawa.wiki.mapper.DocMapperCust;
import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.RedisUtil;
import com.jiawa.wiki.util.RequestContext;
import com.jiawa.wiki.util.SnowFlake;
import com.jiawa.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {

    @Autowired
    public WebSocketServer webSocketServer;

    @Autowired
    private DocMapperCust docMapperCust;

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    public WsService wsService;

    // @Resource
    // private RocketMQTemplate rocketMQTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public List<DocQueryResp> all(Long ebookId){
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
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
        Content content = CopyUtil.copy(req, Content.class);
        // 根据id判断是新增还是更新
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增
            // 生成id  id的算法 一种最简单的自增 还有一种是uuid  再就是雪花算法
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            // 更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count==0 ){
                contentMapper.insert(content);
            }
        }
    }


    /***
     *
     * 删除
     */

    public void delete(Long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String > ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
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


    public String  findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 文档阅读数+1
        docMapperCust.increaseViewCount(id);

        //一般去使用getContent（） 的时候要去判断一下是不是空的
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

        /*
        点赞
         */
        public void vote(Long id){
//            docMapperCust.increaseViewCount(id);
//            远程Ip+doc。id 作为key 24小时内不能重复
            String key = RequestContext.getRemoteAddr();
            if(redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + key ,3600*24)){
                docMapperCust.increaseVoteCount(id);
            }else{
                throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
            }
            // 推送消息4
            Doc docDb = docMapper.selectByPrimaryKey(id);
            String logId = MDC.get("LOG_ID");
            wsService.sendInfo("【" + docDb.getName() + "】被点赞！", logId);
            // rocketMQTemplate.convertAndSend("VOTE_TOPIC", "【" + docDb.getName() + "】被点赞！");
        }

        public void updateEbookInfo(){
            docMapperCust.updateEbookInfo();
        }



}
