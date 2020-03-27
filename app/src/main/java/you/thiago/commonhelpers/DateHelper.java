package you.thiago.commonhelpers;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"unused", "WeakerAccess"})
public class DateHelper {

    /**
     * Default date format list
     */
    private static final String[] DEFAULT_FORMAT_LIST = new String[] {
            "dd/MM/yyyy HH:mm:ss",
            "dd/MM/yyyy",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
    };

    /**
     * Use default date format list to parse string into date
     */
    public static Date stringToDate(String value) {
        return DateHelper.stringToDate(value, DEFAULT_FORMAT_LIST);
    }

    /**
     * Use date format list from values.xml to parse string into date
     */
    public static Date stringToDate(String value, Context context) {
        String[] formatList = context.getResources().getStringArray(R.array.date_format_list);
        return DateHelper.stringToDate(value, formatList);
    }

    /**
     * Parse string into date
     */
    public static Date stringToDate(String value, String[] formatList) {
        String logMsg = "";
        Date date = null;

        if (value != null && !value.equals("") && !value.equals("null") && !value.equals("0000-00-00") &&  !value.equals("0000-00-00 00:00:00")) {
            for (String format : formatList) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                    date = formatter.parse(value);

                    /* clear log MSG */
                    logMsg = "";
                    break;
                } catch (Exception e) {
                    date = null;
                    logMsg = e.getMessage();
                }
            }

            /* set log */
            if (logMsg != null && !logMsg.equals("")) {
                Log.e(DateHelper.class.getSimpleName(), logMsg);
            }
        }

        return date;
    }

    /**
     * Parse string into date with defined pattern
     */
    public static Date stringToDate(String string, String pattern) {
        Date date = null;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
            date = formatter.parse(string);
        } catch (Exception e) {
            Log.e(DateHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return date;
    }

    /**
     * Use default date format list to parse date into string
     */
    public static String dateToString(Date value) {
        return DateHelper.dateToString(value, DEFAULT_FORMAT_LIST);
    }

    /**
     * Use date format list from values.xml to parse date into string
     */
    public static String dateToString(Date value, Context context) {
        String[] formatList = context.getResources().getStringArray(R.array.date_format_list);
        return DateHelper.dateToString(value, formatList);
    }

    /**
     * Parse date into string
     */
    public static String dateToString(Date value, String[] formatList) {
        String stringDate = "", logMsg = "";

        if (value != null) {
            for (String format : formatList) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                    stringDate = formatter.format(value);

                    /* clear log MSG */
                    logMsg = "";
                    break;
                } catch (Exception e) {
                    stringDate = null;
                    logMsg = e.getMessage();
                }
            }

            /* set log */
            if (logMsg != null && !logMsg.equals("")) {
                Log.e(DateHelper.class.getSimpleName(), logMsg);
            }
        }

        return stringDate;
    }

    /**
     * Parse date into string with defined pattern
     */
    public static String dateToString(Date date, String pattern) {
        String dateToString = "";
        try {
            if (date != null) {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
                dateToString = formatter.format(date);
            }
        } catch (Exception e) {
            Log.e(DateHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return dateToString;
    }

    /**
     * Format date with individual values
     */
    public static String formatDate(int year, int month, int day) {
        String date = day + "/" + (month + 1) + "/" + year;

        try {
            date = String.format(Locale.getDefault(), "%02d", day) + "/" + String.format(Locale.getDefault(), "%02d", (month + 1)) + "/" + year;
        } catch (Exception e) {
            Log.e(DateHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return date;
    }

    /**
     * Calculate age between some date and now
     */
    public static String getAge(Date value) {
        if (value == null) {
            return null;
        }

        int age;

        Calendar date = Calendar.getInstance();
        date.setTime(value);

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DATE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate localDate = LocalDate.of(year, month, day);
            age = Period.between(localDate, LocalDate.now()).getYears();
        } else {
            Calendar today = Calendar.getInstance();
            age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < date.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
        }

        return String.valueOf(age);
    }

    public static String getBirthDate(int age) {
        Calendar date = Calendar.getInstance();
        int birthYear = date.get(Calendar.YEAR) - age;

        return String.valueOf(birthYear);
    }

    /**
     * Return full month name
     */
    public static String getMonthName(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        /* get month name */
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.getTime());

        /* return capitalized month */
        return month.substring(0, 1).toUpperCase() + month.substring(1);
    }

    /**
     * Return year diff between an date and now
     */
    public static String getYear(Date value) {
        int year;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate date = value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            year = Period.between(date, LocalDate.now()).getYears();
        } else {
            Calendar date = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            date.setTime(value);

            /* get age based on year */
            year = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);

            /* validate age based on full date */
            if (
                (date.get(Calendar.MONTH) > today.get(Calendar.MONTH)) ||
                (date.get(Calendar.MONTH) == today.get(Calendar.MONTH) && date.get(Calendar.DATE) > today.get(Calendar.DATE))
            ) {
                year--;
            }
        }

        return year > 0 ? Integer.toString(year) : "0";
    }

    public static String millisecondsToTime(int milliseconds) {
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));

        return String.format(Locale.getDefault(), "%02d:%02d", milliseconds, seconds);
    }

    public static String addDaysToDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        return DateHelper.dateToString(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
    }
}
