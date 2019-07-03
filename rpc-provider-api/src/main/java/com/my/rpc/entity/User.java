package com.my.rpc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2019/7/3 12:43
 * @Created by rogan.luo
 */
@Data
public class User implements Serializable {
    private String id;

    private String name;
}
