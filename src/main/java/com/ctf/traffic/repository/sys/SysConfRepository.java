package com.ctf.traffic.repository.sys;

import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import com.ctf.traffic.po.sys.*;

@Repository
public interface SysConfRepository extends BaseRepository<SysConf, Long>{
    SysConf findByCode(String code);

    /** 根据code前缀查找记录，如code_prefix为SYS_, 那么查找到的是所有code前缀为SYS_的记录 */
    @Query(value = "select * from " + SysConf.TABLE_NAME + " where " + SysConf.PROP_CODE
            + " like ?1", nativeQuery = true)
    public List<SysConf> findLikeCodeStart(String code_prefix);

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteByIds(Iterable<Long> ids) {

    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteById(Long id) {

    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void delete(SysConf entity) {

    }

}