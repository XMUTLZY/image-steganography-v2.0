package qeeka.jake.imagesteganography.service.user.impl;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import qeeka.jake.imagesteganography.constants.UserConstant;
import qeeka.jake.imagesteganography.domain.user.UserEntity;
import qeeka.jake.imagesteganography.http.response.BaseResponse;
import qeeka.jake.imagesteganography.pojo.user.User;
import qeeka.jake.imagesteganography.repository.user.UserRepository;
import qeeka.jake.imagesteganography.service.user.UserService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
    public BaseResponse saveUser(User user) {
        BaseResponse response = new BaseResponse();
        UserEntity entity = new UserEntity();
        BeanUtils.copyProperties(user, entity);
        String encrypt = new SecureRandomNumberGenerator().nextBytes().toString();//盐
        String encodePassword = new SimpleHash(UserConstant.ENCRYPTION_TYPE, user.getPassword(), encrypt, UserConstant.ENCRYPTION_TIMES).toString();
        entity.setPassword(encodePassword);
        entity.setEncrypt(encrypt);
        if (!StringUtils.hasText(user.getCompany()))
            entity.setCompany("");
        if (user.getCreateTime() == null)
            entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        userRepository.save(entity);
        return response;
        //userRedisService.put(user.getRedisKey(), entity, -1);
    }

    @Override
    public BaseResponse getUserList(Pageable pageable) {
        Page<UserEntity> pageList = userRepository.findAll(pageable);
        List<User> userList = new ArrayList<>();
        for (UserEntity userEntity : pageList) {
            User user = new User();
            BeanUtils.copyProperties(userEntity, user);
            userList.add(user);
        }
        List<User> list = convertToUserList(userRepository.findAll());//所有用户数 用于获取数量
        BaseResponse response = new BaseResponse();
        response.setData(userList);
        response.setMsg("SUCCESS");
        response.setCount(list.size());
        return response;
    }

    @Override
    public List<User> findUser(User user) {
        List<UserEntity> userEntityList = userRepository.findUser(user.getMobile(), user.getCompany(), user.getAccountName());
        return convertToUserList(userEntityList);
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
