package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Ebook;
import com.jiawa.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;
    @GetMapping("/list")
    public List<Ebook> list(){
        return ebookService.list();
    }

}
