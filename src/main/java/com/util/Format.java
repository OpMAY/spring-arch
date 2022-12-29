package com.util;


import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 형 변환에 관련된 클래스
 */
@Slf4j
public class Format {
    /**
     * @return maskedPhoneNumber (String)
     * <p>
     * 전화번호 가운데 자리 * 처리 (마스킹)
     * @Param phoneNumber (String)
     * @Author OpMAY
     **/
    public static String maskPhoneNumber(String phoneNumber) {
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";

        Matcher matcher = Pattern.compile(regex).matcher(phoneNumber);
        if (matcher.find()) {
            String target = matcher.group(2);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return phoneNumber.replace(target, String.valueOf(c));
        }
        return phoneNumber;
    }

    /**
     * @return (hyphen removed PhoneNumber / hyphened PhoneNumber) (String)
     * <p>
     * 전화번호에 "-" 처리하기 / 삭제하기 (하이픈)
     * @Param phoneNumber (String), isMake (boolean)
     * @Author OpMAY
     **/
    public static String phoneNumberHyphen(String phoneNumber, boolean isMake) {
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
        String regex2 = "(\\d{4})-?(\\d{4})$";

        Matcher matcher = Pattern.compile(regex).matcher(phoneNumber);
        Matcher matcher2 = Pattern.compile(regex2).matcher(phoneNumber);
        if (matcher.find()) {
            // 하이픈 삭제
            phoneNumber = phoneNumber.replaceAll("-", "");
            if (isMake) {
                // 하이픈 넣기
                if (phoneNumber.length() == 11) {
                    // 010-1234-1234
                    phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);

                } else {
                    if (phoneNumber.startsWith("02")) { // 서울은 02-123-1234
                        phoneNumber = phoneNumber.substring(0, 2) + "-" + phoneNumber.substring(2, 5) + "-" + phoneNumber.substring(5);
                    } else { // 그 외는 017-123-1345, 053-634-0716
                        phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
                    }
                }
            }
        } else if (matcher2.find()) {
            phoneNumber = phoneNumber.replaceAll("-", "");
            // 1588-1234
            if (isMake) {
                phoneNumber = phoneNumber.substring(0, 4) + "-" + phoneNumber.substring(4);
            }
        }
        return phoneNumber;
    }

    /**
     * @return 전화번호 형태 맞는지 (boolean)
     * @Param phoneNumber (String) -> 하이픈 유무 상관 없음
     * @Author OpMAY
     **/
    public static boolean validPhoneNumber(String phoneNumber) {
        String regex = "(\\d{2,3})-?(\\d{3,4})-?(\\d{4})$";
        String regex2 = "(\\d{4})-?(\\d{4})$";
        Matcher matcher = Pattern.compile(regex).matcher(phoneNumber);
        Matcher matcher2 = Pattern.compile(regex2).matcher(phoneNumber);
        return matcher.find() || matcher2.find();
    }

    public void example() {
        // 변환할 값
        double n = 12345.6789;

        log.info("\n ======= 숫자형식 변환의 DecimalFormat 클래스 ======= \n");

        // 10진수 - 빈자리는 0으로 채움
        DecimalFormat df = new DecimalFormat("0");
        log.info(df.format(n));// 출력값 : 12346

        // 10진수 - 빈자리는 0으로 채움
        df = new DecimalFormat("0.0");
        log.info(df.format(n)); // 출력값 : 12345.7

        // 10진수 - 빈자리는 0으로 채움
        df = new DecimalFormat("000000.00000");
        log.info(df.format(n)); // 출력값 : 012345.67890

        // 10진수 - 빈자리는 채우지 않음
        df = new DecimalFormat("#");
        log.info(df.format(n)); // 출력값 : 12346

        // 10진수 - 빈자리는 채우지 않음
        df = new DecimalFormat("######.###");
        log.info(df.format(n)); // 출력값 : 12345.679

        // 10진수 - # 0 조합 / 단위구분을 위한 , 추가
        df = new DecimalFormat("#,###.00000");
        log.info(df.format(n)); // 출력값 : 12,345.67890

        // 100을 곱한 후 문자에 % 붙임
        df = new DecimalFormat("#.##%");
        log.info(df.format(n)); // 출력값 : 1234567.89%

        // 통화표시 \u00A4
        df = new DecimalFormat("\u00A4####.##");
        log.info(df.format(n)); // 출력값 : ￦12345.68

        // NumberFormat을 이용하여 통화표시를 지정할 수 있다.
        // Locale을 사용하여 국가 지정시 해당 국가의 통화기호 및 자리수 구분까지 된다.
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.KOREA);
        // 소수점 2자리까지 지정
        nf.setMaximumFractionDigits(2);
        log.info(nf.format(n)); // 출력값 : ￦12,345.68
    }


}