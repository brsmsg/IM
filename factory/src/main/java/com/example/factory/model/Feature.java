package com.example.factory.model;

/**
 * @author brsmsg
 * @time 2020/3/15
 */
public class Feature {
    //滑动间隔时间
    private long interTime;
    //持续时间
    private long duration;
    //开始x坐标
    private double startX;
    //开始y坐标
    private double startY;
    //停止x坐标
    private double endX;
    //停止y坐标
    private double endY;
    //直接距离
    private double directDistance;
    //
    private double meanResultantLength;
    //up/down/left/right
    private double flag;
    //最后三个点速度的中位数
    private double velocityLastThree;
    //20%分位速度
    private double twentyVelocity;
    //50%
    private double fiftyVelocity;
    //80%
    private double eightyVelocity;
    //20%分位加速度
    private double twentyAcc;
    //50%
    private double fiftyAcc;
    //80%
    private double eightyAcc;

    public long getInterTime() {
        return interTime;
    }

    public void setInterTime(long interTime) {
        this.interTime = interTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getDirectDistance() {
        return directDistance;
    }

    public void setDirectDistance(double directDistance) {
        this.directDistance = directDistance;
    }

    public double getMeanResultantLength() {
        return meanResultantLength;
    }

    public void setMeanResultantLength(double meanResultantLength) {
        this.meanResultantLength = meanResultantLength;
    }

    public double getFlag() {
        return flag;
    }

    public void setFlag(double flag) {
        this.flag = flag;
    }

    public double getVelocityLastThree() {
        return velocityLastThree;
    }

    public void setVelocityLastThree(double velocityLastThree) {
        this.velocityLastThree = velocityLastThree;
    }

    public double getTwentyVelocity() {
        return twentyVelocity;
    }

    public void setTwentyVelocity(double twentyVelocity) {
        this.twentyVelocity = twentyVelocity;
    }

    public double getFiftyVelocity() {
        return fiftyVelocity;
    }

    public void setFiftyVelocity(double fiftyVelocity) {
        this.fiftyVelocity = fiftyVelocity;
    }

    public double getEightyVelocity() {
        return eightyVelocity;
    }

    public void setEightyVelocity(double eightyVelocity) {
        this.eightyVelocity = eightyVelocity;
    }

    public double getTwentyAcc() {
        return twentyAcc;
    }

    public void setTwentyAcc(double twentyAcc) {
        this.twentyAcc = twentyAcc;
    }

    public double getFiftyAcc() {
        return fiftyAcc;
    }

    public void setFiftyAcc(double fiftyAcc) {
        this.fiftyAcc = fiftyAcc;
    }

    public double getEightyAcc() {
        return eightyAcc;
    }

    public void setEightyAcc(double eightyAcc) {
        this.eightyAcc = eightyAcc;
    }
}
