package com.llmb.models.base;

import lombok.Data;

import static com.llmb.util.SettingUtil.SETTING;

/**
 * @author LiangTao
 * @date 2023年05月29 11:39
 **/
@Data
public class PoolProperties {

    /**
     * 最大池大小,默认系统核心数*2
     */
    private  Integer maxSize = Runtime.getRuntime().availableProcessors() * 2;

    private  Integer maxRequest = 100;

    private  Integer maxRequestPerHost = 20;

    /**
     * 空闲线程存活时间,单位s,默认60s
     */
    private Integer keepAliveTime = 30;

    public PoolProperties() {
        if (SETTING.containsKey("httpPool", "maxSize")) {
            maxSize = SETTING.getInt("maxSize", "httpPool");
        }

        if (SETTING.containsKey("httpPool", "maxRequest")) {
            maxRequest = SETTING.getInt("maxRequest", "httpPool");
        }
        if (SETTING.containsKey("httpPool", "maxRequestPerHost")) {
            maxRequestPerHost = SETTING.getInt("maxRequestPerHost", "httpPool");
        }
    }

}
