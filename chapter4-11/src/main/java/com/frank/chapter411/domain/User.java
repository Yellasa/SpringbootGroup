package com.frank.chapter411.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @author jianweilin
 * @date 2017/10/24
 */
@Document(indexName = "yttest", type = "userb")
public class User {
    @Id
    private Long id;

    @Field
    private Integer age;

    @Field
    private String userName;

    @Field
    private String userPhone;

    public User() {
    }

    public User(Long id,Integer age, String userName, String userPhone) {
        this.id = id;
        this.age = age;
        this.userName = userName;
        this.userPhone = userPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }
}
