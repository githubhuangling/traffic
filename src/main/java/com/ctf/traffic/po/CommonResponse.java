package com.ctf.traffic.po;

import lombok.*;

/**
 * 通用JSON响应.
 * @author ramer
 * @Date 6/21/2018
 * @see
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse{
    private boolean result;
    private Object msg;
}
