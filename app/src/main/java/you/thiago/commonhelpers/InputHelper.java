package you.thiago.commonhelpers;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.material.textfield.TextInputLayout;

@SuppressWarnings("unused WeakerAccess")
public class InputHelper {

    public static final String MASK_CPF = "###.###.###-##";
    public static final String MASK_CNPJ = "##.###.###/####-##";
    public static final String MASK_PHONE = "(##) ####-####";
    public static final String MASK_PHONE_EXT = "(##) #####-####";
    public static final String MASK_CEP = "#####-###";
    public static final String MASK_DATE = "##/##/####";
    public static final String MASK_VEHICLE_PLATE = "###-####";
    public static final String MASK_YEAR = "####";
    public static final String MASK_TIME = "##:##";

    public static final int DEFAULT_WATCHER = 0;
    public static final int PHONE_WATCHER = 1;
    public static final int EMAIL_WATCHER = 2;
    public static final int DATE_WATCHER = 3;
    public static final int VEHICLE_PLATE_WATCHER = 4;
    public static final int YEAR_WATCHER = 5;
    public static final int CPF_CNPJ_WATCHER = 6;
    public static final int TIME_WATCHER = 7;
    public static final int CEP_WATCHER = 8;
    public static final int DECIMAL_WATCHER = 9;

    private static final int DEFAULT_ERROR = 0;
    private static final int REQUIRED_ERROR = 1;
    private static final int LENGTH_ERROR = 2;
    private static final int MASK_LENGTH_ERROR = 3;
    private static final int EMAIL_ERROR = 4;
    private static final int DATE_ERROR = 5;

    private Activity activity;
    private AppCompatEditText editText;
    private String mask;
    private String altMask;
    private int minLength;
    private int maxLength;
    private int watcherPattern;
    private boolean required;
    private int maskMinLength;
    private int digitsBeforeZero;
    private int digitsAfterZero;

    private ValidationWatcher validationWatcher;
    private TextMask textMask;

    public InputHelper(Activity activity) {
        this.activity = activity;
    }

    public InputHelper() {
    }

    /**
     * Add text validation and mask watcher
     */
    public InputHelper build(EditText editText, TextInputLayout inputLayout, String inputName) {
        buildWatcher(editText, inputLayout, inputName);

        /* add mask */
        if (mask != null) {
            buildMask(activity, editText, mask);
        }

        return this;
    }

    /**
     * Add text validation watcher
     */
    public void buildWatcher(EditText editText, TextInputLayout inputLayout, String inputName) {
        if (activity == null) {
            validationWatcher = new ValidationWatcher(inputName, inputLayout, minLength, maxLength, required, watcherPattern, mask);
        } else {
            validationWatcher = new ValidationWatcher(activity, editText, inputName, inputLayout, minLength, maxLength, required, watcherPattern, mask);
        }

        if (maskMinLength > 0) {
            validationWatcher.setMaskMinLength(maskMinLength);
        }

        editText.addTextChangedListener(validationWatcher);
    }

    /**
     * Add decimal validation (EX: 10,2)
     */
    public void buildDecimalWatcher(EditText editText) {
        editText.setFilters(decimalWatcher(editText, digitsBeforeZero, digitsAfterZero));
    }

    /**
     * Add text mask watcher
     */
    public void buildMask(Activity activity, EditText editText, String mask) {
        /* create input mask */
        textMask = new TextMask(activity, editText, mask, altMask);

        /* set mask */
        textMask.setMaskDigits(textMask.getMaskDigits(mask));

        /* set alternative mask */
        if (altMask != null && !altMask.equals(mask)) {
            textMask.setMaskDigits(textMask.getMaskDigits(altMask));
        }

        if (maskMinLength > 0) {
            textMask.setMaskMinLength(maskMinLength);
        }

        /* add mask */
        editText.addTextChangedListener(textMask);
    }

    public void removeWatchers(EditText editText) {
        if (editText != null) {
            if (validationWatcher != null) {
                editText.removeTextChangedListener(validationWatcher);
            }
            if (textMask != null) {
                editText.setFilters(new InputFilter[] {});
                editText.removeTextChangedListener(textMask);
            }
        }
    }

    public InputHelper setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    public InputHelper setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    public InputHelper setMaskMinLength(int maskMinLength) {
        this.maskMinLength = maskMinLength;
        return this;
    }

    public InputHelper setPattern(int watcherPattern) {
        this.watcherPattern = watcherPattern;
        configPattern();

        return this;
    }

    public InputHelper setMask(String mask) {
        this.mask = mask;
        return this;
    }

    public InputHelper setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public InputHelper setAltMask(String altMask) {
        this.altMask = altMask;
        return this;
    }

    public InputHelper setDigitsBeforeZero(int digitsBeforeZero) {
        this.digitsBeforeZero = digitsBeforeZero;
        return this;
    }

    public InputHelper setDigitsAfterZero(int digitsAfterZero) {
        this.digitsAfterZero = digitsAfterZero;
        return this;
    }

    private void configPattern() {
        switch (watcherPattern) {
            case InputHelper.DEFAULT_WATCHER: {
                required = true;
                minLength = 0;
                maxLength = 0;
                break;
            }
            case InputHelper.PHONE_WATCHER: {
                minLength = 10;
                maxLength = 11;
                mask = InputHelper.MASK_PHONE;
                altMask = InputHelper.MASK_PHONE_EXT;
                break;
            }
            case InputHelper.EMAIL_WATCHER: {
                minLength = 3;
                break;
            }
            case InputHelper.DATE_WATCHER: {
                minLength = 0;
                maxLength = 8;
                mask = InputHelper.MASK_DATE;
                break;
            }
            case InputHelper.VEHICLE_PLATE_WATCHER: {
                minLength = 0;
                maxLength = 7;
                mask = InputHelper.MASK_VEHICLE_PLATE;
                break;
            }
            case InputHelper.YEAR_WATCHER: {
                minLength = 4;
                maxLength = 4;
                required = true;
                mask = InputHelper.MASK_YEAR;
                break;
            }
            case InputHelper.CPF_CNPJ_WATCHER: {
                minLength = 11;
                maxLength = 14;
                mask = InputHelper.MASK_CPF;
                altMask = InputHelper.MASK_CNPJ;
                break;
            }
            case InputHelper.TIME_WATCHER: {
                minLength = 0;
                maxLength = 4;
                mask = InputHelper.MASK_TIME;
                break;
            }
            case InputHelper.CEP_WATCHER: {
                minLength = 8;
                maxLength = 8;
                mask = InputHelper.MASK_CEP;
                break;
            }
            case InputHelper.DECIMAL_WATCHER: {
                digitsBeforeZero = 8;
                digitsAfterZero = 2;
                break;
            }
        }
    }

    /**
     * Add mask to text
     */
    public static String mask(String text, String mask) {
        if (text != null && !text.equals("")) {
            if (mask != null && !mask.equals("")) {
                StringBuilder maskedText = new StringBuilder();
                int textCharCount = 0;

                for (int i = 0; i < mask.length(); i++) {
                    if (mask.charAt(i) != '#') {
                        maskedText.append(mask.charAt(i));
                    } else {
                        if (textCharCount >= text.length()) {
                            break;
                        }

                        maskedText.append(text.charAt(textCharCount++));
                    }
                }

                /* add remaining chars */
                if (textCharCount < text.length()) {
                    for (int i = textCharCount; i < text.length(); i++) {
                        maskedText.append(text.charAt(i));
                    }
                }

                text = maskedText.toString();
            }
        }

        return text;
    }

    public static String unmask(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }

        return text.replaceAll("[-|./(): ]", "");
    }

    public static String unmask(String text, String mask) {
        if (text == null || text.length() == 0) {
            return text;
        }

        StringBuilder regex = new StringBuilder();

        for (int i = 0; i < mask.length(); i++) {
            /* "-" char must be at the beginning or end of the string, otherwise it is treated as "range" */
            if (mask.charAt(i) != '-') {
                regex.append(mask.charAt(i));
            } else {
                regex.insert(0, "-");
            }
        }

        regex = new StringBuilder("[" + regex + "]");
        return text.replaceAll(regex.toString(), "");
    }

    /**
     * Input typing validation
     */
    private static class ValidationWatcher implements TextWatcher {

        private static final int PHONE_MIN_LENGTH = 10;
        private static final int PHONE_MAX_LENGTH = 11;

        private Activity activity;
        private String inputName;
        private TextInputLayout inputLayout;
        private EditText editText;
        private String mask;
        private int minLength;
        private int maxLength;
        private int watcherPattern;
        private boolean required;
        private int maskMinLength;

        private ValidationWatcher(String inputName, TextInputLayout inputLayout, int minLength, int maxLength, boolean required, int watcherPattern, String mask) {
            this.mask = mask;
            this.required = required;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.inputName = inputName;
            this.inputLayout = inputLayout;
            this.watcherPattern = watcherPattern;
        }

        private ValidationWatcher(Activity activity, EditText editText, String inputName, TextInputLayout inputLayout, int minLength, int maxLength, boolean required, int watcherPattern, String mask) {
            this.activity = activity;
            this.editText = editText;
            this.mask = mask;
            this.required = required;
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.inputName = inputName;
            this.inputLayout = inputLayout;
            this.watcherPattern = watcherPattern;
        }

        public void setMaskMinLength(int maskMinLength) {
            this.maskMinLength = maskMinLength;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editableValue) {
            /* execute only when input has focus */
            if (activity != null  && editText != null && activity.getCurrentFocus() != editText) {
                return;
            }

            String value = editableValue.toString();

            /* unmask value */
            if (mask != null) {
                value = unmask(editableValue.toString());
            }

            boolean isValid = true;

            if (required && Validator.isEmpty(value)) {
                inputLayout.setError(getErrorMessage(InputHelper.REQUIRED_ERROR));
                isValid = false;
            } else {
                if (!Validator.validateLength(value, minLength, maxLength)) {
                    if (maskMinLength > 0 && value.length() < maskMinLength) {
                        inputLayout.setError(getErrorMessage(InputHelper.MASK_LENGTH_ERROR));
                    } else {
                        inputLayout.setError(getErrorMessage(InputHelper.LENGTH_ERROR));
                    }

                    isValid = false;
                } else {
                    if (watcherPattern == InputHelper.PHONE_WATCHER) {
                        if (maskMinLength > 0 && value.length() >= maskMinLength) {
                            if (!Validator.validateLength(value, PHONE_MIN_LENGTH, PHONE_MAX_LENGTH)) {
                                inputLayout.setError(getErrorMessage(InputHelper.LENGTH_ERROR));
                                isValid = false;
                            }
                        }
                    }
                    if (watcherPattern == InputHelper.EMAIL_WATCHER) {
                        if (!Validator.validateEmail(value)) {
                            inputLayout.setError(getErrorMessage(InputHelper.EMAIL_ERROR));
                            isValid = false;
                        }
                    }
                    if (watcherPattern == InputHelper.VEHICLE_PLATE_WATCHER) {
                        if (!Validator.validateVehiclePlate(value)) {
                            inputLayout.setError(getErrorMessage(InputHelper.DEFAULT_ERROR));
                            isValid = false;
                        }
                    }
                    if (watcherPattern == InputHelper.YEAR_WATCHER) {
                        if (!Validator.validateYearDigits(value)) {
                            inputLayout.setError(getErrorMessage(InputHelper.DEFAULT_ERROR));
                            isValid = false;
                        }
                    }
                    if (watcherPattern == InputHelper.TIME_WATCHER) {
                        if (!Validator.validateTimeDigits(value)) {
                            inputLayout.setError(getErrorMessage(InputHelper.DEFAULT_ERROR));
                            isValid = false;
                        }
                    }
                    if (watcherPattern == InputHelper.DATE_WATCHER) {
                        if (!Validator.validateDateDigits(editableValue.toString())) {
                            inputLayout.setError(getErrorMessage(InputHelper.DATE_ERROR));
                            isValid = false;
                        }
                    }
                }
            }

            /* remove error flags */
            if (isValid) {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            } else {
                inputLayout.setErrorEnabled(true);
            }
        }

        private String getErrorMessage(int errorCode) {
            String errorMsg = this.inputName;

            switch (watcherPattern) {
                case InputHelper.PHONE_WATCHER: {
                    errorMsg += phoneErrorMsg(errorCode);
                    break;
                }
                case InputHelper.DEFAULT_ERROR:
                case InputHelper.EMAIL_WATCHER:
                case InputHelper.DATE_ERROR:
                default: {
                    errorMsg += defaultErrorMsg(errorCode);
                    break;
                }
            }

            return errorMsg;
        }

        private String defaultErrorMsg(int errorCode) {
            String errorMsg;

            switch (errorCode) {
                case InputHelper.REQUIRED_ERROR: {
                    errorMsg = " é obrigatório(a).";
                    break;
                }
                case InputHelper.EMAIL_ERROR: {
                    errorMsg = " deve ser um email válido (exemplo@email.com).";
                    break;
                }
                case InputHelper.MASK_LENGTH_ERROR:
                case InputHelper.LENGTH_ERROR: {
                    if (minLength > 0 && maxLength > 0 && minLength == maxLength) {
                        errorMsg = " deve conter " + maxLength + " caracteres.";
                    } else if (minLength > 0 && maxLength > 0) {
                        errorMsg = " deve conter entre " + minLength + " e " + maxLength + " caracteres.";
                    } else if (minLength > 0) {
                        errorMsg = " precisa ter ao menos " + minLength + " caracteres.";
                    } else {
                        errorMsg = " precisa ter no máximo " + maxLength + " caracteres.";
                    }
                    break;
                }
                case InputHelper.DEFAULT_ERROR:
                case InputHelper.DATE_ERROR:
                default: {
                    errorMsg = " não é válido(a).";
                    break;
                }
            }

            return errorMsg;
        }

        private String phoneErrorMsg(int errorCode) {
            String errorMsg = "";

            switch (errorCode) {
                case InputHelper.REQUIRED_ERROR: {
                    errorMsg += " é obrigatório(a).";
                    break;
                }
                case InputHelper.MASK_LENGTH_ERROR: {
                    errorMsg += " deve conter ao menos 3 dígitos.";
                    break;
                }
                case InputHelper.LENGTH_ERROR: {
                    errorMsg += " deve conter o (DDD) + 8 ou 9 dígitos.";
                    break;
                }
            }

            return errorMsg;
        }
    }

    /**
     * Input mask validation
     */
    private static class TextMask implements TextWatcher {

        private boolean isRunning = false;
        private boolean isDeleting = false;
        private boolean isMasked = true;

        private Activity activity;
        private EditText editText;
        private String mask;
        private String firstMask;
        private String altMask;
        private int maskMinLength;

        private TextMask(EditText editText, String mask) {
            this.editText = editText;
            this.mask = mask;

            firstMask = mask;
            altMask = mask;

            /* init length filter */
            this.editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(altMask.length()) });
        }

        private TextMask(Activity activity, EditText editText, String mask, String altMask) {
            this.activity = activity;
            this.editText = editText;
            this.mask = mask;

            firstMask = mask;
            if (altMask == null || altMask.length() < mask.length()) {
                this.altMask = mask;
            } else {
                this.altMask = altMask;
            }

            /* init length filter */
            this.editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(this.altMask.length()) });
        }

        public void setMaskMinLength(int minLength) {
            maskMinLength = minLength;
        }

        public void setMaskDigits(String digits) {
            if (digits != null && !digits.equals("")) {
                editText.setKeyListener(DigitsKeyListener.getInstance(digits));
            }
        }

        private String getMaskDigits(String mask) {
            switch (mask) {
                case InputHelper.MASK_CPF: {
                    return "0123456789-.";
                }
                case InputHelper.MASK_CNPJ: {
                    return "0123456789-./";
                }
                case InputHelper.MASK_PHONE: {
                    return "0123456789-() ";
                }
                case InputHelper.MASK_CEP: {
                    return "0123456789-";
                }
                case InputHelper.MASK_DATE: {
                    return "0123456789\\/";
                }
            }

            return "";
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            /* remove length filter */
            editText.setFilters(new InputFilter[] {});
            /* set flag */
            isDeleting = count > after;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            /* validate running flag */
            if (isRunning) {
                return;
            }

            /* execute only when input has focus */
            if (activity != null && activity.getCurrentFocus() != editText) {
                return;
            }

            int editableLength = editable.length();

            /* unmask validation when deleting */
            if (isDeleting) {
                String value = InputHelper.unmask(editable.toString(), mask);
                if (isMasked && maskMinLength > 0 && value.length() < maskMinLength) {
                    isMasked = false;
                    editable.replace(0, editableLength, value);
                } else if (editableLength <= firstMask.length() && !mask.equals(firstMask)) {
                    isMasked = true;
                    mask = firstMask;
                    editable.replace(0, editableLength, InputHelper.mask(value, mask));
                } else {
                    return;
                }
            }

            /* set running flag */
            isRunning = true;

            /* validate alternative mask */
            if (editableLength > mask.length() && !mask.equals(altMask)) {
                isMasked = true;
                mask = altMask;

                String value = InputHelper.unmask(editable.toString());
                editable.replace(0, editable.length(), InputHelper.mask(value, mask));
            } else {
                /* validate input dynamic mask */
                if (maskMinLength > 0 && editableLength > 0 && editableLength == maskMinLength) {
                    isMasked = true;
                    editable.replace(0, editable.length(), InputHelper.mask(editable.toString(), mask));
                } else if (editableLength > maskMinLength) {
                    isMasked = true;
                    if (editableLength < mask.length()) {
                        if (mask.charAt(editableLength) != '#') {
                            editable.append(mask.charAt(editableLength));
                        } else if (mask.charAt(editableLength - 1) != '#' && editable.length() >= editableLength) {
                            editable.insert(editableLength - 1, mask, editableLength - 1, editableLength);
                        }
                    }
                }
            }

            /* set max length */
            editText.setFilters(new InputFilter[] {
                    new InputFilter.LengthFilter(altMask.length())
            });

            /* update running flag */
            isRunning = false;
        }
    }

    private InputFilter[] decimalWatcher(final EditText editText, final int beforeDecimal, final int afterDecimal) {
        editText.setFilters(new InputFilter[] {});

        return new InputFilter[] {
            new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (editText.hasFocus()) {
                        String text = editText.getText().toString();
                        String temp = text;

                        if (source.toString().contains(".")) {
                            if (text.contains(".")) {
                                return "";
                            }
                            temp += ".";
                        } else {
                            temp += source.toString().replace(text, "");
                        }

                        if (temp.equals(".")) {
                            return "0.";
                        } else if (!temp.contains(".")) {
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            int dotPosition;
                            int cursorPosition = editText.getSelectionStart();
                            if (text.contains(".")) {
                                dotPosition = text.indexOf(".");
                            } else {
                                dotPosition = temp.indexOf(".");
                            }

                            if (cursorPosition <= dotPosition) {
                                String beforeDot = text.substring(0, dotPosition);

                                if (beforeDot.length() < beforeDecimal) {
                                    if (source.toString().contains(".")) {
                                        return temp;
                                    } else {
                                        return source;
                                    }
                                } else {
                                    if (source.toString().contains(".")) {
                                        return temp;
                                    } else {
                                        return "";
                                    }
                                }
                            } else {
                                temp = temp.substring(temp.indexOf(".") + 1);
                                if (temp.length() > afterDecimal) {
                                    return "";
                                }
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }

                    return null;
                }
            }
        };
    }
}
