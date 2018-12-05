package com.kaihei.esportingplus.customer.center.domain.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kaihei.commons.cache.api.spi.common.CacheManager;
import com.kaihei.commons.cache.api.spi.common.CacheManagerFactory;
import com.kaihei.esportingplus.api.enums.ComplainOrderTypeEnum;
import com.kaihei.esportingplus.api.enums.ComplainTypeEnum;
import com.kaihei.esportingplus.api.enums.ComplaintStatusEnum;
import com.kaihei.esportingplus.api.params.ComplainCreateParam;
import com.kaihei.esportingplus.api.params.ComplaintQueryParam;
import com.kaihei.esportingplus.api.params.StudioComplainQueryParams;
import com.kaihei.esportingplus.api.vo.BeComplaintInfo;
import com.kaihei.esportingplus.api.vo.ComplaintDetailVo;
import com.kaihei.esportingplus.api.vo.ComplaintInfo;
import com.kaihei.esportingplus.api.vo.ComplaintListVo;
import com.kaihei.esportingplus.api.vo.StudioComplaintListVo;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.ValidateAssert;
import com.kaihei.esportingplus.common.enums.BaojiLevelEnum;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.paging.PagingResponse;
import com.kaihei.esportingplus.common.tools.CollectionUtils;
import com.kaihei.esportingplus.common.web.UserSessionContext;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintItemPictureRepository;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintItemRepository;
import com.kaihei.esportingplus.customer.center.data.repository.CompaintRepository;
import com.kaihei.esportingplus.customer.center.domain.entity.Compaint;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItem;
import com.kaihei.esportingplus.customer.center.domain.entity.CompaintItemPicture;
import com.kaihei.esportingplus.customer.center.domain.service.ICustomerCenterService;
import com.kaihei.esportingplus.customer.center.event.CreateComplaintEvent;
import com.kaihei.esportingplus.customer.center.event.CreateComplaintEventConsumer;
import com.kaihei.esportingplus.gamingteam.api.feign.RPGTeamServiceClient;
import com.kaihei.esportingplus.gamingteam.api.vo.TeamGameResultVO;
import com.kaihei.esportingplus.common.enums.GameResultEnum;
import com.kaihei.esportingplus.trade.api.enums.OrderStatusEnum;
import com.kaihei.esportingplus.trade.api.feign.RPGOrdersServiceClient;
import com.kaihei.esportingplus.trade.api.vo.OrderItemTeamVo;
import com.kaihei.esportingplus.trade.api.vo.OrderVO;
import com.kaihei.esportingplus.user.api.feign.UserServiceClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 客服服务接口
 *
 * @author admin
 */
@Service("customerCenterService")
public class CustomerCenterServiceImpl implements ICustomerCenterService {

    protected static final CacheManager cacheManager = CacheManagerFactory.create();
    //private static final String URL = "http://120.79.211.158:11029/user/uid";
    private static final long STANDARD_TIME = 72 * 60 *60;
    private final Logger log = LoggerFactory.getLogger(CustomerCenterServiceImpl.class);

    @Autowired
    private RPGOrdersServiceClient RPGOrdersServiceClient;
    @Resource(name = "restTemplateExtrnal")
    private RestTemplate restTemplateExtrnal;
    @Autowired
    private CompaintRepository compaintRepository;
    @Autowired
    private CompaintItemRepository compaintItemRepository;
    @Autowired
    private CompaintItemPictureRepository compaintItemPictureRepository;
    @Autowired
    private RPGTeamServiceClient RPGTeamServiceClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private CreateComplaintEventConsumer createComplaintEventConsumer;

    @Override
    public void createComplaint(ComplainCreateParam complainCreateParam) {
        //生成时间戳
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        String timeStamp = df.format(calendar.getTime());
        //合成投诉单号
        String sequeue = "TSDNF" + timeStamp;
        long dateNum = Long.parseLong(timeStamp + String.valueOf("00000"));
        //REDIS中获取递增值
        long value = cacheManager.incr(sequeue);
        //设置过期时间为一天
        cacheManager.expire(sequeue, 86400);
        sequeue = "TSDNF" + String.valueOf(dateNum + value);
        //获取用户ID
        UserSessionContext userDetail = UserSessionContext.getUser();
        String uid = userDetail.getUid();
        //校验老板身份才能对订单发起投诉 todo

        //调用订单服务，根据订单号，获取被投诉人uid，暴鸡等级，游戏code
        OrderItemTeamVo orderItemTeamVo = RPGOrdersServiceClient.selectOrderItemTeamByOrderSequeue(
                complainCreateParam.getBizOrderSequeue()).getData();
        //根据投诉人uid,获取鸡牌号，昵称
        /*ResponsePacket res1 = restTemplateExtrnal
                .getForObject(URL + "?uid=" + uid, ResponsePacket.class);
        String chickenId = ((LinkedHashMap) res1.getData()).get("chicken_id").toString();
        String username = ((LinkedHashMap) res1.getData()).get("username").toString();*/
        ResponsePacket<UserSessionContext> responsePacket = userServiceClient.getUserInfosByUid(uid);
        if(!responsePacket.responseSuccess()){
            throw new BusinessException(responsePacket.getCode(),responsePacket.getMsg());
        }
        String chickenId = responsePacket.getData().getChickenId();
        String username = responsePacket.getData().getUsername();
        //根据被投诉人uid,获取鸡牌号，昵称
        /*ResponsePacket res2 = restTemplateExtrnal
                .getForObject(URL + "?uid=" + orderItemTeamVo.getUid(), ResponsePacket.class);
        String beChickenId = ((LinkedHashMap) res2.getData()).get("chicken_id").toString();
        String beUsername = ((LinkedHashMap) res2.getData()).get("username").toString();*/
        responsePacket = userServiceClient.getUserInfosByUid(orderItemTeamVo.getUid());
        if(!responsePacket.responseSuccess()){
            throw new BusinessException(responsePacket.getCode(),responsePacket.getMsg());
        }
        String beChickenId = responsePacket.getData().getChickenId();
        String beUsername = responsePacket.getData().getUsername();
        //校验投诉人和被投诉人是否同时存在
        int count = compaintRepository.checkUniqueCompaint(uid, orderItemTeamVo.getUid());
        if (count >= 1) {
            throw new BusinessException(BizExceptionEnum.COMPLAINT_NOT_ALLOWED_TWICE);
        }
        //Compaint实体
        Compaint compaint = new Compaint();
        compaint.setType(ComplainOrderTypeEnum.DNF.getCode());
        compaint.setBizOrderSequeue(complainCreateParam.getBizOrderSequeue());
        compaint.setUid(uid);
        compaint.setUsername(username);
        compaint.setChickenId(chickenId);
        compaint.setBeUid(orderItemTeamVo.getUid());
        compaint.setBeChickenId(beChickenId);
        compaint.setBeUsername(beUsername);
        compaint.setBaojiLevel(orderItemTeamVo.getUserBaojiLevel());
        compaint.setGameCode(orderItemTeamVo.getGameCode());
        compaint.setGameName(orderItemTeamVo.getGameName());
        compaint.setSequeue(sequeue);
        compaint.setStatus(ComplaintStatusEnum.APPEALING.getCode());
        //CompaintItem实体
        CompaintItem compaintItem = new CompaintItem();
        compaintItem.setType(ComplainTypeEnum.DEFAULT.getCode());
        compaintItem.setContent(complainCreateParam.getComplainContent() == null ? ""
                : complainCreateParam.getComplainContent());
        //事务不能回滚，暂时不使用异步入库
        CreateComplaintEvent event = new CreateComplaintEvent(compaint, compaintItem,
                complainCreateParam.getComplainImgUrlList());
        createComplaintEventConsumer.createComplaint(event);
        //EventBus.post(event);
    }

    @Override
    public PagingResponse<ComplaintListVo> listComplaint(ComplaintQueryParam complaintQueryParam) {
        //因为python端排序字段是这样组成："-字段名",-表示降序，所以需要解析
        String sortKey = complaintQueryParam.getSortKey();
        if(sortKey!=null && !"".equalsIgnoreCase(sortKey)){
            if("-".equalsIgnoreCase(sortKey.substring(0,1))){
                complaintQueryParam.setSortType(0);//降序
                complaintQueryParam.setSortField(sortKey.substring(1));
            }
        }
        Page<ComplaintListVo> page = PageHelper
                .startPage(complaintQueryParam.getPage(), complaintQueryParam.getPageSize())
                .doSelectPage(() -> compaintRepository.listCompaint(complaintQueryParam));

        List<ComplaintListVo> complaintListVos = combineComplaintData(complaintQueryParam,
                page.getResult());
        PagingResponse<ComplaintListVo> pagingResponse = new PagingResponse<ComplaintListVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), complaintListVos);
        return pagingResponse;
    }

    private List<ComplaintListVo> combineComplaintData(ComplaintQueryParam complaintQueryParam,
            List<ComplaintListVo> list) {
        List<ComplaintListVo> result = new LinkedList<ComplaintListVo>();
        ComplaintListVo complaintListVo = null;
        BeComplaintInfo beComplaintInfo = null;
        ComplaintInfo complaintInfo = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            complaintListVo = list.get(i);
            //暴鸡等级赋值,被投诉人信息拼接
            beComplaintInfo = complaintListVo.getBeComplaintInfo();
            if (BaojiLevelEnum.COMMON.getCode()==(beComplaintInfo.getBaojiLevel())) {
                beComplaintInfo.setBaojiLevelName(BaojiLevelEnum.COMMON.getDesc());
            } else if (BaojiLevelEnum.PREFERENCE.getCode()
                    ==(beComplaintInfo.getBaojiLevel())) {
                beComplaintInfo.setBaojiLevelName(BaojiLevelEnum.PREFERENCE.getDesc());
            } else if (BaojiLevelEnum.SUPER.getCode()==(beComplaintInfo.getBaojiLevel())) {
                beComplaintInfo.setBaojiLevelName(BaojiLevelEnum.SUPER.getDesc());
            } else {
                beComplaintInfo.setBaojiLevelName(BaojiLevelEnum.BN.getDesc());
            }
            beComplaintInfo.setBeComplaintText(beComplaintInfo.getBeUsername() + "/"
                    + beComplaintInfo.getBeUid() + "/" + beComplaintInfo.getBeChickenId());
            complaintListVo.setBeComplaintInfo(beComplaintInfo);
            //拼接投诉人信息
            complaintInfo = complaintListVo.getComplaintInfo();
            complaintInfo.setComplaintText(complaintInfo.getUsername() + "/"
                    + complaintInfo.getUid() + "/" + complaintInfo.getChickenId());
            complaintListVo.setComplaintInfo(complaintInfo);
            //计算剩余时间
            long createTime = complaintListVo.getCreateTime().getTime();
            long now = complaintQueryParam.getNow();
            long pastTime = now - createTime;
            long remainTime = STANDARD_TIME * 1000 - pastTime;
            //超时未申诉
            if (complaintListVo.getStatus() < ((int) (ComplaintStatusEnum.TIMEOUT_NOT_APPEAL
                    .getCode()))) {
                if (remainTime < 0) {
                    complaintListVo
                            .setStatus(((int) (ComplaintStatusEnum.TIMEOUT_NOT_APPEAL.getCode())));
                } else if (remainTime < 24 * 60 * 60 * 1000) {
                    complaintListVo.setRemainingTime("24小时内");
                } else if (remainTime < 48 * 60 * 60 * 1000) {
                    complaintListVo.setRemainingTime("24小时后48小时内");
                } else {
                    complaintListVo.setRemainingTime("48小时后");
                }
            }

            //结束的投诉
            if (complaintListVo.getStatus() >= ((int) (ComplaintStatusEnum.TIMEOUT_NOT_APPEAL
                    .getCode()))) {
                complaintListVo.setRemainingTime("00:00");
            }
            //加入新集合
            result.add(complaintListVo);
        }
        list.clear();
        return result;
    }

    @Override
    public PagingResponse<StudioComplaintListVo> studiolist(
            StudioComplainQueryParams studioComplainQueryParams) {
        Page<StudioComplaintListVo> page = PageHelper
                .startPage(studioComplainQueryParams.getOffset(),
                        studioComplainQueryParams.getLimit())
                .doSelectPage(() -> compaintRepository.studiolist(studioComplainQueryParams));

        List<StudioComplaintListVo> studioComplaintListVos = combineStudioListData(
                page.getResult());
        PagingResponse<StudioComplaintListVo> pagingResponse = new PagingResponse<StudioComplaintListVo>(
                page.getPageNum(), page.getPageSize(),
                page.getTotal(), studioComplaintListVos);
        return pagingResponse;
    }

    private List<StudioComplaintListVo> combineStudioListData(List<StudioComplaintListVo> list) {
        List<StudioComplaintListVo> result = new LinkedList<StudioComplaintListVo>();
        StudioComplaintListVo studioComplaintListVo = null;
        for (int i = 0; list != null && i < list.size(); i++) {
            studioComplaintListVo = list.get(i);
            //计算剩余时间
            long createTime = studioComplaintListVo.getCreateTime().getTime();
            studioComplaintListVo.setRemainingTime(calculateRemainingTime(createTime));
            //加入新集合
            result.add(studioComplaintListVo);
        }
        list.clear();
        return result;
    }

    private String calculateRemainingTime(long creatTime){
        long currentTime = new Date().getTime() / 1000;//秒级
        long createTime = creatTime / 1000;//秒级
        long diffTime = currentTime - createTime;//相差多少秒
        long restTime = STANDARD_TIME - diffTime;//剩余秒数
        String restTimeStr = (restTime / 3600) + ":" + ((restTime % 3600) / 60);//时分
        if(restTime <= 0){
            return "00:00";
        }
        return restTimeStr;
    }

    @Override
    public ComplaintDetailVo getComplaintDetail(Integer oid) {
        ComplaintDetailVo complaintDetailVo = compaintRepository.selectCompaintById(oid);
        ValidateAssert.hasNotNull(BizExceptionEnum.INTERNAL_SERVER_ERROR, complaintDetailVo);
        //暴鸡等级赋值,被投诉人信息拼接
        BeComplaintInfo beComplaintInfo = complaintDetailVo.getBeComplaintInfo();
        beComplaintInfo.setBaojiLevelName(this.getBaojiLevelName(
                beComplaintInfo.getBaojiLevel()));
        beComplaintInfo.setBeComplaintText(beComplaintInfo.getBeUsername() + "/"
                + beComplaintInfo.getBeUid() + "/" + beComplaintInfo.getBeChickenId());

        //拼接投诉人信息
        ComplaintInfo complaintInfo = complaintDetailVo.getComplaintInfo();
        complaintInfo.setComplaintText(complaintInfo.getUsername() + "/"
                + complaintInfo.getUid() + "/" + complaintInfo.getChickenId());

        //拼接投诉信息和投诉原因
        CompaintItem compaintItem = compaintItemRepository.selectByCompaintId(oid);

        complaintDetailVo.setComplainContent(compaintItem.getContent());
        complaintDetailVo.setReasonOption(compaintItem.getType().intValue());

        CollectionUtils
                .find(ComplainTypeEnum.values(), cte -> cte.getCode() == compaintItem.getType())
                .ifPresent(cte -> complaintDetailVo.setReason(cte.getDesc()));

        //拼接投诉图片信息
        List<CompaintItemPicture> compaintItemPictures = compaintItemPictureRepository
                .selectCompaintItemPicturesByCompaintId(oid);

        Optional.ofNullable(compaintItemPictures).filter(cip -> !cip.isEmpty()).ifPresent(cip ->
                complaintDetailVo.setComplainImg(
                        cip.stream().map(CompaintItemPicture::getUrl).collect(
                                Collectors.toList())));
        
        //订单Id
        String premadeOrderOid = complaintDetailVo.getPremadeOrderOid();

        //拼接用户游戏相关信息
        ResponsePacket<OrderVO> orderVOResponsePacket = RPGOrdersServiceClient
                .getBySequenceId(premadeOrderOid);
        ValidateAssert.allNotNull(BizExceptionEnum.INTERNAL_SERVER_ERROR, orderVOResponsePacket);

        OrderVO orderVO = orderVOResponsePacket.getData();
        complaintDetailVo
                .setOrderStatus(OrderStatusEnum.fromCode(orderVO.getStatus()).getDesc());
        complaintDetailVo.setStartTime(orderVO.getResponseTime());
        complaintDetailVo.setEndTime(orderVO.getCloseTime());

        OrderItemTeamVo orderItemTeam = orderVO.getOrderItemTeam();
        if (orderItemTeam != null) {
            String teamSequeue = orderItemTeam.getTeamSequeue();
            complaintDetailVo.setPremadeId(teamSequeue);
            complaintDetailVo.setInstanceZonesType(orderItemTeam.getRaidName());

            //拼接车队标题及车队胜负信息
            ResponsePacket<TeamGameResultVO> gamingTeamGameResult = RPGTeamServiceClient
                    .getGamingTeamGameResult(teamSequeue);
            ValidateAssert.allNotNull(BizExceptionEnum.INTERNAL_SERVER_ERROR,
                    gamingTeamGameResult);

            TeamGameResultVO teamGameResultVO = gamingTeamGameResult.getData();
            complaintDetailVo.setComplainTitle(teamGameResultVO.getTeamTitle());

            GameResultEnum gameResultEnum = GameResultEnum
                    .fromCode(teamGameResultVO.getGameResultCode().intValue());
            complaintDetailVo
                    .setResult(gameResultEnum.getDesc());
        }

        return complaintDetailVo;
    }


    /**
     * 通过baojiLevel计算baojiLevelName
     */
    private String getBaojiLevelName(Integer baojiLevel) {
        BaojiLevelEnum baojiLevelEnum = CollectionUtils
                .find(BaojiLevelEnum.values(), ble -> ble.getCode() == baojiLevel)
                .orElse(null);
        if (baojiLevelEnum == null) {
            return null;
        }

        return baojiLevelEnum.getDesc();
    }

    @Override
    public List<String> checkOrderBeComplainted(List<String> orderSequeues) {
        List<ComplaintListVo> complaintListVos = compaintRepository
                .selectCompaintByOrderSequeues(orderSequeues);
        return complaintListVos.stream()
                .map(ComplaintListVo::getPremadeOrderOid).collect(Collectors.toList());
    }

    /**
     * 根据uid统计 用户是否被投诉过
     *
     * @param uid 用户uid
     * @return 被投诉单数 0 未被投诉
     */
    @Override
    public Integer checkUserBeComplained(String uid) {
        return compaintRepository.selectCountByBeuidAndStatusLte(uid, 3);
    }
}
