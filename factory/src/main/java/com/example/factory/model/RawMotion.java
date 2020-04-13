package com.example.factory.model;

import androidx.annotation.NonNull;

/**
 * @author brsmsg
 * @time 2020/3/15
 */
public class RawMotion {
    //标签
    private String label;
    //x坐标
    private double xCoordinate;
    //y坐标
    private double yCoordinate;
    //压力
    private double pressure;
    //面积
    private double area;
    //action
    private int action;
    //time
    private long time;

    public RawMotion(String label, double xCoordinate, double yCoordinate,
                     double pressure, double area, int action, long time) {
        this.label = label;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.pressure = pressure;
        this.area = area;
        this.action = action;
        this.time = time;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @NonNull
    @Override
    public String toString() {
        return "xCoordinate: " + getxCoordinate()
                +"yCoordinate: " + getyCoordinate()
                +"pressure: " + getPressure()
                +"area: " + getArea()
                +"time: " + getTime()
                +"Action: " + getAction() + "\n";
    }
}
