package com.ctf.traffic.po.response;

import java.util.*;

import lombok.*;

/**
 * @author ramer
 * @Date 7/2/2018
 * @see
 */
@Data
@EqualsAndHashCode(exclude = { "id", "url", "description", "pics" })
@NoArgsConstructor
public class AccidentReasonResponse{
    private Long id;
    private String name;
    private String url;
    /*描述; 形容; 种类; 类型*/
    private String description;
    List<AccidentReasonResponse> pics = new ArrayList<>();

    public AccidentReasonResponse(Long id, String name, String url, String description) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
    }

}
