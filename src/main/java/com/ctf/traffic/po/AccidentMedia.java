package com.ctf.traffic.po;

import java.util.*;

import javax.persistence.*;

import com.ctf.traffic.po.response.AccidentMediaResponse;
import com.ctf.traffic.po.sys.EntityTime;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 事故照片.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Slf4j
public class AccidentMedia extends EntityTime{
    /**
     * @see BrokenParty
     */
    private Integer part;
    /**
     * 图片位置
     */
    private String url;
    /**
     * 事故信息
     */
    @ManyToOne
    private Accident accident;
    /**
     * 行驶证
     */
    @ManyToOne
    private DrivingLicence drivingLicence;

    public AccidentMedia(int part, String url, long accident, long drivingLicence) {
        this.part = part;
        this.url = url;
        this.accident = new Accident(accident);
        this.drivingLicence = new DrivingLicence(drivingLicence);
    }

    public static Set<AccidentMediaResponse> toJson(List<AccidentMedia> medias) {
        if (medias.size() < 1)
            return new HashSet<>();
        Set<AccidentMediaResponse> parts = new HashSet<>();
        medias.forEach(media -> parts.add(new AccidentMediaResponse(BrokenParty.getDesc(media.getPart()))));
        parts.forEach(part -> medias.forEach(media -> {
            if (part.getPart().equals(BrokenParty.getDesc(media.getPart()))) {
                part.getPics().add(media.getUrl());
            }
        }));
        return parts;
    }

    /**
     * <pre>
     * 车损部位.
     *  0: 侧前方
     *  1: 侧后方
     *  2: 碰撞部位
     *  3: 其他
     * </pre>
     */
    public enum BrokenParty {
        SIDE_FRONT("侧前方"), SIDE_BEHIND("侧后方"), BOOM("碰撞部位"), OTHER("其他");
        private String desc;
        /** desc=index */
        private static Map<String, Integer> indexes;
        private static Map<Integer, String> descs;

        BrokenParty(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }

        public static int getIndex(String name) {
            return Optional.ofNullable(indexes).map(descs -> descs.get(name)).orElseGet(() -> {
                indexes = new HashMap<>();
                for (BrokenParty brokenParty : BrokenParty.class.getEnumConstants()) {
                    indexes.put(brokenParty.toString(), brokenParty.ordinal());
                }
                return indexes.get(name);
            });
        }

        public static String getDesc(int index) {
            return Optional.ofNullable(descs).map(descs -> descs.get(index)).orElseGet(() -> {
                descs = new HashMap<>();
                for (BrokenParty brokenParty : BrokenParty.class.getEnumConstants()) {
                    descs.put(brokenParty.ordinal(), brokenParty.toString());
                }
                return descs.get(index);
            });
        }

    }

}