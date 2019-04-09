package com.ctf.traffic.po.sys;

import java.io.*;

public interface Entity extends Serializable{
    /** 表中的字段名，用于条件查询，不能在代码中直接写字段 */
    public static final String PROP_ID = "id";
    public static final String PROP_STATE = "state";
    public static final String PROP_CREATE_TIME = "create_time";
    public static final String PROP_UPDATE_TIME = "update_time";
    public static final int DEFAULT_PAGE_SIZE = 10;//分页每页默认条数
    public static final int STATE_VALID = 1;//正常
    public static final int STATE_INVALID = -1;//删
}
