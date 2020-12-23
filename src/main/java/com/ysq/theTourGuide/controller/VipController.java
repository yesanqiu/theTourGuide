package com.ysq.theTourGuide.controller;

import com.ysq.theTourGuide.base.dto.ResultDTO;
import com.ysq.theTourGuide.base.util.ResultUtil;
import com.ysq.theTourGuide.config.ErrorCode;
import com.ysq.theTourGuide.config.TaskEnum;
import com.ysq.theTourGuide.config.WXPayConstants;
import com.ysq.theTourGuide.dto.TaskDTO;
import com.ysq.theTourGuide.entity.*;
import com.ysq.theTourGuide.service.*;
import com.ysq.theTourGuide.utils.HttpClientUtil;
import com.ysq.theTourGuide.utils.MyMathUtil;
import com.ysq.theTourGuide.utils.WXPayUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/vip")
@RestController
public class VipController {

    @Value("${app.appid}")
    private String appid;



    @Value("${app.appSecret}")
    private String appSecret;

    @Value("${app.mchId}")
    private String mchId;

    @Value("${app.createOrderUrl}")
    private String createOrderUrl;

    @Value("${app.notifyUrl}")
    private String notifyUrl;


    @Autowired
    TouristService touristService;

    @Autowired
    VipService vipService;

    @Autowired
    SignInService signInService;

    @Autowired
    TaskService taskService;

    @Autowired
    LookVideoService lookVideoService;

    @Autowired
    ScenicSpotService scenicSpotService;

    @Autowired
    CardService cardService;

    @Autowired
    MessageService messageService;

    public ResultDTO sss()throws Exception{
        return ResultUtil.Success();
    }



    @PostMapping("/createOrder")
    public ResultDTO createOrder(String openid,int money)throws Exception{
        String mch_id= scenicSpotService.get(Integer.parseInt(mchId)).getName(); //商户号
        String app_id= scenicSpotService.get(Integer.parseInt(appid)).getName(); //小程序号
        String app_secret= scenicSpotService.get(Integer.parseInt(appSecret)).getName(); //小程序密钥

        String today = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String WXPay= WXPayUtil.createCode(8);
        String out_trade_no=mch_id+today+WXPay;//生成订单号
        Map<String,String> result=new HashMap<String,String>();
        //去Service层中去生成签名,用户openid out_trade_no订单号  money支付的金额
        String formData=WXPayUtil.getopenid(openid,out_trade_no,money,app_id,app_secret,mch_id,notifyUrl);
        //在servlet层中生成签名成功后，把下单所要的参数以xml的格式拼接，发送下单接口
        String httpResult = HttpClientUtil.httpXMLPost(createOrderUrl,formData);
        try {
            //xml转换成Map对象或者值
            Map<String, String> resultMap = WXPayUtil.xmlToMap(httpResult);
            result.put("package", "prepay_id=" + resultMap.get("prepay_id")); //这里是拿下单成功的微信交易号去拼接，因为在下面的接口中必须要这个样子
            result.put("nonceStr",resultMap.get("nonce_str")); //随机字符串
        } catch (Exception e) {
            e.printStackTrace();
        }

        String times= WXPayUtil.getCurrentTimestamp()+""; //获取当前时间
        result.put("timeStamp",times); //当前时间戳
        //生成调用支付接口要的签名
        Map<String, String> packageParams = new HashMap<String ,String>();
        packageParams.put("appId", app_id);
        packageParams.put("signType", WXPayConstants.SignType.MD5.toString());
        packageParams.put("nonceStr",result.get("nonceStr")+"");
        packageParams.put("timeStamp",times);
        packageParams.put("package", result.get("package")+"");//商户订单号
        String sign="";
        try {
            sign= WXPayUtil.generateSignature(packageParams, app_secret); //生成签名:
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("paySign",sign);
        return ResultUtil.Success(result);
    }

    /**
     * 成为会员
     * @param touristId
     * @param date
     * @return
     * @throws Exception
     */
    @PostMapping("/toBeVip")
    @ApiOperation("成为会员")
    public ResultDTO toBeVip(Long touristId,Integer date)throws Exception{
        List<Vip> vips = vipService.findByParams(new Vip(touristId));

        //判断是否会员
        if(touristService.get(touristId).getIsVip()){
            //是会员，续费
            if(isVip(touristId)){
                Vip v = new Vip();
                v.setVipId(vips.get(0).getVipId());
                v.setVipDate(date + vips.get(0).getVipDate());
                vipService.update(v);
                receCard(touristId);
                if(date == 365){
                    vipService.update(new Vip(vips.get(0).getVipId(),vips.get(0).getVipScore() + TaskEnum.YEAR_VIP.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.YEAR_VIP,new Date()));
                }
                return ResultUtil.Success();
            }else{
                //是会员，但已过期，重新续费
                Vip v = new Vip();
                v.setVipId(vips.get(0).getVipId());
                v.setVipDate(date);
                v.setVipTime(new Date());
                vipService.update(v);
                Tourist tourist = new Tourist();
                tourist.setId(touristId);
                tourist.setIsVip(true);
                touristService.update(tourist);
                receCard(touristId);
                if(date == 365){
                    vipService.update(new Vip(vips.get(0).getVipId(),vips.get(0).getVipScore() + TaskEnum.YEAR_VIP.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.YEAR_VIP,new Date()));
                }
                return ResultUtil.Success();
            }

        }else{
            //不是会员，开通会员
            if(vips.size() == 0){
                //首次开通会员
                Vip v = new Vip(date, touristId);
                v.setVipTime(new Date());
                vipService.save(v);
                Tourist tourist = new Tourist();
                tourist.setId(touristId);
                tourist.setIsVip(true);
                touristService.update(tourist);
                vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.FIRST_VIP.getScore()));
                taskService.save(new Task(touristId, TaskEnum.FIRST_VIP,new Date()));
                receCard(touristId);
                if(date == 365){
                    vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.YEAR_VIP.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.YEAR_VIP,new Date()));
                }
                return ResultUtil.Success(v);
            }else{
                //不是会员，但不是首次开通会员
                Vip v = new Vip();
                v.setVipId(vips.get(0).getVipId());
                v.setVipDate(date);
                v.setVipTime(new Date());
                vipService.update(v);
                Tourist tourist = new Tourist();
                tourist.setId(touristId);
                tourist.setIsVip(true);
                touristService.update(tourist);
                receCard(touristId);
                if(date == 365){
                    vipService.update(new Vip(vips.get(0).getVipId(),vips.get(0).getVipScore() + TaskEnum.YEAR_VIP.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.YEAR_VIP,new Date()));
                }
                return ResultUtil.Success();
            }
        }
    }

    public void receCard(Long touristId)throws Exception{
        Card card = new Card(touristId);
        cardService.save(card);
        messageService.save(new Message(touristId,"本月的代金卷领取成功"));
    }

    /**
     * 获取vip信息
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getVipMsg")
    @ApiOperation("获取vip信息")
    public ResultDTO getVipMsg(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        Vip v = vipService.findByParams(new Vip(touristId)).get(0);
        if (!isVip(touristId)) {
           return ResultUtil.Error(ErrorCode.NOVIP);
        }
        return ResultUtil.Success(v);
    }
    /**
     * 获取用户自动续费状态
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getAutoRenewal")
    @ApiOperation("获取用户自动续费状态")
    public ResultDTO getAutoRenewal(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        return ResultUtil.Success(vipService.findByParams(new Vip(touristId)).get(0).getAutoRenewal());
    }

    /**
     * 修改续费状态
     * @param touristId
     * @param state
     * @return
     * @throws Exception
     */
    @PostMapping("/autoRenewal")
    @ApiOperation("修改续费状态")
    public ResultDTO autoRenewal(Long touristId,Boolean state)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOEXIST);
        }
        Vip vip = vipService.findByParams(new Vip(touristId)).get(0);
        Vip v = new Vip();
        v.setVipId(vip.getVipId());
        v.setAutoRenewal(state);
        vipService.update(v);
        if(state){
            vipService.update(new Vip(vip.getVipId(),vip.getVipScore() + TaskEnum.AUTO_RENEWAL.getScore()));
            taskService.save(new Task(touristId, TaskEnum.AUTO_RENEWAL,new Date()));
        }
        return ResultUtil.Success();
    }


    /**
     * 签到
     * @param touristId
     * @return
     * @throws Exception
     */
    @PostMapping("/signIn")
    @ApiOperation("签到")
    public ResultDTO signIn(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        SignIn signIn = new SignIn();
        signIn.setTouristId(touristId);
        Date d = new Date();
        for(SignIn s:signInService.findByParams(signIn)){
            if(MyMathUtil.isADay(d,s.getDate())){
                return ResultUtil.Error(ErrorCode.ISEXIST);
            }
        }
        Vip v = vipService.findByParams(new Vip(touristId)).get(0);
        SignIn s = signInService.save(new SignIn(touristId));
        //还没判断满7天加额外10分
        vipService.update(new Vip(v.getVipId(),v.getVipScore() + TaskEnum.SIGN_IN.getScore()));
        taskService.save(new Task(touristId, TaskEnum.SIGN_IN,s.getDate()));

        //判断满7天额外加分
        SignIn seven = new SignIn();
        seven.setTouristId(touristId);
        if(signInService.countAll(seven)/7 > taskService.countAll(new Task(touristId,TaskEnum.SIGN_IN_SEVEN))){
            Vip vip = vipService.get(v.getVipId());
            vipService.update(new Vip(v.getVipId(),vip.getVipScore() + TaskEnum.SIGN_IN_SEVEN.getScore()));
            taskService.save(new Task(touristId, TaskEnum.SIGN_IN_SEVEN,s.getDate()));
        }
        return ResultUtil.Success();
    }

    /**
     * 获得签到的天数
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getCountSignIn")
    @ApiOperation("获得签到的天数")
    public  ResultDTO getCountSignIn(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        SignIn s = new SignIn();
        s.setTouristId(touristId);
        return ResultUtil.Success(signInService.countAll(s));
    }

    /**
     * 获得签到的具体日期
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getSignIndays")
    @ApiOperation("获得签到的具体日期")
    public ResultDTO getSignIndays(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        SignIn s = new SignIn();
        s.setTouristId(touristId);
        List<Date> dateList = new ArrayList<>();
        for(SignIn signIn:signInService.findByParams(s)){
            dateList.add(signIn.getDate());
        }
        Collections.sort(dateList,(o1,o2)-> (int) (o1.getTime()-o2.getTime()));
        return ResultUtil.Success(dateList);
    }

    /**
     * 获取订单数
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getOrdersNum")
    @ApiOperation("获取订单数")
    public ResultDTO getOrdersNum(Long touristId)throws Exception{
        return ResultUtil.Success(taskService.countAll(new Task(touristId,TaskEnum.ORDER)));
    }

    /**
     * 获取当天观看时长
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getLookTime")
    public ResultDTO getLookTime(Long touristId) throws Exception{
        Long second = Long.valueOf(0);
        for(LookVideo lv:lookVideoService.findToday(touristId)){
            second += lv.getTime();
        }
        return ResultUtil.Success(MyMathUtil.timeFormateSecond(second));
    }

    /**
     * 获取成长值明细
     * @param touristId
     * @return
     * @throws Exception
     */
    @GetMapping("/getTask")
    @ApiOperation("获取成长值明细")
    public ResultDTO getTask(Long touristId)throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        List<Task> list = taskService.findByParams(new Task(touristId));
        Collections.sort(list,(o1,o2) -> (int) (o1.getTTime().getTime()-o2.getTTime().getTime()));
        List<TaskDTO> taskDTOS = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int year = 0;
        int month = 0;
        for(Task task:list){
            c.setTime(task.getTTime());
            if(c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month){
                tasks.add(task);
            }else{
                if(year != 0 && month != 0) {
                    taskDTOS.add(new TaskDTO(year + "年" + month + "月", tasks));
                    tasks = new ArrayList<>();
                }
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
            }
        }
        int realMonth = month + 1;
        taskDTOS.add(new TaskDTO(year+"年"+realMonth+"月",tasks));
        return ResultUtil.Success(taskDTOS);
    }



    public boolean isVip(Long touristId)throws Exception{
        levelUp(touristId);
        Vip v = vipService.findByParams(new Vip(touristId)).get(0);
        System.out.println((long)v.getVipDate() * 86400000 + v.getVipTime().getTime());
        System.out.println((long)(v.getVipTime().getTime()));
        System.out.println(new Date().getTime());
        if((long)v.getVipDate() * 86400000 + v.getVipTime().getTime() < new Date().getTime()){
            Tourist tourist = new Tourist();
            tourist.setId(touristId);
            tourist.setIsVip(false);
            touristService.update(tourist);
            return false;
        }else{
            return true;
        }
    }

    public void levelUp(Long touristId)throws Exception{
        Vip v = vipService.findByParams(new Vip(touristId)).get(0);
        String level = v.getVipLevel();
        Integer score = v.getVipScore();
        String nextLevel;
        switch (level){
            case "v1":
                nextLevel = "v2";
                if(score>200){
                    vipService.update(new Vip(v.getVipId(),nextLevel,score-200 + TaskEnum.LEVEL_ONE_TO_TWO.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.LEVEL_ONE_TO_TWO,new Date()));
                }
                break;
            case "v2":
                nextLevel = "v3";
                if(score>400){
                    vipService.update(new Vip(v.getVipId(),nextLevel,score-400 + TaskEnum.LEVEL_TWO_TO_THREE.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.LEVEL_TWO_TO_THREE,new Date()));
                }
                break;
            case "v3":
                nextLevel = "v4";
                if(score>1000){
                    vipService.update(new Vip(v.getVipId(),nextLevel,score-1000 + TaskEnum.LEVEL_THREE_TO_FOUR.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.LEVEL_THREE_TO_FOUR,new Date()));
                }
                break;
            case "v4":
                nextLevel = "v5";
                if(score>2000){
                    vipService.update(new Vip(v.getVipId(),nextLevel,score-2000 + TaskEnum.LEVEL_FOUR_TO_FIVE.getScore()));
                    taskService.save(new Task(touristId, TaskEnum.LEVEL_FOUR_TO_FIVE,new Date()));
                }
                break;
            case "v5":
                nextLevel = "v5";
                break;
        }
    }

//    /**
//     * 领取卡卷
//     */
//    @ApiOperation("领取卡卷")
//    @GetMapping("/receiveCard")
//    public ResultDTO receiveCard(Long touristId)throws Exception{
//        Tourist tourist = touristService.get(touristId);
//        System.out.println(tourist);
//        if(!tourist.getIsVip()){
//            return ResultUtil.Error(ErrorCode.NOVIP);
//        }
//        if ( ! isVip(touristId)) {
//            return ResultUtil.Error(ErrorCode.NOVIP);
//        }
//        String str = "本月的代金卷领取成功";
//        if(taskService.findThisMonth(touristId,str)!=0){
//            return ResultUtil.Error(ErrorCode.ALREADY_RECEIVE);
//        }
//        Card card = new Card(touristId);
//        cardService.save(card);
//        messageService.save(new Message(touristId,str));
//        return ResultUtil.Success();
//    }

    /**
     * 查询可用卡卷
     * @param touristId
     * @return
     * @throws Exception
     */
    @ApiOperation("查询可用卡卷")
    @GetMapping("/getCard")
    public ResultDTO getCard(Long touristId) throws Exception{
        Tourist tourist = touristService.get(touristId);
        if(!tourist.getIsVip()){
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        if ( ! isVip(touristId)) {
            return ResultUtil.Error(ErrorCode.NOVIP);
        }
        Card card = new Card();
        card.setTouristId(touristId);
        List<Card> cards = cardService.findByParams(card);
        if (cards.size() == 0){
            return ResultUtil.Error(ErrorCode.NULL);
        }
        List<Card> useCards = new ArrayList<>();
        for(Card c: cards){
            if(!MyMathUtil.isOverToday(c.getDate(),c.getDay(),c.getMonth(),c.getYear()) && c.getState()){
                useCards.add(c);
            }else{
                cardService.delete(c);
            }
        }
        return ResultUtil.Success(useCards);
    }


//    /**
//     * 使用卡卷
//     * @param touristId
//     * @param cardId
//     * @return
//     * @throws Exception
//     */
//    @ApiOperation("使用卡卷")
//    @PostMapping("/useCard")
//    public ResultDTO useCard(Long touristId,Long cardId)throws Exception{
//        Tourist tourist = touristService.get(touristId);
//        if(!tourist.getIsVip()){
//            return ResultUtil.Error(ErrorCode.NOVIP);
//        }
//        if ( ! isVip(touristId)) {
//            return ResultUtil.Error(ErrorCode.NOVIP);
//        }
//        if(!cardService.get(cardId).getTouristId().equals(touristId)){
//            return ResultUtil.Error(ErrorCode.NOT_YOURS);
//        }
//        cardService.deleteById(cardId);
//        return ResultUtil.Success();
//    }

    /**
     * 每个月一号0时0分0秒发放卡卷
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduler()throws Exception{
        for(Vip v: vipService.findAll()){
            Tourist tourist = touristService.get(v.getTouristId());
            if(!tourist.getIsVip()){
                continue;
            }
            if ( ! isVip(v.getTouristId())) {
                continue;
            }
            String str = "本月的代金卷已发放";
            if(taskService.findThisMonth(v.getTouristId(),str)==0){
                Card card = new Card(v.getTouristId());
                cardService.save(card);
                messageService.save(new Message(v.getTouristId(),str));
            }
        }
    }





}
