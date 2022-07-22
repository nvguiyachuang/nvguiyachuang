package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseApi {

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

        // 判断命名空间
        try {
            admin.getNamespaceDescriptor("xxx");
        } catch (NamespaceNotFoundException e) {
            // 创建命名空间
            admin.createNamespace(NamespaceDescriptor.create("xxx").build());
        }

        TableName tableName = TableName.valueOf("xxx:student");
        boolean tableExists = admin.tableExists(tableName);
        if (!tableExists) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

            HColumnDescriptor columnDescriptor = new HColumnDescriptor("info");
            tableDescriptor.addFamily(columnDescriptor);

            admin.createTable(tableDescriptor);

            System.out.println("执行完毕");
        }else {
            Table table = connection.getTable(tableName);

            String rowKey = "1001";
            Get get = new Get(Bytes.toBytes(rowKey));
            Result result = table.get(get);

            if (result.isEmpty()) {
                // 查询的数据为空, 加点数据
                Put put = new Put(Bytes.toBytes(rowKey));
                String family = "info";
                String column = "name";
                String value = "小小";
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));

                table.put(put);
            }else {
                // 有数据
                Cell[] cells = result.rawCells();
                if (null != cells && cells.length >0) {
                    for (Cell cell : cells) {
                        byte[] bytes2 = CellUtil.cloneRow(cell);
                        System.out.println("rowKey：\t"+ Bytes.toString(bytes2));

                        byte[] bytes1 = CellUtil.cloneFamily(cell);
                        System.out.println("family：\t"+ Bytes.toString(bytes1));

                        byte[] bytes3 = CellUtil.cloneQualifier(cell);
                        System.out.println("column：\t"+ Bytes.toString(bytes3));

                        byte[] bytes = CellUtil.cloneValue(cell);
                        System.out.println("value：\t"+ Bytes.toString(bytes));
                    }
                }
            }
        }
    }
}
