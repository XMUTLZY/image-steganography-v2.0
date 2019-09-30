package qeeka.jake.imagesteganography.service.order.Impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.mathworks.toolbox.javabuilder.MWException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.constants.AlipayConstant;
import qeeka.jake.imagesteganography.constants.OssConstant;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.response.ImageResultResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.repository.order.OrderRepository;
import qeeka.jake.imagesteganography.service.order.OrderService;
import stegangraphy.embeddingInfo;
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

    @Override
    public String payment(OrderDetailsRequest request) {
        String result = null;
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConstant.GATEWAR_URL, AlipayConstant.APP_ID, AlipayConstant.MECHART_PRIVATE_KEY,
                "json", AlipayConstant.CHARSET, AlipayConstant.APLPAY_PUBLIC_KEY, AlipayConstant.SIGN_TYPE);
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        alipayTradePagePayRequest.setReturnUrl(AlipayConstant.RETURN_URL);
        alipayTradePagePayRequest.setNotifyUrl(AlipayConstant.NOTIFY_URL);
        alipayTradePagePayRequest.setBizContent("{\"out_trade_no\":\""+ request.getOut_trade_no() +"\","
                + "\"total_amount\":\""+ request.getTotal_amount() +"\","
                + "\"subject\":\""+ request.getSubject() +"\","
                + "\"body\":\""+ request.getBody() +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        try {
            result = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void runMatlab(Order order) {
        embeddingInfo embeddingInfo = null;
        try {
            embeddingInfo = new embeddingInfo();
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
//        }
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setCacheControl("no-cache");
//        metadata.setHeader("Pragma", "no-cache");
//        metadata.setContentEncoding("utf-8");
//        metadata.setContentType(imageName.split("\\.")[1]);
//        metadata.setContentDisposition("filename/filesize=" + imageName + "/512" + "Byte.");
//        ossClient.putObject(OssConstant.BUCKET_NAME, OssConstant.RESULT_IMAGE_FOLDER + resultImageName1, inputStream1, metadata);
//        ossClient.putObject(OssConstant.BUCKET_NAME, OssConstant.RESULT_IMAGE_FOLDER + resultImageName2, inputStream2, metadata);
//        String resultImageOne = "https://" + OssConstant.BUCKET_NAME + "." + OssConstant.ENDPOINT + "/" + OssConstant.RESULT_IMAGE_FOLDER + resultImageName1;
//        String resultImageTwo = "https://" + OssConstant.BUCKET_NAME + "." + OssConstant.ENDPOINT + "/" + OssConstant.RESULT_IMAGE_FOLDER + resultImageName2;
//        map.put("resultImageOne", resultImageOne);
//        map.put("resultImageTwo", resultImageTwo);
//        userOrderEntity.setResultImage1(resultImageOne);
//        userOrderEntity.setResultImage2(resultImageTwo);
         map.put("resultImageOne", "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aOne.bmp?" + OssConstant.RESULT_IMAGE_STYLE2);
         map.put("resultImageTwo", "https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aTwo.bmp?" + OssConstant.RESULT_IMAGE_STYLE2);
         userOrderEntity.setResultImage1("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aOne.bmp?" + OssConstant.RESULT_IMAGE_STYLE2);
         userOrderEntity.setResultImage2("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aTwo.bmp?" + OssConstant.RESULT_IMAGE_STYLE2);
         response.setMap(map);
    }
}
