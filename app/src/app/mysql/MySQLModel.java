package app.mysql;

import com.mysql.jdbc.Driver;
import java.sql.*;


public class MySQLModel {
    private static final String DB_URL = "jdbc:mysql://localhost:8889/app_profile?useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

//    private static Connection con;
//    private static Statement stmt;
//    private static ResultSet rs;

//    public static void main(String args[]) {
//        String query = "select count(*) from user";
//
////        Connection con;
////        Statement stmt;
////        ResultSet rs;
//
//        try {
//            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
//
//            stmt = con.createStatement();
//
//            rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                int count = rs.getInt(1);
//                System.out.println("Total number of books in the user : " + count);
//            }
//
//        } catch (SQLException sqlEx) {
//            sqlEx.printStackTrace();
//        } finally {
//            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
//            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
//            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
//        }
//    }

    public void query(QueryBody queryBody){
        Connection con = null;
//        Statement stmt = null;
//        ResultSet rs = null;

        try {
            con = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            queryBody.body(con);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } //finally {
//            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
//            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
//            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
//        }
    }

    public static interface QueryBody{
        public void body(Connection con) throws SQLException;
    }
}
