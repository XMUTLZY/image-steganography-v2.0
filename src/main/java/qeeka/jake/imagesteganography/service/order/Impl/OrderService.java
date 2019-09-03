package qeeka.jake.imagesteganography.service.order.Impl;

import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.order.Order;

public interface OrderService {
    BaseResponse generateImage(Order order);
}
