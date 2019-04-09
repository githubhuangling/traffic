package com.ctf.traffic.service.impl;

import com.ctf.traffic.util.*;
import java.text.*;
import java.util.*;

import javax.annotation.*;
import javax.persistence.*;
import javax.transaction.*;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.*;
import org.springframework.util.*;

import com.ctf.traffic.po.*;
import com.ctf.traffic.po.sys.*;
import com.ctf.traffic.service.*;

import lombok.extern.slf4j.*;

@Service
@Slf4j
public class SysPersonServiceImpl implements SysPersonService{
    @Resource
    private com.ctf.traffic.repository.sys.SysPersonRepository personRepository;
    @Resource
    private com.ctf.traffic.repository.sys.SysRoleRepository roleRepository;
    @Resource
    private EntityManager em;

    @Override
    public boolean login(String empNo, String password) {
        List<Object> pwds = personRepository.findBySQL(em, "select " + SysPerson.PROP_PWD + " from "
                + SysPerson.TABLE_NAME + " where " + SysPerson.PROP_EMPNO + " = ?1", new Object[] { empNo });

        if (pwds.size() > 0) {
            if (null != pwds.get(0) && password.equals(pwds.get(0).toString())) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public void update(SysPerson o, String roleIds, String validDate1, long substationId) {
        Optional<SysPerson> opl = personRepository.findById(o.getId());
        SysPerson p;
        if (opl.isPresent()) {
            p = opl.orElse(null);
        } else {
            return;
        }

        if (o.getEmpNo() != null && o.getEmpNo().trim().length() > 0) {
            p.setEmpNo(o.getEmpNo().trim());
        }
        if (o.getName() != null && o.getName().trim().length() > 0) {
            p.setName(o.getName().trim());
        }
        if (o.getPassword() != null && o.getPassword().trim().length() > 0) {
            p.setPassword(o.getPassword().trim());
        }
        if (o.getPhone() != null && o.getPhone().trim().length() > 0) {
            p.setPhone(o.getPhone().trim());
        }
        if (o.getActive() != null && (o.getActive() == 1 || o.getActive() == 0)) {
            p.setActive(o.getActive());
        }
        if (!StringUtils.isEmpty(o.getSignaturePic())) {
            p.setSignaturePic(o.getSignaturePic());
        }
        if (o.getGender() != null && (o.getGender() == 1 || o.getGender() == 0)) {
            p.setGender(o.getGender());
        }
        if (substationId > 0) {
            p.setSubstation(new Substation(substationId));
        }
        if (roleIds != null) {
            List<SysRole> roles = new ArrayList<SysRole>();
            String[] roleIdStrs = roleIds.split(",");

            for (String id : roleIdStrs) {
                roles.add(roleRepository.findById(Long.parseLong(id)).orElse(null));
                p.setRoles(roles);
            }
        }

        if (validDate1 != null && validDate1.trim().length() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date vd = null;
            try {
                vd = sdf.parse(validDate1);
            } catch (ParseException e) {
                throw new RuntimeException("sysperson update 日期格式转换异常");
            }
            p.setValidDate(vd);
        }

        personRepository.saveOrUpdate(p);

    }

    @Override
    public List<SysPerson> findByRole(Long roleId) {
        List<Object> listo = personRepository.findByJPQL(em, "select person from " + SysPerson.class.getSimpleName()
                    + " person join person.roles role where role.id=?1", new Object[] { roleId });
        List<SysPerson> polices=new ArrayList<>();
        listo.forEach(a->{
            polices.add((SysPerson)a);
        });
        return polices;
    }

    @Override
    public SysPerson findByEmpNo(String empNo) {
        SysPerson sp = new SysPerson();
        sp.setEmpNo(empNo);
        Example<SysPerson> ex = Example.of(sp);
        Optional<SysPerson> o = personRepository.findOne(ex);
        return o.orElse(null);
    }

    @Override
    public SysPerson findById(String id) {
        return personRepository.findById(Long.parseLong(id)).orElse(null);
    }

    @Override
    public CommonResponse changepassword(String oldpassword, String newpassword) {
        Optional<SysPerson> opt = personRepository.findById(ContextThreadLocal.getPerson().getId());
        if(opt.isPresent()){
            SysPerson person = opt.get();
            log.info(" person.getPassword()!.changepassword : [{}]", person.getPassword());
            log.info(" oldpassword.changepassword : [{}]",oldpassword );
            if(!person.getPassword().equals(oldpassword)){
                return new CommonResponse(false,"原密码输入不正确");
            }
            person.setPassword(newpassword);
            boolean i=personRepository.saveOrUpdate(person).getId()>0;
            if (i){
                return new CommonResponse(true,"修改密码成功");
            }else {
                return new CommonResponse(false,"修改密码失败");
            }

        }else{
            return new CommonResponse(false,"用户不存在");
        }


    }

    @Override
    public Page<SysPerson> findByExample(SysPerson example, int page, int size) {
        return personRepository.pages(example, page, size, true);
    }

    @Override
    public Page<SysPerson> findBySubstationId(long substationId, String empNo, int page, int size) {
        return personRepository.findAll(
                (root, query, builder) -> empNo != null
                        ? builder.and(builder.equal(root.get("state"), Constant.STATE_ON),
                                builder.equal(root.get("substation_id"), substationId),
                                builder.like(root.get("empNo"), "%" + empNo + "%"))
                        : builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
                PageRequest.of(page - 1, size, Direction.DESC, "updateTime"));
    }

    @Transactional
    @Override
    public SysPerson add(SysPerson o, String roleIds, Date validDate1, long centerId) {
        List<SysRole> roles = new ArrayList<SysRole>();
        log.info(" SysPersonServiceImpl.add : [{}]", centerId);
        String[] roleIdStrs = roleIds.split(",");
        for (String id : roleIdStrs) {
            roles.add(roleRepository.findById(Long.parseLong(id)).orElse(null));
            o.setRoles(roles);
        }
        o.setActive(SysPerson.ACTIVE_TRUE);
        o.setValidDate(validDate1);
        if (centerId > 0) {
            o.setSubstation(new Substation(centerId));
        }
        return personRepository.saveOrUpdate(o);
    }

}
