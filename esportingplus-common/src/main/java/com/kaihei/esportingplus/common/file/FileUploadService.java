package com.kaihei.esportingplus.common.file;


import com.kaihei.esportingplus.common.exception.BusinessException;

import java.io.File;
import java.io.InputStream;

/**
 * 文件上传接口，不同上传渠道分别实现
 * @author zhangfang
 * @date 2018/7/28
 */
public interface FileUploadService {
    /**
     *
     * @param stream 文件输入流
     * @param fileName 文件名称
     * @return 上传到服务器的文件名
     * @throws BusinessException
     */
    String upload(InputStream stream, String fileName) throws BusinessException;
    /**
     *
     * @param stream 文件输入流
     * @param fileName 文件名称
     * @param uploadFloder 上传的文件夹
     * @param forceRefresh 是否强制刷新
     * @return 上传到服务器的文件名
     * @throws BusinessException
     */
    String upload(InputStream stream,String uploadFloder,String fileName,boolean forceRefresh)throws BusinessException;

    /**
     *
     * @param stream 文件输入流
     * @param fileName 文件名称
     * @param env 上传的环境
     * @param uploadFloder 上传的文件夹
     * @param forceRefresh 是否强制刷新
     * @return 上传到服务器的文件名
     * @throws BusinessException
     */
    String upload(InputStream stream,String env,String uploadFloder,String fileName,boolean forceRefresh)throws BusinessException;
    /**
     *
     * @param file 本地文件对象
     * @param fileName 文件名称
     * @return 上传到服务器的文件名
     * @throws BusinessException
     */
    String upload(File file, String fileName) throws BusinessException;

    /**
     *
     * @param localFileName  本地文件地址
     * @param fileName 文件名称
     * @return 上传到服务器的文件名
     * @throws BusinessException
     */
    String upload(String localFileName, String fileName) throws BusinessException;

    /**
     *
     * @param serverfileName 服务器文件名称
     * @return 改文件对外链接
     * @throws BusinessException
     */
    String getUploadFileLink(String serverfileName) throws BusinessException;


    /**
     * 获取七牛token
     * @param s token类型
     * @return
     * @throws BusinessException
     */
    String getUploadToken(String s, String fileName) throws BusinessException;


    /**
     * 获取七牛凭证(覆盖上传)
     * @param bucketName
     * @param fileName
     * @return
     * @throws BusinessException
     */
    String getUploadTokenToCover(String bucketName, String fileName) throws BusinessException;
}
