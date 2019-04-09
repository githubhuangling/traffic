package com.ctf.traffic.po.sys;

import net.sf.json.*;

public class RtnJson{
    private static final String SUCCESS = "success";
    private static final String MSG = "msg";
    private static final String CONTENT = "content";

    private JSONObject json = new JSONObject();

    /**
     * @param success 返回成功与否
     * @param msg 返回消息
     * */
    public RtnJson(boolean success, String msg) {
        init(success, msg, null, null);
    }

    /**
     * @param success 返回成功与否
     * @param msg 返回消息
     * @param content 返回数据
     * */
    public RtnJson(boolean success, String msg, Object content) {
        init(success, msg, content, null);
    }

    /**
     * @param success 返回成功与否
     * @param msg 返回消息
     * @param content 返回数据
     * @param props 返回数据中会被过滤的属性，用JsonConfig实现
     * */
    public RtnJson(boolean success, String msg, Object content, String[] props) {
        init(success, msg, content, props);
    }

    private void init(boolean success, String msg, Object content, String[] props) {
        json.accumulate(SUCCESS, success);
        json.accumulate(MSG, msg);
        if (null != content) {
            if (null != props && props.length > 0) {
                JsonConfig config = new JsonConfig();
                config.setExcludes(props);
                json.accumulate(CONTENT,
                        (content instanceof java.util.Collection) ? JSONArray.fromObject(content, config)
                                : JSONObject.fromObject(content, config));
            } else {
                json.accumulate(CONTENT, (content instanceof java.util.Collection) ? JSONArray.fromObject(content)
                        : JSONObject.fromObject(content));
            }
        }
    }

    /**
     * 向返回的json中添加更多内容
     * */
    public JSONObject put(String key, Object value) {
        json.accumulate(key, value);
        return json;
    }

    /**
     * 返回当前对像的json字符串
     * */
    public String toString() {
        return json.toString();
    }

}
