package qeeka.jake.imagesteganography.service.upload.Impl;

import com.aliyun.oss.OSSClient;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qeeka.jake.imagesteganography.constants.OssConstant;
import qeeka.jake.imagesteganography.constants.UploadConstant;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.service.upload.UploadService;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    @Override
    public BaseResponse uploadImage(String imagePath) {
        //获取图片后缀
        String imageSuffix = imagePath.substring(imagePath.lastIndexOf(".") + 1);//获取图片后缀
        BaseResponse response = new BaseResponse();
        //构造一个带指定Zone对象的构造器
        Configuration cfg = new Configuration(Zone.zone2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //获取本地图片路径
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID().toString() + "." + imageSuffix;
        //验证七牛云身份是否通过
        Auth auth = Auth.create(UploadConstant.ACCESS_KEY, UploadConstant.SECRET_KEY);
        //生成凭证
        String token = auth.uploadToken(UploadConstant.BUCKET);
        //返回图片服务器地址
        String imageUrl = null;
        try {
            Response response1 = uploadManager.put(imagePath, key, token);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response1.bodyString(), DefaultPutRet.class);
            imageUrl = "http://" + UploadConstant.DOMAIN_URL + "/" + key;
            response.setMsg(imageUrl);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public BaseResponse uploadImageOss(MultipartFile file) {
        OSSClient ossClient = new OSSClient(OssConstant.ENDPOINT, OssConstant.ACCESS_KEY, OssConstant.ACCESS_SECRET);
        return null;
    }

}
