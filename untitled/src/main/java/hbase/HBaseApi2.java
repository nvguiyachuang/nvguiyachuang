package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseApi2 {

    public static Configuration conf;

    static {
        //使用HBaseConfiguration的单例方法实例化
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.220.111");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }

    public static void main(String[] args) throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        TableName tableName = TableName.valueOf("xxx:student");

//        deleteTable(admin, tableName);

//        deleteData(connection, admin, tableName);

        scan(connection, tableName);
    }

    // 扫描
    private static void scan(Connection connection, TableName tableName) throws IOException {
        Table table = connection.getTable(tableName);
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(1000));

        ResultScanner results = table.getScanner(scan);
        for (Result result : results) {
            System.out.println(result.current());
        }
    }

    // 删除数据
    private static void deleteData(Connection connection, Admin admin, TableName tableName) throws IOException {
        Table table = connection.getTable(tableName);

        String rowKey = "1001";
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes("family"), Bytes.toBytes("qualifier-column"));

        table.delete(delete);
    }

    // 删除表格
    private static void deleteTable(Admin admin, TableName tableName) throws IOException {
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }

        boolean b = admin.tableExists(tableName);
        if (!b) {
            System.out.println("表删除成功---");
        }
    }
}
