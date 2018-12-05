package com.kaihei.esportingplus.user.data.manager;

import com.alibaba.fastjson.JSON;
import com.kaihei.esportingplus.common.ResponsePacket;
import com.kaihei.esportingplus.common.enums.BizExceptionEnum;
import com.kaihei.esportingplus.common.exception.BusinessException;
import com.kaihei.esportingplus.common.tools.StringUtils;
import com.kaihei.esportingplus.core.api.feign.QiniuManageServiceClient;
import com.kaihei.esportingplus.core.api.vo.QiniuImageCheckVo;
import com.kaihei.esportingplus.user.api.enums.AlbumPictureType;
import com.kaihei.esportingplus.user.api.vo.PicturesVo;
import com.kaihei.esportingplus.user.api.vo.UserUrlMessageVo;
import com.kaihei.esportingplus.user.constant.MembersAuthConstants;
import com.kaihei.esportingplus.user.data.pyrepository.MembersAlbumpictureRepository;
import com.kaihei.esportingplus.user.domain.entity.MembersAlbumpicture;
import com.kaihei.esportingplus.user.mq.producer.CommonProducer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: linruihe
 * @Date: 2018-10-25 17:41
 * @Description:
 */
@Service
public class MembersAlbumpictureManagerImpl implements MembersAlbumpictureManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int PICTURE_COUNT_MIN = 1;
    private static final int PICTURE_COUNT_MAX = 8;

    @Autowired
    private MembersAlbumpictureRepository membersAlbumpictureRepository;

    @Autowired
    private CommonProducer commonProducer;

    @Autowired
    private QiniuManageServiceClient qiniuManageServiceClient;

    /**
     * 替换图片
     * @param userId
     * @param url
     * @param id
     * @return
     */
    @Override
    public PicturesVo changePicture(Integer userId, String url, Integer id) {
        if(id == null || StringUtils.isEmpty(url)){
            logger.error("cmd=changePicture.insert param | userId={} | url={} | id={} | msg={}",
                    userId,url,id,JSON.toJSON(BizExceptionEnum.PARAM_ENTRY_ERROR));
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }

        //根据userId和图片id查询图片信息
        MembersAlbumpicture albumpicture = new MembersAlbumpicture();
        albumpicture.setId(id);
        List<MembersAlbumpicture> membersAlbumpictureList =
                membersAlbumpictureRepository.selectPicturesByUserId(null,userId,id);
        if(membersAlbumpictureList == null || membersAlbumpictureList.size() <= 0){
            logger.error("cmd=changePicture.selectPicturesByUserId param | userId={} | url={} | id={} | msg={}",
                    userId,url,id,JSON.toJSON(BizExceptionEnum.PICTURE_NO_FIND));
            throw new BusinessException(BizExceptionEnum.PICTURE_NO_FIND);
        }
        logger.info("cmd=changePicture selectPicturesByUserId param | userId={} | url={} | id={} | membersAlbumpictureList={}",
                userId,url,id,JSON.toJSON(membersAlbumpictureList));

        //判断图片状态
        MembersAlbumpicture membersAlbumpicture = membersAlbumpictureList.get(0);
        int albumStatus = membersAlbumpicture.getStatus();
        if(albumStatus == AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode()){
            logger.error("cmd=changePicture.selectPicturesByUserId param | userId={} | url={} | albumStatus={} | msg={}",
                    userId,url,albumStatus,JSON.toJSON(BizExceptionEnum.PICTURE_VERIFYING));
            throw new BusinessException(BizExceptionEnum.PICTURE_VERIFYING);
        }
        return replaceAlbumpicture(userId,url,id);
    }

    /**
     * 删除图片
     * @param userId
     * @param id
     * @return
     */
    @Override
    public PicturesVo deletePicture(Integer userId, Integer id) {
        PicturesVo picturesVo = new PicturesVo();
        if(id == null){
            logger.error("cmd=deletePicture param | userId={} | id={} | msg={}",
                    userId,id,JSON.toJSON(BizExceptionEnum.PARAM_ENTRY_ERROR));
            throw new BusinessException(BizExceptionEnum.PARAM_ENTRY_ERROR);
        }

        List<Integer> statusList = new ArrayList<>();
        statusList.add(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode());
        statusList.add(AlbumPictureType.PICTURE_STATUS_VERIFY_SUCCESS.getCode());
        List<MembersAlbumpicture> membersAlbumpictureList1 =
                membersAlbumpictureRepository.selectPicturesByUserId(statusList,userId,null);
        if(membersAlbumpictureList1.size() <= PICTURE_COUNT_MIN){
            logger.error("cmd=deletePicture selectPicturesByUserId | userId={} | msg={}",
                    userId,JSON.toJSON(BizExceptionEnum.PICTURE_OVER_MIN));
            throw new BusinessException(BizExceptionEnum.PICTURE_OVER_MIN);
        }
        MembersAlbumpicture deleteAlbumpicture = null;
        for (MembersAlbumpicture membersAlbumpicture: membersAlbumpictureList1) {
            if(id.equals(membersAlbumpicture.getId())){
                deleteAlbumpicture = membersAlbumpicture;
                break;
            }
        }
        if(deleteAlbumpicture == null){
            return picturesVo;
        }
        if(deleteAlbumpicture.getStatus().equals(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode())){
            logger.error("cmd=deletePicture selectPicturesByUserId | userId={} | deleteAlbumpicture={} | msg={}",
                    userId,deleteAlbumpicture,JSON.toJSON(BizExceptionEnum.PICTURE_VERIFYING));
            throw new BusinessException(BizExceptionEnum.PICTURE_VERIFYING);
        }

        //软删除
        deleteAlbumpicture = deleteWeakPicture(deleteAlbumpicture);
        logger.info("cmd=deletePicture deleteWeakPicture | userId={} | deleteAlbumpicture={}", userId,JSON.toJSON(deleteAlbumpicture));

        //软删除后修复weights
        membersAlbumpictureRepository.updateUserIdWeights(statusList,userId,deleteAlbumpicture.getWeights());

        picturesVo.setId(deleteAlbumpicture.getId());
        picturesVo.setStatus(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode());
        picturesVo.setUrl(deleteAlbumpicture.getPicturePath());
        logger.info("cmd=deletePicture result | userId={} | picturesVo={}", userId,JSON.toJSON(picturesVo));
        return picturesVo;
    }

    /**
     * 添加照片
     * @param userId
     * @param url
     * @return
     */
    @Override
    public PicturesVo addPicture(Integer userId, String url) {

        //判断URL合法性
        String regEx = "^http[s]?://.*?\\.kaiheikeji\\.com";
        boolean rs = Pattern.compile(regEx).matcher(url).matches();
        if(!rs){
            logger.error("cmd=addPicture param | userId={} | url={} | msg={}", userId,url,JSON.toJSON(BizExceptionEnum.PICTURE_URL_UNLAWFUL));
            throw new BusinessException(BizExceptionEnum.PICTURE_URL_UNLAWFUL);
        }

        //判断图片是否超上限
        MembersAlbumpicture albumpictureCount = new MembersAlbumpicture();
        albumpictureCount.setUserId(userId);
        List<Integer> statusList = new ArrayList<>();
        statusList.add(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode());
        statusList.add(AlbumPictureType.PICTURE_STATUS_VERIFY_SUCCESS.getCode());
        int count = membersAlbumpictureRepository.pictureCountByUserId(statusList,userId,null);
        if(count >= PICTURE_COUNT_MAX){
            logger.error("cmd=addPicture pictureCountByUserId | userId={} | url={} | count={} | PICTURE_COUNT_MAX={} | msg={}",
                    userId,url,count,PICTURE_COUNT_MAX,JSON.toJSON(BizExceptionEnum.PICTURE_OVER_MAX));
            throw new BusinessException(BizExceptionEnum.PICTURE_OVER_MAX);
        }

        return insertPicture(url,userId,count);
    }

    /**
     * 审核图片
     * @param userId
     * @param picturePath
     * @param id
     */
    @Override
    public void verifyPicture(int userId, String picturePath,int id) {
        //查找照片信息
        MembersAlbumpicture albumpicture = new MembersAlbumpicture();
        albumpicture.setId(id);
        albumpicture.setUserId(userId);
        albumpicture.setPicturePath(picturePath);
        List<MembersAlbumpicture> membersAlbumpictureList = membersAlbumpictureRepository.select(albumpicture);
        logger.info("cmd=verifyPicture select | userId={} | picturePath={} | id={} | membersAlbumpictureList={}",
                userId,picturePath,id,JSON.toJSON(membersAlbumpictureList));
        if(membersAlbumpictureList != null && membersAlbumpictureList.size() > 0){
            int pictureStatus = membersAlbumpictureList.get(0).getStatus();
            if(pictureStatus == AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode()){
                int status = AlbumPictureType.PICTURE_STATUS_VERIFY_SUCCESS.getCode();
                //鉴定图片是否涉黄
                ResponsePacket<QiniuImageCheckVo> qpulpResult =  qiniuManageServiceClient.checkQpulpImage(picturePath);
                logger.info("cmd=verifyPicture checkQpulpImage | userId={} | picturePath={} | id={} | qpulpResult={}",
                        userId,picturePath,id,JSON.toJSON(qpulpResult.getData()));
                if(qpulpResult.getData().getVerify()){
                    //鉴定图片是否涉暴
                    ResponsePacket<QiniuImageCheckVo> qterrortResult = qiniuManageServiceClient.checkQterrorImage(picturePath);
                    logger.info("cmd=verifyPicture checkQterrorImage | userId={} | picturePath={} | id={} | qterrortResult={}",
                            userId,picturePath,id,JSON.toJSON(qterrortResult.getData()));
                    status = qterrortResult.getData().getVerify() ? status : AlbumPictureType.PICTURE_STATUS_VERIFY_FAIL.getCode();
                }else{
                    status = AlbumPictureType.PICTURE_STATUS_VERIFY_FAIL.getCode();
                }

                //修改照片状态
                MembersAlbumpicture membersAlbumpicture = new MembersAlbumpicture();
                membersAlbumpicture.setId(id);
                membersAlbumpicture.setPicturePath(picturePath);
                membersAlbumpicture.setUserId(userId);
                membersAlbumpicture.setVerifyDatetime(new Date());
                membersAlbumpicture.setStatus(status);
                logger.info("cmd=verifyPicture updateByPrimaryKey | membersAlbumpicture={}", JSON.toJSON(membersAlbumpicture));
                membersAlbumpictureRepository.updateByPrimaryKey(membersAlbumpicture);
            }
        }
    }

    /**
     * 替换照片
     * @param userId
     * @param url
     * @param id
     * @return
     */
    @Transactional
    @Override
    public PicturesVo replaceAlbumpicture(Integer userId, String url, Integer id) {
        MembersAlbumpicture membersAlbumpicture = new MembersAlbumpicture();
        membersAlbumpicture.setId(id);
        List<MembersAlbumpicture> membersAlbumpictureList = membersAlbumpictureRepository.select(membersAlbumpicture);
        logger.info("cmd=replaceAlbumpicture select | userId={} | url={} | id={} | membersAlbumpictureList={}",
                userId,url,id,JSON.toJSON(membersAlbumpictureList));
        if(membersAlbumpictureList != null && membersAlbumpictureList.size() > 0){
            MembersAlbumpicture deleteAlbumpicture = membersAlbumpictureList.get(0);
            deleteAlbumpicture = deleteWeakPicture(deleteAlbumpicture);
            logger.info("cmd=replaceAlbumpicture select | userId={} | url={} | id={} | deleteAlbumpicture={}",
                    userId,url,id,JSON.toJSON(deleteAlbumpicture));
            return insertPicture(url,userId,deleteAlbumpicture.getWeights());
        }
        return null;
    }

    /**
     * 新增图片
     * @param url
     * @param userId
     * @param weights
     * @return
     */
    private PicturesVo insertPicture(String url,int userId,int weights){
        //新增图片
        PicturesVo picturesVo = new PicturesVo();
        MembersAlbumpicture membersAlbumpicture = new MembersAlbumpicture();
        membersAlbumpicture.setPicturePath(url);
        membersAlbumpicture.setUserId(userId);
        membersAlbumpicture.setStatus(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode());
        membersAlbumpicture.setWeights(weights);
        membersAlbumpicture.setCreateDatetime(new Date());
        logger.info("cmd=insertPicture insert | userId={} | url={} | weights={} | membersAlbumpicture={}",
                userId,url,weights,JSON.toJSON(membersAlbumpicture));
        membersAlbumpictureRepository.insert(membersAlbumpicture);

        //推送消息队列异步处理
        UserUrlMessageVo vo = new UserUrlMessageVo();
        vo.setUserId(userId);
        vo.setId(membersAlbumpicture.getId());
        vo.setUrl(url);
        logger.info("cmd=insertPicture sendAsync | userId={} | url={} | weights={} | vo={}",
                userId,url,weights,JSON.toJSON(vo));
        commonProducer.sendAsync(MembersAuthConstants.USER_MQ_REGIST_LOGIN_TOPIC_KEY,
                MembersAuthConstants.USER_MQ_PICTURE_VERIFY_TOPIC_TAG_KEY,"verifyPicture",vo);

        picturesVo.setId(membersAlbumpicture.getId());
        picturesVo.setStatus(AlbumPictureType.PICTURE_STATUS_VERIFYING.getCode());
        picturesVo.setUrl(membersAlbumpicture.getPicturePath());
        return picturesVo;
    }

    /**
     * 照片软删除
     * @param albumpicture
     * @return
     */
    private MembersAlbumpicture deleteWeakPicture(MembersAlbumpicture albumpicture){
        albumpicture.setStatus(AlbumPictureType.PICTURE_STATUS_DELETE.getCode());
        membersAlbumpictureRepository.updateByPrimaryKey(albumpicture);
        return albumpicture;
    }
}