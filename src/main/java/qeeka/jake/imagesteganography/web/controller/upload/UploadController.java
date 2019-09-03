package qeeka.jake.imagesteganography.web.controller.upload;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/upload")
@Controller
public class UploadController {

    private static final String ACCESS_KEY = "7ZTUd7jCIytlt2ixL1WqcQsos99Mfzr5SQMOyfvC";//访问秘钥
    private static final String SECRET_KEY = "hj5ND21XDDwKZUge0ImJ0NJp8IgWPsP3YtH0uzib";//授权秘钥
    private static final String BUCKET = "image";//存储空间名称
    private static final String DOMAIN_URL = "pwm7p4mff.bkt.clouddn.com";//外链域名 普通上传
    private static final String IMAGE_STYLE = "imageView2/2/w/512/h/512/interlace/0/q/100|watermark/2/" +
            "text/5Zu-5YOP6ZqQ5YaZ5Zyo57q_5pyN5Yqh5bmz5Y-wLUpha2Xmnpc=/" +
            "font/5a6L5L2T/fontsize/240/fill/I0VGRUZFRg==" +
            "/dissolve/92/gravity/SouthEast/dx/10/dy/10";
    //本地路径上传
    @RequestMapping(value = "/imageUrl", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse uploadImage(@RequestParam("file") MultipartFile file) {
        BaseResponse response = new BaseResponse();
        //构造一个带指定Zone对象的构造器
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //获取本地图片路径
        String imagePath = getImagePathLocal(file);
        //获取图片后缀
        String imageSuffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);//获取图片后缀
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString() + "." + imageSuffix;
        //验证七牛云身份是否通过
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        //生成凭证
        String token = auth.uploadToken(BUCKET);
        //返回图片服务器地址
        String imageUrl = null;
        try {
            Response response1 = uploadManager.put(imagePath, key, token);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response1.bodyString(), DefaultPutRet.class);
            imageUrl = "http://" + DOMAIN_URL + "/" + key;
            response.setMsg(imageUrl);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getImagePathLocal(MultipartFile file) {
        String localPath = "F:\\virtual-image-file-folder";//本地文件夹
        File file1 = new File(localPath);
        if (!file1.exists() && !file1.mkdirs()) {
            //创建文件夹
        }
        String imageName = file.getOriginalFilename();
        String imageSuffix = imageName.substring(imageName.lastIndexOf(".") + 1);
        String resultImageName = UUID.randomUUID().toString() + "." + imageSuffix;
        File uploadFile = new File(localPath + "/" + resultImageName);
        try {
            // 将上传的图片二进制流保存为文件
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(uploadFile));
            return localPath + "/" + resultImageName;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
