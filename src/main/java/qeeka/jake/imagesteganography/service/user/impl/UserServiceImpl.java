package qeeka.jake.imagesteganography.service.user.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.user.UserEntity;
import qeeka.jake.imagesteganography.pojo.user.User;
import qeeka.jake.imagesteganography.repository.user.UserRepository;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Resource
    private UserRedisServiceImpl userRedisService;

    @Override
    public User getUser(User user) {
        UserEntity userEntity = userRepository.findByMobile(user.getMobile());
        if (userEntity != null) {
            User user1 = new User();
            BeanUtils.copyProperties(userEntity, user1);
            return user1;
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(user, entity);
        userRepository.save(entity);
        //userRedisService.put(user.getRedisKey(), entity, -1);
    }

    @Override
    public List<User> getUserList() {
        return convertToUserList(userRepository.findAll());
    }

    private List<User> convertToUserList(List<UserEntity> userEntityList) {
        if (userEntityList == null || userEntityList.isEmpty())
            return null;
        List<User> list = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            list.add(user);
        }
        return list;
    }
}
