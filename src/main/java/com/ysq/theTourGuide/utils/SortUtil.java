package com.ysq.theTourGuide.utils;

import com.ysq.theTourGuide.dto.VideoDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类
 * @author 叶三秋
 * @date 2020/2/16
 */
public class SortUtil {

    /**
     * 排序视频根据点赞数
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @return
     */
    public static List<VideoDTO> sortVideoByLikeNums(List<VideoDTO> list,String orderBy){
        if(orderBy == "ASC") {
            Collections.sort(list,
//                    (o1,o2) -> o1.getLikeNums() - o2.getLikeNums() 等同于以下语句
                    Comparator.comparingInt(VideoDTO::getLikeNums)
            );
            return list;
        }else if(orderBy == "DESC"){
            Collections.sort(list, (o1,o2) -> o2.getLikeNums() - o1.getLikeNums());
            return list;
        }else{
            return null;
        }
    }

    /**
     * 排序视频根据点赞数且分页
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @return
     */
    public static List<VideoDTO> sortVideoByLikeNumsAndPage(List<VideoDTO> list,String orderBy,int pageNums,int pageSize){
        if(orderBy == "ASC") {
            Collections.sort(list,
//                    (o1,o2) -> o1.getLikeNums() - o2.getLikeNums() 等同于以下语句
                    Comparator.comparingInt(VideoDTO::getLikeNums)
            );
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return list;
        }else if(orderBy == "DESC"){
            Collections.sort(list, (o1,o2) -> o2.getLikeNums() - o1.getLikeNums());
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 排序视频根据导游等级
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @return
     */
    public static List<VideoDTO> sortByGuideLevel(List<VideoDTO> list, String orderBy){
        if(orderBy == "ASC"){
            Collections.sort(list,
//                    (o1, o2) -> o1.getGuideLevel() - o2.getGuideLevel()
                    Comparator.comparingInt(VideoDTO::getGuide_level)
            );
            return list;
        }else if(orderBy == "DESC"){
            Collections.sort(list, (o1,o2) -> o2.getGuide_level() - o1.getGuide_level());
            return list;
        }else {
            return null;
        }


    }

    /**
     * 排序视频根据导游等级且分页
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @return
     */
    public static List<VideoDTO> sortByGuideLevelAndPage(List<VideoDTO> list, String orderBy,int pageNums,int pageSize) {
        if (orderBy == "ASC") {
            Collections.sort(list,
//                    (o1, o2) -> o1.getGuideLevel() - o2.getGuideLevel()
                    Comparator.comparingInt(VideoDTO::getGuide_level)
            );
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return list;
        } else if (orderBy == "DESC") {
            Collections.sort(list, (o1, o2) -> o2.getGuide_level() - o1.getGuide_level());
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 排序视频根据距离
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @return
     */
    public static List<VideoDTO> sortByDistance(List<VideoDTO> list, String orderBy){
        if(orderBy == "ASC"){
            Collections.sort(list,
//                    (o1, o2) -> o1.getDistance() - o2.getDistance()
                    Comparator.comparingDouble(VideoDTO::getDistance)
            );
            return list;
        }else if(orderBy == "DESC"){
            Collections.sort(list, (o1,o2) -> (int)(Math.round(o2.getDistance()) - Math.round(o1.getDistance())));
            return list;
        }else {
            return null;
        }
    }


    /**
     * 排序视频根据距离且分页
     * @param list
     * @param orderBy ASC为正序，DESC为倒序
     * @param pageNums 页码
     * @param pageSize 页长
     * @return
     */
    public static List<VideoDTO> sortByDistanceAndPage(List<VideoDTO> list, String orderBy,int pageNums,int pageSize){

        if(orderBy == "ASC"){
            Collections.sort(list,
//                    (o1, o2) -> o1.getDistance() - o2.getDistance()
                    Comparator.comparingDouble(VideoDTO::getDistance)
            );
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return videoDTOS;
        }else if(orderBy == "DESC"){
            Collections.sort(list, (o1,o2) -> (int)(Math.round(o2.getDistance()) - Math.round(o1.getDistance())));
            List<VideoDTO> videoDTOS = new ArrayList<>();
            if(pageNums*pageSize+pageSize>list.size()) {
                videoDTOS = list.subList(pageNums*pageSize,list.size());
            }else {
                videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
            }
            return videoDTOS;
        }else {
            return null;
        }
    }

    public static List<VideoDTO> sortReturnPage(List<VideoDTO> list,int pageNums,int pageSize){

        List<VideoDTO> videoDTOS = new ArrayList<>();
        if(pageNums*pageSize+pageSize>list.size()) {
            videoDTOS = list.subList(pageNums*pageSize,list.size());
        }else {
            videoDTOS = list.subList(pageNums * pageSize,pageNums * pageSize + pageSize);
        }
       return videoDTOS;
    }

    public static List<VideoDTO> findRangeVideo(List<VideoDTO> videoDTOS,Integer num){
        List<VideoDTO> resultVideoDTOS = new ArrayList<>();
        for(VideoDTO v:videoDTOS){
            if(v.getDistance()<num){
                resultVideoDTOS.add(v);
            }
        }
        return resultVideoDTOS;
    }
}