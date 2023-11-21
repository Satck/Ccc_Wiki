package com.jiawa.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.Category;
import com.jiawa.wiki.domain.CategoryExample;
import com.jiawa.wiki.mapper.CategoryMapper;
import com.jiawa.wiki.req.CategoryQueryReq;
import com.jiawa.wiki.req.CategorySaveReq;
import com.jiawa.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;



    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public List<CategoryQueryResp> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return   list;
    }

    /**
     * 保存
     */

    public void save (CategorySaveReq req){
        Category category  = CopyUtil.copy(req,Category.class);
        // 根据id判断是新增还是更新
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增

            // 生成id  id的算法 一种最简单的自增 还有一种是uuid  再就是雪花算法
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }


    /***
     *
     * 删除
     */

    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        CategoryExample categoryExample = new CategoryExample();
        //相当于是where条件
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        categoryExample.setOrderByClause("sort asc");

        PageHelper.startPage(req.getPage(),req.getSize());
        // 持久层返回List<Category> 需要转成List<CategoryResp> 再返回给controller
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{ }",pageInfo.getTotal());
        LOG.info("总页数：{ }",pageInfo.getPages());
//        List<CategoryResp> respList = new ArrayList<>();
////        将 categoryList中的实体转换倒CategoryResp当中 再将CategoryResp当中的实体转换到 List<CategoryResp> 当中
//        for(Category category : categoryList){
//            CategoryResp  categoryResp = new CategoryResp();
//            BeanUtils.copyProperties(category,categoryResp);
//            respList.add(categoryResp);
//        }
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return   pageResp;
    }
}
