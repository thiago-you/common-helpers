package you.thiago.commonhelpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import you.thiago.simplealert.SimpleAlert;

@SuppressWarnings("unused WeakerAccess")
public class DeviceHelper {

    public static final int PERMISSION_REQUEST_CODE = 99;

    public static void hasConnectivity(@NonNull Activity activity, @NonNull AsyncTask async) {
        if (async.isCancelled()) {
            return;
        }

        if (!isOnline(activity)) {
            if (!async.isCancelled()) {
                new SimpleAlert(activity)
                        .setType(SimpleAlert.ERROR)
                        .setTitle(activity.getString(R.string.connection_error_title))
                        .setMessage(activity.getString(R.string.connection_error_msg))
                        .setDismissTimeout(SimpleAlert.LENGTH_SHORT)
                        .show();

                async.cancel(true);
            }
        } else if (isConnectionSlow(activity)) {
            Toast.makeText(activity, activity.getString(R.string.connection_is_slow), Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                NetworkInfo network = manager.getActiveNetworkInfo();
                isConnected = network != null && network.isConnectedOrConnecting();
            } else {
                Network n = manager.getActiveNetwork();

                if (n != null) {
                    NetworkCapabilities nc = manager.getNetworkCapabilities(n);

                    if (nc != null) {
                        isConnected = nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                    }
                }
            }
        }

        return isConnected;
    }

    public static boolean isGpsOnline(Context context) {
        boolean status = false;

        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                status = true;
            }
        }

        return status;
    }

    public static boolean isConnectionSlow(Context context) {
        int netSubType = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo network = manager.getActiveNetworkInfo();

                if (network != null) {
                    netSubType = network.getSubtype();
                }
            }
        } else {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (manager != null) {
                if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                    netSubType = manager.getDataNetworkType();
                }
            }
        }

        return netSubType == TelephonyManager.NETWORK_TYPE_GPRS ||
                netSubType == TelephonyManager.NETWORK_TYPE_EDGE ||
                netSubType == TelephonyManager.NETWORK_TYPE_1xRTT;
    }

    public static boolean hasWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            return wifiInfo.getNetworkId() > -1;
        }

        return false;
    }

    public static ArrayList<String> getPermissionList(Activity activity) {
        ArrayList<String> permissionList = new ArrayList<>();

        try {
            String pack = activity.getPackageName();
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissionList.addAll(Arrays.asList(info.requestedPermissions));
            }
        } catch (Exception e) {
            Log.e(DeviceHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return permissionList;
    }

    public static boolean hasRequiredPermissions(Activity activity) {
        ArrayList<String> permissionList = DeviceHelper.getPermissionList(activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return DeviceHelper.getNotAllowedList(activity, permissionList).size() == 0;
        }

        return true;
    }

    public static boolean requestPermissions(Activity activity) {
        ArrayList<String> permissionList = DeviceHelper.getPermissionList(activity);
        return DeviceHelper.requestPermissions(activity, permissionList);
    }

    public static boolean requestPermissions(Activity activity, ArrayList<String> permissionList) {
        return DeviceHelper.requestPermissions(activity, permissionList.toArray(new String[0]));
    }

    public static boolean requestPermissions(Activity activity, String[] permissionList) {
        List<String> permissionsToRequest;
        boolean permissionStatus = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionsToRequest = DeviceHelper.getNotAllowedList(activity, permissionList);

            if (permissionsToRequest.size() > 0) {
                permissionStatus = false;

                ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[0]), DeviceHelper.PERMISSION_REQUEST_CODE);
            }
        }

        return permissionStatus;
    }

    /**
     * Show overlay permission request dialog
     */
    public static boolean requestOverlayPermissions(final Activity activity) {
        boolean permissionStatus = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                permissionStatus = false;

                new SimpleAlert(activity, SimpleAlert.STYLE_SYSTEM)
                        .setType(SimpleAlert.INFO)
                        .setMessage(activity.getString(R.string.overlay_permission_msg))
                        .setBtnConfirmTitle(activity.getString(R.string.btn_open_config))
                        .setConfirmClickListener(new SimpleAlert.OnSimpleAlertClickListener() {
                            @Override
                            public void onClick(SimpleAlert alertDialog) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                                activity.startActivityForResult(intent, PERMISSION_REQUEST_CODE);

                                alertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }

        return permissionStatus;
    }

    public static boolean checkCameraPermissions(Activity activity) {
        String[] permissionList = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        return DeviceHelper.requestPermissions(activity, permissionList);
    }

    public static boolean checkLocationPermission(Activity activity) {
        String[] permissionList = new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        return DeviceHelper.requestPermissions(activity, permissionList);
    }

    public static boolean checkReadPhonePermission(Activity activity) {
        String[] permissionList = new String[] { Manifest.permission.READ_PHONE_STATE };
        return DeviceHelper.requestPermissions(activity, permissionList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkReadPhoneNumberPermission(Activity activity) {
        String[] permissionList = new String[] {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.READ_SMS
        };

        return DeviceHelper.requestPermissions(activity, permissionList);
    }

    private static ArrayList<String> getNotAllowedList(Context context, ArrayList<String> permissionList) {
        return DeviceHelper.getNotAllowedList(context, permissionList.toArray(new String[0]));
    }

    private static ArrayList<String> getNotAllowedList(Context context, String[] permissionList) {
        ArrayList<String> requestList = new ArrayList<>();

        if (permissionList.length > 0) {
            for (String permission : permissionList) {
                if (!checkPermission(context, permission)) {
                    /* don't add system alert window */
                    if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW) || permission.equals("android.permission.ACTION_MANAGE_OVERLAY_PERMISSION")) {
                        continue;
                    }
                    /* add foreground permission only to API 28 and above (android 9) */
                    if (permission.equals(Manifest.permission.FOREGROUND_SERVICE) && Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                        continue;
                    }

                    requestList.add(permission);
                }
            }
        }

        return requestList;
    }

    private static boolean checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static Location getDeviceLocation(Activity activity) {
        Location location = null;
        LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null && DeviceHelper.checkLocationPermission(activity)) {
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location == null) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }

        return location;
    }

    @SuppressLint({"HardwareIds"})
    public static String getDeviceImei(Context context) {
        String imei = "";

        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                        imei = manager.getImei();
                    }
                } else {
                    if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                        imei = manager.getDeviceId();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(DeviceHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return imei;
    }

    @SuppressLint({"HardwareIds"})
    public static String getDevicePhoneNumber(Activity activity) {
        String phoneNumber = "";

        try {
            TelephonyManager manager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (DeviceHelper.checkReadPhoneNumberPermission(activity)) {
                        phoneNumber = manager.getLine1Number();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(DeviceHelper.class.getSimpleName(), e.getMessage(), e);
        }

        return phoneNumber;
    }

    public static boolean isAppRunning(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            final List<ActivityManager.RunningAppProcessInfo> processInfo = activityManager.getRunningAppProcesses();

            if (processInfo != null) {
                for (final ActivityManager.RunningAppProcessInfo process : processInfo) {
                    if (process.processName.equals(context.getPackageName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow((null == activity.getCurrentFocus())
                    ? null
                    : activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS
            );
        }
    }

    public static void hideKeyboardFromFragment(Activity activity, View view) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public static boolean hasIntent(Context context, Intent intent) {
        List<ResolveInfo> info = context.getPackageManager().queryIntentActivities(intent, 0);
        return info.size() != 0;
    }

    public static View.OnLongClickListener startRecognizeSpeechListener(final Activity activity, final String text, final int requestCode) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeviceHelper.startRecognizeSpeech(activity, text, requestCode);
                return true;
            }
        };
    }

    public static void startRecognizeSpeech(Activity activity, String text, int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, text);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        if (hasIntent(activity, intent)) {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void downloadFile(Context context, Uri uri, String filename) {
        /* create and configure request */
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(filename);

        /* get manager and start download */
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Objects.requireNonNull(manager).enqueue(request);
    }
}
