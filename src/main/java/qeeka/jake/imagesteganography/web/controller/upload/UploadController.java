package qeeka.jake.imagesteganography.web.controller.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.service.upload.UploadService;
@RequestMapping("/upload")
@Controller
public class UploadController {
    @Autowired
    private UploadService uploadService;

    //OSS上传图片
    @RequestMapping(value = "/imageUrlOss", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImageOss(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadImageOss(file);
    }

}
