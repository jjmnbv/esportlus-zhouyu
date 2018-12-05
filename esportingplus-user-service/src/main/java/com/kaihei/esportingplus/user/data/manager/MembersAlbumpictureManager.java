package com.kaihei.esportingplus.user.data.manager;

import com.kaihei.esportingplus.user.api.vo.PicturesVo;

/**
 * @Auther: linruihe
 * @Date: 2018-10-12 17:41
 * @Description:
 */
public interface MembersAlbumpictureManager {

    /**
     * 修改照片
     * @param userId
     * @param url
     * @param id
     * @return
     */
    public PicturesVo changePicture(Integer userId, String url,Integer id);

    /**
     * 替换照片
     * @param userId
     * @param url
     * @param id
     */
    public PicturesVo replaceAlbumpicture(Integer userId, String url, Integer id);

    /**
     * 删除图片
     * @param userId
     * @param id
     * @return
     */
    public PicturesVo deletePicture(Integer userId, Integer id);

    /**
     * 添加图片
     * @param userId
     * @param url
     * @return
     */
    public PicturesVo addPicture(Integer userId, String url);

    /**
     * 审核照片
     * @param userId
     * @param picturePath
     */
    void verifyPicture(int userId, String picturePath,int id);
}
