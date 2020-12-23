package com.ysq.theTourGuide.controller;

import com.ysq.theTourGuide.base.dto.ResultDTO;
import com.ysq.theTourGuide.base.util.ResultUtil;
import com.ysq.theTourGuide.config.ErrorCode;
import com.ysq.theTourGuide.dto.*;
import com.ysq.theTourGuide.entity.*;
import com.ysq.theTourGuide.service.*;
import com.ysq.theTourGuide.service.redis.GuideGeoService;
import com.ysq.theTourGuide.utils.Location;
import com.ysq.theTourGuide.utils.ScoreUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class GuideController {

    @Autowired
    private TouristService touristService;

    @Autowired
    GuideService guideService;

    @Autowired
    RouteService routeService;

    @Autowired
    VideoService videoService;

    @Autowired
    LikeVideoService likeVideoService;

    @Autowired
    TheOrderService theOrderService;

    @Autowired
    GuideGeoService guideGeoService;

    @Autowired
    ScenicService scenicService;

    @Autowired
    UGuideService uGuideService;

    /**
     * 注册成为导游
     * @param guide
     * @return
     * @throws Exception
     */
    @PostMapping("/toBeAGuide")
    @ApiOperation("注册成为导游")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "姓名",name = "name",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "电话",name = "phone",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "导游证url",name = "touristCertificateUrl",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "等级",name = "level",dataType = "int",paramType = "query"),
            @ApiImplicitParam(value = "语言",name = "language",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "导游证号",name = "guide_number",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "所属组织",name = "organization",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "期限",name = "date",dataType = "String",paramType = "query"),
    })
    public ResultDTO toBeAGuide(GuideResiterDTO guide) throws Exception{
        Guide g = new Guide();
        g.setTouristId(guide.getTouristId());
        List<Guide> guides = guideService.findByParams(g);
        if(guides.size()!=0){
            if (uGuideService.findByParams(new UGuide(guide.getTouristId())).size() != 0) {
                return ResultUtil.Error(ErrorCode.ISEXIST);
            }
            UGuide guide2 = new UGuide(guide);
            guide2.setId(guides.get(0).getId());
            guide2.setState(3);
            uGuideService.save(guide2);
            return ResultUtil.Success();
        }
        return ResultUtil.Success(guideService.save(new Guide(guide)));
    }

    /**
     * 发布信息
     * @param route
     * @param video
     * @param touristId
     * @return
     */
    @PostMapping("/postRoute")
    @ApiOperation("发布信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "路线",name = "line",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "语言",name = "gLanguage",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "时长_天数",name = "rDay",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(value = "时长_夜数",name = "rNight",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(value = "景点个数",name = "noss",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "经典景点个数",name = "nosss",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "是否购物",name = "hShop",dataType = "Boolean",paramType = "query"),
            @ApiImplicitParam(value = "人数上限",name = "nOP",dataType = "int",paramType = "query"),
            @ApiImplicitParam(value = "价格",name = "price",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "优惠类型id,减免为1，折扣为2",name = "discountTypeId",dataType = "int",paramType = "query"),
            @ApiImplicitParam(value = "优惠额度",name = "discountValue",dataType = "int",paramType = "query"),
            @ApiImplicitParam(value = "服务描述",name = "rDescribe",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "路线id",name = "routeId",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(value = "景区id",name = "scenicId",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(value = "视频地址",name = "videoUrl",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "视频描述",name = "vDescribe",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "视频地址经度",name = "longitude",dataType = "Double",paramType = "query"),
            @ApiImplicitParam(value = "视频地址维度",name = "latitude",dataType = "Double",paramType = "query"),
            @ApiImplicitParam(value = "城市",name = "city",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "省份",name = "province",dataType = "String",paramType = "query"),
    })
    public ResultDTO postMsg(Route route, Video video,Long touristId )throws Exception{
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        route.setGuideId(guide.getId());
        video.setGuideId(guide.getId());
        video.setLikeNums(0);
        video.setDate(new Date());
        Video saveVideo = videoService.save(video);
        route.setVideoId(saveVideo.getId());
        Route saveRoute = routeService.save(route);
        saveVideo.setRouteId(saveRoute.getId());
        videoService.update(saveVideo);
        return ResultUtil.Success(new MsgDTO(saveRoute,saveVideo));
    }



    /**
     * 获得导游信息
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuideMsg")
    @ApiOperation("获得导游信息")
    public ResultDTO getGuideMsg(Long touristId)throws Exception{
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        Long guideId = guide.getId();
        Integer allLikeNums = 0;
        for(Video v: videoService.findByParams(new Video(guideId))){
            allLikeNums += v.getLikeNums();
        }
        Long videoNums = videoService.countAll(new Video(guideId));
        Long orderNums = theOrderService.countAll(new TheOrder(guideId,"111"));
        guideService.update(new Guide(guideId,allLikeNums,
                ScoreUtil.getScore(guide.getLevel(),videoNums,orderNums,allLikeNums)));
        return ResultUtil.Success(new GuideMsgDTO(guide,guide.getLevel(),ScoreUtil.getGuideLevelScore(guide.getLevel()),
                videoNums,ScoreUtil.getVideoNumsScore(videoNums),orderNums,ScoreUtil.getOrderNumsScore(orderNums),guide.getLikeNums(),ScoreUtil.getLikeNumsScore(guide.getLikeNums())));
    }

    /**
     * 修改导游信息
     * @param guideDTO
     * @param touristId
     * @return
     * @throws Exception
     */
    @PostMapping("/updateGuideMsg")
    @ApiOperation("修改导游信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "姓名",name = "name",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "电话",name = "phone",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "导游证",name = "touristCertificateUrl",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "导游证号",name = "theGuideNumber",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "所在机构",name = "organization",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "期限",name = "date",dataType = "String",paramType = "query"),
            @ApiImplicitParam(value = "导游年份",name = "years",dataType = "String",paramType = "query"),
    })
    public ResultDTO updateGuideMsg(GuideDTO guideDTO,Long touristId)throws Exception{
        Guide g = guideService.findByParams(new Guide(touristId)).get(0);
        if(g.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        Guide guide = new Guide();
        guide.setTouristId(touristId);
        guideDTO.setId(guideService.findByParams(guide).get(0).getId());
        guideService.updateDTO(guideDTO,Guide.class);
        return ResultUtil.Success();
    }

    /**
     * 获得我（导游）的视频
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getVideo")
    @ApiOperation("获得我（导游）的视频")
    public ResultDTO getVideo(Long touristId )throws Exception{
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        Long guideId = guide.getId();

        return ResultUtil.Success(videoService.findByParams(new Video(guideId)));
    }

    /**
     * 获得我的（导游）的喜欢
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getLike")
    public ResultDTO getLike(Long touristId)throws Exception{
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        LikeVideo likeVideo = new LikeVideo();
        likeVideo.setTouristId(touristId);
        List<Video> videoList = new ArrayList<>();
        for(LikeVideo l:likeVideoService.findByParams(likeVideo)){
            videoList.add(videoService.get(l.getLikeVideoId()));
        }
        return ResultUtil.Success(videoList);
    }

    /**
     * 获得我的预约
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMyOrder")
    @ApiOperation("获得我的预约")
    public ResultDTO getMyOrder(Long touristId)throws Exception {
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        Long guideId = guide.getId();
        List<TheOrderDTO> theOrderDTOS = new ArrayList<>();
        for(TheOrder t:theOrderService.findByParams(new TheOrder(guideId))){
            
            theOrderDTOS.add(new TheOrderDTO(t,routeService.get(t.getRouteId()).getLine(),null,null,null));
        }
        return ResultUtil.Success(theOrderDTOS);
    }


    /**
     * 保存导游位置
     * @param longitude
     * @param latitude
     * @param touristId
     * @return
     */
    @PostMapping("/saveLocation")
    @ApiOperation("保存导游位置")
    public ResultDTO saveLocation(Double longitude,Double latitude,Long touristId){
        guideGeoService.saveGuideLocation(new Location(touristId.toString(),longitude,latitude));
        return ResultUtil.Success();
    }

    /**
     * 得到路线以及视频
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getHisRouteAndVideo")
    @ApiOperation("得到路线以及视频")
    public ResultDTO getHisRouteAndVideo(Long touristId) throws Exception{
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        if(guide.getState()==2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        Long guideId = guide.getId();
        List<MsgDTO> msgDTOS = new ArrayList<>();
        for(Route r:routeService.findByParams(new Route(guideId))){
            msgDTOS.add(new MsgDTO(r,videoService.get(r.getVideoId())));
        }
        return ResultUtil.Success(msgDTOS);
    }


    /**
     * 得到某城市的景区
     * @param cityName
     * @return
     * @throws Exception
     */
    @GetMapping("/getScenic")
    @ApiOperation("得到某城市的景区")
    public ResultDTO getScenic(String cityName) throws Exception{
        return ResultUtil.Success(scenicService.findByParams(new Scenic(cityName)));
    }
}
