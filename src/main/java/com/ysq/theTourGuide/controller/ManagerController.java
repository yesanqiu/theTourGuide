package com.ysq.theTourGuide.controller;

import com.ysq.theTourGuide.base.dto.ResultDTO;
import com.ysq.theTourGuide.base.util.ResultUtil;
import com.ysq.theTourGuide.config.ErrorCode;
import com.ysq.theTourGuide.dto.AdministratorDTO;
import com.ysq.theTourGuide.dto.ManagerOrderDTO;
import com.ysq.theTourGuide.dto.ManagerOrderMsgDTO;
import com.ysq.theTourGuide.entity.*;
import com.ysq.theTourGuide.service.*;
import com.ysq.theTourGuide.utils.MyMathUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    AdministratorService administratorService;

    @Autowired
    AdministratorTypeService administratorTypeService;

    @Autowired
    AdministratorAuthorityService administratorAuthorityService;

    @Autowired
    ScenicService scenicService;

    @Autowired
    GuideService guideService;

    @Autowired
    VideoService videoService;

    @Autowired
    TheOrderService theOrderService;

    @Autowired
    RouteService routeService;

    @Autowired
    MessageService messageService;

    @Autowired
    TouristService touristService;

    @Autowired
    LikeVideoService likeVideoService;

    @Autowired
    UGuideService uGuideService;
    /**
     * 管理员登录
     * @param account
     * @param psw
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @ApiOperation("管理员登录")
    public ResultDTO login(String account,String psw)throws Exception{
        List<Administrator> administrators = administratorService.findByParams(new Administrator(account));
        if(administrators.size()==0){
            return ResultUtil.Error(ErrorCode.ADMINISTRATOR_NOEXIST);
        }
        Administrator administrator = administrators.get(0);
        if(administrator.getPassword().equals(psw)){
            return ResultUtil.Success(administrator);
        }else{
            return ResultUtil.Error(ErrorCode.ERROR_PSW);
        }
    }

    /**
     * 添加管理员
     * @param administratorId
     * @param administratorTypeId
     * @param account
     * @param psw
     * @return
     * @throws Exception
     */
    @PostMapping("/addAdministrator")
    @ApiOperation("添加管理员")
    public ResultDTO addAdministrator(Integer administratorId,Integer administratorTypeId,String account,String psw)throws Exception{
        AdministratorType administratorType = administratorTypeService.get(
                administratorService.get(administratorId).getTypeId());
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(administratorType
                .getAuthorityId());
        if(administratorAuthority.getAddAdministrator() || (administratorAuthority.getAddChildAdmistrator() && ((administratorTypeId ==3 && administratorType.getId() == 2)|| (administratorTypeId == 5 &&  administratorType.getId() == 4)))) {
            if(administratorService.findByParams(new Administrator(account)).size()==0) {
                return ResultUtil.Success(administratorService.save(new Administrator(account, psw, administratorTypeId)));
            }else {
                return ResultUtil.Error(ErrorCode.ISEXIST);
            }
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 获得管理类型
     * @return
     * @throws Exception
     */
    @GetMapping("/getAdministratorType")
    @ApiOperation("获得管理类型")
    public ResultDTO getAdministratorType()throws Exception{
        return ResultUtil.Success(administratorTypeService.findAll());
    }

    /**
     * 删除管理员
     * @param administratorId
     * @param deleteAdministratorId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/deleteAdministrator")
    @ApiOperation("删除管理员")
    public ResultDTO deleteAdministrator(Integer administratorId,Integer deleteAdministratorId)throws Exception{
        if(administratorId == deleteAdministratorId){
            return ResultUtil.Error(ErrorCode.CANTDOIT);
        }
        AdministratorType administratorType = administratorTypeService.get(
                administratorService.get(administratorId).getTypeId());
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(administratorType
                .getAuthorityId());
        AdministratorType deleteAdministratorType = administratorTypeService.get(
                administratorService.get(administratorId).getTypeId());
        if(administratorAuthority.getAddAdministrator() || (administratorAuthority.getAddChildAdmistrator() && ((deleteAdministratorType.getId() ==3 && administratorType.getId() == 2)|| (deleteAdministratorType.getId() == 5 &&  administratorType.getId() == 4)))) {
            administratorService.deleteById(deleteAdministratorId);
            return ResultUtil.Success();
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 获得我的景区
     * @param administratorId
     * @return
     * @throws Exception
     */
    @GetMapping("/getMyScenic")
    @ApiOperation("获得我的景区")
    public ResultDTO getMyScenic(Integer administratorId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageScenic()){
            if (administratorAuthority.getAddChildAdmistrator()||administratorAuthority.getAddAdministrator()){
                return ResultUtil.Success(scenicService.findAll());
            }
            return ResultUtil.Success(scenicService.findByParams(new Scenic(administratorId)));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 搜素景区
     * @param administratorId
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findMyScenic")
    @ApiOperation("搜素景区")
    public ResultDTO findMyScenic(Integer administratorId,String attr)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageScenic()){
            Scenic scenic = new Scenic();
            scenic.setName(attr);
            if (administratorAuthority.getAddChildAdmistrator()||administratorAuthority.getAddAdministrator()){
                return ResultUtil.Success(scenicService.findByParams(scenic));
            }
            scenic.setAdministratorId(administratorId);
            return ResultUtil.Success(scenicService.findByParams(scenic));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 添加景区
     * @param administratorId
     * @param scenic
     * @return
     * @throws Exception
     */
    @PostMapping("/addScenic")
    @ApiOperation("添加景区")
    public ResultDTO addScenic(Integer administratorId,Scenic scenic)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageScenic()){
            scenic.setAdministratorId(administratorId);
            return ResultUtil.Success(scenicService.save(scenic));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 删除景区
     * @param administratorId
     * @param scenicId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/deleteScenic")
    @ApiOperation("删除景区")
    public ResultDTO deleteScenic(Integer administratorId,Long scenicId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageScenic()){
            if (administratorAuthority.getAddChildAdmistrator()||administratorAuthority.getAddAdministrator()){
                scenicService.deleteById(scenicId);
                return ResultUtil.Success();
            }
            if(scenicService.get(scenicId).getAdministratorId() == administratorId) {
                scenicService.deleteById(scenicId);
                return ResultUtil.Success();
            }
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 获取申请的导游信息
     * @param administratorId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuideing")
    @ApiOperation("获取申请的导游信息")
    public ResultDTO getGuideing(Integer administratorId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            List<Guide> guides = new ArrayList<>();
            guides.addAll(guideService.findByParams(new Guide(0)));
            guides.addAll(uGuideService.findAll());
            return ResultUtil.Success(guides);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 搜索未审核的导游
     * @param administratorId
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findGuideing")
    @ApiOperation("搜索未审核的导游")
    public ResultDTO findGuideing(Integer administratorId,String attr)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            List<Guide> result = new ArrayList<>();
            Guide guide = new Guide();
            guide.setName(attr);
            guide.setState(0);
            result.addAll(guideService.findByParams(guide));
            Guide g = new Guide();
            g.setPhone(attr);
            g.setState(0);
            result.addAll(guideService.findByParams(g));
            Guide g1 = new Guide();
            g1.setName(attr);
            for (Guide ggg:guideService.findByParams(g1)){
                result.addAll(uGuideService.findByParams(new UGuide(ggg.getTouristId())));
            }
            Guide guide1 = new Guide();
            guide1.setPhone(attr);
            for (Guide gggg:guideService.findByParams(guide1)){
                result.addAll(uGuideService.findByParams(new UGuide(gggg.getTouristId())));
            }
            Collections.sort(result,(o1,o2)-> (int) (o1.getTime().getTime() - o2.getTime().getTime()));
            return ResultUtil.Success(result);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }


    /**
     * 获取所有通过导游信息
     * @param administratorId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuide")
    @ApiOperation("获取所有通过导游信息")
    public ResultDTO getGuide(Integer administratorId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            return ResultUtil.Success(guideService.findByParams(new Guide(1)));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 搜索审核通过的导游
     * @param administratorId
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findGuide")
    @ApiOperation("搜索导游")
    public ResultDTO findGuide(Integer administratorId,String attr)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            if(!StringUtils.isNotBlank(attr)){
                return ResultUtil.Success();
            }
            List<Guide> result = new ArrayList<>();
            Guide guide = new Guide();
            guide.setName(attr);
            result.addAll(guideService.findByParams(guide));
            Guide g = new Guide();
            g.setPhone(attr);
            result.addAll(guideService.findByParams(g));
            return ResultUtil.Success(result);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }


    /**
     * 获取导游详情
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuideMsg")
    @ApiOperation("获取导游详情")
    public ResultDTO getGuideMsg(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            return ResultUtil.Success(guideService.get(guideId));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }


    /**
     * 获取导游视频
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuideVideos")
    @ApiOperation("获取导游视频")
    public ResultDTO getGuideVideos(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageVideo()){
            return ResultUtil.Success(videoService.findByParams(new Video(guideId)));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 获取导游订单
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @GetMapping("/getGuideOrder")
    @ApiOperation("获取导游订单")
    public ResultDTO getGuideOrder(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageOrder()){
            List<ManagerOrderDTO> managerOrderDTOS = new ArrayList<>();
            for(TheOrder o:theOrderService.findByParams(new TheOrder(guideId))){
                Route r = routeService.get(o.getRouteId());
                managerOrderDTOS.add(new ManagerOrderDTO(o.getId(),r.getLine(),o.getTName(),MyMathUtil.getTime(o.getTime(),r.getRDay()),o.getState()));
            }
            return ResultUtil.Success(managerOrderDTOS);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 得到订单详情
     * @param administratorId
     * @param orderId
     * @return
     * @throws Exception
     */
    @GetMapping("/getOrder")
    @ApiOperation("得到订单详情")
    public ResultDTO getOrder(Integer administratorId,Long orderId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageOrder()){
            TheOrder theOrder = theOrderService.get(orderId);
            return ResultUtil.Success(new ManagerOrderMsgDTO(routeService.get(theOrder.getRouteId()).getLine(),
                    guideService.get(theOrder.getGuideId()).getName(),theOrder.getTStart(),theOrder.getNOP(),theOrder.getTName(),
                    theOrder.getIdNumber(),theOrder.getPhone(),theOrder.getMeetTime()));
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }

    }


    /**
     * 搜素订单
     * @param administratorId
     * @param attr
     * @return
     * @throws Exception
     */
    @GetMapping("/findOrder")
    @ApiOperation("搜素订单")
    public ResultDTO findOrder(Integer administratorId,String attr)throws  Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageOrder()){
            List<ManagerOrderDTO> managerOrderDTOS = new ArrayList<>();
            TheOrder t = new TheOrder();
            t.setTName(attr);
            TheOrder to = new TheOrder();
            to.setPhone(attr);
            for(TheOrder o:theOrderService.findByParams(t)){
                Route r = routeService.get(o.getRouteId());
                managerOrderDTOS.add(new ManagerOrderDTO(o.getId(),r.getLine(),o.getTName(),MyMathUtil.getTime(o.getTime(),r.getRDay()),o.getState()));
            }
            for(TheOrder o:theOrderService.findByParams(to)){
                Route r = routeService.get(o.getRouteId());
                managerOrderDTOS.add(new ManagerOrderDTO(o.getId(),r.getLine(),o.getTName(),MyMathUtil.getTime(o.getTime(),r.getRDay()),o.getState()));
            }
            return ResultUtil.Success(managerOrderDTOS);
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 取消订单
     * @param administratorId
     * @param orderId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/cancleOrder")
    @ApiOperation("取消订单")
    public ResultDTO cancleOrder(Integer administratorId,Long orderId) throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageOrder()){
            TheOrder theOrder = theOrderService.get(orderId);
            if(theOrder != null && theOrder.getState().equals("222")) {
                TheOrder t = new TheOrder();
                t.setId(orderId);
                t.setState("333");
                theOrderService.update(t);
                messageService.save(new Message(theOrder.getTouristId(),"为维护神曲健康生态，平台坚决抵制一切低俗内容。你2020年1月11日发布的视频因违反平台协议，平台已将其删除。详情请拨打****（电话）咨询。"));
                return ResultUtil.Success();
            }else{
                return ResultUtil.Error(ErrorCode.NOEXIST);
            }
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 通过认证
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @PostMapping("/passGuide")
    @ApiOperation("通过认证")
    public ResultDTO passGuide(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            Guide g = guideService.get(guideId);
            if(g.getState()==1){
                List<UGuide> uGuides = uGuideService.findByParams(new UGuide(g.getTouristId()));
                if(uGuides.size()>1){
                    return ResultUtil.Error(ErrorCode.UNKNOWERROR);
                }
                guideService.update(new Guide(guideId,uGuides.get(0)));
                uGuideService.deleteById(guideId);
                messageService.save(new Message(g.getTouristId(),"用户你好，恭喜你的导游信息修改成功"));
                return ResultUtil.Success();
            }
            guideService.update(new Guide(guideId,1));
            touristService.update(new Tourist(g.getTouristId(),true));
            messageService.save(new Message(g.getTouristId(),"用户你好，恭喜你成功认证成为超级导游！【我的】页面点击“我的导游世界”发布你的视频吧！"));
            return ResultUtil.Success();
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 不通过认证
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @PostMapping("/noPassGuide")
    @ApiOperation("不通过认证")
    public ResultDTO noPassGuide(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            guideService.update(new Guide(guideId,2));
            messageService.save(new Message(guideService.get(guideId).getTouristId(),"用户你好，你的超级导游认证申请由于####（原因）认证失败，赶快修改信息重新申请吧！"));
            return ResultUtil.Success();
        }else{
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }


    /**
     * 获取管理员账户
     * @param administratorId
     * @return
     * @throws Exception
     */
    @GetMapping("/getAdministrators")
    @ApiOperation("获取管理员账户")
    public ResultDTO getAdministrators(Integer administratorId)throws Exception{
        AdministratorType administratorType = administratorTypeService.get(
                administratorService.get(administratorId).getTypeId());
        AdministratorAuthority administratorAuthority =
                administratorAuthorityService.get(administratorType.getAuthorityId());
        List<AdministratorDTO> list = new ArrayList<>();
        if(administratorAuthority.getAddAdministrator()){
            for (Administrator a:administratorService.findAll()){
                list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
            }
            return ResultUtil.Success(list);
        }else if(administratorAuthority.getAddChildAdmistrator() && administratorType.getId() == 2){
            for(Administrator a:administratorService.findByParams(new Administrator(3))){
                list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
            }
            return ResultUtil.Success(list);
        }else if(administratorAuthority.getAddChildAdmistrator() && administratorType.getId() == 4){
            for(Administrator a:administratorService.findByParams(new Administrator(5))){
                list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
            }
            return ResultUtil.Success(list);
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }


    /**
     * 搜素管理员账户
     * @param administratorId
     * @param administratorTypeId
     * @return
     * @throws Exception
     */
    @GetMapping("/findAdministrator")
    @ApiOperation("搜素管理员账户")
    public ResultDTO findAdministrator(Integer administratorId,Integer administratorTypeId)throws Exception{
        AdministratorType administratorType = administratorTypeService.get(
                administratorService.get(administratorId).getTypeId());
        AdministratorAuthority administratorAuthority =
                administratorAuthorityService.get(administratorType.getAuthorityId());
        List<AdministratorDTO> list = new ArrayList<>();
        if(administratorAuthority.getAddAdministrator()){
            for(Administrator a:administratorService.findByParams(new Administrator(administratorTypeId))){
                list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
            }
            return ResultUtil.Success(list);
        }else if(administratorAuthority.getAddChildAdmistrator() && administratorType.getId() == 2){
            if(administratorTypeId == 3){
                for(Administrator a:administratorService.findByParams(new Administrator(administratorTypeId))){
                    list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
                }
                return ResultUtil.Success(list);
               }else {
                return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
            }
        }else if(administratorAuthority.getAddChildAdmistrator() && administratorType.getId() == 4){
            if(administratorTypeId == 5){
                for(Administrator a:administratorService.findByParams(new Administrator(administratorTypeId))){
                    list.add(new AdministratorDTO(a,administratorTypeService.get(a.getTypeId()).getTypeName()));
                }
                return ResultUtil.Success(list);
            }else {
                return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
            }
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 发送通告
     * @param administratorId
     * @param touristId
     * @param msg
     * @return
     * @throws Exception
     */
    @PostMapping("/informMsg")
    @ApiOperation("发送通告")
    public ResultDTO informMsg(Integer administratorId,Long touristId,String msg)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getInform()){
            return ResultUtil.Success(messageService.save(new Message(touristId,msg)));
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 删除导游视频
     * @param administratorId
     * @param videoId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/removeVideo")
    @ApiOperation("删除导游视频")
    public ResultDTO removeVideo(Integer administratorId,Long videoId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageVideo()){
            messageService.save(new Message(guideService.get(videoService.get(videoId).getGuideId()).getTouristId(),"为维护神曲健康生态，平台坚决抵制一切低俗内容。你2020年1月11日发布的视频因违反平台协议，平台已将其删除。详情请拨打****（电话）咨询。"));
            videoService.deleteById(videoId);
            likeVideoService.delete(new LikeVideo(videoId));
            return ResultUtil.Success();
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

    /**
     * 取消认证
     * @param administratorId
     * @param guideId
     * @return
     * @throws Exception
     */
    @DeleteMapping("/removeGuide")
    @ApiOperation("取消认证")
    public ResultDTO removeGuide(Integer administratorId,Long guideId)throws Exception{
        AdministratorAuthority administratorAuthority = administratorAuthorityService.get(
                administratorTypeService.get(
                        administratorService.get(administratorId).getTypeId()).getAuthorityId());
        if(administratorAuthority.getManageGuide()){
            Guide g = guideService.get(guideId);
            if(g == null){
                return ResultUtil.Error(ErrorCode.NOEXIST);
            }
            guideService.update(new Guide(guideId,2));
            touristService.update(new Tourist(g.getTouristId(),false));
            messageService.save(new Message(g.getTouristId(),"为维护社区健康生态，平台坚决抵制一切违规行为。你的部分行为已经违反“超级导游”的相关协定，因此，平台决定取消你的导游认证。详情请拨打###（电话）咨询。"));
            return ResultUtil.Success();
        }else {
            return ResultUtil.Error(ErrorCode.LIMITED_AUTHORITY);
        }
    }

}
