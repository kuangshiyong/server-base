package cn.bjzfgcjs.idefense.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Now {

    private static ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> formatter2 = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> formatter3 = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> formatter4 = new ThreadLocal<SimpleDateFormat>();

    private static ThreadLocal<SimpleDateFormat> formatter5 = new ThreadLocal<SimpleDateFormat>();

    public static String getNowString() {
        return Long.toString((new java.util.Date()).getTime() / 1000);
    }

    public static int getNow() {
        return (int) ((new java.util.Date()).getTime() / 1000);
    }

    public static long getTime() {
        return (new java.util.Date()).getTime() / 1000;
    }

    public static long getMillis() {
        return System.currentTimeMillis();
    }

    public static String getDatetime() {
        return getSdf1().format(new java.util.Date());
    }

    /*
     * 返回格式2指定的时间类型yyyyMMddHHmmss
     */
    public static String getDatetime2() {
        return (getSdf2().format(new java.util.Date()));

    }

    public static String monthsAfter(int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, months);
        return getSdf1().format(calendar.getTime());

    }

    public static String daysAfter(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return getSdf1().format(calendar.getTime());

    }

    /**
     * 按小时调整时间
     *
     * @param hours
     *            前进/后退小时数
     * @return yy-MM-dd
     */
    public static String hoursAfter(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return getSdf1().format(calendar.getTime());

    }

    public static void main(String... strings) {
        System.out.println(daysAfter(30));
    }

    public static SimpleDateFormat getSdf1() {
        SimpleDateFormat sdf = formatter.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.set(sdf);
        }
        return sdf;
    }

    public static SimpleDateFormat getSdf2() {
        SimpleDateFormat sdf = formatter2.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter2.set(sdf);
        }
        return sdf;
    }

    public static SimpleDateFormat getSdf3() {
        SimpleDateFormat sdf = formatter3.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            formatter3.set(sdf);
        }
        return sdf;
    }

    public static SimpleDateFormat getSdf4() {
        SimpleDateFormat sdf = formatter4.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyyMMdd");
            formatter4.set(sdf);
        }
        return sdf;
    }

    public static SimpleDateFormat getSdf5() {
        SimpleDateFormat sdf = formatter5.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            formatter5.set(sdf);
        }
        return sdf;
    }
}
