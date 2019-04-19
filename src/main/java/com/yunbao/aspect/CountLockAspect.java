package com.yunbao.aspect;

import com.yunbao.annotation.CountLock;
import com.yunbao.util.CommonRedisHelper;
import com.yunbao.util.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: djl
 * @Date: 2019/4/19 15:06
 * @Version 1.0
 */
@Component
@Aspect
public class CountLockAspect {

    private static final String LOCK_NAME = "lockName";
    private static final String RETRY_TIMES = "retryTimes";
    private static final String RETRY_WAIT = "retryWait";

    @Autowired
    private CommonRedisHelper redisHelper;

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.yunbao.annotation.CountLock)")
    public void doCountLock(){

    }

    @Around("doCountLock()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        //获取注解中的参数
        Map<String,Object> argsMap = this.getAnnotationArgs(point);
        String lockName = (String) argsMap.get(LOCK_NAME);
        Assert.notNull(lockName, "分布式,锁名不能为空");
        int retryTimes = (int) argsMap.get(RETRY_TIMES);
        long retryWait = (long) argsMap.get(RETRY_WAIT);

        // 获取锁
        boolean lock = redisHelper.lock(lockName);
        if (lock) {
            return execut(point, lockName);
        } else {
            if (retryTimes <= 0) {
                throw new Exception("已经被锁，不重试");
            }

            if (retryWait == 0) {
                retryWait = 200;
            }
            // 设置失败次数计数器, 当到达指定次数时, 返回失败
            int failCount = 1;
            while (failCount <= retryTimes) {
                // 等待指定时间ms
                try {
                    Thread.sleep(retryWait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (redisHelper.lock(lockName)) {
                    // 执行主逻辑
                    return execut(point, lockName);
                } else {

                    failCount++;
                }
            }
            throw new Exception("系统繁忙, 请稍等再试");
        }

    }

    public Object execut(ProceedingJoinPoint point,String lockName) throws Throwable{
        try {
            Object proceed = point.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            redisUtil.expire(lockName, 0);
        }
    }

    /** 获取注解中的参数*/
    private Map<String,Object> getAnnotationArgs(ProceedingJoinPoint point) {
        Class target = point.getTarget().getClass();
        Method[] methods = target.getMethods();
        String methodName = point.getSignature().getName();
        Map<String,Object> result = new HashMap<String,Object>();
        for(Method method : methods) {
            if(method.getName().equals(methodName)) {
                CountLock countLock = method.getAnnotation(CountLock.class);
                Integer secondarg = getSecontArg(point);
                result.put(LOCK_NAME,countLock.lockName().concat(secondarg.toString()));
                result.put(RETRY_TIMES,countLock.retryTimes());
                result.put(RETRY_WAIT,countLock.retryWait());
            }
        }
        return result;
    }

    /** 获取第二个参数*/
    private Integer getSecontArg(ProceedingJoinPoint point) {
        Object[] args = point.getArgs();
        if (args != null && args.length > 0) {
            return (Integer)args[1];
        }
        return null;
    }

}
