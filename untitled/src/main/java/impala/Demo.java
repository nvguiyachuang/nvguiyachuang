package impala;

import util.CloseUtil;

import java.sql.*;

public class Demo {
    public static void main(String[] args) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String JDBC_DRIVER = "com.cloudera.impala.jdbc41.Driver";
//        String CONNECTION_URL = "jdbc:impala://devcdh1:21050/lcb_mars_db;AuthMech=3;UID=hive;PWD=hive@lcb";
        String CONNECTION_URL = "jdbc:impala://192.168.6.76:21050/information_schema";

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(CONNECTION_URL);
            ps = conn.prepareStatement("SELECT * FROM information_schema.`indicator` LIMIT 100");
            rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(ps,rs,conn);
        }
    }
}
