package com.minquiers.download.json;

import lombok.Data;

import java.util.List;

@Data
public class Chapter {
    private List<Section> sections;
    private String title;
    private String courseId;
}
