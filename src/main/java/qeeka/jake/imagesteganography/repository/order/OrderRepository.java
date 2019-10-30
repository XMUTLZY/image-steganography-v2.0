package qeeka.jake.imagesteganography.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;
import java.util.List;

public interface OrderRepository extends JpaRepository<UserOrderEntity, Integer> {
    UserOrderEntity findFirstByOrderByOrderTimeDesc();

    @Query(value = "select * from user_order where user_id = :userId and payment_status = :paymentStatus " +
            "and order_status = :orderStatus and download_status = :downloadStatus", nativeQuery = true)
    List<UserOrderEntity> noDownloadOrder(@Param("userId") Integer userId, @Param("paymentStatus") Integer paymentStatus,
                                          @Param("orderStatus") Integer orderStatus, @Param("downloadStatus") Integer downloadStatus);

    Page<UserOrderEntity> findAllByUserIdAndOrderStatus(Integer userId, Integer orderStatus, Pageable pageable);
    Page<UserOrderEntity> findAllByUserId(Integer userId, Pageable pageable);
}
