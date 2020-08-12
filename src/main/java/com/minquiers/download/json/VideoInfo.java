package com.minquiers.download.json;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideoInfo {
    private String videoCategoryName;
    private String courseId;
    private List<VideoResourceInfo> videoResourceInfos;

    public void addResourceId(VideoResourceInfo videoResourceInfo) {
        if (null == videoResourceInfos) {
            videoResourceInfos = new ArrayList<>();
        }
        videoResourceInfos.add(videoResourceInfo);
    }
}
