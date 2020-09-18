package moniClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by admin on 2017/8/17.
 */
public class Client {

    static double count = 1458985738762.0;


    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {

                 //   Socket socket = new Socket("10.192.16.117", 30000);

                 //   Socket socket = new Socket(" 121.248.25.213", 30000);
             //    Socket socket = new Socket("127.0.0.1", 50001);
                  Socket socket = new Socket("139.224.195.10", 50001);

                 //   Socket socket = new Socket("10.192.33.89", 30000);

                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    InputStream is=socket.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    double lat =32.05877181930;
                    // double count = 170712.00;
                    while (true) {
                        if(lat >=32.05877181930 && lat<35.05877181930){
                            lat=lat + 0.5;
                        }else{
                            lat =32.05877181930;
                        }


                     //  String s = "2016-12-03 17:41:09,32.65085017928,117.68866140934,ID2";
                     //   s = "$" + s;//+ count
                     //   String s = "2016-12-03 17:41:09,a,b,32.65085017928,117.68866140934";
                     //   s = "@" + s;//+ count
                    //    String s = "2016-12-03 17:41:09,120.68866140934,32.65085017928,ID2";

                     //  String s = "2016-12-03 17:41:09,32.65085017928,117.68866140934,ID2";
                     //   s = "$" + s;//+ count
                      //  String s = "2016-12-03 17:41:09,a,b,32.65085017928,117.68866140934";
                     //   s = "@" + s;//+ count
                     //   String s = "2016-12-03 17:41:09,120.68866140934,32.65085017928,ID2";

                       // String s = "2016-12-03 17:41:09,117.68866140934,32.65085017928,ID2";

                       // s = "$" + s;//+ count
                       // String s = "2016-12-03 17:41:09,117.68866140934,32.65085017928,ID2";
                        //#realtimedata，当前设备时间，设备id，数据类型（即表名），数据个数n，数据1名称，数据1数值，……,数据n名称，数据n数值，校验值*
                      String s = "realtimedata,201803161212113,jly000002,tb_pilerealtime,16,ZhuangNum,0001,Lon,118.4,Lat,38.9,Curtime,20180316121213," +
                                "CurLength,12,flow,0.5,slurry,5,concrete,6,Incurrent,2.0,OutCurrent,1.2,FBAngularity,9,LRAngularity,2,Verticality,98,Speed,10," +
                                "DeviceNum,YX-1,1*" ;
                      /*String s = "historydata,201803161212113,jly000001,tb_pilerealtime,16,ZhuangNum,0001,Lon,,Lat,,Curtime,20180316121213," +
                                "CurLength,12,flow,0.5,slurry,5,concrete,6,Incurrent,2.0,OutCurrent,1.2,FBAngularity,9,LRAngularity,2,Verticality,98,Speed,10," +
                                "DeviceNum,z01,1*" ;*/
                   /*  String s = "login,ZJ000001,65*" ;*/
                       /* String s = "realtimedata,20180316121213,jly-000001,tb_okzhuanginfo,18,ZhuangNum,11111111111111111111,Lon,118,Lat,32,Starttime,20180316121213,Endtime,20180316160404," +
                                "ExpLength,12,RealLength,11,Pentime,5,Totalslurry,6,Totalconcrete,2.0,Upspeed,1.2,DownSpeed,9,Incurrent,2,OutCurrent,98,Verticality,10," +
                                "DeviceNum,YX-1,UploadTime,20180416145522,State,1,1*" ;*/
                       // String s = "historydata,20180410111529,WX-5,tb_okzhuanginfo,18,ZhuangNum,CK0+509-000-2-20,Lon,118.78856567544,Lat,32.05846922021,starttime,20180410111429,endtime,20180410111529,ExpLength,13,RealLength,14.91,Pentime,1110,Totalslurry,850.34,Totalconcrete,968.64,Upspeed,474.15,DownSpeed,358.54,Incurrent,78.52,OutCurrent,52.75,Verticality,3,DeviceNum,WX-5,UploadTime,20180410111529,state,1,32*";
                        s = "#" + s;


                       /* byte[] check = s.substring(1).getBytes();
                        // char[] ch = sb.toString().toCharArray();
                        int sum_check = 0;
                        for (int k = 0; k < check.length; k++) {
                            // System.out.println("ch"+ch[k]+":"+check[k]);
                            sum_check += check[k];
                        }
                        int check_date = (byte) sum_check & 0x00000ff;*/
                       // s = s +"#\n";s
                        // ",107#"
                        count++;
                   //     s += s;
                        ps.print(s);
                        System.out.println(br.readLine());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }

                } catch (UnknownHostException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }

}

