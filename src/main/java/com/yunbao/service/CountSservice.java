package com.yunbao.service;

import com.yunbao.annotation.CountLock;
import com.yunbao.util.RedisConstantUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: djl
 * @Date: 2019/4/18 15:30
 * @Version 1.0
 */
@Service
public class CountSservice {


    @CountLock(lockName = RedisConstantUtils.REDIS_LOCK_PREFIX,
            retryTimes = RedisConstantUtils.RETRY_TIMES,
            retryWait = RedisConstantUtils.RETRY_WAIT)
    public int doUserCount(Integer userId,Integer countId)  {
        return 1;
    }
}
