package qeeka.jake.imagesteganography.repository.admin;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import qeeka.jake.imagesteganography.domain.admin.AdminOperateEs;
import java.util.List;

public interface AdminOperateEsRepository extends ElasticsearchRepository<AdminOperateEs, Integer> {
    List<AdminOperateEs> findByOperateTimeLikeOrOperateLikeOrIpLike(String operateTime, String operate, String ip);
}
