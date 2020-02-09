package com.example.slideshowfinalversion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CellsModel {

    private static final String SLIDER_INTERVAL = "sliderInterval";
    private static final String URLS = "urls";

    @SerializedName(SLIDER_INTERVAL)
    private int sliderInterval;
    @SerializedName(URLS)
    private List<UrlModel> urls;

    public CellsModel() {
    }

    public int getSliderInterval() {
        return sliderInterval;
    }

    public List<UrlModel> getUrls() {
        return urls;
    }
}
