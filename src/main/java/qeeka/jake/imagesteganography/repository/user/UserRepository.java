package qeeka.jake.imagesteganography.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import qeeka.jake.imagesteganography.domain.user.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByMobile(String mobile);
}
