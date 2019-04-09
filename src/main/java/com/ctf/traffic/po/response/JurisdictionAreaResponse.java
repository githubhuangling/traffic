package com.ctf.traffic.po.response;

import java.util.*;

import com.ctf.traffic.po.*;

import lombok.*;

/**
 * @author ramer
 * @Date 6/27/2018
 * @see
 */
@Data
public class JurisdictionAreaResponse{
    private long id;
    private String name;
    private int level;
    private long pid;
    private List<JurisdictionAreaResponse> child;

    public JurisdictionAreaResponse(JurisdictionArea jurisdictionArea, long pid) {
        this.id = jurisdictionArea.getId();
        this.name = jurisdictionArea.getName();
        this.level = jurisdictionArea.getLevel();
        this.pid = pid;
    }
}
