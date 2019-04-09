package com.ctf.traffic.service;

import com.ctf.traffic.po.*;
import java.util.*;

import org.springframework.data.domain.*;

import com.ctf.traffic.po.sys.*;

public interface SysPersonService{
    boolean login(String empNo, String password);

    SysPerson add(SysPerson o, String roleIds, Date validDate1, long substationId) throws Exception;

    void update(SysPerson o, String roleIds, String validDate1, long substationId);

    List<SysPerson> findByRole(Long RoleId) ;

    SysPerson findByEmpNo(String empNo);

    SysPerson findById(String id);

    CommonResponse changepassword(String oldpassword,String newpassword);

    /**
     *
     * @param example 条件
     * @param page
     * @param size
     * @return
     */
    Page<SysPerson> findByExample(SysPerson example, int page, int size);

    Page<SysPerson> findBySubstationId(long ccId, String empNo, int page, int size);

}
