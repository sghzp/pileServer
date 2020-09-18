package com.server;


import com.alibaba.fastjson.JSONObject;
import com.beans.PileRealtime;
import com.beans.WholePile;
import com.beans.XunJianInfo;
import com.seu.database.JDBCTools;
import com.seu.database.PileRealtimeDao;
import com.seu.database.WholePileDao;
import com.seu.database.XunJianInfoDao;
import com.seu.tool.DBO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.spi.DataFormat;


import javax.jms.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lifan on 2017/8/17.
 */


public class ServerHandler extends SimpleChannelInboundHandler<String> {
   /* public static final String[] messageName = new String[]{"经度", "纬度", "工号", "轨迹编号", "UTC时间"};
    private String tablename;// 数据库的表名;
    double longitude = 0.0D;
    double latitude = 0.0D;
    int flag = 0;
    private String EmployID;
    String time;
    String timeget;*/
    public static final String[] messageName = new String[]{"当前设备时间","设备id","数据类型（即表名）","数据个数n","数据1名称", "数据1数值","数据n名称","数据n数值","校验值"};
    private String tablename;// 数据库的表名;
    private String ZhuangNum;//桩编号
    private String ProjectID;//所属项目
    private double Lon ;// 经度数据，格式为XXX.XXXXXX，单位为度
    private double Lat;// 纬度数据，格式为XXX.XXXXXX，单位为度
    private String Curtime;// 当前设备时间
    private double CurLength;// 当前桩深度（m）
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

    private String Starttime;// 开始时间
    private String Endtime;// 结束时间
    private String UploadTime;//上传时间


    /**
     * socket连接成功之后接受数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
       /* XunJianInfo xunjianInfo = new XunJianInfo();*/
      //  XunJianInfo xunjianInfo=saveData_DB(ctx, msg);
        PileRealtime pileRealtime = saveData_DB(ctx, msg);

       /* xunjianInfo.setRouteID("轨迹000000001");
        xunjianInfo.setLongitude(lon);
        xunjianInfo.setLatitude(lat);

        if(flag == 0){
            xunjianInfo.setEmployID("X000000001");
            flag = 1;
        }else{
            xunjianInfo.setEmployID("X000000002");
            flag = 0;
        }
        if (lon > 118) {
            lon = 117;
        } else {
            lon = lon + 0.1;
        }
        if (lat > 33) {
            lat = 32;
        } else {
            lat = lat + 0.1;
        }*/
        if(pileRealtime!=null) {
            ConnectionFactory connFactory = new ActiveMQConnectionFactory();
            Connection conn = connFactory.createConnection();
            Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = sess.createTopic(pileRealtime.getProjectID());
            System.out.println(pileRealtime.getProjectID()+"8888888888888888888888888888888888888888888888888888888888888");
            MessageProducer prod = sess.createProducer(dest);
            //  Message msg0 = sess.createTextMessage(JSONObject.toJSONString(xunjianInfo));
            Message msg0 = sess.createTextMessage(JSONObject.toJSONString(pileRealtime));
            prod.send(msg0);

            conn.close();
        }


    }

    /**
     * 新客户端接入
     * <p>
     * channelActive()方法将会在连接被建立并且准备进行通信时被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        String fa = "$time," + this.getStringDate() + "\r\n";
        String fa1 = new String(fa.getBytes(), "utf-8");
        System.out.println(fa1);
        ctx.channel().writeAndFlush(fa1);
        //信息交互
    }

    /**
     * 客户端断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    /**
     * 获取当前时间
     * @return
     */
    public String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 异或运算，用于异或校验
     */
    public static String getXor(byte[] datas){
//        System.out.println(datas.length);
        byte temp=datas[0];
        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
//            System.out.println(datas[i]);
        }
        String check = Integer.toHexString(temp);
        return check;
    }

//#realtimedata，当前设备时间，设备id，数据类型（即表名），数据个数n，数据1名称，数据1数值，……,数据n名称，数据n数值，校验值*

    /**
     * 数据解析函数
     * @param ctx
     * @param msg
     * @return
     * @throws Exception
     */
    public PileRealtime saveData_DB(ChannelHandlerContext ctx, String msg) throws Exception {
        PileRealtime pileRealtime = new PileRealtime();

        System.out.println(msg);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        if (msg.startsWith("#realtimedata")&& msg.endsWith("*")) {
            String substr1 = msg.substring(0, msg.length() - 1);

            String[] divide_str1 = substr1.split(",");
            System.out.println(divide_str1[divide_str1.length - 1]);
            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str1.length - 1; i++) {

                sb.append(divide_str1[i]);
                sb.append(",");

            }
            byte[] check = sb.toString().getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(sum_check);
            int check_date = sum_check & 0x00000ff;
            System.out.println(check_date);
//            String mysum = check_date + "";
            boolean check_result = false;
            if (check_date == Integer.parseInt(divide_str1[divide_str1.length - 1])) {
                check_result = true;
            } else {
                check_result = false;
//                return null;
            }


            String substr = msg.substring(14, msg.length());
            String[] divide_str = substr.split(",");
          /*  for (int k = 0; k < divide_str.length; k++) {
            }*/
/*            // 计算异或校验---------start;
            boolean check_result = false;
            String str = msg.substring(msg.indexOf("#") + 1,msg.indexOf("*") + 1);
            if(getXor(str.getBytes()).equals(divide_str[divide_str.length - 1].substring(1))){
                check_result = true;
            } else {
                check_result = false;
                System.out.println("error check");
//				return null;
            }
            // 计算校验和---------end;*/
/*
            if (msg.startsWith("#") && msg.endsWith("8\n")) {
                String substr = msg.substring(1, msg.indexOf("#\n"));
                String[] divide_str = substr.split(",");
                for (int k = 0; k < divide_str.length; k++) {
                }*/
            tablename = divide_str[2];
            //  tablename = "tb_pilerealtime";
            //      System.out.println(tablename+"________________________");
            boolean flag = JDBCTools.HasTable(tablename);
            if (!flag) {

                System.out.println("先创建表，因为没有对应的表存在");
              /*String sql = "create table " + tablename + "(id int auto_increment primary key,"
                        + " time timestamp not null  default CURRENT_TIMESTAMP ," + " longitude double," + " latitude double,"
                         + " EmployID varchar(30)," + " RouteID varchar(30));";
					System.out.println(sql);
                JDBCTools.createTable(sql);*/
            }
            if (tablename.equals("tb_pilerealtime")) {
                PileRealtimeDao pileRealtimeDao = new PileRealtimeDao();
                int a = Integer.parseInt(divide_str[3]);

                // Object[] object = new Object[a+1];
                Object[] object = new Object[a + 1];
                //   object[1] = "S0001";
                for (int i = 0; i < Integer.parseInt(divide_str[3]); i++) {
                    if (i == 0) {
                        object[i] = divide_str[5];
                    } else if (i == 1) {
//                        object[1] = "S0001";
                    if (divide_str[33] != null) {
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[33] + "';";//查询 通知sql语句，；
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("inin");
                                object[1] = rs.getString("CurProject");
                            } else {
                                System.out.println("inin2");
                                return null;
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                    } else if (i == 4) {
                        String time = divide_str[5 + 2 * (i - 1)];
                        //     String time = divide_str[5 + 2 * i];
                        Curtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        pileRealtime.setCurtime(Curtime);
                        object[i] = Curtime;
                    } else if (i == 15) {
                        object[i] = divide_str[5 + 2 * (i - 1)];
                        //                   object[i] = divide_str[5 + 2 * i];
                    } /*else if (i == 15) {
                    SystemTime = simpleDateFormat.format(new Date());
                    pileRealtime.setSystemTime(SystemTime);
                    object[divide_str.length] = SystemTime;
                } else if (i == 16) {
                    object[i] = Integer.parseInt(divide_str[5 + 2 * i]);
                } */ else if (i == 2 || i == 3) {
//                    if(null==divide_str[5 + 2 * i]||"".equals(divide_str[5 + 2 * i])){
                        if (null == divide_str[5 + 2 * (i - 1)] || "".equals(divide_str[5 + 2 * (i - 1)])) {
                            object[i] = -1.0;
                        } else {
//                        object[i] = Double.parseDouble(divide_str[5 + 2 * i]);
                            object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                        }
                    } else {
//                    object[i] = Double.parseDouble(divide_str[5 + 2 * i]);
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                    }

                }
                SystemTime = simpleDateFormat.format(new Date());
                System.out.println(SystemTime + "_____________");
                pileRealtime.setSystemTime(SystemTime);
                object[a] = SystemTime;


                /*if( i == 0){
                    ZhuangNum=divide_str[5];
                    //判断哪个表？
                    pileRealtime.setZhuangNum(ZhuangNum);
                    object[i] =ZhuangNum;
                }else if(i==1){

                } else if(i==2){
                    Lon = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lon);
                    object[i] = Lon;
                }else if(i==3){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==4){
                    String time = divide_str[5+2*i];
                    Curtime = time.substring(0,4) + "-" + time.substring(4,6) + "-" +
                            time.substring(6,8) + " " +  time.substring(8,10) + ":" +
                            time.substring(10,12) + ":" + time.substring(12,14);
//						System.out.println(time);
                    pileRealtime.setCurtime(Curtime);
                    object[i] = Curtime;
                }else if(i==5){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==6){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==3){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==3){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==3){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }else if(i==3){
                    Lat = Double.parseDouble(divide_str[5+2*i]);
                    pileRealtime.setLon(Lat);
                    object[i] = Lat;
                }
*/





                /*if (i < 4) {
                    if (divide_str[i].equals("")) {
                        object[i] = null;
                    } else {
                        if (i == 0) {
                            object[i] = simpleDateFormat.parse(divide_str[i]);
//                            xunjianInfo.setTimeget((String)object[i] );
                        } else if (i == 1) {
                            latitude = Double.parseDouble(divide_str[i]);
                            xunjianInfo.setLongitude(latitude);
								*//*longitude = GPS_transform(longitude);*//*
                            object[i] = latitude;
                        } else if (i == 2) {
                            longitude = Double.parseDouble(divide_str[i]);
                            xunjianInfo.setLongitude(longitude);
								*//*longitude = GPS_transform(longitude);*//*
                            object[i] = longitude;
                        } else if (i == 3) {
                            EmployID = divide_str[i];
                            xunjianInfo.setEmployID(EmployID);
                            object[i] = EmployID;
                        }

                    }
                }

            }
            time = simpleDateFormat.format(new Date());
            xunjianInfo.setTime(time);
            object[divide_str.length] = time;*/


                pileRealtime.setAll(object);
                boolean response = true;
                pileRealtimeDao.InsertAll(tablename, object);
                if (response) {
                    ctx.channel().writeAndFlush("#realtimedataok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                } else {
                    ctx.channel().writeAndFlush("#realtimedatanotok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                }

                return pileRealtime;
            }
            if (tablename.equals("tb_okzhuanginfo")) {
                System.out.print("in11111111111");
                WholePile wholePile = new WholePile();
                WholePileDao wholePileDao = new WholePileDao();
                int a = Integer.parseInt(divide_str[3]);
                Object[] object = new Object[a+1];
                //   object[1] = "S0001";
                for (int i = 0; i < Integer.parseInt(divide_str[3])+1; i++) {
                    if (i == 0) {
                        object[i] = divide_str[5];
                    } else if (i == 1) {
//                        object[1] = "S0001";
                        if (divide_str[35] != null) {
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[35] + "';";//查询 通知sql语句，；
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("inin");
                                object[1] = rs.getString("CurProject");
                                System.out.println( object[1]);
                            } else {
                                System.out.println("inin2");
                                return null;
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                    }else if (i == 2 || i == 3) {
//                    if(null==divide_str[5 + 2 * i]||"".equals(divide_str[5 + 2 * i])){
                        if (null == divide_str[5 + 2 * (i - 1)] || "".equals(divide_str[5 + 2 * (i - 1)])) {
                            object[i] = -1.0;
                        }else{
                            object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                        }
                    } else if (i == 4) {
                        String time = divide_str[5 + 2 * (i - 1)];
                        //     String time = divide_str[5 + 2 * i];
                        Starttime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        object[i] = Starttime;
                    } else if (i == 5) {
                        String time = divide_str[5 + 2 * (i - 1)];
                        //     String time = divide_str[5 + 2 * i];
                        Endtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        object[i] = Endtime;
                    }else if (i == 16) {
                        object[i] = divide_str[5 + 2 * (i - 1)];
                    } else if (i == 17) {
                        String time = divide_str[5 + 2 * (i - 1)];
                        //     String time = divide_str[5 + 2 * i];
                        UploadTime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        object[i] = UploadTime;
                    }else if(i == 18){
                        object[i] = Integer.parseInt(divide_str[5 + 2 * (i - 1)]);
                        System.out.println("/////////////////////////////////////////////////////////////////////////");
                        System.out.println(object[18]);
                    }else {
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                    }
                }
                wholePile.setAll(object);
                boolean response = true;
                wholePileDao.InsertAll(tablename, object);
                if (response) {
                    ctx.channel().writeAndFlush("#okzhuangdataok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                } else {
                    ctx.channel().writeAndFlush("#okzhuangdatanotok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                }

                return null;
            }
            if (tablename.equals("tb_pileokrealtime")) {
                PileRealtimeDao pileRealtimeDao = new PileRealtimeDao();
                int a = Integer.parseInt(divide_str[3]);

                // Object[] object = new Object[a+1];
                Object[] object = new Object[a + 1];
                //   object[1] = "S0001";
                for (int i = 0; i < Integer.parseInt(divide_str[3]); i++) {
                    if (i == 0) {
                        object[i] = divide_str[5];
                    } else if (i == 1) {
//                        object[1] = "S0001";
                    if (divide_str[33] != null) {
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[33] + "';";//查询 通知sql语句,
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("inin1");
                                object[1] = rs.getString("CurProject");
                                System.out.println(object[1]);
                            } else {
                                System.out.println("inin2");
                                return null;
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                    } else if (i == 4) {
                        String time = divide_str[5 + 2 * (i - 1)];
                        //     String time = divide_str[5 + 2 * i];
                        Curtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        pileRealtime.setCurtime(Curtime);
                        object[i] = Curtime;
                    } else if (i == 15) {
                        object[i] = divide_str[5 + 2 * (i - 1)];
                        //                   object[i] = divide_str[5 + 2 * i];
                    } /*else if (i == 15) {
                    SystemTime = simpleDateFormat.format(new Date());
                    pileRealtime.setSystemTime(SystemTime);
                    object[divide_str.length] = SystemTime;
                } else if (i == 16) {
                    object[i] = Integer.parseInt(divide_str[5 + 2 * i]);
                } */ else if (i == 2 || i == 3) {
//                    if(null==divide_str[5 + 2 * i]||"".equals(divide_str[5 + 2 * i])){
                        if (null == divide_str[5 + 2 * (i - 1)] || "".equals(divide_str[5 + 2 * (i - 1)])) {
                            object[i] = -1.0;
                        } else {
//                        object[i] = Double.parseDouble(divide_str[5 + 2 * i]);
                            object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                        }
                    } else {
//                    object[i] = Double.parseDouble(divide_str[5 + 2 * i]);
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                    }

                }
                SystemTime = simpleDateFormat.format(new Date());
                System.out.println(SystemTime + "_____________");
                pileRealtime.setSystemTime(SystemTime);
                object[a] = SystemTime;


                boolean response = true;
                pileRealtimeDao.InsertAll(tablename, object);
                if (response) {
                    ctx.channel().writeAndFlush("#pileokrealtimedataok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                } else {
                    ctx.channel().writeAndFlush("#pileokrealtimedatanotok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
                }

                return null;
            }

        }


        /****************************************设备历史漏发数据*******************************************/
        if (msg.startsWith("#historydata")&& msg.endsWith("*")) {
            String substr1 = msg.substring(0, msg.length()-1 );
            String[] divide_str1 = substr1.split(",");
            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str1.length - 1; i++) {

                sb.append(divide_str1[i]);
                sb.append(",");

            }
            byte[] check = sb.toString().getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!");
            System.out.println(sum_check);
            int check_date = sum_check & 0x00000ff;
            System.out.println(check_date);
//            String mysum = check_date + "";
            boolean check_result = false;
            if (check_date == Integer.parseInt(divide_str1[divide_str1.length - 1])) {
                check_result = true;
            } else {
                check_result = false;
//                return null;
            }

            String substr = msg.substring(13, msg.length());
            String[] divide_str = substr.split(",");
            tablename = divide_str[2];

       //     tablename = divide_str[2];
            //  tablename = "tb_pilerealtime";
                 System.out.println(tablename+"________________________");
            boolean flag = JDBCTools.HasTable(tablename);
            if (!flag) {
                System.out.println("先创建表，因为没有对应的表存在");

            }
            if(tablename.equals("tb_pilerealtime")){
            PileRealtimeDao pileRealtimeDao = new PileRealtimeDao();
            int a = Integer.parseInt(divide_str[3]);

            Object[] object = new Object[a+1];
            for (int i = 0; i < Integer.parseInt(divide_str[3]); i++) {
                if (i == 0) {
                    object[i] = divide_str[5];
                } else if(i==1) {
//                    object[1] = "S0001";
                    if (divide_str[33] != null) {
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[33] + "';";//查询 通知sql语句，；
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("ininin");
                                object[1] = rs.getString("CurProject");
                            } else {
                                System.out.println("ininin2");
                                return null;
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                }else if (i == 4) {
                    String time = divide_str[5 + 2 * (i-1)];
                    Curtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                            time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                            time.substring(10, 12) + ":" + time.substring(12, 14);
                    System.out.println(time);
                    pileRealtime.setCurtime(Curtime);
                    object[i] = Curtime;
                } else if (i == 15) {
                    object[i] = divide_str[5 + 2 * (i-1)];
                } else if(i==2||i ==3){
                    if(null==divide_str[5 + 2 * (i-1)]||"".equals(divide_str[5 + 2 * (i-1)])){
                        object[i]=-1.0;
                    }else{
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i-1)]);
                    }
                }
                else {
                    object[i] = Double.parseDouble(divide_str[5 + 2 * (i-1)]);
                }

            }
            SystemTime = simpleDateFormat.format(new Date());
            System.out.println(SystemTime+"_____________");
            pileRealtime.setSystemTime(SystemTime);
            object[a] = SystemTime;


            pileRealtime.setAll(object);
            boolean response  = true;
            pileRealtimeDao.InsertAll(tablename, object);
            if(response){
                ctx.channel().writeAndFlush("#historyrealtimedataok," + divide_str1[1] + ","  + divide_str[2]  + ","+ check_date + "*\r\n");
            }else{
                ctx.channel().writeAndFlush("#historyrealtimedatanotok," + divide_str1[1] + ","  + divide_str[2]  + ","+ check_date + "*\r\n");
            }

            return null;
            /*增加记录完成回复#historydataok，当前设备时间，设备id，校验值*
                    增加记录失败回复#historydatanotok，当前设备时间，设备id，校验值**/


        }
        if(tablename.equals("tb_okzhuanginfo")){
            WholePile wholePile = new WholePile();
            WholePileDao wholePileDao = new WholePileDao();
            int a = Integer.parseInt(divide_str[3]);
            Object[] object = new Object[a+1];
            //   object[1] = "S0001";
            for (int i = 0; i < Integer.parseInt(divide_str[3])+1; i++) {
                if (i == 0) {
                    object[i] = divide_str[5];
                } else if (i == 1) {
//                    object[1] = "S0001";
                        if (divide_str[35] != null) {
                        java.sql.Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[35] + "';";//查询 通知sql语句，；
                        try {
                            conn = JDBCTools.getConnection();
                            ps = conn.prepareStatement(sql1);
                            rs = ps.executeQuery();
                            if (rs.next()) {
                                System.out.println("inin1yjfuyfguh");
                                object[1] = rs.getString("CurProject");
                            } else {
                                System.out.println("inin2");
                                return null;
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } finally {
                            try {
                                rs.close();
                                ps.close();
                                conn.close();
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    }
                }else if (i == 2 || i == 3) {
//                    if(null==divide_str[5 + 2 * i]||"".equals(divide_str[5 + 2 * i])){
                    if (null == divide_str[5 + 2 * (i - 1)] || "".equals(divide_str[5 + 2 * (i - 1)])) {
                        object[i] = -1.0;
                    }else{
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                    }
                } else if (i == 4) {
                    String time = divide_str[5 + 2 * (i - 1)];
                    //     String time = divide_str[5 + 2 * i];
                    Starttime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                            time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                            time.substring(10, 12) + ":" + time.substring(12, 14);
                    System.out.println(time);
                    object[i] = Starttime;
                } else if (i == 5) {
                    String time = divide_str[5 + 2 * (i - 1)];
                    //     String time = divide_str[5 + 2 * i];
                    Endtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                            time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                            time.substring(10, 12) + ":" + time.substring(12, 14);
                    System.out.println(time);
                    object[i] = Endtime;
                }else if (i == 16) {
                    object[i] = divide_str[5 + 2 * (i - 1)];
                } else if (i == 17) {
                    String time = divide_str[5 + 2 * (i - 1)];
                    //     String time = divide_str[5 + 2 * i];
                    UploadTime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                            time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                            time.substring(10, 12) + ":" + time.substring(12, 14);
                    System.out.println(time);
                    object[i] = UploadTime;
                }else if(i == 18){
                    object[i] = Integer.parseInt(divide_str[5 + 2 * (i - 1)]);
                    System.out.println("/////////////////////////////////////////////////////////////////////////");
                    System.out.println(object[18]);
                }else {
                    object[i] = Double.parseDouble(divide_str[5 + 2 * (i - 1)]);
                }
            }
            wholePile.setAll(object);



            boolean response = true;
            wholePileDao.InsertAll(tablename, object);
            if (response) {
                ctx.channel().writeAndFlush("#historyokzhuangdataok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
            } else {
                ctx.channel().writeAndFlush("#historyokzhuangdatanotok," + divide_str1[1] + "," + divide_str[2] + "," + check_date + "*\r\n");
            }

            return null;
        }
            if(tablename.equals("tb_pileokrealtime")){
                PileRealtimeDao pileRealtimeDao = new PileRealtimeDao();
                int a = Integer.parseInt(divide_str[3]);

                Object[] object = new Object[a+1];
                for (int i = 0; i < Integer.parseInt(divide_str[3]); i++) {
                    if (i == 0) {
                        object[i] = divide_str[5];
                    } else if(i==1) {
//                        object[1] = "S0001";
                        if (divide_str[33] != null) {
                            java.sql.Connection conn = null;
                            PreparedStatement ps = null;
                            ResultSet rs = null;
                            String sql1 = "SELECT CurProject FROM tb_deviceinfo WHERE Num = '" + divide_str[33] + "';";//查询 通知sql语句，；
                            try {
                                conn = JDBCTools.getConnection();
                                ps = conn.prepareStatement(sql1);
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    System.out.println("inin");
                                    object[1] = rs.getString("CurProject");
                                } else {
                                    System.out.println("inin2");
                                    return null;
                                }
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } finally {
                                try {
                                    rs.close();
                                    ps.close();
                                    conn.close();
                                } catch (SQLException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        }

                    }else if (i == 4) {
                        String time = divide_str[5 + 2 * (i-1)];
                        Curtime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                                time.substring(10, 12) + ":" + time.substring(12, 14);
                        System.out.println(time);
                        pileRealtime.setCurtime(Curtime);
                        object[i] = Curtime;
                    } else if (i == 15) {
                        object[i] = divide_str[5 + 2 * (i-1)];
                    } else if(i==2||i ==3){
                        if(null==divide_str[5 + 2 * (i-1)]||"".equals(divide_str[5 + 2 * (i-1)])){
                            object[i]=-1.0;
                        }else{
                            object[i] = Double.parseDouble(divide_str[5 + 2 * (i-1)]);
                        }
                    }
                    else {
                        object[i] = Double.parseDouble(divide_str[5 + 2 * (i-1)]);
                    }

                }
                SystemTime = simpleDateFormat.format(new Date());
                System.out.println(SystemTime+"_____________");
                pileRealtime.setSystemTime(SystemTime);
                object[a] = SystemTime;


                pileRealtime.setAll(object);
                boolean response  = true;
                pileRealtimeDao.InsertAll(tablename, object);
                if(response){
                    ctx.channel().writeAndFlush("#historyokrealtimedataok," + divide_str1[1] + ","  + divide_str[2]  + ","+ check_date + "*\r\n");
                }else{
                    ctx.channel().writeAndFlush("#historyokrealtimedatanotok," + divide_str1[1] + ","  + divide_str[2]  + ","+ check_date + "*\r\n");
                }

                return null;
            /*增加记录完成回复#historydataok，当前设备时间，设备id，校验值*
                    增加记录失败回复#historydatanotok，当前设备时间，设备id，校验值**/


            }

        }


        /****************************************设备请求登录**********************************************/
        if (msg.startsWith("#login")&& msg.endsWith("*")) {
           String substr = msg.substring(0, msg.length()-1 );
           String[] divide_str = substr.split(",");

            System.out.println(divide_str[0]);
            System.out.println(divide_str[1]);
           System.out.println(divide_str[divide_str.length - 1]);
            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str.length ; i++) {

                    sb.append(divide_str[i]);
                    sb.append(",");

            }
            byte[] check = sb.toString().getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }


           /* String substr = msg.substring(7, msg.length() - 2);
            String[] divide_str = substr.split(",");
            for (int k = 0; k < divide_str.length; k++) {
            }

            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str.length - 1; i++) {
                if (i != divide_str.length - 2) {
                    sb.append(divide_str[i]);
                    sb.append(",");
                } else {
                    sb.append(divide_str[i]);
                }
            }
            byte[] check = sb.toString().getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }*/
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(sum_check);
            int check_date = sum_check & 0x00000ff;
            System.out.println(check_date);
//            String mysum = check_date + "";
            boolean check_result = false;
            if (check_date == Integer.parseInt(divide_str[divide_str.length - 1])) {
                check_result = true;
            } else {
                check_result = false;
//                return null;
            }

            //           String substr = msg.substring(7, msg.length());
            //           String[] divide_str = substr.split(",");
            //          String deviceNum = divide_str[0];
          /*  // 计算异或校验---------start;
            boolean check_result = false;
            String str = msg.substring(msg.indexOf("$") + 1,msg.indexOf("*") + 1);
            if(getXor(str.getBytes()).equals(divide_str[divide_str.length - 1].substring(1))){
                check_result = true;
            } else {
                check_result = false;
                System.out.println("error check");
//				return null;
            }*/
            System.out.println("0");
            if (null != divide_str[1] || !"".equals(divide_str[1])) {
                System.out.println("1");
                //进行数据库操作。
//                DBO db = new DBO();//初始链接数据库类
//                ResultSet rs = null;
//                String sql1 = "SELECT  * FROM tb_deviceinfo WHERE Num = " + divide_str[0] + ";";//查询 通知sql语句，；


                java.sql.Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                String sql1 = "SELECT  * FROM tb_deviceinfo WHERE Num = '" + divide_str[0] + "';";//查询 通知sql语句，；
                try {
                    conn = JDBCTools.getConnection();
                    ps = conn.prepareStatement(sql1);
                    rs = ps.executeQuery();
                    SystemTime = simpleDateFormat.format(new Date());

                    if (rs.next()) {
                        System.out.println("in");
                        ctx.channel().writeAndFlush("#loginok," + divide_str[1] + "," + SystemTime + "," + check_date + "*\r\n");
                    } else {
                        System.out.println("in2");
                        ctx.channel().writeAndFlush("#loginnotok,"+divide_str[1]+","+SystemTime+","+check_date+"*\r\n");        /*存在则回复：#loginok，设备编号，当前服务器时间，校验值*
                                不存在则回复：#loginnotok，设备编号，当前服务器时间，校验值**/
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        rs.close();
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


            }

        }
    /****************************************搅拌桩机获取配对制浆机**********************************************/
        if (msg.startsWith("#RequestSlurryData")&& msg.endsWith("*")) {
//            String substr = msg.substring(7, msg.length()-2 );
            String substr = msg.substring(0, msg.length() - 1);
            String[] divide_str = substr.split(",");
            for (int k = 0; k < divide_str.length; k++) {
            }

            // 计算校验和;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < divide_str.length ; i++) {
            //    if (i != divide_str.length - 2) {
                    sb.append(divide_str[i]);
                    sb.append(",");
              /*  } else {
                    sb.append(divide_str[i]);
                }*/
            }
            byte[] check = sb.toString().getBytes();
            int sum_check = 0;
            for (int k = 0; k < check.length; k++) {
                sum_check += check[k];
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(sum_check);
            int check_date = sum_check & 0x00000ff;
            System.out.println(check_date);
//            String mysum = check_date + "";
            boolean check_result = false;
            if (check_date == Integer.parseInt(divide_str[divide_str.length - 1])) {
                check_result = true;
            } else {
                check_result = false;
//                return null;
            }

            System.out.println("0");
            if (null != divide_str[1] || !"".equals(divide_str[1])) {
                System.out.println("1");
                //进行数据库操作。
//                DBO db = new DBO();//初始链接数据库类
//                ResultSet rs = null;
//                String sql1 = "SELECT  * FROM tb_deviceinfo WHERE Num = " + divide_str[0] + ";";//查询 通知sql语句，；


                java.sql.Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
            //   String sql1 = "SELECT  PairID  FROM tb_deviceinfo WHERE Num = '" + divide_str[0] + "';";//查询 通知sql语句，；
                String sql2 = "SELECT  Ratio,density  top 1 FROM tb_jianginfo  WHERE DeviceNum = (SELECT  PairID  FROM tb_deviceinfo WHERE Num = '"+ divide_str[0] + "')order by Curtime desc;";

                try {
                    conn = JDBCTools.getConnection();
                    ps = conn.prepareStatement(sql2);
                    rs = ps.executeQuery();
               //     String Ratio = rs.getString("Ratio");
               //     String density = rs.getString("density");

                 //   SystemTime = simpleDateFormat.format(new Date());
                    if (rs.next()) {
                        String Ratio = rs.getString("Ratio");
                        String density = rs.getString("density");
                        /*String substr1 = rs.substring(0,msg.length() - 1);
                        String Ratio = divide_str1 [0];
                        String density = divide_str1 [1];*/
                        System.out.println("in");
                        ctx.channel().writeAndFlush("#RequestSlurryDataok," + divide_str[1] + "," + Ratio + "," + density + "," + check_date + "*\r\n");

                    } else {
                        System.out.println("in2");
                        ctx.channel().writeAndFlush("#RequestSlurryDatanotok,"+divide_str[1]+ "," + check_date + "*\r\n");
                    }
                   /* 服务器操作：
                    1）	依据设备编号从设备信息表中查出配对制浆机设备编号
                    2）	依据制浆机编号从制浆机详细记录表中查询时间最近的一条记录，取出水灰比和密度数据
                    3）	回复：
                    成功获取数据回复：#RequestSlurryDataok，设备编号，配对制浆机当前水灰比，配对制浆机当前密度，校验值*
                            无法获取数据则回复：#RequestSlurryDatanotok，设备编号，校验值**/

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        rs.close();
                        ps.close();
                        conn.close();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }


            }

        }
        return null;

    }
}





