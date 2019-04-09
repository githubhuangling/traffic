package com.ctf.traffic.po.response;

import java.util.*;

import lombok.*;

/**
 * @author ramer
 * @Date 7/2/2018
 * @see
 */
@Data
public class AccidentMediaResponse{
    private String part;
    private List<String> pics = new ArrayList<>();

    public AccidentMediaResponse(String part) {
        this.part = part;
    }
}
