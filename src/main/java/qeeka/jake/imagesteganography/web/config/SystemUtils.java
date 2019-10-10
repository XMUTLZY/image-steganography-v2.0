package qeeka.jake.imagesteganography.web.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemUtils {
    public static String dateToFormat(Date date) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //HH表示24小时制；
        return dFormat.format(date);
    }
}
