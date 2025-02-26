package com.vlad.kuzhyr.driverservice.utility.logger;

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

    public static String maskFormattedCarNumber(String carNumber) {
        if (carNumber == null || carNumber.isEmpty()) {
            return carNumber;
        }

        String cleanedNumber = carNumber.replaceAll("[^A-Za-z0-9]", "");
        int length = cleanedNumber.length();

        if (length <= 4) {
            return carNumber;
        }

        return cleanedNumber.substring(0, 2) + "*".repeat(length - 4) + cleanedNumber.substring(length - 2);
    }

}
