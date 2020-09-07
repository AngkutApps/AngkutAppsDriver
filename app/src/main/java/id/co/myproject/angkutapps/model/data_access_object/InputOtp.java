package id.co.myproject.angkutapps.model.data_access_object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InputOtp {
    @SerializedName("maxAttempt")
    @Expose
    private int maxAttempt;

    @SerializedName("phoneNum")
    @Expose
    private String phoneNum;

    @SerializedName("expireIn")
    @Expose
    private int expireIn;

    @SerializedName("digit")
    @Expose
    private int digit;

    @SerializedName("otpstr")
    @Expose
    private String otpStr;

    public InputOtp() {
    }

    public InputOtp(int maxAttempt, String phoneNum, int expireIn, int digit) {
        this.maxAttempt = maxAttempt;
        this.phoneNum = phoneNum;
        this.expireIn = expireIn;
        this.digit = digit;
    }

    public InputOtp(int maxAttempt, int expireIn, int digit, String otpStr) {
        this.maxAttempt = maxAttempt;
        this.expireIn = expireIn;
        this.digit = digit;
        this.otpStr = otpStr;
    }

    public int getMaxAttempt() {
        return maxAttempt;
    }

    public void setMaxAttempt(int maxAttempt) {
        this.maxAttempt = maxAttempt;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public String getOtpStr() {
        return otpStr;
    }

    public void setOtpStr(String otpStr) {
        this.otpStr = otpStr;
    }
}

