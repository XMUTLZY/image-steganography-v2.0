package qeeka.jake.imagesteganography.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import qeeka.jake.imagesteganography.domain.order.UserOrderEntity;

public interface OrderRepository extends JpaRepository<UserOrderEntity, Integer> {
}
