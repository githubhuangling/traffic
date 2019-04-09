package com.ctf.traffic.repository.sys;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Query;

import org.hibernate.*;
import org.hibernate.transform.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.*;

import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.po.sys.Entity;

/**
 * 
 * */
@NoRepositoryBean
public interface BaseRepository<T extends EntityTime, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{

    default <S extends T> S saveOrUpdate(S entity) {
        Date time = new Date();
        if (null == entity.getId()) {
            entity.setState(Entity.STATE_VALID);
            entity.setCreateTime(time);
        }
        entity.setUpdateTime(time);
        return saveAndFlush(entity);
    }

    @Override
    default void deleteById(ID id) {
        T entity = findById(id).orElse(null);;
        entity.setState(Entity.STATE_INVALID);
        entity.setUpdateTime(new Date());
        saveAndFlush(entity);
    }

    @Override
    default void delete(T entity) {
        entity.setState(Entity.STATE_INVALID);
        entity.setUpdateTime(new Date());
        saveAndFlush(entity);
    }

    default void deleteByIds(Iterable<ID> ids) {
        long time = System.currentTimeMillis();
        List<T> ts = findAllById(ids);
        for (T t : ts) {
            t.setState(Entity.STATE_INVALID);
            t.setCreateTime(new Date());
            saveAndFlush(t);
        }
    }

    /**
     * 根据条件分页取得对象
     * @param example 过滤条件(非空)
     * @param page 第几页(从1开始)
     * @param size 每页数量(默认10条)
     * @param valid 是否只取未被删除的(取所有的传null值,取未删取的true，取已删除的取false)
     * */
    default Page<T> pages(T example, Integer page, Integer size, Boolean valid) {
        if (example == null)
            throw new RuntimeException("example can not be null");
        if (page == null || page <= 0)
            page = 1;
        if (size == null || size <= 0)
            size = Entity.DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page - 1, size);
        if (valid == null) {

        } else if (valid) {
            example.setState(EntityTime.STATE_VALID);
        } else {
            example.setState(EntityTime.STATE_INVALID);
        }
        Example<T> ex = Example.of(example);
        return findAll(ex, pageable);
    }

    /**
     * 根据条件分页取得对象
     * @param example 过滤条件(非空)
     * @param page 第几页(从1开始)
     * @param matcher 模糊查询条件
     * @param size 每页数量(默认10条)
     * @param valid 是否只取未被删除的(取所有的传null值,取未删取的true，取已删除的取false)
     * */
    default Page<T> pages(T example, ExampleMatcher matcher, Integer page, Integer size, Boolean valid) {
        if (example == null)
            throw new RuntimeException("example can not be null");
        if (page == null || page <= 0)
            page = 1;
        if (size == null || size <= 0)
            size = Entity.DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page - 1, size);
        if (valid == null) {

        } else if (valid) {
            example.setState(EntityTime.STATE_VALID);
        } else {
            example.setState(EntityTime.STATE_INVALID);
        }
        Example<T> ex = Example.of(example, matcher);
        return findAll(ex, pageable);
    }

    /**
     * 根据条件分页取得对象
     * @param example 过滤条件
     * @param page 第几页(从1开始)
     * @param size 每页数量(默认10条)
     * */
    default Page<T> pages(Example<T> example, Integer page, Integer size) {
        if (page == null || page <= 0)
            page = 1;
        if (size == null || size <= 0)
            size = Entity.DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(page - 1, size);
        return findAll(example, pageable);
    }

    /**
     * 根据sql查询记录
     * @param em EntityManager 请在调用类中注解注入
     * @param sql 带参数的sql语句，如：select * from table where type=?1 name like ?2
     * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
     * */
    @SuppressWarnings("unchecked")
    default List<Object> findBySQL(EntityManager em, String sql, Object[] params) {
        Query q = em.createNativeQuery(sql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        return q.getResultList();
    }

    /**
     * 根据jpql查询记录
     * @param em EntityManager 请在调用类中注解注入
     * @param jpql 带参数的jpql语句，如：select * from table where type=?1 name like ?2
     * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
     * */
    @SuppressWarnings("unchecked")
    default List<Object> findByJPQL(EntityManager em, String jpql, Object[] params) {
        Query q = em.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        return q.getResultList();
    }

    /**
     * 执行sql语句
     * @param em EntityManager 请在调用类中注解注入
     * @param sql 带参数的sql语句，如：update table set type = ?1,name = ?2
     * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张三"}
     * */
    default void executeBySQL(EntityManager em, String sql, Object[] params) {
        Query q = em.createNativeQuery(sql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        q.executeUpdate();
    }

    /**
     * 执行jpql语句
     * @param em EntityManager 请在调用类中注解注入
     * @param jpql 带参数的jpql语句，如：update table set type = ?1,name = ?2
     * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张三"}
     * */
    default void executeByJPQL(EntityManager em, String jpql, Object[] params) {
        Query q = em.createQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        q.executeUpdate();
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default <S extends T> S save(S entity) {
        return null;
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default <S extends T> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteAll(Iterable<? extends T> entities) {
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteAll() {
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteInBatch(Iterable<T> entities) {
    }

    /** 被禁用，调用此方法不会对数据做任何处理 */
    @Deprecated
    @Override
    default void deleteAllInBatch() {
    }

    /**
     * 根据sql查询记录
     * @param em EntityManager 请在调用类中注解注入
     * @param sql 带参数的sql语句，如：select * from table where type=?1 name like ?2
     * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
     * */
    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    default List<Map<String, Object>> findBySQLToMap(EntityManager em, String sql, Object[] params) {
        Query q = em.createNativeQuery(sql);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List rows = q.getResultList();
        List retList = new ArrayList();
        for (Object obj : rows) {
            retList.add((Map) obj);
        }
        return retList;
    }
}