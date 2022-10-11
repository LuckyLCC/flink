package com.liuchang.test.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-08-15  15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    String name;
    Integer age;
}
