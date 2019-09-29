package qeeka.jake.imagesteganography.web.controller.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.constants.UploadConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.service.upload.UploadService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/upload")
@Controller
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //本地路径上传
    @RequestMapping(value = "/imageUrl", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadImage(getImagePathLocal(file));
    }

    //将上传的图片保存在本地文件夹
    private String getImagePathLocal(MultipartFile file) {
        File file1 = new File(UploadConstant.LOCAL_PATH);
        if (!file1.exists() && !file1.mkdirs()) {
            //创建文件夹
        }
        String imageName = file.getOriginalFilename();
        String imageSuffix = imageName.substring(imageName.lastIndexOf(".") + 1);
        String resultImageName = UUID.randomUUID().toString() + "." + imageSuffix;
        File uploadFile = new File(UploadConstant.LOCAL_PATH + "/" + resultImageName);
        try {
            // 将上传的图片二进制流保存为文件
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
            return UploadConstant.LOCAL_PATH + "/" + resultImageName;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
