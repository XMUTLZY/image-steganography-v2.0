package qeeka.jake.imagesteganography.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import qeeka.jake.imagesteganography.domain.admin.AdminRoleEntity;

public interface AdminRoleRepository extends JpaRepository<AdminRoleEntity, Integer> {
    AdminRoleEntity findAllById(Integer id);
}
