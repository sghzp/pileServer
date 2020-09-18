package com.beans;

/**
 * Created by admin on 2017/8/17.
 */
public class XunJianInfo {
    private String time;// UTC时间，格式为hhmmss.ss
    private double longitude;// 经度数据，格式为XXX.XXXXXX，单位为度
    private double latitude;// 纬度数据，格式为XXX.XXXXXX，单位为度
    private String EmployID;// 工号，格式为xxxxx，其中x为任意大写字母或数字
    private String RouteID;// 巡检编号
    private String timeget;

    public String getTimeget() {
        return timeget;
    }

    public void setTimeget(String timeget) {
        this.timeget = timeget;
    }




    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getEmployID() {
        return EmployID;
    }

    public void setEmployID(String employID) {
        EmployID = employID;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }



    public boolean setAll(Object[] object) {
        int i = 0;

        this.time = object[i++].toString();
        if (i >= object.length) {
            return false;
        }
        this.longitude = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.latitude = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.EmployID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.RouteID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }

        return true;
		/*this.warningState = warningState;*/

    }
    @Override
    public String toString() {
        return "XunJianInfo{" +
                "timeget='" + timeget + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", EmployID='" + EmployID + '\'' +
                ", time='" + time+
                '}';
    }

}
