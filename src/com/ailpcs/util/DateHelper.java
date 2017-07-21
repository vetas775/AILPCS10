package com.ailpcs.util;

import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换工具类
 *
 */
public class DateHelper {

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     *            日期类型
     * @return 日期字符串
     */
    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将Date类型转换为字符串
     *
     * @param date
     *            日期类型
     * @param pattern
     *            字符串格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date
     *            字符串类型
     * @return 日期类型
     */
    public static Date format(String date) {
        return format(date, null);
    }

    /**
     * 将字符串转换为Date类型
     *
     * @param date
     *            字符串类型
     * @param pattern
     *            格式
     * @return 日期类型
     */
    public static Date format(String date, String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (date == null || date.equals("") || date.equals("null")) {
            return new Date();
        }
        Date d = null;
        try {
            d = new java.text.SimpleDateFormat(pattern).parse(date);
        } catch (ParseException pe) {
        }
        return d;
    }

    /**
     * 把字符串转换为java.sql.Timestamp
     */
    public static Timestamp ReturnTimestamp(String date) {
        Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=null;
        try{
            d=(Date)f.parseObject(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new Timestamp(d.getTime());
    }

    /**
     * 将毫秒转换为年月日时分秒
     */
    public static String getYearMonthDayHourMinuteSecond(long timeMillis) {
        int timezone = 8; // 时区
        long totalSeconds = timeMillis / 1000;
        totalSeconds += 60 * 60 * timezone;
        int second = (int) (totalSeconds % 60);// 秒
        long totalMinutes = totalSeconds / 60;
        int minute = (int) (totalMinutes % 60);// 分
        long totalHours = totalMinutes / 60;
        int hour = (int) (totalHours % 24);// 时
        int totalDays = (int) (totalHours / 24);
        int _year = 1970;
        int year = _year + totalDays / 366;
        int month = 1;
        int day = 1;
        int diffDays;
        boolean leapYear;
        while (true) {
            int diff = (year - _year) * 365;
            diff += (year - 1) / 4 - (_year - 1) / 4;
            diff -= ((year - 1) / 100 - (_year - 1) / 100);
            diff += (year - 1) / 400 - (_year - 1) / 400;
            diffDays = totalDays - diff;
            leapYear = (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
            if (!leapYear && diffDays < 365 || leapYear && diffDays < 366) {
                break;
            } else {
                year++;
            }
        }

        int[] monthDays;
        if (diffDays >= 59 && leapYear) {
            monthDays = new int[] { -1, 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 };
        } else {
            monthDays = new int[] { -1, 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334 };
        }
        for (int i = monthDays.length - 1; i >= 1; i--) {
            if (diffDays >= monthDays[i]) {
                month = i;
                day = diffDays - monthDays[i] + 1;
                break;
            }
        }
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
    
    /**
     * 	获取当前时间的前一天
     * @return dBefore 
     */
    public static Date getBefore(){
    	Date dNow = new Date();   //当前时间
    	Date dBefore = null;

    	Calendar calendar = Calendar.getInstance(); //得到日历
    	calendar.setTime(dNow);//把当前时间赋给日历
    	calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
    	dBefore = calendar.getTime();   //得到前一天的时间


    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
    	String defaultStartDate = sdf.format(dBefore);    //格式化前一天
    	String defaultEndDate = sdf.format(dNow); //格式化当前时间


    	System.out.println("前一天的时间是：" + defaultStartDate);
    	System.out.println("当天的时间是：" + defaultEndDate);  

    	return dBefore;
    }

    /**
     * 	获取当前时间的后一天
     * @return dBefore 
     */
    public static Date getAfter(){
    	Date dNow = new Date();   //当前时间
    	Date dBefore = null;

    	Calendar calendar = Calendar.getInstance(); //得到日历
    	calendar.setTime(dNow);//把当前时间赋给日历
    	calendar.add(Calendar.DAY_OF_MONTH, +1);  //设置为前一天
    	dBefore = calendar.getTime();   //得到前一天的时间


    	//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
    	//String defaultStartDate = sdf.format(dBefore);    //格式化前一天
    	//String defaultEndDate = sdf.format(dNow); //格式化当前时间
    	//System.out.println("后一天的时间是：" + defaultStartDate);
    	//System.out.println("当天的时间是：" + defaultEndDate);  

    	return dBefore;
    }
    /**
    * 得到下月的指定日期
    */
    public static Date  getAfterMonthDay(int i,Date date) {
            Calendar c = Calendar.getInstance();//获得一个日历的实例   
            c.setTime(date);//设置日历时间   
            c.set(Calendar.DATE, i);
            c.add(Calendar.MONTH,+1);//在日历的月份上增加1个月   
            return c.getTime();

     }
    /**
     * 得到本月的指定日期
     */
     public static Date  getNowMonthDay(Integer i,Date date,Integer month) {
             Calendar c = Calendar.getInstance();//获得一个日历的实例   
             c.setTime(date);//设置日历时间   
             if(i != null){
            	 c.set(Calendar.DATE, i);
             }
             if(month != null){
            	 c.set(Calendar.MONTH, month-1);
             }
             return c.getTime();

      }
    /**
     * 得到指定日期的n天后的日期
     */
    public static Date  getAfterMonth(int day,Date date) {
        Calendar c = Calendar.getInstance();//获得一个日历的实例   
        c.setTime(date);//设置日历时间   
        c.add(Calendar.DATE,day);//在日历的日期上增加day天
        
        return c.getTime();

 }
    
    /** 
     * 根据年 月 获取对应的月份 天数  2015-01
     * */  
    public static int getDaysByYearMonth(String date) {  
        String riqi[] =  date.split("-");
    
    	int year= Integer.parseInt(riqi[0]);
    	int month = Integer.parseInt(riqi[1]) ;
    	
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }  
    /**
     * 得到当前月份N个月后的月份 n可以是负数即n个月前
     */
     public static Date  getNMonth(int i,Date date) {
             Calendar c = Calendar.getInstance();//获得一个日历的实例   
             if(date == null){
            	 date = new Date();
             }
             c.setTime(date);//设置日历时间   
             c.add(Calendar.MONTH,+i);//在日历的月份上增加i个月   
             return c.getTime();

      }
     
     /**
      * 获取指定年份的指定月份
      */
      public static Date  getSpecifiedMonth(int year,int month) {
    	  Calendar a = Calendar.getInstance();  
          a.set(Calendar.YEAR, year);  
          a.set(Calendar.MONTH, month - 1);  
          a.set(Calendar.DATE, 1);  
          return a.getTime();

       }
      /**
       * 
       * @param endDate 大时间
       * @param nowDate 小时间
       * @return
       */
      public static String getBetweenTime(Date endDate, Date nowDate) {
    		 
  	    long nd = 1000 * 24 * 60 * 60;
  	    long nh = 1000 * 60 * 60;
  	    long nm = 1000 * 60;
  	    // long ns = 1000;
  	    // 获得两个时间的毫秒时间差异
  	    long diff = endDate.getTime() - nowDate.getTime();
  	    // 计算差多少天
  	    long day = diff / nd;
  	    // 计算差多少小时
  	    long hour = diff % nd / nh;
  	    // 计算差多少分钟
  	    long min = diff % nd % nh / nm;
  	    // 计算差多少秒//输出结果
  	    // long sec = diff % nd % nh % nm / ns;
  	    return day + "天" + hour + "小时" + min + "分钟";
  	}
     /*
      * 返回当前月的第一日的日期，格式：2016-09-01
      */
  	public static String findYearMonth()
  	{
  		/**
  		 * 声明一个int变量year
  		 */
  		int year;
  		/**
  		 * 声明一个int变量month
  		 */
  		int month;
  		/**
  		 * 声明一个字符串变量date
  		 */
  		String date;
  		/**
  		 * 实例化一个对象calendar
  		 */
  		Calendar calendar = Calendar.getInstance();
  		/**
  		 * 获取年份
  		 */
  		year = calendar.get(Calendar.YEAR);
  		/**
  		 * 获取月份
  		 */
  		month = calendar.get(Calendar.MONTH) + 1;
  		/**
  		 * 拼接年份和月份
  		 */
  		date = year + "-" + ( month<10 ? "0" + month : month)+"-"+"01";
  		/**
  		 * 返回当前年月
  		 */
  		return date;
  	}
  	/**
  	 * 对日期进行格式化
  	 * @param date
  	 * @param pattern
  	 * @return
  	 */
  	public static Date formatDate(Date date , String pattern){
  		if(date==null){
  			return null;
  		}else{
  	  		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
  	  		String defaultStartDate = sdf.format(date);
  	  		Date d = null;
  	  		try {
  	  			d =sdf.parse(defaultStartDate);
  			} catch (ParseException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  	  		return d;
  		}

  	}
  	
  	
  	/**
  	 * 把传入值转换成时分秒的格式 ： 123小时，59分59秒
  	 * @param seconds 秒
  	 * @return
  	 */
  	public static String formatSeconds(int seconds) { 
  		int theTime1 = 0;// 分 
  		int theTime2 = 0;// 小时 
  		// alert(theTime); 
  		if(seconds > 60) { 
  			theTime1 = seconds/60; 
  			seconds = seconds%60; 
  		// alert(theTime1+"-"+theTime); 
  			if(theTime1 > 60) { 
  				theTime2 = theTime1/60; 
  				theTime1 = theTime1%60; 
  			} 
  		} 
  		String  result = ""+seconds+"秒"; 
  		if(theTime1 > 0) { 
  			result = ""+theTime1+"分"+result; 
  		} 
  		if(theTime2 > 0) { 
  			result = ""+theTime2+"小时"+result; 
  		} 
  		return result; 
  	} 
  	
}

