package com.jiawa.wiki.controller;

import com.jiawa.wiki.req.DocQueryReq;
import com.jiawa.wiki.req.DocSaveReq;
import com.jiawa.wiki.resp.CommonResp;
import com.jiawa.wiki.resp.DocQueryResp;
import com.jiawa.wiki.resp.PageResp;
import com.jiawa.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId){  // 加上@Valid 表明这组数据要开启校验
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){  // 加上@Valid 表明这组数据要开启校验
        CommonResp<PageResp <DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    //关于doc的保存接口
    // 这个注解对应的就是axios 的 json方式的（POST）提交   用json方式的提交后端需要用RequestBody来读取
    // 如果是使用form表单的形式去提交的话 就不需要用RequestBody
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr ) {   //这样的话 Long类型的id 会自动映射 路径中的id属性
        CommonResp resp = new CommonResp<>();
        List<String> content = Arrays.asList(idsStr.split(","));
        docService.delete(content );
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id){  // 加上@Valid 表明这组数据要开启校验
        CommonResp<String> resp = new CommonResp<>();
        String content  = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

}
