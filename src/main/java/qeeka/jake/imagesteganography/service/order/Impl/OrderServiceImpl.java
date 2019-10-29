package qeeka.jake.imagesteganography.service.order.Impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.mathworks.toolbox.javabuilder.MWException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.constants.AlipayConstant;
import qeeka.jake.imagesteganography.constants.OrderConstant;
import qeeka.jake.imagesteganography.constants.OssConstant;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.domain.user.UserEntity;
import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.response.ImageResultResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import qeeka.jake.imagesteganography.repository.order.OrderRepository;
import qeeka.jake.imagesteganography.service.order.OrderService;
//import stegangraphy.embeddingInfo;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

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
    public String payment(OrderDetailsRequest request, HttpServletRequest httpServletRequest) {
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
        httpServletRequest.getSession().setAttribute("payIndex", result);
        return result;
    }

    @Override
    public String payResult(HttpServletRequest request) {
        String trade_no = null;
        String total_amount = null;
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.put(name, valueStr);
        }
        boolean signVerified = false;//验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConstant.APLPAY_PUBLIC_KEY,
                    AlipayConstant.CHARSET, AlipayConstant.SIGN_TYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (signVerified) {
            try {
                trade_no = new String(request.getParameter("out_trade_no").
                        getBytes("ISO-8859-1"), "UTF-8");
                total_amount = new String(request.getParameter("total_amount").
                        getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            UserOrderEntity userOrderEntity = orderRepository.findFirstByOrderByOrderTimeDesc();
            userOrderEntity.setOrderNumber(trade_no);
            userOrderEntity.setPaymentAmount(total_amount);
            userOrderEntity.setPaymentStatus(OrderConstant.PAYMENT_STATUS_YES);
            request.getSession().setAttribute("order", userOrderEntity);
            orderRepository.save(userOrderEntity);
        }
        return "/userView/index";
    }

    @Override
    public BaseResponse isDownloadImage(User user) {
        List<UserOrderEntity> list = orderRepository.noDownloadOrder(user.getId(), OrderConstant.PAYMENT_STATUS_YES,
                OrderConstant.ORDER_STATUS_EXIT, OrderConstant.DOWNLOAD_NO);
        BaseResponse response = new BaseResponse();
        response.setData(list);
        return response;
    }

    @Override
    public void updateDownloadStatus(User user) {
        List<UserOrderEntity> list = orderRepository.noDownloadOrder(user.getId(), OrderConstant.PAYMENT_STATUS_YES,
                OrderConstant.ORDER_STATUS_EXIT, OrderConstant.DOWNLOAD_NO);
        for (UserOrderEntity userOrderEntity : list) {
            userOrderEntity.setDownloadStatus(OrderConstant.DOWNLOAD_YES);
            orderRepository.save(userOrderEntity);
        }
    }

    @Override
    public BaseResponse getPersonalOrders(Order order) {
        List<UserOrderEntity> userOrderEntityList;
        if (order.getOrderStatus() == null) {
            userOrderEntityList = orderRepository.findAllByUserId(order.getUserId());
        } else {
            userOrderEntityList = orderRepository.getPersonalOrders(order.getUserId(), order.getOrderStatus());
        }
        List<Order> orderList = new ArrayList<>();
        for (UserOrderEntity userOrderEntity : userOrderEntityList) {
            Order order1 = new Order();
            BeanUtils.copyProperties(userOrderEntity, order1);
            if (order1.getDownloadStatus() == OrderConstant.PAYMENT_STATUS_YES) {
                order1.setDownloadStatusString("已下载");
            } else {
                order1.setDownloadStatusString("未下载");
            }
            if (order1.getPaymentStatus() == OrderConstant.PAYMENT_STATUS_YES) {
                order1.setPaymentStatusString("已付款");
            } else {
                order1.setPaymentStatusString("待支付");
            }
            orderList.add(order1);
        }
        BaseResponse response = new BaseResponse();
        response.setData(orderList);
        return response;
    }


//    private void runMatlab(Order order) {
//        embeddingInfo embeddingInfo = null;
//        try {
//            embeddingInfo = new embeddingInfo();
//        } catch (MWException e) {
//            e.printStackTrace();
//        }
//        try {
//            embeddingInfo.start(order.getOrginalImage(), converToUtf(order.getHiddenData()));
//        } catch (MWException e) {
//            e.printStackTrace();
//        }
//    }

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
         userOrderEntity.setResultImage1("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aOne.bmp");
         userOrderEntity.setResultImage2("https://image-steganography.oss-cn-hangzhou.aliyuncs.com/resultImage/2c676a93-6e1f-44f0-973a-f9227a88b49aTwo.bmp");
         response.setMap(map);
    }
}
