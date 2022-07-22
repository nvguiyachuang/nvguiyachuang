package util;

import org.apache.commons.collections.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcSqlUtil {

    private static Connection getConn() {
        return null;
    }

    public static List<Map<String, Object>> executeQuery(String sql, List<Object> params) throws SQLException {
        Connection conn = getConn();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        assert conn != null;
        pstmt = conn.prepareStatement(sql);

        //为变量赋值
        if (CollectionUtils.isNotEmpty(params)) {
            for (int i = 1; i <= params.size(); i++) {
                pstmt.setObject(i, params.get(i - 1));
            }
        }

        //执行查询
        rs = pstmt.executeQuery();
        //解析结果
        return parseResult(rs);
    }

    public static List<Map<String, Object>> parseResult(ResultSet rs) {
        if (rs != null) {
            List<Map<String, Object>> resultList = new ArrayList<>();

            List<String> columnList = new ArrayList<>();
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    columnList.add(metaData.getColumnName(i).toLowerCase());
                }

                while (rs.next()) {
                    Map<String, Object> rowMap = new HashMap<>(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        rowMap.put(columnList.get(i - 1), rs.getObject(i));
                    }
                    resultList.add(rowMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultList;
        }
        return null;
    }

    public static void execSql(String sql,  List<Object> params) throws Exception {
        Connection conn = getConn();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        assert conn != null;
        pstmt = conn.prepareStatement(sql);

        //为变量赋值
        if (CollectionUtils.isNotEmpty(params)) {
            fillParams(pstmt, params);
        }

        //执行SQL
        boolean result = pstmt.execute();
//        int i = pstmt.executeUpdate();
    }

    public static void fillParams(PreparedStatement pstmt, List<Object> param) throws SQLException {
        for (int i = 1; i <= param.size(); i++) {
            Object obj = param.get(i - 1);
            if (obj instanceof Boolean) {
                pstmt.setBoolean(i, (boolean) obj);
            } else if (obj instanceof Byte) {
                pstmt.setByte(i, (byte) obj);
            } else if (obj instanceof Short) {
                pstmt.setShort(i, (short) obj);
            } else if (obj instanceof Integer) {
                pstmt.setInt(i, (int) obj);
            } else if (obj instanceof Long) {
                pstmt.setLong(i, (long) obj);
            } else if (obj instanceof Float) {
                pstmt.setFloat(i, (float) obj);
            } else if (obj instanceof Double) {
                pstmt.setDouble(i, (double) obj);
            } else if (obj instanceof String) {
                pstmt.setString(i, String.valueOf(obj));
            }
        }
    }
}
