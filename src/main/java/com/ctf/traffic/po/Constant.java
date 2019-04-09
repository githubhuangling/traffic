package com.ctf.traffic.po;

/**
 * 系统常量定义.
 * @author ramer
 * @Date 6/20/2018
 * @see
 */
public class Constant{
    /** 可用状态 */
    public static final int STATE_ON = 1;
    /** 不可用状态 */
    public static final int STATE_OFF = -1;
    /** 每个事故大约等待时间 */
    public static final String SYS_WAIT_TIME_CODE = "SYS_WAIT_TIME";
    /** 事故排队号 */
    public static final String ACCIDENT_SEQ_NUMBER_CODE = "ACCIDENT_SEQ_NUMBER";

    // 返回结果码
    public enum ResultCode {
        E0000("成功"), E0001("没有此保险公司或编号错误"), E0002("此保险公司已被禁用"), E0003("接口验证失败、加密方式错误或secret错误"), E0004(
                "保险信息已存在"), E0005("其他原因");
        private String desc;

        ResultCode(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    /**
     * <pre>
     *     性别:
     *     0: 男
     *     1: 女
     * </pre>
     */
    public enum Gender {
        MALE("男"), FEMALE("女");
        private String desc;

        Gender(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return this.desc;
        }
    }
}
