package com.minquiers.download.util;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

@Log4j
public class TaoBaoUtil {

    public static String cookie = "";
    public static String videoInfoUrl = "https://h5api.m.taobao.com/h5/mtop.lifemallweb.courseinfomtopservice.getresource/1.0/?jsv=2.4.11&appKey=%s&t=%s&sign=%s&v=1.0&api=mtop.lifemallweb.courseinfomtopservice.getresource&type=jsonp&dataType=jsonp&callback=mtopjsonp2&data={\"courseId\":\"%s\",\"resourceId\":\"%s\"}";
    public static String aIdPage = "http://v.xue.taobao.com/learn.htm?itemId=";
    public static Long t;
    public static String appKey = "12574478";
    public static String auth_key_str = "auth_key=";
    public static String hardware = "hardware=true";

    public static String getToken(String cookie) {
        Pattern pattern = Pattern.compile("_m_h5_tk=\\S+;");
        java.util.regex.Matcher matcher = pattern.matcher(cookie);
        if (matcher.find()) {
            return matcher.group(0).split("=")[1].split("_")[0];
        }
        return null;
    }

    public static Long getT(boolean refresh) {
        if (null == t || refresh) {
            t = new Date().getTime();
        }
        return t;
    }

    public static String getSign(String courseId, String resourceId) {
        try {
            String token = getToken(cookie);
            Long t = getT(false);
            log.info("token - " + token);
            log.info("t - " + t);
            return MD5Utils.MD5_32bit_lower(token + "&" + t + "&" + appKey + "&" + "{\"courseId\":\"" + courseId + "\",\"resourceId\":\"" + resourceId + "\"}");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String args[]) throws HttpProcessException, UnsupportedEncodingException {
        String a = "";
        log.info(URLEncoder.encode(String.format(a , 1 , 2) , "UTF-8"));

    }

    public static boolean checkCookie(String s) {
        if(StringUtils.isNotBlank(getToken(s))){
            return true;
        }
        return false;
    }
}
