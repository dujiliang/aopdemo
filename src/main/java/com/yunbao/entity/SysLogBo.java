package com.yunbao.entity;

import lombok.Data;

/**
 * @Author: djl
 * @Date: 2019/4/18 11:26
 * @Version 1.0
 */
@Data
public class SysLogBo {

    private String className;

    private String methodName;

    private String params;

    private Long exeuTime;

    private String remark;

    private String createDate;

    @Override
    public String toString() {
        return "SysLogBo{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params='" + params + '\'' +
                ", exeuTime=" + exeuTime +
                ", remark='" + remark + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
