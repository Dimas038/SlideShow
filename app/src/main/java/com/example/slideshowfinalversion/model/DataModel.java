package com.example.slideshowfinalversion.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataModel {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String CELLS = "cells";

    @SerializedName(ID)
    private String id;
    @SerializedName(NAME)
    private String name;
    @SerializedName(CELLS)
    private List<CellsModel> cells;

    public DataModel() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<CellsModel> getCells() {
        return cells;
    }
}
