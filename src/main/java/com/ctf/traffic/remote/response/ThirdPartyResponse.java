package com.ctf.traffic.remote.response;

import com.ctf.traffic.po.Constant.*;

import lombok.*;

/**
 * @author ramer
 * @Date 7/26/2018
 * @see
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyResponse{
    private boolean success;
    private String message;
    private String errcode;
    private Object data;

    public ThirdPartyResponse(boolean success, ResultCode resultCode) {
        this.success = success;
        this.message = resultCode.toString();
        this.errcode = resultCode.name();
    }
}
