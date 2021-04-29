package com.navercorp.pinpoint.web.alarm.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengxgs on 2018/5/29.
 */
public class WxNotifyUtils {

    private static String corpid = "wx6810a1001ac73cec";
    private static String corpsecret = "OcPsQwhN8kt9ko3CA9tJeK6ErW3rWuo4vG857hg44qYgiRBp5_1JCjvl-N_lzOQT";
    private static String agentId = "1";


    public static void main(String[] args) {
        sendMsg("16", "出错");
    }

    public static void sendMsg(final String recever, final String content) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + getAccessToken();
        String ctype = "application/x-www-form-urlencoded;charset=UTF-8";
        try {
            String resp = WebUtils.doPost(url, ctype, getMsg(recever, content).getBytes("utf-8"), 2000, 2000);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getAccessToken() {
        try {
            String resp = WebUtils.doGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret, null);
            JSONObject json = JSONObject.parseObject(resp);
            return json.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMsg(String recever, String ss) {
        Map<String, Object> msg = new HashMap<>(5);
        msg.put("touser", recever);
        msg.put("msgtype", "text");
        msg.put("agentid", agentId);
        msg.put("safe", "0");
        Map<String, String> content = new HashMap<>(1);
        content.put("content", ss);
        msg.put("text", content);
        return JSON.toJSONString(msg);
    }
}