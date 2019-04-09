package com.ctf.traffic.service.impl;

import com.ctf.traffic.po.*;
import com.ctf.traffic.po.Accident.*;
import com.ctf.traffic.service.*;
import com.ctf.traffic.util.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

/**
 * @author jiangmin
 * @Date 2018/8/7
 * @see
 */
@Service
@Slf4j
public class AccidentProcessServiceImpl implements AccidentProcessService {
    @Resource
    private AccidentService accidentService;
    @Resource
    private SysPersonService sysPersonService;
    @Override
    public synchronized CommonResponse start(Long id) {
        Accident accident=accidentService.findById(id);

        if (accident.getProcessStatus()==2){
            return new CommonResponse(false,"事故已处理完成");
        }
        if(accident.getProcessStatus()==0){
            accident.setProcessStatus(1);
            accident.setSysPerson(sysPersonService.findById(ContextThreadLocal.getPerson().getId().toString()));
            accidentService.saveOrUpdate(accident);
            return new CommonResponse(true,"开始处理");
        }
        if(accident.getProcessStatus()==1){
            //当事故状态为正在处理时，没有处理人，此条件在正式数据时不会出现
            if(accident.getSysPerson()==null){
                accident.setProcessStatus(1);
                accident.setSysPerson(sysPersonService.findById(ContextThreadLocal.getPerson().getId().toString()));
                accidentService.saveOrUpdate(accident);
                return new CommonResponse(true,"开始处理");
            }
            return accident.getSysPerson().getId()==ContextThreadLocal.getPerson().getId()?new CommonResponse(true,"继续处理"):new CommonResponse(false,"其他工作人员正在处理该事故");
        }
        return new CommonResponse(false,"事故处理状态异常");
    }

    @Override
    public CommonResponse reprocess(Long id) {
        Accident accident=accidentService.findById(id);
        accident.setProcessStatus(ProcessStatus.TO_DEAL.ordinal());
        accident.setProcessMode(ProcessMode.COORDINATE.ordinal());
        return accidentService.saveOrUpdate(accident).getId()>-1?new CommonResponse(true,"重新认定设置成功，请到待处理事故中处理。"):new CommonResponse(false,"重新认定失败。");
    }
}
