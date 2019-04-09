package com.ctf.traffic.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ctf.traffic.po.Constant;
import com.ctf.traffic.po.RoadSectionCode;
import com.ctf.traffic.repository.RoadSectionCodeRepository;
import com.ctf.traffic.service.RoadSectionCodeService;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Service
public class RoadSectionCodeServiceImpl implements RoadSectionCodeService {
	@Resource
	private RoadSectionCodeRepository repository;

	@Override
	public List<RoadSectionCode> findByDldm(String xzqh, String dldm, String ldmc) {
		return repository.findAll(
				(root, query, builder) -> builder.and(
						builder.equal(root.get("state"), Constant.STATE_ON),
						null!=xzqh ? builder.equal(root.get("xzqh"), xzqh) : builder.equal(root.get("state"), Constant.STATE_ON),
						builder.and(builder.equal(root.get("dldm"), dldm)),
						null!=ldmc ? builder.like(root.get("ldmc"), "%" + ldmc + "%") : builder.equal(root.get("state"), Constant.STATE_ON)
				), 
				Sort.by(Direction.ASC, "id")
		);
	}
}
