package qeeka.jake.imagesteganography.service.user;

import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.user.User;
import java.util.List;

public interface UserService {
    User getUser(User user);
    BaseResponse saveUser(User user);
    List<User> getUserList();
    List<User> findUser(User user);
}
