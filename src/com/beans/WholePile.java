package com.beans;

public class WholePile {
    /**
     * Created by lifan on 2018/04/016.
     */
    private String ZhuangNum;//桩编号
    private String ProjectID;//所属项目
    private double Lon;// 经度数据，格式为XXX.XXXXXX，单位为度
    private double Lat;// 纬度数据，格式为XXX.XXXXXX，单位为度
    private String Starttime;// 开始时间
    private String  Endtime;// 结束时间
    private double ExpLength;// 设计桩长（m）
    private double RealLength ;// 实际桩长（m）
    private double Pentime;// 喷浆时间（s）
    private double Totalslurry;// 总浆量（L）
    private double Totalconcrete;// 总灰量(Kg)
    private double Upspeed;// 最大提钻速度(cm/min)
    private double DownSpeed;// 最大下钻速度(cm/min)
    private double Incurrent;//最大内电流(A)
    private double OutCurrent;// 最大外电流(A)
    private double Verticality;// 最大倾斜度(%)
    private String DeviceNum;// 桩机号
    private String UploadTime;// 上传时间
    private Integer State;// 桩状态（0,未完成；1，已完成）

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

    public String getStarttime() {
        return Starttime;
    }

    public void setStarttime(String starttime) {
        Starttime = starttime;
    }

    public String getEndtime() {
        return Endtime;
    }

    public void setEndtime(String endtime) {
        Endtime = endtime;
    }

    public double getExpLength() {
        return ExpLength;
    }

    public void setExpLength(double expLength) {
        ExpLength = expLength;
    }

    public double getRealLength() {
        return RealLength;
    }

    public void setRealLength(double realLength) {
        RealLength = realLength;
    }

    public double getPentime() {
        return Pentime;
    }

    public void setPentime(double pentime) {
        Pentime = pentime;
    }

    public double getTotalslurry() {
        return Totalslurry;
    }

    public void setTotalslurry(double totalslurry) {
        Totalslurry = totalslurry;
    }

    public double getTotalconcrete() {
        return Totalconcrete;
    }

    public void setTotalconcrete(double totalconcrete) {
        Totalconcrete = totalconcrete;
    }

    public double getUpspeed() {
        return Upspeed;
    }

    public void setUpspeed(double upspeed) {
        Upspeed = upspeed;
    }

    public double getDownSpeed() {
        return DownSpeed;
    }

    public void setDownSpeed(double downSpeed) {
        DownSpeed = downSpeed;
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

    public double getVerticality() {
        return Verticality;
    }

    public void setVerticality(double verticality) {
        Verticality = verticality;
    }

    public String getDeviceNum() {
        return DeviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        DeviceNum = deviceNum;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
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
        this.Starttime= object[i++].toString();
        if (i >= object.length) {
            return false;
        }
        this.Endtime= object[i++].toString();
        if (i >= object.length) {
            return false;
        }
        this.ExpLength = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.RealLength = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Pentime= (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Totalslurry = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Totalconcrete = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.Upspeed = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.DownSpeed = (double) object[i++];
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
        this.Verticality = (double) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.DeviceNum = (String) object[i++];
        if (i >= object.length) {
            return false;
        }
        this.UploadTime = object[i ++].toString();
        if (i >= object.length) {
            return false;
        }
        this.State = (Integer) object[i++];
        return true;
    }

    @Override
    public String toString() {
        return "WholePile{" +
                "ZhuangNum='" + ZhuangNum + '\'' +
                ", ProjectID='" + ProjectID + '\'' +
                ", Lon=" + Lon +
                ", Lat=" + Lat +
                ", Starttime='" + Starttime + '\'' +
                ", Endtime='" + Endtime + '\'' +
                ", ExpLength=" + ExpLength +
                ", RealLength=" + RealLength +
                ", Pentime=" + Pentime +
                ", Totalslurry=" + Totalslurry +
                ", Totalconcrete=" + Totalconcrete +
                ", Upspeed=" + Upspeed +
                ", DownSpeed=" + DownSpeed +
                ", Incurrent=" + Incurrent +
                ", OutCurrent=" + OutCurrent +
                ", Verticality=" + Verticality +
                ", DeviceNum='" + DeviceNum + '\'' +
                ", UploadTime='" + UploadTime + '\'' +
                ", State=" + State +
                '}';
    }
}
