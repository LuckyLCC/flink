package com.liuchang.pojo;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    public String user;
    public String url;
    public Long timestamp;

}
