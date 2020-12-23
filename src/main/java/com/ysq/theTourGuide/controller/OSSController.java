package com.ysq.theTourGuide.controller;

import com.ysq.theTourGuide.dto.DownloadFileResult;
import com.ysq.theTourGuide.dto.UploadFileResult;
import com.ysq.theTourGuide.utils.AliyunOSSUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/oss")
public class OSSController {
    @Autowired
    AliyunOSSUtil aliyunOSSUtil;

    /**
     * 文件上传（供前端调用）
     */
    @PostMapping(value = "/uploadFiles")
    @ApiOperation(value="上传文件接口")
    public UploadFileResult uploadFiles(@RequestParam("files") MultipartFile[] file, String filehost) {
        UploadFileResult uploadUrl = null;
        List<String> filenameList = new ArrayList<>();
        for(MultipartFile f:file){
            filenameList.add(f.getOriginalFilename());
        }
//        String[] filename = file.getOriginalFilename();
        try {

            if (file != null) {
                int i = 0;
                for(String filename:filenameList){
                    if (!"".equals(filename.trim())) {
                        File newFile = new File(filename);
                        FileOutputStream os = new FileOutputStream(newFile);
                        os.write(file[i].getBytes());
                        os.close();
                        file[i].transferTo(newFile);
                        // 上传到OSS
                        uploadUrl = aliyunOSSUtil.upLoad(newFile,filehost);
                    }
                    i++;
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uploadUrl;
    }

    @PostMapping(value = "/uploadFile")
    @ApiOperation(value="上传文件接口")
    public UploadFileResult uploadFile(@RequestParam("file") MultipartFile file, String filehost) {
        UploadFileResult uploadUrl = null;
        String filename = file.getOriginalFilename();
        try {

            if (file != null) {
                    if (!"".equals(filename.trim())) {
                        File newFile = new File(filename);
                        FileOutputStream os = new FileOutputStream(newFile);
                        os.write(file.getBytes());
                        os.close();
                        file.transferTo(newFile);
                        // 上传到OSS
                        uploadUrl = aliyunOSSUtil.upLoad(newFile,filehost);
                    }
                }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uploadUrl;
    }

    /**
     * 文件下载
     */
    @GetMapping("/downloadFile")
    @ApiOperation(value="下载文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="objectName",dataType = "String",required = true,value="下载文件名"),
            @ApiImplicitParam(paramType = "query",name="fileName",dataType = "String",required = true,value="下载文件地址")
    })
    public DownloadFileResult downloadFile(String objectName, String fileName){

        DownloadFileResult downloadFileResult = aliyunOSSUtil.downloadFile(objectName, fileName);

        return downloadFileResult;

    }
}
