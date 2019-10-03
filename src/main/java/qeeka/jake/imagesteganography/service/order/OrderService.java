package qeeka.jake.imagesteganography.service.order;

import qeeka.jake.imagesteganography.http.request.OrderDetailsRequest;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.order.Order;
import qeeka.jake.imagesteganography.http.vo.user.User;
import javax.servlet.http.HttpServletRequest;

public interface OrderService {
    BaseResponse generateImage(Order order);
    String payment(OrderDetailsRequest request, HttpServletRequest httpServletRequest);
    String  payResult(HttpServletRequest request);
    BaseResponse isDownloadImage(User user);
    void updateDownloadStatus(User user);
}
