package qeeka.jake.imagesteganography.web.controller.upload;

import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import java.util.UUID;

@RequestMapping("/upload")
@Controller
public class UploadController {

    private static final String ACCESS_KEY = "7ZTUd7jCIytlt2ixL1WqcQsos99Mfzr5SQMOyfvC";//访问秘钥
    private static final String SECRET_KEY = "hj5ND21XDDwKZUge0ImJ0NJp8IgWPsP3YtH0uzib";//授权秘钥
    private static final String BUCKET = "Jake_Image_store";//存储空间名称
    private static final String DOMAIN = "pwkxjnoj3.bkt.clouddn.com";//外链域名
    //图片上传,返回图片的服务器地址
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file) {
        BaseResponse response = new BaseResponse();
        String imageSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);//获取图片后缀
        //验证七牛云身份是否通过
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //生成凭证
        String token = auth.uploadToken(BUCKET);
        //生成实际路径名
        String randomImageFileName  = UUID.randomUUID().toString() + imageSuffix;
        response.setMsg(imageSuffix);
        return response;
    }
}
