package com.jiawa.wiki.controller;

import com.jiawa.wiki.req.UserQueryReq;
import com.jiawa.wiki.req.UserSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.UserQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){  // 加上@Valid 表明这组数据要开启校验
        CommonResp<PageResp <UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    //关于user的保存接口
    // 这个注解对应的就是axios 的 json方式的（POST）提交   用json方式的提交后端需要用RequestBody来读取
    // 如果是使用form表单的形式去提交的话 就不需要用RequestBody
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id ) {   //这样的话 Long类型的id 会自动映射 路径中的id属性
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
