/*
 */
package com.mycompany.itvieccrawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
/**
 *
 * @author namlq2
 */
public class AppUtil {

    private static final Logger _Logger = Logger.getLogger(AppUtil.class);

    public static String getTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date now = new Date();
        return dateFormat.format(now);
    }

    private static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public static String getPreviousHour(String format, int total) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date current = new Date();
        Date startDate = addHour(current, total * -1);
        return dateFormat.format(startDate);
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static String getCurrentDate(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static List<String> dateGenerate(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> result = new ArrayList<>();
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);
            Date date = startDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            while (true) {
                result.add(dateFormat.format(date));
                if (date.compareTo(endDate) == 0) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date = calendar.getTime();
            }
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static String nextDay(String day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            date = calendar.getTime();
            return dateFormat.format(date);
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public static List<String> dateIntervalGenerate(String start, int totalDay, String maxDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> result = new ArrayList<>();
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = addDay(startDate, totalDay);
            if (maxDay != null && !maxDay.isEmpty() && isAfter(dateFormat.format(endDate), maxDay, "yyyy-MM-dd")) {
                endDate = dateFormat.parse(maxDay);
            }

            Date date = startDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            while (true) {
                result.add(dateFormat.format(date));
                if (date.compareTo(endDate) == 0 || result.size() >= totalDay) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date = calendar.getTime();
            }
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private static boolean isAfter(String firstDay, String secondDate, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date startDate = dateFormat.parse(firstDay);
            Date endDate = dateFormat.parse(secondDate);
            return startDate.after(endDate);
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return false;
    }

    private static Date addDay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static List<String> dateGeneratePrevious(String start, int previous) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> result = new ArrayList<>();
        try {
            Date endDate = dateFormat.parse(start);
            Date startDate = addDay(endDate, previous * -1);
            Date date = startDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            while (true) {
                result.add(dateFormat.format(date));
                if (date.compareTo(endDate) == 0) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date = calendar.getTime();
            }
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }

        return result;
    }

    public static String getPreviousDate(String format, int total) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date current = new Date();
        Date startDate = addDay(current, total * -1);
        return dateFormat.format(startDate);
    }

    public static List<String> dateGenerate(String start, int totalDay, String maxDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> result = new ArrayList<>();
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = addDay(startDate, totalDay);
            if (maxDay != null && !maxDay.isEmpty() && isAfter(dateFormat.format(endDate), maxDay, "yyyy-MM-dd")) {
                endDate = dateFormat.parse(maxDay);
            }

            Date date = startDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            while (true) {
                result.add(dateFormat.format(date));
                if (date.compareTo(endDate) == 0 || result.size() >= totalDay) {
                    break;
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date = calendar.getTime();
            }
        } catch (ParseException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public static void sumMap(Map<String, Long> first, Map<String, Long> second) {
        for (String key : second.keySet()) {
            Long value = second.get(key);
            Long pre = first.get(key);
            if (pre == null) {
                pre = new Long(0);
            }
            pre += value;
            first.put(key, pre);
        }
    }

    public static String getFutureDay(String date, int total)  {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date anchorDate = dateFormat.parse(date);
            Date futureDate = addDay(anchorDate, total);
            return dateFormat.format(futureDate);
        } catch (ParseException ex) {
			_Logger.error(ex.getMessage(), ex);
        }
        return null;
    }
	
	public static long parseBaomoiTime(String date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date d = f.parse(date);
			return d.getTime();
		} catch (Exception e) {
			_Logger.error(e.getMessage(), e);
		}
		return 0;
	}
	
	public static String formatTimestamp(long timestamp, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			String d = f.format(timestamp);
			return d;
		} catch (Exception e) {
			_Logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static long getTimeStamp(String format, String date){
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			return f.parse(date).getTime();
		} catch (Exception e) {
			_Logger.info(e.getMessage(), e);
		}
		return -1;
	}
    public static void main(String args[]) {
        List<String> dates = AppUtil.dateGeneratePrevious("2018-04-19", 30);
		long timestamp = AppUtil.getTimeStamp("yyyy-MM-dd", "2019-03-08");
		System.out.println(timestamp);
        _Logger.info(dates);
    }
}
