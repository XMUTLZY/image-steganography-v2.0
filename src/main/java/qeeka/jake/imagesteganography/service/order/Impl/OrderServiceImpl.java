package qeeka.jake.imagesteganography.service.order.Impl;

//import ImageSteganographyPack.EmbeddingInfo;
//import com.mathworks.toolbox.javabuilder.MWException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import qeeka.jake.imagesteganography.constants.UploadConstant;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.repository.order.OrderRepository;
import qeeka.jake.imagesteganography.service.order.OrderService;
import qeeka.jake.imagesteganography.service.upload.UploadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UploadService uploadService;

    @Override
    public BaseResponse generateImage(Order order) {
//        EmbeddingInfo embeddingInfo = null;
//        try {
//            embeddingInfo = new EmbeddingInfo();
//        } catch (MWException e) {
//            e.printStackTrace();
//        }
//        try {
//            embeddingInfo.start(order.getOrginalImage(), converToUtf(order.getHiddenData()));
//        } catch (MWException e) {
//            e.printStackTrace();
//        }
        String testUrl = "http://pwm7p4mff.bkt.clouddn.com/6c924d35-761c-4b2c-b45c-11229da73af4.jpg";
        String imageName = testUrl.split("/|\\.")[6];
        BaseResponse response = new BaseResponse();
        UserOrderEntity userOrderEntity = new UserOrderEntity();
        BeanUtils.copyProperties(order, userOrderEntity);
        uploadToServer(imageName, userOrderEntity);//将生成的图片上传到服务器
        orderRepository.save(userOrderEntity);
        return response;
    }

    private int[] converToUtf(String inputInfo) {
        //将中文字符转换成bit（中文使用）
        byte[] b = null;
        try {
            b = inputInfo.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("转换前:"+inputInfo);
        System.out.print("转换后:");
        //定义一个一维数组存放转换后的数据
        int[] bit = new int[b.length*8];
        int t = 0;
        for (int i = 0;i < b.length;i++){
            System.out.print(Integer.toBinaryString(b[i] & 0xff));
            for(int j = 0;j < 8;j++,t++){
                if(j%8==0)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,7);
                if(j%8==1)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,6)%10;
                if(j%8==2)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,5)%10;
                if(j%8==3)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,4)%10;
                if(j%8==4)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,3)%10;
                if(j%8==5)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,2)%10;
                if(j%8==6)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))/(int)Math.pow(10,1)%10;
                if(j%8==7)
                    bit[t] = (Integer.parseInt(Integer.toBinaryString(b[i] & 0xff)))%10;
            }
        }
        return bit;
    }

    private void uploadToServer(String imageName, UserOrderEntity userOrderEntity) {
        File file1 = new File(UploadConstant.LOCAL_PATH);
        if (!file1.exists() && !file1.mkdirs()) {
            //创建文件夹
        }
        String resultImageName1 = UploadConstant.LOCAL_PATH + "/" + imageName + "1.bmp";
        String resultImageName2 = UploadConstant.LOCAL_PATH + "/" + imageName + "2.bmp";
        BaseResponse response1 = uploadService.uploadImage(resultImageName1);
        BaseResponse response2 = uploadService.uploadImage(resultImageName2);
        userOrderEntity.setResultImage1(response1.getMsg());
        userOrderEntity.setResultImage2(response2.getMsg());
    }
}
