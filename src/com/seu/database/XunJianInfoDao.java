package com.seu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by admin on 2017/8/17.
 */

    public class XunJianInfoDao extends DAO {

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

        // 插入所有的数据进入其中，按照对象插入;
        public void InsertAll(String tablename, Object... obj) {
            String sql = "insert into " + tablename + "(timeget,longitude,latitude,"
                    + "EmployID,time) values("
                    + "?,?,?,?,?)";

            updateAll(sql, obj);
        }


    }


