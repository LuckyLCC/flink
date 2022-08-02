package com.liuchang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-07-26  16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    public String userId;
    public String orderId;
    public String eventType;
    public Long timestamp;
}
