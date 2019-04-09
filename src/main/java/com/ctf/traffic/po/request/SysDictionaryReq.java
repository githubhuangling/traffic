package com.ctf.traffic.po.request;

import lombok.Data;

/**
 * 系统字典请求对象.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Data
public class SysDictionaryReq{
    private String name;
    private String remark;
    private Integer dicId;

}
