package com.jiawa.wiki.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.wiki.domain.User;
import com.jiawa.wiki.domain.UserExample;
import com.jiawa.wiki.exception.BusinessException;
import com.jiawa.wiki.exception.BusinessExceptionCode;
import com.jiawa.wiki.mapper.UserMapper;
import com.jiawa.wiki.req.UserLoginReq;
import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserResetPasswordReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.resp.UserLoginResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.util.CopyUtil;
import com.jiawa.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;



    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq req){
        UserExample userExample = new UserExample();
        //相当于是where条件
        UserExample.Criteria criteria = userExample.createCriteria();
        // 动态sql
        if(!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        // 打印日志
        // 这个pageHelper只对遇到的第一个sql语句 有作用
        PageHelper.startPage(req.getPage(),req.getSize());
        // 持久层返回List<User> 需要转成List<UserResp> 再返回给controller
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{ }",pageInfo.getTotal());
        LOG.info("总页数：{ }",pageInfo.getPages());
//        List<UserResp> respList = new ArrayList<>();
////        将 userList中的实体转换倒UserResp当中 再将UserResp当中的实体转换到 List<UserResp> 当中
//        for(User user : userList){
//            UserResp  userResp = new UserResp();
//            BeanUtils.copyProperties(user,userResp);
//            respList.add(userResp);
//        }
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return   pageResp;
    }

    /**
     * 保存
     */

    public void save (UserSaveReq req){
        User user  = CopyUtil.copy(req,User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            User userDB = selectByLoginName(req.getLoginName());
            if(ObjectUtils.isEmpty(userDB)){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else {
                // 用户名已经存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        }else{
            // 更新
            // 将用户名清空
            user.setLoginName(null);
            // 将密码清空给
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
            // 这个是指user里面有值 采取更新 没有不更新
        }
    }

    /***
     *
     * 删除
     */
    public void delete(Long id){
        userMapper.deleteByPrimaryKey(id);
    }
    public User selectByLoginName(String LoginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }


    /**
     * 重置密码
     */

    public void resetPassword (UserResetPasswordReq req){
        User user  = CopyUtil.copy(req,User.class);
        userMapper.updateByPrimaryKeySelective(user);
        }


    /**
     *登录
     */

    public UserLoginResp login  (UserLoginReq req){
        User userDb = selectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(userDb)){
            // 用户名不存在
            LOG.info("用户名不存在，{}",req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else {
            if(userDb.getPassword().equals(req.getPassword())){
                // 登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDb,UserLoginResp.class);
                return userLoginResp;
            }else{
                // 密码不对
                LOG.info("密码不对，输入密码:{},数据库密码：{}",req.getLoginName(),userDb.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);

            }
        }
    }

}
