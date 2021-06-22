package com.malaab.ya.action.actionyamalaab.admin.events;

import com.malaab.ya.action.actionyamalaab.admin.annotations.SearchOption;

public class OnEventSearch {

    @SearchOption
    private int searchOption;

    private float priceStart, priceEnd;
    private String region;
    private int ageStart, ageEnd;
    private int size;


    public OnEventSearch(int searchOption, float priceStart, float priceEnd, String region, int ageStart, int ageEnd, int size) {
        this.searchOption = searchOption;
        this.priceStart = priceStart;
        this.priceEnd = priceEnd;
        this.region = region;
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.size = size;
    }


    public int getSearchOption() {
        return searchOption;
    }


    public float getPriceStart() {
        return priceStart;
    }

    public float getPriceEnd() {
        return priceEnd;
    }


    public String getRegion() {
        return region;
    }


    public int getAgeStart() {
        return ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }


    public int getSize() {
        return size;
    }
}
