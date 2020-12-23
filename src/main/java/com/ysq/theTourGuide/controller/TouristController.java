package com.ysq.theTourGuide.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysq.theTourGuide.base.dto.ResultDTO;
import com.ysq.theTourGuide.base.page.PageList;
import com.ysq.theTourGuide.base.util.ResultUtil;
import com.ysq.theTourGuide.config.ErrorCode;
import com.ysq.theTourGuide.config.OrderState;
import com.ysq.theTourGuide.config.RecommendAttrs;
import com.ysq.theTourGuide.config.TaskEnum;
import com.ysq.theTourGuide.dto.*;
import com.ysq.theTourGuide.entity.*;
import com.ysq.theTourGuide.service.*;
import com.ysq.theTourGuide.service.redis.IGeoService;
import com.ysq.theTourGuide.service.redis.RedisService;
import com.ysq.theTourGuide.utils.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TouristController {


    @Value("${app.appid}")
    private String appid;

    @Value("${app.appSecret}")
    private String appSecret;

    @Value("${app.appTimeOut}")
    private long appTimeOut;

    @Value("${range}")
    private Integer range;


    @Autowired
    TouristService touristService;
    @Autowired
    VideoService videoService;

    @Autowired
    RedisService redisService;

    @Autowired
    IGeoService geoService;

    @Autowired
    GuideService guideService;

    @Autowired
    ScenicService scenicService;

    @Autowired
    TheOrderService theOrderService;

    @Autowired
    LikeVideoService likeVideoService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeCommentService likeCommentService;

    @Autowired
    MessageService messageService;

    @Autowired
    RouteService routeService;

    @Autowired
    VipService vipService;


    @Autowired
    TaskService taskService;

    @Autowired
    LookVideoService lookVideoService;

    @Autowired
    ScenicSpotService scenicSpotService;

    @Autowired
    FansService fansService;

    @Autowired
    CardService cardService;

//    @PostMapping("/login")
    public ResultDTO login(String encryptedData, String iv, String code){
        if(!StringUtils.isNotBlank(code)){
            return ResultUtil.Error(ErrorCode.INVALID_PARAMETERS);
        }
        String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+appSecret+"&js_code="+code+"&grant_type=authorization_code";
        System.out.println(apiUrl);
        String responseBody = HttpClientUtil.doGet(apiUrl);
        System.out.println(responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if(StringUtils.isNotBlank(jsonObject.getString("openid"))&&StringUtils.isNotBlank(jsonObject.getString("session_key"))){
            //解密获取用户信息
            JSONObject userInfoJSON= WechatGetUserInfoUtil.getUserInfo(encryptedData,jsonObject.getString("session_key"),iv);
            if(userInfoJSON!=null){
                //这步应该set进实体类
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                // 解密unionId & openId;
                if (userInfoJSON.get("unionId")!=null) {
                    userInfo.put("unionId", userInfoJSON.get("unionId"));
                }
                //然后根据openid去数据库判断有没有该用户信息，若没有则存入数据库，有则返回用户数据
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("userInfo", userInfo);
                String uuid=UUID.randomUUID().toString();
                dataMap.put("WXTOKEN", uuid);
                redisService.set(uuid,userInfo,appTimeOut);
//                redisTemplate.opsForValue().set(uuid,userInfo);
//                redisTemplate.expire(uuid,appTimeOut, TimeUnit.SECONDS);
                return ResultUtil.Success(dataMap);
            }else{
                return ResultUtil.Error(ErrorCode.UNKNOWERROR);
            }
        }else{
            return ResultUtil.Error(ErrorCode.UNKNOWERROR);
        }


    }

    /**
     * 登录接口返回openid 以及 session_key
     * @param code
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录接口返回openid 以及 session_key")
    public ResultDTO login(String code)throws Exception{
        if(!StringUtils.isNotBlank(code)){
            return ResultUtil.Error(ErrorCode.UNKNOWERROR);
        }
        String aid = scenicSpotService.get(Integer.parseInt(appid)).getName();
        String as = scenicSpotService.get(Integer.parseInt(appSecret)).getName();
        String apiUrl="https://api.weixin.qq.com/sns/jscode2session?appid="+aid+"&secret="+as+"&js_code="+code+"&grant_type=authorization_code";
        System.out.println(apiUrl);
        String responseBody = HttpClientUtil.doGet(apiUrl);
        System.out.println(responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);

        return ResultUtil.Success(jsonObject);
    }

    /**
     * 提交用户信息，若已存在则对比信息是否一致，否则更新数据库
     * @param userInfo
     * @return
     * @throws Exception
     */
    @PostMapping("/saveUserInfo")
    @ApiOperation("提交用户信息，若已存在则对比信息是否一致，否则更新数据库")
    public ResultDTO saveUserInfo(UserInfo userInfo)throws Exception{
        List<Tourist> touristList = touristService.findByParams(new Tourist(userInfo.getOpenId()));
        if(touristList.size() == 0){
            Tourist tourist = new Tourist(userInfo);
            Long id = MyMathUtil.getId();
            List<Long> ids = new ArrayList<>();
            for(Tourist t: touristService.findAll()){
                ids.add(t.getId());
            }
            while (ids.contains(id)){
                id = MyMathUtil.getId();
            }
            tourist.setId(id);
            Tourist save = touristService.save(tourist);
            return ResultUtil.Success(save);
        }else{
            Tourist tourist = touristList.get(0);
            if(!userInfo.equals(new UserInfo(tourist))){
                touristService.update(new Tourist(tourist.getId(),userInfo));
                return ResultUtil.Success(tourist);
            }else{
                return ResultUtil.Success(tourist);
            }
        }

    }


    /**
     * 根据参数排序视频给用户
     * @param attr //level，游客等级，distance 离景区距离，goodNums 点赞数
     * @return
     * @throws Exception
     */
    @PostMapping("/recommend")
    @ApiOperation("根据参数排序视频给用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "attr",value = "level，游客等级，distance 离景区距离，goodNums 点赞数",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "longitude",value = "经度",paramType = "query",dataType = "Double"),
            @ApiImplicitParam(name = "latitude",value = "维度",paramType = "query",dataType = "Double"),
            @ApiImplicitParam(name = "pageNum",value = "页码",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize",value = "页面长度",paramType = "query",dataType = "Integer"),
    })

    public ResultDTO recommend(String attr,Double longitude,Double latitude,Long touristId,Integer pageNum,Integer pageSize)throws Exception{
        List<VideoDTO> videoDTOS = new ArrayList<>();
        List<Video> videoList = new ArrayList<>();
        videoList = videoService.findAll();

        for(Video v:videoList){
            Guide guide = guideService.get(v.getGuideId());
            if(guide.getState()==2){
                continue;
            }
            Tourist tourist = touristService.get(guide.getTouristId());
            boolean isLike = likeVideoService.findByParams(new LikeVideo(v.getId(), touristId)).size() != 0;
            Integer comment_counts = commentService.findByParams(new Comment(v.getId())).size();
            boolean isFans = fansService.findByParams(new Fans(touristId, guide.getId())).size() != 0;
            Scenic s;
            if(v.getScenicId() == null){
                videoDTOS.add(new VideoDTO(v,
                        MyMathUtil.getTwoPointDist(latitude,longitude,v.getLatitude(),v.getLongitude()),
                        tourist.getAvatarUrl(),
                        guide.getLevel(),
                        isLike,
                        comment_counts,
                        v.getLikeNums(),
                        guide.getName(),
                        null,
                        v.getLongitude(),
                        v.getLatitude(),
                        guide.getGrade(),
                        guide.getId(),
                        isFans
                ));
            }else{
                s = scenicService.get(v.getScenicId());
                videoDTOS.add(new VideoDTO(v,
                        MyMathUtil.getTwoPointDist(latitude,longitude,s.getLatitude(),s.getLongitude()),
                        tourist.getAvatarUrl(),
                        guide.getLevel(),
                        isLike,
                        comment_counts,
                        v.getLikeNums(),
                        guide.getName(),
                        s.getCity(),
                        s.getLongitude(),
                        s.getLatitude(),
                        guide.getGrade(),
                        guide.getId(),
                        isFans
                ));
            }
        }
        System.out.println(videoDTOS.size());
        videoDTOS = SortUtil.findRangeVideo(videoDTOS,range);
        System.out.println(videoDTOS.size());
        List<VideoDTO> result = new ArrayList<>();
        if(attr.equals(RecommendAttrs.DIS.getAttr()) ){
            result = SortUtil.sortByDistanceAndPage(videoDTOS,"ASC",pageNum,pageSize);
        }else if(attr.equals(RecommendAttrs.LEV.getAttr()) ){
            result = SortUtil.sortByGuideLevelAndPage(videoDTOS,"DESC",pageNum,pageSize);
        }else if(attr.equals(RecommendAttrs.GN.getAttr()) ){
            result = SortUtil.sortVideoByLikeNumsAndPage(videoDTOS,"DESC",pageNum,pageSize);
        } else{
            return ResultUtil.Error(ErrorCode.INVALID_PARAMETERS);
        }
        System.out.println(result.size());
        return ResultUtil.Success(new PageList<>(result,pageNum,pageSize,videoDTOS.size()));
    }


    /**
     * 通过目的地和景区搜索
     * @param touristId
     * @param longitude
     * @param latitude
     * @param cityName
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findVideo")
    public ResultDTO findVideo(Long touristId,Double longitude,Double latitude,String cityName,String attr,Integer pageNum,Integer pageSize)throws Exception{
        List<VideoDTO> videoDTOS = new ArrayList<>();
        Integer totalItems = 0;
        List<Video> pageList = new ArrayList<>();
        for(Scenic s:scenicService.findScenicThree(attr)){
            Video video = new Video();
            video.setScenicId(s.getId());
            pageList = videoService.findByParams(video);
            totalItems += pageList.size();
            for(Video v:pageList){
                Guide guide = guideService.get(v.getGuideId());
                if(guide.getState()==2){
                    continue;
                }
                Tourist tourist = touristService.get(guide.getTouristId());
                boolean isLike = likeVideoService.findByParams(new LikeVideo(v.getId(), touristId)).size() != 0;
                Integer comment_counts = commentService.findByParams(new Comment(v.getId())).size();
                boolean isFans = fansService.findByParams(new Fans(touristId, guide.getId())).size() != 0;
                videoDTOS.add(new VideoDTO(v,
                        MyMathUtil.getTwoPointDist(
                                latitude,longitude,s.getLatitude(),s.getLongitude()),
                        tourist.getAvatarUrl(),
                        guide.getLevel(),
                        isLike,
                        comment_counts,
                        v.getLikeNums(),
                        guide.getName(),
                        s.getCity(),
                        s.getLongitude(),
                        s.getLatitude(),
                        guide.getGrade(),
                        guide.getId(),
                        isFans
                ));
            }
        }

        for(Video v:videoService.findVideoByLocation(attr)){
            totalItems++;
            System.out.println("video:" + v);
            Guide guide = guideService.get(v.getGuideId());
            if(guide.getState()==2){
                continue;
            }
            Tourist tourist = touristService.get(guide.getTouristId());
            boolean isLike = likeVideoService.findByParams(new LikeVideo(v.getId(), touristId)).size() != 0;
            Integer comment_counts = commentService.findByParams(new Comment(v.getId())).size();
            boolean isFans = fansService.findByParams(new Fans(touristId, guide.getId())).size() != 0;
            videoDTOS.add(new VideoDTO(v,
                    MyMathUtil.getTwoPointDist(
                            latitude,longitude,v.getLatitude(),v.getLongitude()),
                    tourist.getAvatarUrl(),
                    guide.getLevel(),
                    isLike,
                    comment_counts,
                    v.getLikeNums(),
                    guide.getName(),
                    v.getCity(),
                    v.getLongitude(),
                    v.getLatitude(),
                    guide.getGrade(),
                    guide.getId(),
                    isFans
            ));
        }
        return ResultUtil.Success(new PageList(videoDTOS,pageNum,pageSize,totalItems));
    }

    /**
     *获取用户信息
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getUserInfo")
    @ApiOperation("获取用户信息")
    public ResultDTO getUserInfo(Long touristId)throws Exception{
        return ResultUtil.Success(touristService.get(touristId));
    }

    /**
     * 预约订单
     * @param order
     * @return
     * @throws Exception
     */
    @PostMapping("/booking")
    @ApiOperation("预约订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "touristId",value = "游客id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "routeId",value = "路线id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "guideId",value = "导游id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "tStart",value = "出发点",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "nOP",value = "人数",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "time",value = "开始日期，格式为'yyyy-MM-dd",paramType = "query",dataType = "Date"),
            @ApiImplicitParam(name = "meetTime",value = "碰面时间,格式为‘yyyy-MM-dd HH:mm:ss",paramType = "query",dataType = "Date"),
            @ApiImplicitParam(name = "tName",value = "名字",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "idNumber",value = "身份证",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "phone",value = "电话",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "price",value = "订单成交时的价格，不用填",paramType = "query",dataType = "Double"),
            @ApiImplicitParam(name = "cardId",value = "卡卷id",paramType = "query",dataType = "Long"),
    })
    public ResultDTO booking(TheOrder order)throws Exception{
        order.setState("222");
        Route route = routeService.get(order.getRouteId());
        Guide guide = guideService.get(order.getGuideId());
        Double price;
        if(guideService.get(order.getGuideId()).getState() == 2){
            return ResultUtil.Error(ErrorCode.BAN);
        }
        if(route.getNOP()<order.getNOP()){
            return ResultUtil.Error(ErrorCode.NOPOVER);
        }
        if (theOrderService.findByParams(new TheOrder(order.getTouristId(),order.getRouteId(),order.getTime())).size()==0) {

            if(order.getNOP()>=route.getDiscountTypeId()) {
                price = route.getPrice() - route.getDiscountValue();
            }else{
                price = route.getPrice();
            }
            if(order.getCardId() != null) {
                Card c = cardService.get(order.getCardId());
                if (!c.getTouristId().equals(order.getTouristId())) {
                    return ResultUtil.Error(ErrorCode.NOT_YOURS);
                }
                messageService.save(new Message(order.getTouristId(),"你的卡卷已使用"));
                price = price - c.getPrice();
                Card card = new Card();
                card.setId(order.getCardId());
                card.setState(false);
                cardService.update(card);
            }
            order.setPrice(price);
            TheOrder save = theOrderService.save(order);
            messageService.save(new Message(save.getTouristId(),"订单号为" + save.getId() + "的订单预约成功"));
            if(touristService.get(order.getTouristId()).getIsVip()){
                Vip v = vipService.findByParams(new Vip(order.getTouristId())).get(0);
                //还没判断满7单加额外分
                vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.ORDER.getScore()));
                taskService.save(new Task(order.getTouristId(), TaskEnum.ORDER,new Date()));
                //判断满7单加分
                if(taskService.countAll(new Task(order.getTouristId(),TaskEnum.ORDER))/7 > taskService.countAll(new Task(order.getTouristId(),TaskEnum.ORDER_SEVEN))){
                    Vip vip = vipService.get(v.getVipId());
                    vipService.update(new Vip(v.getVipId(),vip.getVipScore() + TaskEnum.ORDER_SEVEN.getScore()));
                    taskService.save(new Task(order.getTouristId(), TaskEnum.ORDER_SEVEN,new Date()));
                }
            }
            return ResultUtil.Success(save);
        }else{
            return ResultUtil.Error(ErrorCode.ISEXIST);
        }
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/cancelOrder")
    @ApiOperation("取消订单")
    @ApiImplicitParam(value = "订单id",name = "orderId",paramType = "query",dataType = "Long")
    public ResultDTO cancelOrder(Long orderId)throws Exception{
        TheOrder order = theOrderService.get(orderId);
        if(order!=null) {
            if(order.getCardId()!= null){
                Card c = new Card();
                c.setId(order.getCardId());
                c.setState(true);
                cardService.update(c);
                messageService.save(new Message(order.getTouristId(),"由于取消订单，退还卡卷"));
            }
            TheOrder theOrder = new TheOrder();
            theOrder.setState(OrderState.CANCEL.getState());
            theOrder.setId(orderId);
            theOrderService.update(theOrder);
            return ResultUtil.Success();
        }else{
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
    }

    /**
     * 完成订单
     * @param orderId
     * @return
     * @throws Exception
     */
    @PostMapping("/finishOrder")
    @ApiOperation("完成订单")
    @ApiImplicitParam(value = "订单id",name = "orderId",paramType = "query",dataType = "Long")
    public ResultDTO finishOrder(Long orderId)throws Exception{
        TheOrder theOrder = new TheOrder();
        theOrder.setState(OrderState.FINISH.getState());
        theOrder.setId(orderId);
        theOrderService.update(theOrder);
        theOrderService.update(theOrder);
        return ResultUtil.Success();
    }

    /**
     * 给视频点赞
     * @param videoId
     * @param touristId
     * @return
     * @throws Exception
     */
    @PostMapping("/likeVideo")
    @ApiOperation("给视频点赞")
    @ApiImplicitParam(name = "videoId",value = "视频id",paramType = "query",dataType = "Long")
    public ResultDTO likeVideo(Long videoId,Long touristId)throws Exception{
        if(videoService.get(videoId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        if(likeVideoService.findByParams(new LikeVideo(videoId,touristId)).size()==0){
            Video video = videoService.get(videoId);
            int beforeLikeNums = 0;
            try {
                beforeLikeNums = video.getLikeNums();
            } catch (Exception e) {
                beforeLikeNums = 0;
            }
            videoService.update(new Video(videoId,beforeLikeNums+1));
            Long guideId = video.getGuideId();
            Integer allLikeNums = 0;
            for(Video v: videoService.findByParams(new Video(guideId))){
                allLikeNums += v.getLikeNums();
            }
            Long videoNums = videoService.countAll(new Video(guideId));
            Long orderNums = theOrderService.countAll(new TheOrder(guideId,"111"));
            guideService.update(new Guide(guideId,allLikeNums,
                    ScoreUtil.getScore(guideService.get(guideId).getLevel(),videoNums,orderNums,allLikeNums)));
            likeVideoService.save(new LikeVideo(videoId,touristId));
            if(touristService.get(touristId).getIsVip()) {
                LikeVideo lv = new LikeVideo();
                lv.setTouristId(touristId);
                Long likeVideoNums = likeVideoService.countAll(lv);
                if (likeVideoNums / 100 > taskService.countAll(new Task(touristId, TaskEnum.LIKE_VIDEO))){
                    Vip v = vipService.findByParams(new Vip(touristId)).get(0);
                    vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.LIKE_VIDEO.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.LIKE_VIDEO,new Date()));
                }
            }
            return ResultUtil.Success();
        }else {
            return ResultUtil.Error(ErrorCode.ISEXIST);
        }

    }

    /**
     * 取消视频点赞
     * @param touristId
     * @param videoId
     * @return
     * @throws Exception
     */
    @PostMapping("/unlikeVideo")
    @ApiOperation("取消视频点赞")
    public ResultDTO unlikeVideo(Long touristId,Long videoId)throws Exception{
        if(touristService.get(touristId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        if(videoService.get(videoId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        List<LikeVideo> likeVideos = likeVideoService.findByParams(new LikeVideo(videoId,touristId));
        if (likeVideos.size()==0) {
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        likeVideoService.deleteById(likeVideos.get(0).getId());
        int beforeLikeNums = 0;
        try {
            beforeLikeNums = videoService.get(videoId).getLikeNums();
        } catch (Exception e) {
            beforeLikeNums = 0;
        }
        videoService.update(new Video(videoId,beforeLikeNums - 1));
        return ResultUtil.Success();
    }

    /**
     * 发表评论
     * @param comment
     * @return
     * @throws Exception
     */
    @PostMapping("/comment")
    @ApiOperation("发表评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoId",value = "视频id",paramType = "query",dataType = "Long"),
            @ApiImplicitParam(name = "content",value = "评论内容",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "likeNums",value = "点赞数",paramType = "query",dataType = "String")
    })
    public ResultDTO comment(Comment comment,Long touristId)throws Exception{
        if(videoService.get(comment.getVideoId())==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        comment.setTouristId(touristId);
        comment.setCreatetime(new Date());
        comment.setState(1);
        return ResultUtil.Success(commentService.save(comment));
    }

    /**
     * 获取评论
     * @param videoId
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getComments")
    @ApiOperation("获取评论")
    @ApiImplicitParam(name = "videoId",value = "视频id",paramType = "query",dataType = "Long")
    public ResultDTO getComments(Long videoId,Long touristId)throws Exception{
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment c:commentService.findByParams(new Comment(videoId))){
            Tourist tourist = touristService.get(c.getTouristId());
            boolean isLike = likeCommentService.findByParams(new LikeComment(touristId,c.getId())).size()==0 ? false : true;
            commentDTOS.add(new CommentDTO(tourist.getAvatarUrl(),
                    tourist.getNickname(),
                    isLike,
                    c,
                    MyMathUtil.getTimeFromHere(c.getCreatetime()))
            );
        }
        return ResultUtil.Success(commentDTOS);
    }

    /**
     * 给评论点赞
     * @param commentId
     * @param touristId
     * @return
     * @throws Exception
     */
    @PostMapping("/likeComment")
    @ApiOperation("给评论点赞")
    public ResultDTO likeComment(Long commentId,Long touristId)throws Exception{
        if(commentService.get(commentId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        if(likeCommentService.findByParams(new LikeComment(touristId,commentId)).size()==0){
            int beforeLikeNums = 0;
            try {
                beforeLikeNums = commentService.get(commentId).getLikeNums();
            } catch (Exception e) {
                beforeLikeNums = 0;
            }
            commentService.update(new Comment(commentId,beforeLikeNums+1));
            return ResultUtil.Success(likeCommentService.save(new LikeComment(touristId,commentId)));
        }else {
            return ResultUtil.Error(ErrorCode.ISEXIST);
        }
    }

    /**
     * 评论取消点赞
     * @param touristId
     * @param commentId
     * @return
     * @throws Exception
     */
    @PostMapping("/unlikeComment")
    @ApiOperation("评论取消点赞")
    public ResultDTO unlikeComment(Long touristId,Long commentId)throws Exception{
        if(touristService.get(touristId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        if(commentService.get(commentId)==null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        List<LikeComment> likeComments = likeCommentService.findByParams(new LikeComment(touristId,commentId));
        if (likeComments.size()==0) {
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        likeCommentService.deleteById(likeComments.get(0).getId());
        int beforeLikeNums = 0;
        try {
            beforeLikeNums = commentService.get(commentId).getLikeNums();
        } catch (Exception e) {
            beforeLikeNums = 0;
        }
        commentService.update(new Comment(commentId,beforeLikeNums - 1));
        return ResultUtil.Success();
    }


    /**
     * 得到我的喜欢
     * @param touristId
     * @param longitude
     * @param latitude
     * @return
     * @throws Exception
     */
    @GetMapping("/getMyLike")
    @ApiOperation("得到我的喜欢")
    public ResultDTO getMyLike(Long touristId,Double longitude,Double latitude)throws Exception{
        LikeVideo lv = new LikeVideo();
        lv.setTouristId(touristId);
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for (LikeVideo l:likeVideoService.findByParams(lv)) {
            Video v = videoService.get(l.getLikeVideoId());
            Guide guide = guideService.get(v.getGuideId());
            Tourist tourist = touristService.get(guide.getTouristId());
            boolean isLike = likeVideoService.findByParams(new LikeVideo(v.getId(),touristId)).size()==0;
            Integer comment_counts = commentService.findByParams(new Comment(v.getId())).size();
            boolean isFans = fansService.findByParams(new Fans(touristId,guide.getId())).size()==0;
            if(guide.getState()==2){
                continue;
            }
            Scenic s;
            if(v.getScenicId() == null){
                videoDTOS.add(new VideoDTO(v,
                        MyMathUtil.getTwoPointDist(
                                new Point(longitude,latitude),
                                new Point(v.getLongitude(),v.getLatitude())),
                        tourist.getAvatarUrl(),
                        guide.getLevel(),
                        isLike,
                        comment_counts,
                        v.getLikeNums(),
                        guide.getName(),
                        null,
                        v.getLongitude(),
                        v.getLatitude(),
                        guide.getGrade(),
                        guide.getId(),
                        isFans
                ));
            }else {
                s = scenicService.get(v.getScenicId());
                videoDTOS.add(new VideoDTO(v,
                        MyMathUtil.getTwoPointDist(
                                new Point(longitude,latitude),
                                new Point(s.getLongitude(),s.getLatitude())),
                        tourist.getAvatarUrl(),
                        guide.getLevel(),
                        isLike,
                        comment_counts,
                        v.getLikeNums(),
                        guide.getName(),
                        s.getCity(),
                        s.getLongitude(),
                        s.getLatitude(),
                        guide.getGrade(),
                        guide.getId(),
                        isFans
                ));
            }
        }
        return ResultUtil.Success(videoDTOS);
    }
    /**
     * 获取他的视频
     * @param guideId
     * @return
     * @throws Exception
     */
    @GetMapping("/getHisVideo")
    @ApiOperation("获取他的视频")
    @ApiImplicitParams ({
        @ApiImplicitParam(name = "touristId", value = "自己的游客id", paramType = "query", dataType = "Long"),
        @ApiImplicitParam(name = "guideId", value = "导游id", paramType = "query", dataType = "Long")
    })
    public ResultDTO getHisVideo(Long touristId,Long guideId)throws Exception{
        boolean isLike = false;
        List<VideoDTO> videoDTOS = new ArrayList<>();
        for(Video v:videoService.findByParams(new Video(guideId))){
            if (likeVideoService.findByParams(new LikeVideo(v.getId(),touristId)).size()!=0) {
                isLike = true;
            }
            videoDTOS.add(new VideoDTO(v,null,null,null,isLike,null,null,null,null,null,null,null,null,null));
            isLike = false;
        }
        return ResultUtil.Success(videoDTOS);
    }

    /**
     * 获取它的喜欢
     * @param guideId
     * @return
     * @throws Exception
     */
    @GetMapping("/getHisLike")
    @ApiOperation("获取它的喜欢")
    @ApiImplicitParam(name = "guideId",value = "导游id",paramType = "query",dataType = "Long")
    public ResultDTO getHisLike(Long guideId)throws Exception{
        Long touristId = guideService.get(guideId).getTouristId();
        LikeVideo likeVideo = new LikeVideo();
        likeVideo.setTouristId(touristId);
        List<Video> videoList = new ArrayList<>();
        for(LikeVideo l:likeVideoService.findByParams(likeVideo)){
            videoList.add(videoService.get(l.getLikeVideoId()));
        }
        return ResultUtil.Success(videoList);
    }




    /**
     * 获取他的未查阅的消息数量
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMsgNums")
    @ApiOperation("获取他的未查阅的消息数量")
    public ResultDTO getMsgNums(Long touristId )throws Exception{
        return ResultUtil.Success(messageService.countAll(new Message(touristId)));
    }

    /**
     * 获取他的消息
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMsgs")
    @ApiOperation("获取他的消息")
    public ResultDTO getMsgs(Long touristId )throws Exception{
        Message message = new Message();
        message.setTouristId(touristId);
        return ResultUtil.Success(messageService.findByParams(message));
    }

    /**
     * 获取消息详情
     * @param messageId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMsg")
    @ApiOperation("获取消息详情")
    @ApiImplicitParam(name = "messageId",value = "消息id",paramType = "query",dataType = "Long")
    public ResultDTO getMsg(Long messageId)throws Exception{
        Message message = messageService.get(messageId);
        Byte state = 1;
        messageService.update(new Message(messageId,state));
        return ResultUtil.Success(message);
    }

    /**
     * 获取路线信息
     * @param videoId
     * @return
     * @throws Exception
     */
    @GetMapping("/getRoute")
    @ApiOperation("获取路线信息")
    public ResultDTO getRoute(Long videoId)throws Exception{
        Video video = videoService.get(videoId);
        Guide guide = guideService.get(video.getGuideId());
        Route route;
        if(video.getRouteId() == null){
            return  ResultUtil.Success(new RouteDTO(guide,
                    null,
                    new TouristDTO(touristService.get(guide.getTouristId()))));
        }else{
            return ResultUtil.Success(new RouteDTO(guide,
                    routeService.get(video.getRouteId()),
                    new TouristDTO(touristService.get(guide.getTouristId()))));
        }

    }


    /**
     * 返回该路线不能选择的天数
     */
    @GetMapping("/getSelectDays")
    @ApiOperation("返回该路线不能选择的天数")
    public ResultDTO getSelectDays(Long routeId)throws Exception{
        Route route = routeService.get(routeId);
        List list = new ArrayList();
        for(TheOrder t:theOrderService.findByParams(new TheOrder(route.getGuideId(),"222"))){
            list.add(MyMathUtil.returnSelectDays(t.getTime(),route.getRDay()));
        }
        return ResultUtil.Success(list);
    }

    /**
     * 得到订单详情
     * @param orderId
     * @return
     * @throws Exception
     */
    @GetMapping("/getOrder")
    @ApiOperation("得到订单详情")
    public ResultDTO getOrder(Long orderId)throws Exception{
        TheOrder theOrder = theOrderService.get(orderId);
        Guide guide = guideService.get(theOrder.getGuideId());
        Route route = routeService.get(theOrder.getRouteId());
        Double price;
        if(theOrder.getNOP()>=route.getDiscountTypeId()) {
            price = route.getPrice() - route.getDiscountValue();
        }else{
            price = route.getPrice();
        }
        if(theOrder.getCardId() != null){
            Card card = cardService.get(theOrder.getCardId());
            if(price > card.getPrice()) {
                price -= card.getPrice();
            }else {
                price = 0.00;
            }
        }
        return ResultUtil.Success(new ReturnOrderDTO(theOrder,guide,new TouristDTO(touristService.get(guide.getTouristId())),route,price));
    }

    /**
     * 获取我的订单列表
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getOrders")
    @ApiOperation("获取我的订单列表")
    public ResultDTO getOrders(Long touristId,String attr)throws Exception{
        List<TheOrderDTO> theOrderDTOS = new ArrayList<>();
        TheOrder theOrder = new TheOrder();
        theOrder.setTouristId(touristId);
        theOrder.setState(attr);
        for (TheOrder t:theOrderService.findByParams(theOrder)) {
            Guide guide = guideService.get(t.getGuideId());
            if(guide.getState()==2){
                continue;
            }
            Route route = routeService.get(t.getRouteId());
            theOrderDTOS.add(new TheOrderDTO(t,route.getLine(),guide.getName(),route.getRDay(),route.getRNight()));
        }
        return ResultUtil.Success(theOrderDTOS);
    }

    /**
     * 获取景点详情
     * @param scenicId
     * @return
     * @throws Exception
     */
    @GetMapping("/getScenicMsg")
    @ApiOperation("获取景点详情")
    public ResultDTO getScenicMsg(Long scenicId)throws Exception{
        return ResultUtil.Success(scenicService.get(scenicId));
    }

    /**
     * 搜索景区
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findScenic")
    public ResultDTO findScenic(String cityName,String attr)throws Exception{
        return ResultUtil.Success(scenicService.findScenic(cityName,attr));
    }

    /**
     * 上传观看视频时长
     * @param touristId
     * @param videoId
     * @param time
     * @return
     * @throws Exception
     */
    @PostMapping("/saveLookVideoTime")
    @ApiOperation("上传观看视频时长")
    public ResultDTO saveLookVideoTime(Long touristId,Long videoId,Long time)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Long timeLong = Long.valueOf(0);
        lookVideoService.save(new LookVideo(touristId,videoId,time));
        for(LookVideo lv:lookVideoService.findToday(touristId)){
            timeLong += lv.getTime();
        }
        if(timeLong > 3600){
            if(taskService.findToday(touristId,TaskEnum.DAY_VIDEO) == 0){
                Vip v = vipService.findByParams(new Vip(touristId)).get(0);
                vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.DAY_VIDEO.getScore()));
                taskService.save(new Task(touristId, TaskEnum.DAY_VIDEO,new Date()));
            }
        }
        return ResultUtil.Success();
    }


    /**
     * 根据视频id返回视频
     * @param touristId
     * @param videoId
     * @return
     * @throws Exception
     */
    @GetMapping("/getVideoById")
    @ApiOperation("根据视频id返回视频")
    public ResultDTO getVideoById(Long touristId,Long videoId,Double longitude,Double latitude)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Video v = videoService.get(videoId);
        Scenic s;
        Guide guide = guideService.get(v.getGuideId());
        boolean isLike = likeVideoService.findByParams(new LikeVideo(v.getId(),touristId)).size()==0;
        Integer comment_counts = commentService.findByParams(new Comment(v.getId())).size();
        boolean isFans = fansService.findByParams(new Fans(touristId,guide.getId())).size()==0;
        if(v.getScenicId()==null){
            return ResultUtil.Success(new VideoDTO(v,
                    MyMathUtil.getTwoPointDist(
                            new Point(longitude, latitude),
                            new Point(v.getLongitude(), v.getLatitude())),
                    tourist.getAvatarUrl(),
                    guide.getLevel(),
                    isLike,
                    comment_counts,
                    v.getLikeNums(),
                    guide.getName(),
                    null,
                    v.getLongitude(),
                    v.getLatitude(),
                    guide.getGrade(),
                    guide.getId(),
                    isFans
            ));
        }else {
            s = scenicService.get(v.getScenicId());
            return ResultUtil.Success(new VideoDTO(v,
                    MyMathUtil.getTwoPointDist(
                            new Point(longitude, latitude),
                            new Point(s.getLongitude(), s.getLatitude())),
                    tourist.getAvatarUrl(),
                    guide.getLevel(),
                    isLike,
                    comment_counts,
                    v.getLikeNums(),
                    guide.getName(),
                    s.getCity(),
                    s.getLongitude(),
                    s.getLatitude(),
                    guide.getGrade(),
                    guide.getId(),
                    isFans
            ));
        }
    }


    /**
     * 关注导游
     */
    @ApiOperation("关注导游")
    @PostMapping("/fans")
    public ResultDTO fans(Long touristId,Long guideId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Guide guide = guideService.get(guideId);
        if(guide == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        if(guide.getTouristId().equals(touristId)){

            return ResultUtil.Error(ErrorCode.FANS_MYSELF);
        }
        if(fansService.findByParams(new Fans(touristId,guideId)).size()==1){
            return ResultUtil.Error(ErrorCode.ISEXIST);
        }
        return ResultUtil.Success(fansService.save(new Fans(touristId,guideId)));
    }


    /**
     * 获取关注我的
     */
    @ApiOperation("获取关注我的")
    @GetMapping("/getMyFans")
    public ResultDTO getMyFans(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null && tourist.getIsGuide() == false){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        Fans f = new Fans();
        f.setGuideId(guide.getId());
        List<TouristDTO> tourists = new ArrayList<>();
        for (Fans fans:fansService.findByParams(f)) {
            tourists.add(new TouristDTO(touristService.get(fans.getTouristId())));
        }
        return ResultUtil.Success(tourists);
    }


    /**
     * 获取我的粉丝数量
     * @param touristId
     * @return
     * @throws Exception
     */
    @ApiOperation("获取我的粉丝数量")
    @GetMapping("/getMyFansNums")
    public ResultDTO getMyFansNums(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null && tourist.getIsGuide() == false){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Guide guide = guideService.findByParams(new Guide(touristId)).get(0);
        Fans f = new Fans();
        f.setGuideId(guide.getId());
        return ResultUtil.Success(fansService.countAll(f));
    }


    /**
     * 获取我关注的
     * @param touristId
     * @return
     * @throws Exception
     */
    @ApiOperation("获取我关注的")
    @GetMapping("/getMyStar")
    public ResultDTO getMyStar(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Fans f = new Fans();
        f.setTouristId(touristId);
        List<GuideAndAvataturl> guides = new ArrayList<>();
        for (Fans fans:fansService.findByParams(f)) {
            Guide g = guideService.get(fans.getGuideId());
            guides.add(new GuideAndAvataturl(g,touristService.get(g.getTouristId()).getAvatarUrl()));
        }
        return ResultUtil.Success(guides);
    }


    /**
     * 获得我关注的数量
     * @param touristId
     * @return
     * @throws Exception
     */
    @ApiOperation("获得我关注的数量")
    @GetMapping("/getMyStarNums")
    public ResultDTO getMyStarNums(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(tourist == null){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Fans f = new Fans();
        f.setTouristId(touristId);
        return ResultUtil.Success(fansService.countAll(f));
    }


    /**
     * 取消关注
     * @param touristId
     * @param guideId
     * @return
     * @throws Exception
     */
    @ApiOperation("取消关注")
    @DeleteMapping("/removeFans")
    public ResultDTO removeFans(Long touristId,Long guideId)throws Exception{
        if(fansService.findByParams(new Fans(touristId,guideId)).size()==0){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        fansService.delete(new Fans(touristId,guideId));
        return ResultUtil.Success();
    }

//
}
