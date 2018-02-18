package com.flymr92gmail.sejonghangugeo.POJO;

import java.io.Serializable;

/**
 * Created by hp on 16.02.2018.
 */

public class Legend implements Serializable{
    private int mId;
    private String name;
    private String nameTranslate;
    private String legendText;

    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameTranslate(String nameTranslate) {
        this.nameTranslate = nameTranslate;
    }

    public void setLegendText(String legendText) {
        this.legendText = legendText;
    }

    public int getmId() {

        return mId;
    }

    public String getName() {
        return name;
    }

    public String getNameTranslate() {
        return nameTranslate;
    }

    public String getLegendText() {
        return legendText;
    }
}
