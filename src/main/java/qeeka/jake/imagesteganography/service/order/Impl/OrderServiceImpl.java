package qeeka.jake.imagesteganography.service.order.Impl;

import ImageSteganographyPack.EmbeddingInfo;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.mathworks.toolbox.javabuilder.MWException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.constants.OssConstant;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.response.ImageResultResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.repository.order.OrderRepository;
import qeeka.jake.imagesteganography.service.order.OrderService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public BaseResponse generateImage(Order order) {
        ImageResultResponse response = new ImageResultResponse();
        String imageUrl = order.getOrginalImage();
//        runMatlab(order);
        UserOrderEntity userOrderEntity = new UserOrderEntity();
        BeanUtils.copyProperties(order, userOrderEntity);
        userOrderEntity.setOrderTime(new Date());
        setResultImagesToResponse(response, imageUrl.substring(imageUrl.lastIndexOf("/") + 1), userOrderEntity);
        orderRepository.save(userOrderEntity);
        return response;
    }

    private void runMatlab(Order order) {
        EmbeddingInfo embeddingInfo = null;
        try {
            embeddingInfo = new EmbeddingInfo();
        } catch (MWException e) {
            e.printStackTrace();
        }
        try {
            embeddingInfo.start(order.getOrginalImage(), converToUtf(order.getHiddenData()));
        } catch (MWException e) {
            e.printStackTrace();
        }
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

    private void setResultImagesToResponse(ImageResultResponse response, String imageName, UserOrderEntity userOrderEntity) {
        Map<String, String> map = new HashMap<>();
//        String localImagePath = "C:\\resultImage\\";//该路径存放matlab算法生成的2张结果图  命名("test1.bmp" or "test2.bmp")
//        String resultImageName1 = imageName.split("\\.")[0] + "One." + imageName.split("\\.")[1];
//        String resultImageName2 = imageName.split("\\.")[0] + "Two." + imageName.split("\\.")[1];
//        InputStream inputStream1 = null;
//        InputStream inputStream2 = null;
//        OSSClient ossClient = new OSSClient(OssConstant.ENDPOINT, OssConstant.ACCESS_KEY, OssConstant.ACCESS_SECRET);
//        try {
//            inputStream1 = new FileInputStream(localImagePath + resultImageName1);
//            inputStream2 = new FileInputStream(localImagePath + resultImageName2);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
////        }
////        ObjectMetadata metadata = new ObjectMetadata();
////        metadata.setCacheControl("no-cache");
////        metadata.setHeader("Pragma", "no-cache");
////        metadata.setContentEncoding("utf-8");
////        metadata.setContentType(imageName.split("\\.")[1]);
////        metadata.setContentDisposition("filename/filesize=" + imageName + "/512" + "Byte.");
////        ossClient.putObject(OssConstant.BUCKET_NAME, OssConstant.RESULT_IMAGE_FOLDER + resultImageName1, inputStream1, metadata);
////        ossClient.putObject(OssConstant.BUCKET_NAME, OssConstant.RESULT_IMAGE_FOLDER + resultImageName2, inputStream2, metadata);
////        String resultImageOne = "http://" + OssConstant.BUCKET_NAME + "." + OssConstant.ENDPOINT + "/" + OssConstant.RESULT_IMAGE_FOLDER + resultImageName1;
////        String resultImageTwo = "http://" + OssConstant.BUCKET_NAME + "." + OssConstant.ENDPOINT + "/" + OssConstant.RESULT_IMAGE_FOLDER + resultImageName2;
////        map.put("97.00", resultImageOne);
////        map.put("98.00", resultImageTwo);
////        userOrderEntity.setResultImage1(resultImageOne);
////        userOrderEntity.setResultImage2(resultImageTwo);
        map.put("resultImageOne", "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aOne.bmp");
        map.put("resultImageTwo", "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aTwo.bmp");
        userOrderEntity.setResultImage1("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aOne.bmp");
        userOrderEntity.setResultImage2("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aTwo.bmp");
        response.setMap(map);
    }
}
