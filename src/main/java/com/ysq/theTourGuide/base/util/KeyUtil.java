package com.ysq.theTourGuide.base.util;

import java.util.Random;

/**
 * @author ReMidDream
 * @date 2018-02-22 22:31
 **/
public class KeyUtil {

    public static synchronized String getUniqueKey(){
        Random random = new Random();
        Integer randomNumber = random.nextInt(900000)+100000;
        return System.currentTimeMillis() + String.valueOf(randomNumber);
    }
}
