package com.util;

import java.util.Random;

public class Utility {
    /**
     * @param tokenSize : Token을 구성할 사이즈
     * @return Token : 해당 파라미터 자리수의 Token 반환
     */
    public static String RandomToken(int tokenSize) {
        if (tokenSize >= 1) {
            Random rnd = new Random();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < tokenSize; i++) {
                // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
                if (rnd.nextBoolean()) {
                    buf.append((char) ((int) (rnd.nextInt(26)) + 97));
                } else {
                    buf.append((rnd.nextInt(10)));
                }
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    public static String RandomStringToken(int tokenSize) {
        if (tokenSize >= 1) {
            Random rnd = new Random();
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < tokenSize; i++) {
                // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
                if (rnd.nextBoolean()) {
                    buf.append((char) ((int) (rnd.nextInt(26)) + 97));
                } else {
                    buf.append((char) ((int) (rnd.nextInt(26)) + 97));
                }
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    /**
     * @param tokenSize : Min : 1, Max : 16
     * @return : String Token(All Number)
     * Description : 토큰 생성기
     * Date : 2021-10-12
     * Version : 1
     */
    public static String RandomIntegerToken(int tokenSize) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        long token = Math.abs(random.nextLong());
        return String.valueOf(token).substring(0, tokenSize);
    }

    public static String RandomTokenIntAndCapital(int tokenSize) {
        if (tokenSize >= 1) {
            Random rnd = new Random();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < tokenSize; i++) {
                if (rnd.nextBoolean()) { // 대문자
                    buf.append((char) ((rnd.nextInt(26)) + 65));
                } else { // 숫자
                    buf.append((rnd.nextInt(10)));
                }
            }
            return buf.toString();
        } else {
            return null;
        }
    }

    public static Integer RandomInteger(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }
}
