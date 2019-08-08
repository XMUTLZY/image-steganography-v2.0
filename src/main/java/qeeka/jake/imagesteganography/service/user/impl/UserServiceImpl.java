package qeeka.jake.imagesteganography.service.user.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.user.UserEntity;
import qeeka.jake.imagesteganography.pojo.user.User;
import qeeka.jake.imagesteganography.repository.user.UserRepository;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Resource
    private UserRedisServiceImpl userRedisService;

    @Override
    public User getUser(User user) {
        if (userRepository.findByMobile(user.getMobile()) != null) {
            User user1 = new User();
            BeanUtils.copyProperties(userRepository.findByMobile(user.getMobile()), user1);
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
}
