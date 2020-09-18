package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by admin on 2017/8/17.
 */

    public class PileRealtimeDao extends DAO {

        // INSERT所有元素
        public void updateAll(String sql, Object... args) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = JDBCTools.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                System.out.println("object_num:" + args.length);
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i + 1, args[i]);
                }

                preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                JDBCTools.releaseDB(null, preparedStatement, connection);
            }
        }
        // search重复数据
        public void selectAll(String sql, Object... args) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            //             String sql0 = "SELECT  * FROM tb_deviceinfo WHERE Num = '" + divide_str[0] + "';";//查询 通知sql语句，；

            try {
                connection = JDBCTools.getConnection();
  //              String sql0 = SELECT * FROM TABLE WHERE 字段名=插入值
   //             String sql0 = "SELECT  * FROM tb_deviceinfo WHERE Num = '" + divide_str[0] + "';";//查询 通知sql语句，；
                preparedStatement = connection.prepareStatement(sql);
                System.out.println("object_num:" + args.length);
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i + 1, args[i]);

                }
//                String sql0 = "select * from"+ tablename +

                //    preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                JDBCTools.releaseDB(null, preparedStatement, connection);
            }
        }






        // 插入所有的数据进入其中，按照对象插入;
        public void InsertAll(String tablename, Object... obj) {
            boolean response = false;
 //           String sql0 = "SELECT  * FROM " + tablename + "WHERE ZhuangNum = '" + obj[0] + "';";//查询 通知sql语句，；


            //          String sql0 = "select * from" + tablename +
       //     if(SELECT * FROM TABLE WHERE 字段名=插入值){
                String sql = "insert  into " + tablename + "(ZhuangNum,ProjectID,Lon,Lat,Curtime,"
                        + "CurLength,flow,slurry,concrete,Incurrent,OutCurrent,FBAngularity,LRAngularity,Verticality,Speed,DeviceNum,"
                        + "SystemTime) values("
                        + "?,?,?,?,?,?,?,?,?,?,?,"
                        + "?,?,?,?,?,?)";

                updateAll(sql, obj);
      //      }
            response = true;
        }



    }


