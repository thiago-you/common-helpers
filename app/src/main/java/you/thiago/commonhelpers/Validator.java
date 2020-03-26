package you.thiago.commonhelpers;

import android.text.Editable;
import android.util.Log;
import android.util.Patterns;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "unused", "WeakerAccess"})
public class Validator {

    public static boolean isEmpty(String value) {
        return value == null || value.equals("null") || value.equals("") || value.trim().length() == 0;
    }

    public static boolean isEmpty(Editable editable) {
        return editable == null || isEmpty(editable.toString());
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Editable editable) {
        return editable != null && !isEmpty(editable.toString());
    }

    public static boolean isEmptyInteger(String value) {
        int intVal = 0;

        try {
            intVal = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.i(Validator.class.getSimpleName(), e.getMessage(), e);
        }

        return intVal == 0;
    }

    public static boolean isEmptyInteger(Editable editable) {
        if (isEmpty(editable)) {
            return true;
        }

        return isEmptyInteger(editable.toString());
    }

    public static boolean isNotEmptyInteger(String value) {
        return !isEmptyInteger(value);
    }

    public static boolean isNotEmptyInteger(Editable editable) {
        if (isNotEmpty(editable)) {
            return false;
        }

        return !isEmptyInteger(editable.toString());
    }

    public static boolean isEmptyDouble(String value) {
        double doubleVal = 0.0;

        try {
            doubleVal = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Log.i(Validator.class.getSimpleName(), e.getMessage(), e);
        }

        return doubleVal == 0.0;
    }

    public static boolean isEmptyDouble(Editable editable) {
        if (isEmpty(editable)) {
            return true;
        }

        return isEmptyDouble(editable.toString());
    }

    public static boolean isNotEmptyDouble(String value) {
        return !isEmptyDouble(value);
    }

    public static boolean isNotEmptyDouble(Editable editable) {
        if (isNotEmpty(editable)) {
            return false;
        }

        return !isEmptyDouble(editable.toString());
    }

    public static boolean validateLength(String value, int minLength, int maxLength) {
        boolean isValid = true;

        if (value != null && !value.equals("") && value.length() > 0) {
            if (minLength > 0 && value.length() < minLength) {
                isValid = false;
            }
            if (maxLength > 0 && value.length() > maxLength) {
                isValid = false;
            }
        }

        return isValid;
    }

    public static boolean validateEmail(String value) {
        boolean isValid = true;

        if (value.length() > 0) {
            if (value.length() < 3) {
                isValid = false;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                isValid = false;
            }
        }

        return isValid;
    }

    public static boolean validateDate(String value) {
        boolean isValid = true;

        /* date validator */
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                /* validate field lengths */
                String[] splitValue = value.split("/");
                if (splitValue.length != 3) {
                    throw new Exception();
                }

                int day = Integer.parseInt(splitValue[0]);
                int month = Integer.parseInt(splitValue[1]);
                int year = Integer.parseInt(splitValue[2]);

                if (month < 1 || month > 12) {
                    throw new Exception();
                }
                if (year < 1900 || year > 2100) {
                    throw new Exception();
                }
                if (day < 1 || day > maxDaysInMonth(year, month)) {
                    throw new Exception();
                }

                /* validate parse */
                try {
                    String formattedDate = formatter.format(Objects.requireNonNull(formatter.parse(value)));
                    if (!formattedDate.equals(value)) {
                        throw new Exception();
                    }
                } catch (ParseException e) {
                    throw new Exception();
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    public static boolean validateDate(String value, String dateFormat) {
        boolean isValid = true;

        /* date validator */
        DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                /* validate field lengths */
                String[] splitValue = value.split("/");
                if (splitValue.length != 3) {
                    throw new Exception();
                }

                int day = Integer.parseInt(splitValue[0]);
                int month = Integer.parseInt(splitValue[1]);
                int year = Integer.parseInt(splitValue[2]);

                if (month < 1 || month > 12) {
                    throw new Exception();
                }
                if (year < 1900 || year > 2100) {
                    throw new Exception();
                }
                if (day < 1 || day > maxDaysInMonth(year, month)) {
                    throw new Exception();
                }

                /* validate parse */
                try {
                    String formattedDate = formatter.format(Objects.requireNonNull(formatter.parse(value)));
                    if (!formattedDate.equals(value)) {
                        throw new Exception();
                    }
                } catch (ParseException e) {
                    throw new Exception();
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Specific validation for each date digits (while typing)
     */
    public static boolean validateDateDigits(String value) {
        boolean isValid = true;

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                int day, month, year;

                /* validate field lengths */
                String[] splitValue = value.split("/");

                if (splitValue.length == 1) {
                    day = Integer.parseInt(splitValue[0]);

                    if (day < 1 || day > 31) {
                        throw new Exception();
                    }
                }
                if (splitValue.length == 2) {
                    month = Integer.parseInt(splitValue[1]);
                    day = Integer.parseInt(splitValue[0]);

                    if (splitValue[1].length() == 1) {
                        if (month < 0 || month > 12) {
                            throw new Exception();
                        }
                    } else {
                        if (month < 1 || month > 12) {
                            throw new Exception();
                        }
                    }

                    if (day < 1 || day > maxDaysInMonth(0, month)) {
                        throw new Exception();
                    }
                }
                if (splitValue.length == 3) {
                    year = Integer.parseInt(splitValue[2]);

                    if (splitValue[2].length() == 1) {
                        if (year < 1 || year > 2) {
                            throw new Exception();
                        }
                    }
                    if (splitValue[2].length() == 2) {
                        if (year < 19 || year > 21) {
                            throw new Exception();
                        }
                    }
                    if (splitValue[2].length() == 3) {
                        if (year < 190 || year > 210) {
                            throw new Exception();
                        }
                    }
                    if (splitValue[2].length() == 4) {
                        month = Integer.parseInt(splitValue[1]);
                        day = Integer.parseInt(splitValue[0]);

                        if (year < 1900 || year > 2100) {
                            throw new Exception();
                        }
                        if (month < 1 || month > 12) {
                            throw new Exception();
                        }
                        if (day < 1 || day > maxDaysInMonth(year, month)) {
                            throw new Exception();
                        }
                    }
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Validation to year digits (while typing)
     */
    public static boolean validateYearDigits(String value) {
        boolean isValid = true;

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                int year = Integer.parseInt(value);

                if (value.length() == 1 && (year < 1 || year > 2)) {
                    throw new Exception();
                }
                if (value.length() == 2 && (year < 19 || year > 21)) {
                    throw new Exception();
                }
                if (value.length() == 3 && (year < 190 || year > 210)) {
                    throw new Exception();
                }
                if (value.length() == 4 && (year < 1900 || year > 2100)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Validation to vehicle plates (while typing)
     */
    public static boolean validateVehiclePlate(String value) {
        boolean isValid = true;

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                if (value.length() > 7) {
                    throw new Exception();
                }

                for (int i = 0; i < value.length(); i++) {
                    if (i < 3) {
                        if (!Character.isLetter(value.charAt(i))) {
                            throw new Exception();
                        }
                    } else {
                        if (i != 4 && !Character.isDigit(value.charAt(i))) {
                            throw new Exception();
                        }
                        if (i == 4 && !Character.isLetterOrDigit(value.charAt(i))) {
                            throw new Exception();
                        }
                    }
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Validation to time digits (while typing)
     */
    public static boolean validateTimeDigits(String value) {
        boolean isValid = true;

        if (value != null && !value.equals("null") && value.length() > 0) {
            try {
                int time = Integer.parseInt(value);

                if (value.length() > 4) {
                    throw new Exception();
                }
                if (time < 0) {
                    throw new Exception();
                }

                if (value.length() == 1 && time > 2) {
                    throw new Exception();
                }
                if (value.length() == 2 && time > 23) {
                    throw new Exception();
                }
                if (value.length() == 3 && time > 235) {
                    throw new Exception();
                }
                if (value.length() == 4 && time > 2359) {
                    throw new Exception();
                }
            } catch (Exception e) {
                isValid = false;
            }
        }

        return isValid;
    }

    private static int maxDaysInMonth(int year, int month) {
        int daysInMonth;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysInMonth = 31;
                break;
            case 2:
                if (year > 0 && (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))) {
                    daysInMonth = 29;
                } else {
                    daysInMonth = 28;
                }
                break;
            default:
                daysInMonth = 30;
        }

        return daysInMonth;
    }

    public static String getValidUrl(String url) {
        String protocol = "";

        if (url.contains("http://")) {
            url = url.replace("http://", "");
            protocol = "http://";
        } else if (url.contains("https://")) {
            url = url.replace("https://", "");
            protocol = "https://";
        }

        /* remove all extra slashes from endpoint */
        url = url.replaceAll("/+", "/");

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return protocol + url;
    }
}
