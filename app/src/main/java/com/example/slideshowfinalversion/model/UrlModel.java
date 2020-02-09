package com.example.slideshowfinalversion.model;

import com.google.gson.annotations.SerializedName;

public class UrlModel {

    private static final String URL = "url";
    private static final String MD5 = "md5";
    private static final String MIME_TYPE = "mime_type";

    @SerializedName(URL)
    private String url;
    @SerializedName(MD5)
    private String md5;
    @SerializedName(MIME_TYPE)
    private String mimeType;

    public UrlModel() {
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public String getMimeType() {
        return mimeType;
    }
}
