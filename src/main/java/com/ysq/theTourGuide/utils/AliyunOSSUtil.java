package com.ysq.theTourGuide.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.ysq.theTourGuide.config.constants.AliyunOSSConfigConstant;
import com.ysq.theTourGuide.dto.DownloadFileResult;
import com.ysq.theTourGuide.dto.UploadFileResult;
import com.ysq.theTourGuide.entity.ScenicSpot;
import com.ysq.theTourGuide.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class AliyunOSSUtil {

    @Autowired
    ScenicSpotService scenicSpotService;

    public static AliyunOSSUtil aliyunOSSUtil;

    private static String FILE_URL;
    private static String bucketName = AliyunOSSConfigConstant.BUCKE_NAME;
    private static String endpoint = AliyunOSSConfigConstant.END_POINT;
    private String accessKeyId;
    private String accessKeySecret;

    @PostConstruct
    public void init(){
        aliyunOSSUtil = this;
        aliyunOSSUtil.scenicSpotService = this.scenicSpotService;
    }


    /**
     * 上传文件。
     *
     * @param file 需要上传的文件路径
     * @return 如果上传的文件是图片的话，会返回图片的"URL"，如果非图片的话会返回"非图片，不可预览。文件路径为：+文件路径"
     */
    public UploadFileResult upLoad(File file, String fileHost) {

        UploadFileResult uploadFileResult = new UploadFileResult();

        // 默认值为：true
//        boolean isImage = true;
        // 判断所要上传的图片是否是图片，图片可以预览，其他文件不提供通过URL预览
//        try {
//            Image image = ImageIO.read( file);
//            isImage = image == null ? false : true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        // 判断文件
        if (file == null) {
            return null;
        }
        // 创建OSSClient实例。
        try {
            ScenicSpot s = scenicSpotService.get(1);
            this.accessKeyId = s.getScenicId();
            this.accessKeySecret = s.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
//            if (isImage) {//如果是图片，则图片的URL为：....
                FILE_URL = "https://" + bucketName + "." + endpoint + "/" + fileUrl;
//            } else {
//                FILE_URL = "非图片，不可预览。文件路径为：" + fileUrl;
//            }

            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl,  file));
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                uploadFileResult.setCode("200");
                uploadFileResult.setObjectName(fileUrl);
                uploadFileResult.setHttpUrl(FILE_URL);
            }
        } catch (OSSException oe) {
        } catch (ClientException ce) {
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return uploadFileResult;
    }


    /**
     * 通过文件名下载文件
     *
     * @param objectName    要下载的文件名
     * @param localFileName 本地要创建的文件名
     */
    public DownloadFileResult downloadFile(String objectName, String localFileName) {

        DownloadFileResult downloadFileResult = new DownloadFileResult();
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ObjectMetadata object = ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFileName));
        if(object!=null){
            downloadFileResult.setCode("200");
            downloadFileResult.setObjectName(localFileName);
        }
        // 关闭OSSClient。
        ossClient.shutdown();
        return downloadFileResult;


    }

    /**
     * 列举 test 文件下所有的文件
     */
    public void listFile() {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // 设置prefix参数来获取fun目录下的所有文件。
        listObjectsRequest.setPrefix("test/");
        // 列出文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }
        // 遍历所有commonPrefix。
        System.out.println("CommonPrefixes:");
        for (String commonPrefix : listing.getCommonPrefixes()) {
            System.out.println(commonPrefix);
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}

