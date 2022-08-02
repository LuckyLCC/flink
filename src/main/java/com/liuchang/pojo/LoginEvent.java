package com.liuchang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-07-26  13:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEvent {

    public String userId;
    public String ipAddress;
    public String eventType;
    public Long timestamp;
}
