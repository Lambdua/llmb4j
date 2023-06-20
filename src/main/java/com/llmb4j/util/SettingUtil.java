package com.llmb4j.util;

import cn.hutool.setting.Setting;

/**
 * @author LiangTao
 * @date 2023年05月29 13:49
 **/
public class SettingUtil {
    private SettingUtil() {
        throw new IllegalStateException("工具类，不允许实例化");
    }

    public static final Setting SETTING = new Setting("config.setting");

    public static final Setting PROMPT = new Setting("prompt.setting");


}
