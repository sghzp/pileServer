package com.beans;

public class PileRealtime {
    /**
     * Created by lifan on 2018/04/06.
     */
    private String ZhuangNum;//桩编号
    private String ProjectID;//所属项目
    private double Lon;// 经度数据，格式为XXX.XXXXXX，单位为度
    private double Lat;// 纬度数据，格式为XXX.XXXXXX，单位为度
    private String Curtime;// 当前设备时间
    private double CurLength;// 当前桩深度（m）
    private double flow ;// 流量（L/min)
    private double slurry;// 浆量（L）
    private double concrete;// 灰量(Kg)
    private double Incurrent;//内电流(A)
    private double OutCurrent;// 外电流(A)
    private double FBAngularity;// 前后倾斜角（deg）
    private double LRAngularity;// 左右倾斜角（deg）
    private double Verticality;// 垂直度(%)
    private double Speed;//速度(cm/min)
    private String DeviceNum;// 桩机号
    private String SystemTime;// 系统时间
    private Integer UploadState;//上传服务器数据状态（0，未上传，1，已上传）

    public String getZhuangNum() {
        return ZhuangNum;
    }

    public void setZhuangNum(String zhuangNum) {
        ZhuangNum = zhuangNum;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public String getCurtime() {
        return Curtime;
    }

    public void setCurtime(String curtime) {
        Curtime = curtime;
    }

    public double getCurLength() {
        return CurLength;
    }

    public void setCurLength(double curLength) {
        CurLength = curLength;
    }

    public double getFlow() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow = flow;
    }

    public double getSlurry() {
        return slurry;
    }

    public void setSlurry(double slurry) {
        this.slurry = slurry;
    }

    public double getConcrete() {
        return concrete;
    }

    public void setConcrete(double concrete) {
        this.concrete = concrete;
    }

    public double getIncurrent() {
        return Incurrent;
    }

    public void setIncurrent(double incurrent) {
        Incurrent = incurrent;
    }

    public double getOutCurrent() {
        return OutCurrent;
    }

    public void setOutCurrent(double outCurrent) {
        OutCurrent = outCurrent;
    }

    public double getFBAngularity() {
        return FBAngularity;
    }

    public void setFBAngularity(double FBAngularity) {
        this.FBAngularity = FBAngularity;
    }

    public double getLRAngularity() {
        return LRAngularity;
    }

    public void setLRAngularity(double LRAngularity) {
        this.LRAngularity = LRAngularity;
    }

    public double getVerticality() {
        return Verticality;
    }

    public void setVerticality(double verticality) {
        Verticality = verticality;
    }

    public double getSpeed() {
        return Speed;
    }

    public void setSpeed(double speed) {
        Speed = speed;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        DeviceNum = deviceNum;
    }

    public String getSystemTime() {
        return SystemTime;
    }

    public void setSystemTime(String systemTime) {
        SystemTime = systemTime;
    }

    public Integer getUploadState() {
        return UploadState;
    }

    public void setUploadState(Integer uploadState) {
        UploadState = uploadState;
    }

    public boolean setAll(Object[] object) {
        int i = 0;

        this.ZhuangNum = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.ProjectID = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Lon = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Lat = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Curtime = object[i++].toString();
        if (i >= object.length) {
            return false;
        }
        this.CurLength = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.flow = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.slurry = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.concrete = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Incurrent = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.OutCurrent = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.FBAngularity = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.LRAngularity = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Verticality = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Speed = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.DeviceNum= (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.SystemTime= object[i ++].toString();
        /*if (i >= object.length) {
            return false;
        }
        this.UploadState = (Integer) object[i++];*/
        return true;


    }

    @Override
    public String toString() {
        return "PileRealtime{" +
                "ZhuangNum='" + ZhuangNum + '\'' +
                ", ProjectID='" + ProjectID + '\'' +
                ", Lon=" + Lon +
                ", Lat=" + Lat +
                ", Curtime='" + Curtime + '\'' +
                ", CurLength=" + CurLength +
                ", flow=" + flow +
                ", slurry=" + slurry +
                ", concrete=" + concrete +
                ", Incurrent=" + Incurrent +
                ", OutCurrent=" + OutCurrent +
                ", FBAngularity=" + FBAngularity +
                ", LRAngularity=" + LRAngularity +
                ", Verticality=" + Verticality +
                ", Speed=" + Speed +
                ", DeviceNum='" + DeviceNum + '\'' +
                ", SystemTime='" + SystemTime + '\'' +
                ", UploadState=" + UploadState +
                '}';
    }
}
