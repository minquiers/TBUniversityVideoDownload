package com.minquiers.download.json;

import lombok.Data;

import java.util.List;

@Data
public class Section {
    private List<Resource> resources;
    private String title;
}
