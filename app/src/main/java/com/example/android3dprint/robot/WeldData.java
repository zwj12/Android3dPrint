package com.example.android3dprint.robot;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

public class WeldData implements Serializable {
    private final  String strTaskName = "T_ROB1";
    private final  String strDataModuleName = "JQR365WeldDataModule";
    private final  String strDataName = "weld";
    private final  String strDataType = "welddata";

    private int index;

    private double weldSpeed;

    private double orgWeldSpeed;

    private ArcData mainArc=new ArcData();

    private ArcData orgArc = new ArcData();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getWeldSpeed() {
        return weldSpeed;
    }

    public void setWeldSpeed(double weldSpeed) {
        this.weldSpeed = weldSpeed;
    }

    public double getOrgWeldSpeed() {
        return orgWeldSpeed;
    }

    public void setOrgWeldSpeed(double orgWeldSpeed) {
        this.orgWeldSpeed = orgWeldSpeed;
    }

    public ArcData getMainArc() {
        return mainArc;
    }

    public void setMainArc(ArcData mainArc) {
        this.mainArc = mainArc;
    }

    public ArcData getOrgArc() {
        return orgArc;
    }

    public void setOrgArc(ArcData orgArc) {
        this.orgArc = orgArc;
    }

    public WeldData() {
    }

    @NonNull
    @Override
    public String toString() {
        Locale l = Locale.ENGLISH;
        return String.format(l,"[%.1f,%.1f,%s,%s]"
                , this.weldSpeed, this.orgWeldSpeed, this.mainArc, this.orgArc);
    }

    void parse(String strWeldData) {
        int numStartIndex = 0;
        int numStopIndex = strWeldData.indexOf("[");

        // console.log(strWeldData);

        numStartIndex = numStopIndex + 1;
        numStopIndex = strWeldData.indexOf(",", numStartIndex);
        this.weldSpeed = Float.parseFloat(strWeldData.substring(numStartIndex, numStopIndex));

        numStartIndex = numStopIndex + 1;
        numStopIndex = strWeldData.indexOf(",", numStartIndex);
        this.orgWeldSpeed = Float.parseFloat(strWeldData.substring(numStartIndex, numStopIndex));

        numStartIndex = numStopIndex + 1;
        numStopIndex = strWeldData.indexOf("]", numStartIndex);
        numStopIndex =numStopIndex +1;
        this.mainArc.parse(strWeldData.substring(numStartIndex, numStopIndex));

        numStartIndex = numStopIndex + 1;
        numStopIndex = strWeldData.indexOf("]", numStartIndex);
        numStopIndex =numStopIndex +1;
        this.orgArc.parse(strWeldData.substring(numStartIndex, numStopIndex));
    }
}
