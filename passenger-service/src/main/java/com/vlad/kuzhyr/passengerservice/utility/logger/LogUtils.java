package com.vlad.kuzhyr.passengerservice.utility.logger;

public class LogUtils {

    public static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1) {
            return email;
        }

        String localPart = email.substring(0, Math.min(atIndex, 3));
        String domain = email.substring(atIndex);

        return localPart + "***" + domain;
    }

    public static String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }

        int length = phone.length();
        if (length <= 4) {
            return phone;
        }

        String visibleSuffix = phone.substring(length - 4);
        return "******" + visibleSuffix;
    }

}
