package you.thiago.commonhelpers;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("unused UnusedReturnValue WeakerAcess")
public class JsonHelper extends JSONObject {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private SimpleDateFormat dateFormatter;

    public JsonHelper() {
        super();
    }

    public JsonHelper(String stringForm) throws JSONException {
        super(stringForm);
    }

    /**
     * Init date formatter
     */
    private void initDateFormatter(String dateFormat) {
        this.dateFormatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
    }

    @NonNull
    public JsonHelper put(@NonNull String name, int value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, double value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, String value) {
        try {
            if (value != null) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, Object value) {
        try {
            if (value != null) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, int value, Integer defaultValue) {
        try {
            if (value >= 0) {
                super.put(name, value);
            } else {
                if (defaultValue != null) {
                    super.put(name, defaultValue);
                } else {
                    super.put(name, NULL);
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, double value, double defaultValue) {
        try {
            if (value > 0) {
                super.put(name, value);
            } else {
                super.put(name, defaultValue);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, String value, String defaultValue) {
        try {
            if (!Validator.isEmpty(value)) {
                super.put(name, value);
            } else {
                if (defaultValue == null) {
                    super.put(name, NULL);
                } else {
                    super.put(name, defaultValue);
                }
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, JSONArray value, JSONArray defaultValue) {
        try {
            if (value != null) {
                super.put(name, value);
            } else {
                super.put(name, defaultValue);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

//    TODO
//    @NonNull
//    public JsonHelper put(@NonNull String name, Bitmap value) {
//        try {
//            if (value != null) {
//                super.put(name, ImageComponent.getBitmapBase64(value));
//            } else {
//                super.put(name, "");
//            }
//        } catch (JSONException e) {
//            Log.e(getClass().getSimpleName(), e.getMessage(), e);
//        }
//
//        return this;
//    }

    @NonNull
    public JsonHelper put(@NonNull String name, Date value) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(DEFAULT_DATE_FORMAT);
                }

                String date = dateFormatter.format(value);
                super.put(name, date);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, Date value, String pattern) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(DEFAULT_DATE_FORMAT);
                }

                dateFormatter.applyPattern(pattern);
                String date = dateFormatter.format(value);
                super.put(name, date);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, Date value, String pattern, String dateFormat) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(dateFormat);
                }

                dateFormatter.applyPattern(pattern);
                String date = dateFormatter.format(value);
                super.put(name, date);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, JSONArray value) {
        try {
            if (value != null) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper put(@NonNull String name, boolean value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putIntNull(@NonNull String name, int value) {
        try {
            if (value > 0) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putIntNull(@NonNull String name, int value, int nullValue) {
        try {
            if (value > nullValue) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, int value) {
        try {
            if (value > 0) {
                super.put(name, value);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, double value) {
        try {
            if (value > 0) {
                super.put(name, value);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, String value) {
        try {
            if (!Validator.isEmpty(value)) {
                super.put(name, value);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putStringNull(@NonNull String name, String value) {
        try {
            if (!Validator.isEmpty(value)) {
                super.put(name, value);
            } else {
                super.put(name, NULL);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, Date value) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(DEFAULT_DATE_FORMAT);
                }

                String date = dateFormatter.format(value);
                super.put(name, date);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, Date value, String pattern) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(DEFAULT_DATE_FORMAT);
                }

                dateFormatter.applyPattern(pattern);
                String date = dateFormatter.format(value);
                super.put(name, date);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, Date value, String pattern, String dateFormat) {
        try {
            if (value != null) {
                if (dateFormatter == null) {
                    initDateFormatter(dateFormat);
                }

                dateFormatter.applyPattern(pattern);
                String date = dateFormatter.format(value);
                super.put(name, date);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

//    TODO
//    @NonNull
//    public JsonHelper putNonNull(@NonNull String name, Bitmap value) {
//        try {
//            if (value != null) {
//                super.put(name, ImageComponent.getBitmapBase64(value));
//            }
//        } catch (JSONException e) {
//            Log.e(getClass().getSimpleName(), e.getMessage(), e);
//        }
//
//        return this;
//    }

//    TODO
//    @NonNull
//    public JsonHelper putNonNull(@NonNull String name, Bitmap value, int imgWidth, int imgHeight, int imgQuality) {
//        try {
//            if (value != null) {
//                super.put(name, ImageComponent.getBitmapBase64(value, imgWidth, imgHeight, imgQuality));
//            }
//        } catch (JSONException e) {
//            Log.e(getClass().getSimpleName(), e.getMessage(), e);
//        }
//
//        return this;
//    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, JSONArray value) {
        try {
            if (value != null && value.length() > 0) {
                super.put(name, value);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }

    @NonNull
    public JsonHelper putNonNull(@NonNull String name, ArrayList value) {
        try {
            if (value != null && value.size() > 0) {
                super.put(name, new JSONArray(value));
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return this;
    }
}
