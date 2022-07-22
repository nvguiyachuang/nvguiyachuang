package util;

import java.io.*;

public class ReadTableUtil {
  public static void main(String[] args) throws Exception {
//    test1();
//    test2();
    test3();
  }

  private static void test3() throws Exception {
    String path = "C:\\Users\\xuzihang.LCB\\Desktop\\data_transfer\\table1.txt";
    FileInputStream fis = new FileInputStream(path);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line;

    while ((line = br.readLine()) != null) {
      if (!line.contains("KUDU")){
        System.out.println(line.trim());
      }
    }

    br.close();
  }

  private static void test2() throws Exception {
    String path = "C:\\Users\\xuzihang.LCB\\Desktop\\新文件 1.txt";
    FileInputStream fis = new FileInputStream(path);
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line;

    while ((line = br.readLine()) != null) {
      System.out.println("alter table lcb_mars_db." + line + " rename to lcb_mars_db.external" + line + ";");
    }

    br.close();
  }

  private static void test1() throws Exception {
    FileInputStream fis = new FileInputStream("C:\\Users\\xuzihang.LCB\\Downloads\\create_table_result.txt");
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String line;

    while ((line = br.readLine()) != null) {
      if (line.contains("CREATE") && line.contains("TABLE")) {
        line = line
          .replaceAll("CREATE", "")
          .replaceAll("TABLE", "")
          .replaceAll("IF", "")
          .replaceAll("NOT", "")
          .replaceAll("\\(", "")
          .replaceAll("EXISTS", "")
          .trim();

        System.out.println(line);
      }

      if (line.contains("STORED")) {
        System.out.println(line);
      }
    }

    br.close();
  }
}
