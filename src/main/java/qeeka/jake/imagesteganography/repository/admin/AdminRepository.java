package qeeka.jake.imagesteganography.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import qeeka.jake.imagesteganography.domain.admin.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
    AdminEntity findByMobile(String mobile);
}
