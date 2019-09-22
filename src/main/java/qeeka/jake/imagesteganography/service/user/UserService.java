package qeeka.jake.imagesteganography.service.user;

import org.springframework.data.domain.Pageable;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.http.vo.user.User;
import java.util.List;

public interface UserService {
    User getUser(User user);
    BaseResponse saveUser(User user);
    BaseResponse getUserList(Pageable pageable);
    List<User> findUser(User user);
}
