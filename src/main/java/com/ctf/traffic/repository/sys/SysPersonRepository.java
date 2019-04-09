package com.ctf.traffic.repository.sys;

import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.sys.SysPerson;

@Repository
public interface SysPersonRepository extends BaseRepository<SysPerson,Long> {
}