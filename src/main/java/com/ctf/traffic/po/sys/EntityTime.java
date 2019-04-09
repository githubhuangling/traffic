package com.ctf.traffic.po.sys;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;
import com.ctf.traffic.po.sys.Entity;

/**
 * 持久类的公有父类，定义了公有的属性，简化了持久类的书写
 * */
@MappedSuperclass
@Data
public class EntityTime implements Entity{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "tinyint default 1")
    private Integer state = 1;//状态：1正常 -1删除
    @CreationTimestamp
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;//创建时间
    @UpdateTimestamp
    @Column(columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date updateTime;//修改时间
}
