package qeeka.jake.imagesteganography.service.user.impl;

import org.springframework.stereotype.Service;
import qeeka.jake.imagesteganography.domain.user.UserEntity;
import qeeka.jake.imagesteganography.service.redis.RedisService;

/**
 * 用户redis service继承类
 *
 */
@Service("userRedisService")
public class UserRedisServiceImpl extends RedisService<UserEntity> {

    //自定义redis key作为Hash表的key名称
    private static final String REDIS_KEY = "USER_KEY";

    @Override
    protected String getRedisKey() {
        // TODO Auto-generated method stub
        return REDIS_KEY;
    }

}