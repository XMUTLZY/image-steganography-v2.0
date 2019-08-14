package qeeka.jake.imagesteganography.service.user;

import qeeka.jake.imagesteganography.pojo.user.User;
import java.util.List;

public interface UserService {
    User getUser(User user);
    void saveUser(User user);
    List<User> getUserList();
}
