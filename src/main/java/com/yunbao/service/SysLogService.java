package com.yunbao.service;

import com.yunbao.entity.SysLogBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: djl
 * @Date: 2019/4/18 14:20
 * @Version 1.0
 */
@Service
@Slf4j
public class SysLogService {

    public boolean infoLog(SysLogBo sysLogBo) {

        log.info(sysLogBo.toString());
        return true;
    }
}
