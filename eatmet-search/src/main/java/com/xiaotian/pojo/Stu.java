package com.xiaotian.pojo;

import io.swagger.models.auth.In;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * @description:
 * @author: Tian
 * @time: 2022/10/19 22:42
 */

@Document(indexName = "stu", type = "_doc")
public class Stu {

    @Id
    private Long stuId;

    @Field(store = true)
    private String name;

    @Field(store = true)
    private Integer age;

    @Field(store = true)
    private Double money;

    @Field(store = true)
    private Boolean sex;

    @Field(store = true)
    private String desc;

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", money=" + money +
                ", sex=" + sex +
                ", desc='" + desc + '\'' +
                '}';
    }
}
