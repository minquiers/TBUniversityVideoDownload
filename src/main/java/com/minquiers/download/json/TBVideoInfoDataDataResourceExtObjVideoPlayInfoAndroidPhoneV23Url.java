package com.minquiers.download.json;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class TBVideoInfoDataDataResourceExtObjVideoPlayInfoAndroidPhoneV23Url {
    private String sd;
    private String ld;
    private String hd;

    public String getHd(){
        if(StringUtils.isBlank(hd)){
            if(StringUtils.isNotBlank(ld)){
                return ld;
            }else if(StringUtils.isNotBlank(sd)){
                return sd;
            }
        }
        return hd;
    }
}
