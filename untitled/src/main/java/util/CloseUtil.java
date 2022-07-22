package util;

public class CloseUtil {

    public static void close(AutoCloseable... args) {
        if (args != null && args.length > 0) {
            for (AutoCloseable arg : args) {
                try {
                    if (null != arg) arg.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
