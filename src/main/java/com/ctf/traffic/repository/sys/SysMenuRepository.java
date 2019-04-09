package com.ctf.traffic.repository.sys;

import org.springframework.stereotype.Repository;

import com.ctf.traffic.po.sys.SysMenu;

@Repository
public interface SysMenuRepository extends BaseRepository<SysMenu,Long> {

	/** 被禁用，调用此方法不会对数据做任何处理 */
	@Deprecated
	@Override
	default void deleteByIds(Iterable<Long> ids) {
		
	}

	/** 被禁用，调用此方法不会对数据做任何处理 */
	@Deprecated
	@Override
	default <S extends SysMenu> S saveAndFlush(S entity) {
		return null;
	}

	/** 被禁用，调用此方法不会对数据做任何处理 */
	@Deprecated
	@Override
	default <S extends SysMenu> S save(S entity) {
		return null;
	}

	/** 被禁用，调用此方法不会对数据做任何处理 */
	@Deprecated
	@Override
	default void deleteById(Long id) {
		
	}

	/** 被禁用，调用此方法不会对数据做任何处理 */
	@Deprecated
	@Override
	default void delete(SysMenu entity) {
		
	}

}