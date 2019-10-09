package qeeka.jake.imagesteganography.repository.admin;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import qeeka.jake.imagesteganography.domain.admin.AdminOperateEs;

public interface AdminOperateEsRepository extends ElasticsearchRepository<AdminOperateEs, Integer> {
}
