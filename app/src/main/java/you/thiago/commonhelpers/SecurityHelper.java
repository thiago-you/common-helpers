package you.thiago.commonhelpers;

import android.content.Context;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Objects;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SecurityHelper {
    /**
     * SSL password
     */
    public static final String OPEN_SSL_PASSWORD_CRYPT_KEY = "COMM0NHELP3R";

    public static String crypt(String password) {
        StringBuilder cryptPassword = new StringBuilder();

        try {
            /* encrypt password to MD5 */
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());

            /* set bytes MD5 to String */
            String hash = new BigInteger(1, m.digest()).toString(16);

            /* add 0 to the left to fill the hash */
            cryptPassword.append(hash);
            while (cryptPassword.toString().length() < 32) {
                cryptPassword.insert(0, "0");
            }
        } catch (Exception e) {
            Log.e(SecurityHelper.class.getSimpleName(), Objects.requireNonNull(e.getMessage()));
        }

        return cryptPassword.substring(0, 30);
    }

    public static boolean validateMainPassword(Context context, String password) {
        boolean isValidPass = false;

        if (password.equals(SecurityHelper.crypt(context.getString(R.string.main_app_pass))) ||
            password.equals(SecurityHelper.crypt(context.getString(R.string.main_user_pass)))
        ) {
            isValidPass = true;
        }

        return isValidPass;
    }
}
