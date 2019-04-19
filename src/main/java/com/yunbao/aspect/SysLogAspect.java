package com.yunbao.aspect;

import com.google.gson.Gson;
import com.yunbao.annotation.SysLog;
import com.yunbao.entity.SysLogBo;
import com.yunbao.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: djl
 * @Date: 2019/4/18 11:43
 * @Version 1.0
 */

@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Pointcut("@annotation(com.yunbao.annotation.SysLog)")
    public void syslogPointCut() {

    }

    @Around("syslogPointCut()")
    public Object arount(ProceedingJoinPoint point) throws Throwable{
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long endTime = System.currentTimeMillis();
        Long executeTime = endTime - beginTime;
        saveLog(point,executeTime);
        return result;
    }

    public void saveLog (ProceedingJoinPoint point , long executeTime) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        SysLog logAnnotation = method.getAnnotation(SysLog.class);
        SysLogBo log = new SysLogBo();
        log.setExeuTime(executeTime);
        log.setCreateDate("2019-04-18");
        log.setRemark(logAnnotation.value());
        log.setMethodName(method.getName());
        log.setClassName(point.getTarget().getClass().getName());

        // 请求的参数
        Object[] args = point.getArgs();
        List<String> paramsList = new ArrayList<String>();
        for(Object o : args) {
            paramsList.add(new Gson().toJson(o));
        }
        log.setParams(paramsList.toString());
        sysLogService.infoLog(log);
    }
}
