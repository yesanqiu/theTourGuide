package com.ysq.theTourGuide.utils;

public class ScoreUtil {



    public static Double getScore(Integer guideLevel,Long videoNums,Long orderNums,Integer likeNums){
        return 0.5*getGuideLevelScore(guideLevel) +
                0.15 * getVideoNumsScore(videoNums) +
                0.2 * getOrderNumsScore(orderNums) +
                0.15 * getLikeNumsScore(likeNums);
    }

    public static Integer getGuideLevelScore(Integer guideLevel){
        if(guideLevel == 1){
            return 60;
        }else if(guideLevel == 2){
            return 70;
        }else if(guideLevel == 3){
            return 80;
        }else if(guideLevel == 4){
            return 100;
        }
        return 0;
    }


    public static Integer getVideoNumsScore(Long videoNums){
        if(videoNums >= 0 && videoNums < 10){
            return 50;
        }else if(videoNums >= 10 && videoNums < 20){
            return 60;
        }else if(videoNums >= 20 && videoNums < 100){
            return 70;
        }else if(videoNums >= 100 && videoNums < 200){
            return 80;
        }else if(videoNums >= 200 && videoNums < 400){
            return 85;
        }else if(videoNums >= 400 && videoNums < 700){
            return 90;
        }else if(videoNums >= 700 && videoNums < 1000){
            return 95;
        }else if(videoNums >= 1000){
            return 100;
        }
        return 0;
    }

    public static Integer getOrderNumsScore(Long orderNums){
        if (orderNums >= 0 && orderNums < 10){
            return 60;
        }else if(orderNums >= 10 && orderNums < 20){
            return 70;
        }else if(orderNums >= 20 && orderNums < 40){
            return 75;
        }else if(orderNums >= 40 && orderNums < 100){
            return 80;
        }else if(orderNums >= 100 && orderNums < 200){
            return 85;
        }else if(orderNums >= 200 && orderNums < 400){
            return 90;
        }else if(orderNums >= 400 && orderNums < 500){
            return 95;
        }else if(orderNums >= 500){
            return 100;
        }
        return 0;
    }

    public static Integer getLikeNumsScore(Integer likeNums){
        if (likeNums >= 0 && likeNums < 1000){
            return 60;
        }else if(likeNums >= 1000 && likeNums < 3000){
            return 70;
        }else if(likeNums >= 3000 && likeNums < 6000){
            return 80;
        }else if(likeNums >= 6000 && likeNums < 10000){
            return 90;
        }else if(likeNums >= 10000 && likeNums < 20000){
            return 95;
        }else if(likeNums >= 20000){
            return 100;
        }
        return 0;
    }



}
