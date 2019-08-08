package qeeka.jake.imagesteganography.service.user;

import qeeka.jake.imagesteganography.pojo.user.User;

public interface UserService {
    User getUser(User user);
    void saveUser(User user);
}
