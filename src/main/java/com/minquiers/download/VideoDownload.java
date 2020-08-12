package com.minquiers.download;

import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.HttpResult;
import com.arronlong.httpclientutil.common.Utils;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.minquiers.download.json.TBVideoInfo;
import com.minquiers.download.json.Video;
import com.minquiers.download.json.VideoInfo;
import com.minquiers.download.json.VideoResourceInfo;
import com.minquiers.download.util.TaoBaoUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log4j
public class VideoDownload {
    static String aId = "";
    static String ua = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";


    public static void main(String args[]) throws Exception {
        /* Utils.debug();*/
        log.info("--------------课程编号(aId/itemId):");
        aId = new Scanner(System.in).nextLine().trim();
        log.info("--------------cookie:");
        while (!TaoBaoUtil.checkCookie(TaoBaoUtil.cookie = new Scanner(System.in).nextLine().trim())) {
            System.out.println("cookie不正确，需要找【h5.m.taobao.com】cookie中间包含【_m_h5_tk=】的数据,请重新输入:");
        }
        log.info("--------------开始获取------------------");
        String coursePageRes = HttpClientUtil.get(HttpConfig.custom().url(TaoBaoUtil.aIdPage + aId).headers(HttpHeader.custom().userAgent(ua).cookie(TaoBaoUtil.cookie).build()));
        Document document = Jsoup.parse(coursePageRes);
        String script = document.select("script").get(6).data().trim().replaceAll("tx_learn_config.isRecord = true;", "").trim().replaceAll("var tx_learn_config  = ", "");
        script = script.substring(0, script.length() - 1);
        Video video = JSONObject.parseObject(script, Video.class);
        List<VideoInfo> videoInfoList = new ArrayList<>();
        video.getOutline().getChapters().forEach(e -> {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoCategoryName(e.getTitle());
            videoInfo.setCourseId(e.getCourseId());
            e.getSections().forEach(x -> {
                String resourceId = x.getResources().get(0).getId();
                String resourceName = x.getTitle();
                VideoResourceInfo videoResourceInfo = new VideoResourceInfo();
                videoResourceInfo.setResourceId(resourceId);
                videoResourceInfo.setResourceName(resourceName);
                videoInfo.addResourceId(videoResourceInfo);
            });
            videoInfoList.add(videoInfo);
        });

        videoInfoList.forEach(x -> {
            x.getVideoResourceInfos().forEach(c -> {
                try {
                    String url = String.format(TaoBaoUtil.videoInfoUrl,
                            TaoBaoUtil.appKey,
                            TaoBaoUtil.getT(true),
                            TaoBaoUtil.getSign(x.getCourseId(), c.getResourceId()),
                            x.getCourseId(),
                            c.getResourceId()
                    );
                    String urlPre = url.substring(0, url.lastIndexOf("=") + 1);
                    String urlsuf = url.substring(url.lastIndexOf("=") + 1);
                    urlsuf = URLEncoder.encode(urlsuf, "UTF-8");
                    url = urlPre + urlsuf;
                    log.info(url);
                    String videoInfoRes = HttpClientUtil.get(HttpConfig.custom()
                            .url(url)
                            .headers(HttpHeader
                                    .custom()
                                    .userAgent(ua)
                                    .cookie(TaoBaoUtil.cookie)
                                    .build())).trim();
                    videoInfoRes = videoInfoRes.substring(0, videoInfoRes.length() - 1).replaceAll("mtopjsonp2\\(", "");
                    TBVideoInfo tbVideoInfo = JSONObject.parseObject(videoInfoRes, TBVideoInfo.class);
                    c.setDownloadUrl(tbVideoInfo.getData().getData().getResource().getExtObj().getVideoPlayInfo().getAndroidPhoneV23Url().getHd() +
                            "?" +
                            TaoBaoUtil.auth_key_str +
                            tbVideoInfo.getData().getData().getResource().getAuthority().getAuthKey() +
                            "&" +
                            TaoBaoUtil.hardware
                    );
                    HttpConfig httpConfig = HttpConfig.custom().timeout(RequestConfig.custom().setRedirectsEnabled(false).build()).url(c.getDownloadUrl()).headers(HttpHeader.custom().userAgent(ua).cookie(TaoBaoUtil.cookie).build(), true);
                    HttpResult httpResult = HttpClientUtil.sendAndGetResp(httpConfig);
                    if (null != httpResult && null != httpResult.getHeaders("Location")) {
                        c.setDownloadUrl(httpResult.getHeaders("Location").getValue());
                    }
                    log.info(JSONObject.toJSONString(c));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        });
        log.info("\r\n\r\n----------------------");
        log.info(JSONObject.toJSONString(videoInfoList));
    }
}
