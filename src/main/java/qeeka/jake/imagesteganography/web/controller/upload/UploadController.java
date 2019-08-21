package qeeka.jake.imagesteganography.web.controller.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.http.response.BaseResponse;

@RequestMapping("/upload")
@Controller
public class UploadController {
    //图片上传,返回图片的服务器地址
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        BaseResponse response = new BaseResponse();
        response.setMsg(name);
        return response;
    }
}
