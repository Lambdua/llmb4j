package com.llmb4j.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.llmb4j.exception.PyScriptException;
import jep.JepConfig;
import jep.MainInterpreter;
import jep.SharedInterpreter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 执行python脚本封装
 * @author LiangTao
 * @date 2023年04月26 13:44
 **/
@Slf4j
public class PyScript {

    private static final JepConfig jepConf = new JepConfig();

    private static final String tempJepPath = System.getProperty("java.io.tmpdir") + "jep_path.py";

    private static final String tempScriptPath = System.getProperty("java.io.tmpdir") + "llmb4j_py_script" + File.separator;

    static {
        String pyJepPath = SettingUtil.SETTING.get("python", "pyJepPath");
        String pyScriptPath = SettingUtil.SETTING.get("python", "includeScripts");

        //复制到系统的临时目录下
        URL url = ResourceUtil.getResource(pyScriptPath);
        FileUtil.copy(url.getFile(), tempScriptPath, true);
        try (InputStream is = ResourceUtil.getStream(pyJepPath); FileOutputStream out = new FileOutputStream(tempJepPath);) {
            //获取resources目录下的python脚本路径
            IoUtil.copy(is, out);
            Process p = Runtime.getRuntime().exec("python " + tempJepPath);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String jepPath = in.readLine();
                MainInterpreter.setJepLibraryPath(jepPath);
                jepConf.addIncludePaths(tempScriptPath+pyScriptPath);
                SharedInterpreter.setConfig(jepConf);
            }
        } catch (IOException e) {
            throw new PyScriptException("python 脚本初始化失败", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            log.info("程序down,开始清除临时文件");
            //删除临时文件
            FileUtil.del(tempJepPath);
            FileUtil.del(tempScriptPath);
        }));
    }


    public static int openAiTokenLen(String s, String modelName) {
        if (StrUtil.isEmpty(s)) {
            return 0;
        }
        try (SharedInterpreter sharedInterpreter = new SharedInterpreter()) {
            sharedInterpreter.exec("import JavaUtils");
            Object tokenLength = sharedInterpreter.invoke("JavaUtils.tokenLen", Map.of("ss", s, "model_name", modelName));
            return Convert.toInt(tokenLength);
        } catch (Exception e) {
            throw new PyScriptException("python 调用计算tokenLen失败", e);
        }

    }

    public static List<Integer> tokenIds(String text,String modelName){
        if (StrUtil.isEmpty(text)) {
            return Collections.emptyList();
        }
        try (SharedInterpreter sharedInterpreter = new SharedInterpreter()) {
            sharedInterpreter.exec("import JavaUtils");
            Object tokenIds = sharedInterpreter.invoke("JavaUtils.tokenIds", Map.of("text", text, "model_name", modelName));
            return Convert.toList(Integer.class,tokenIds);
        } catch (Exception e) {
            throw new PyScriptException("python 调用计算tokenLen失败", e);
        }
    }


}
