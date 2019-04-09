package com.ctf.traffic.util;

import com.alibaba.fastjson.*;
import java.util.*;
import javax.annotation.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import okhttp3.Request.*;
import okhttp3.ResponseBody;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

/**
 * @author ramer
 * @Date 6/28/2018
 * @see
 */
@Component
@Slf4j
public class HttpUtils{
    private static OkHttpClient okHttpClient;

    @Resource
    public void setOkHttpClient(OkHttpClient okHttpClient) {
    		HttpUtils.okHttpClient = okHttpClient;
    }

    public static <T> T request(String url, Map<String, String> data, RequestMethod requestMethod, Class<T> clazz) {
        log.debug(" HttpUtils.request : [{},{},{}]", url, JSON.toJSONString(data), requestMethod);
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (data != null && data.size() > 0) {
            boolean isGetMethod = requestMethod.equals(RequestMethod.GET);
            StringBuilder urlBuilder = new StringBuilder(url);
            String finalUrl = url;
            data.forEach((key, val) -> {
                formBuilder.add(key, val);
                if (isGetMethod) {
                    if (urlBuilder.length() == finalUrl.length()) {
                        urlBuilder.append("?");
                    } else {
                        urlBuilder.append("&");
                    }
                    urlBuilder.append(key).append("=").append(val);
                }
            });
            url = urlBuilder.toString();
        }
        FormBody requestBody = formBuilder.build();
        Builder builder = new Request.Builder().url(url);
        switch (requestMethod) {
        case GET:
            builder.get();
            break;
        case POST:
            builder.post(requestBody);
            break;
        case PUT:
            builder.put(requestBody);
            break;
        case DELETE:
            builder.delete(requestBody);
            break;
        default:
            log.error("request method {} is not supported", requestMethod);
        }
        String result=null;
        try {
            ResponseBody body = okHttpClient.newCall(builder.build()).execute().body();
            result = body.string();
            log.info(" HttpUtils.request : response [{}]", result);
            return JSON.parseObject(result, clazz);
        } catch (Exception e) {
            if(e instanceof JSONException){
                return (T) result;
            }
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
