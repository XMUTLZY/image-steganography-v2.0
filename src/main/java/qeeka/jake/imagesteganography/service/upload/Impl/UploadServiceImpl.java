package qeeka.jake.imagesteganography.service.upload.Impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.constants.OssConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.service.upload.UploadService;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public BaseResponse uploadImageOss(MultipartFile file) {
        BaseResponse response = new BaseResponse();
        OSSClient ossClient = new OSSClient(OssConstant.ENDPOINT, OssConstant.ACCESS_KEY, OssConstant.ACCESS_SECRET);
        String resultImageUrl = null;
        String fileName = file.getOriginalFilename();
        String imageName = UUID.randomUUID().toString() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
        Long fileSize = file.getSize();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        metadata.setCacheControl("no-cache");
        metadata.setHeader("Pragma", "no-cache");
        metadata.setContentEncoding("utf-8");
        metadata.setContentType(getContentType(imageName));
        metadata.setContentDisposition("filename/filesize=" + imageName + "/" + fileSize + "Byte.");
        //上传文件
        try {
            ossClient.putObject(OssConstant.BUCKET_NAME, OssConstant.IMAGE_FOLDER + imageName, file.getInputStream(), metadata);
            resultImageUrl = "http://" + OssConstant.BUCKET_NAME + "." + OssConstant.ENDPOINT + "/" + OssConstant.IMAGE_FOLDER + imageName;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        response.setMsg("http://image-steganography.oss-cn-hangzhou.aliyuncs.com/image/2c676a93-6e1f-44f0-973a-f9227a88b49a.bmp");//测试图片
        response.setMsg(resultImageUrl);
        return response;
    }

    private String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension) || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        return null;
    }
}
