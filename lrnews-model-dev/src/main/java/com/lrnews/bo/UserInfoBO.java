package com.lrnews.bo;

import javax.validation.constraints.NotBlank;

public class UserInfoBO {
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Verify code is required")
    private String verifyCode;

    public UserInfoBO(String phone, String verifyCode) {
        this.phone = phone;
        this.verifyCode = verifyCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "UserInfoBO{" +
                "phone='" + phone + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
