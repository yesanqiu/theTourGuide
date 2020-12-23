package com.ysq.theTourGuide.utils;


import org.springframework.data.geo.Point;

import java.text.SimpleDateFormat;
import java.util.*;

public class MyMathUtil {

    public static Double getTwoPointDist(Point p1,Point p2){
        double EARTH_RADIUS = 6378.137;
        double firstRadianLongitude = p1.getX();
        double firstRadianLatitude = p1.getY();
        double secondRadianLongitude = p2.getX();
        double secondRadianLatitude = p2.getY();
        double a = firstRadianLatitude - secondRadianLatitude;
        double b = firstRadianLongitude - secondRadianLongitude;
        double cal = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(firstRadianLatitude) * Math.cos(secondRadianLatitude)
                * Math.pow(Math.sin(b / 2), 2)));
        cal = cal * EARTH_RADIUS;

        return Math.round(cal * 10000d) / 10000d;
    }

    public static Double getTwoPointDist(double lat1, double lng1, double lat2, double lng2){
        double EARTH_RADIUS = 6378.137;
        double radLat1 = lat1*Math.PI / 180.0;
        double radLat2 = lat2*Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double  b = lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;

        return Math.round(s * 10000d) / 10000d;
    }

    public static List<Date> returnSelectDays(Date startDay,Integer day){
        List<Date> dates = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.setTime(startDay);
        c.add(Calendar.DAY_OF_MONTH, -1);
        Date previousDay = c.getTime();
        dates.add(previousDay);
        dates.add(startDay);
        Date today = startDay;
        if(day != 0){
            for(int i = 0; i < day; i++){
                c.setTime(today);
                c.add(Calendar.DAY_OF_MONTH,+1);
                today = c.getTime();
                dates.add(today);
            }
        }
        return dates;
    }

    public static String getTime(Date time,Integer l){
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.DAY_OF_MONTH,l);
        Date lastDay = c.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time) + " - "  + sdf.format(lastDay);
    }

    public static Long getId(){
        Random random = new Random();
        String i = "";
        int index = 0;
        while(index < 10) {
            i += Integer.toString(random.nextInt(10));
            index ++;
        }
        System.out.println(i);
        return Long.valueOf(i);
    }

    public static String getTimeFromHere(Date time){
        Calendar beforeCalendar = Calendar.getInstance();
        beforeCalendar.setTime(time);
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(new Date());
        //年
        if(beforeCalendar.get(Calendar.YEAR) != nowCalendar.get(Calendar.YEAR)){
            return nowCalendar.get(Calendar.YEAR) - beforeCalendar.get(Calendar.YEAR)+ "年前";
        }

        //月
        if(beforeCalendar.get(Calendar.MONTH) != nowCalendar.get(Calendar.MONTH)){
            return nowCalendar.get(Calendar.MONTH) - beforeCalendar.get(Calendar.MONTH) + "月前";
        }

        //日
        if(beforeCalendar.get(Calendar.DAY_OF_MONTH) != nowCalendar.get(Calendar.DAY_OF_MONTH)){
            return nowCalendar.get(Calendar.DAY_OF_MONTH) - beforeCalendar.get(Calendar.DAY_OF_MONTH) + "天前";
        }

        //时
        if(beforeCalendar.get(Calendar.HOUR) != nowCalendar.get(Calendar.HOUR)){
            return nowCalendar.get(Calendar.HOUR) - beforeCalendar.get(Calendar.HOUR)+ "小时前";
        }

        //分
        if(beforeCalendar.get(Calendar.MINUTE) != nowCalendar.get(Calendar.MINUTE)){
            return nowCalendar.get(Calendar.MINUTE) - beforeCalendar.get(Calendar.MINUTE) + "分钟前";
        }

        //秒
        if(beforeCalendar.get(Calendar.SECOND) != nowCalendar.get(Calendar.SECOND)){
            return "1分钟前";
        }

        return "error";
    }


    public static Boolean isADay(Date d1,Date d2){
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public static Long timeFromHere(Date date){
        return (new Date().getTime() - date.getTime())/86400000;
    }

    public static String timeFormateSecond(Long second){
        Long min = Long.valueOf(0);
        Long hour = Long.valueOf(0);
        min = second/60;
        hour = min/60;
        min = min - (hour * 60);
        Long sec = second - (60*min) - (3600 *hour);
        if(hour == 0){
            return min+"分"+sec+"秒";
        }
        if(min == 0){
            return sec + "秒";
        }
        return hour + "小时" + min + "分" + sec+"秒";
    }

    public static boolean isOverToday(Date date,Integer day,Integer month,Integer year){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(c.get(Calendar.YEAR)+year,c.get(Calendar.MONTH)+month,c.get(Calendar.DAY_OF_MONTH)+day);
        return c.getTime().before(new Date());
    }
    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(2020,1,1);
        Date d = c.getTime();
        System.out.println(isOverToday(d,0,1,0));
    }
}
