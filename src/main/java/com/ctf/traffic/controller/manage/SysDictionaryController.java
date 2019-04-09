package com.ctf.traffic.controller.manage;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.service.*;
import io.swagger.annotations.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/s/sysDictionary")
@Api(description = "管理端系统字典接口")
public class SysDictionaryController{
    @Resource
    private SysDictionaryService dictionaryService;

    /*获取所有字典类型.*/
    @GetMapping("/person/getSysDictionaryCate")
    String listCate() {
        return new RtnJson(true, "查询成功", dictionaryService.listCate()).toString();
    }

    /*根据类型获取字典.*/
    @GetMapping("/person/getSysDictionaryByCate")
    String listDic(@RequestParam("cateCode") String code, @RequestParam("page") Integer page,
            @RequestParam("size") Integer size) {
        return new RtnJson(true, "查询成功", dictionaryService.listDicByCode(code, page, size),
                new String[] { "pageable", "sort" }).toString();
    }

    /*在对于cate内添加字典*/
    @PostMapping("/person/addSysDictionary")
    String addSysDictionary(@RequestParam("name") String name, @RequestParam("remark") String remark,
            @RequestParam("cateCode") String cateCode) {
        return new RtnJson(true, "新建成功", dictionaryService.addSysDictionary(name, remark, cateCode)).toString();
    }

    /*修改字典*/
    @PutMapping("/person/updateSysDictionary")
    String updateSysDictionary(@RequestParam("dicId") String dicId, @RequestParam("name") String name,
            @RequestParam("remark") String remark) {
        return new RtnJson(true, "修改成功", dictionaryService.updateSysDictionary(name, remark, dicId)).toString();
    }

    /*删除字典*/
    @PostMapping("/person/deleteSysDictionary")
    String deleteSysDictionary(@RequestParam("dicId") String dicId) {
        dictionaryService.deleteSysDictionary(dicId);
        return new RtnJson(true, "删除成功").toString();
    }
}
