package qeeka.jake.imagesteganography.service.order;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.order.Order;
import qeeka.jake.imagesteganography.repository.order.OrderRepository;
import qeeka.jake.imagesteganography.service.order.Impl.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public BaseResponse generateImage(Order order) {
        BaseResponse response = new BaseResponse();
        UserOrderEntity userOrderEntity = new UserOrderEntity();
        BeanUtils.copyProperties(order, userOrderEntity);
        orderRepository.save(userOrderEntity);
        return response;
    }
}
