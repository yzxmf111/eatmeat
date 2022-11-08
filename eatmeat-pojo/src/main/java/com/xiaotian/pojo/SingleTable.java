package com.xiaotian.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xiaotian
 * @description
 * @date 2022-09-27 11:35
 */
@Table(name = "single_table")
public class SingleTable {

    @Id
    private Integer id;
    @Column(name = "key1")
    private String key1;
    @Column(name = "key2")
    private int key2;
    @Column(name = "key3")
    private String key3;
    @Column(name = "key_part1")
    private String keyPart1;
    @Column(name = "key_part2")
    private String keyPart2;
    @Column(name = "key_part3")
    private String keyPart3;
    @Column(name = "common_field")
    private String commonField;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public int getKey2() {
        return key2;
    }

    public void setKey2(int key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKeyPart1() {
        return keyPart1;
    }

    public void setKeyPart1(String keyPart1) {
        this.keyPart1 = keyPart1;
    }

    public String getKeyPart2() {
        return keyPart2;
    }

    public void setKeyPart2(String keyPart2) {
        this.keyPart2 = keyPart2;
    }

    public String getKeyPart3() {
        return keyPart3;
    }

    public void setKeyPart3(String keyPart3) {
        this.keyPart3 = keyPart3;
    }

    public String getCommonField() {
        return commonField;
    }

    public void setCommonField(String commonField) {
        this.commonField = commonField;
    }
}
