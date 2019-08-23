package qeeka.jake.imagesteganography.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qeeka.jake.imagesteganography.domain.admin.AdminPrivilegeEntity;

import java.util.List;

public interface AdminPrivilegeRepository extends JpaRepository<AdminPrivilegeEntity,Integer> {
    @Query(value = "select * from admin_privilege where id in(:list)", nativeQuery = true)
    List<AdminPrivilegeEntity> getPrivilegeList(@Param("list") List<Integer> list);
}
