package com.online.edu.common.utils;

import java.util.Random;

public class RandomUtil {
    public static String getForBitRandom() {
        String code = "";
        Random random = new Random();
        for (int i=0; i<4; i++){
           code += random.nextInt(10) + "";
        }
        return code;
    }

    public static void main(String[] args) {
        String forBitRandom = RandomUtil.getForBitRandom();
        System.out.println(forBitRandom);
    }
}
