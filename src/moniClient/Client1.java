package moniClient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by admin on 2017/8/17.
 */
public class Client1 {

    static double count = 1458985738762.0;


    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Socket socket = new Socket("10.192.16.117", 30000);
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

                        String s = "2017-09-13 10:48:17,ly,12,6,6,11,4";
                        s = "*" + s;//+ coSunt
                        byte[] check = s.substring(1).getBytes();
                        // char[] ch = sb.toString().toCharArray();
                        int sum_check = 0;
                        for (int k = 0; k < check.length; k++) {
                            // System.out.println("ch"+ch[k]+":"+check[k]);
                            sum_check += check[k];
                        }
                        int check_date = (byte) sum_check & 0x00000ff;
                        s = s +"#\n";
                        // ",107#"
                        count++;
                        s += s;
                        ps.print(s);
                        System.out.println(br.readLine());
                        try {
                            Thread.sleep(300000);
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

