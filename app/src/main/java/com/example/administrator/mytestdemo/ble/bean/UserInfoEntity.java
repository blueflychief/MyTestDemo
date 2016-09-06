package com.example.administrator.mytestdemo.ble.bean;

import java.io.Serializable;

public class UserInfoEntity implements Serializable {

    private int user_id;
    private int pulse;
    private int age;
    private int height;
    private int sex;
    private float weight;
    private int action_code ;
    private int kcal;
    private int staticHb;

    public int getAction_code() {
        return action_code;
    }

    public void setAction_code(int action_code) {
        this.action_code = action_code;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getStaticHb() {
        return staticHb;
    }

    public void setStaticHb(int staticHb) {
        this.staticHb = staticHb;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "user_id=" + user_id +
                ", pulse=" + pulse +
                ", age=" + age +
                ", height=" + height +
                ", sex=" + sex +
                ", weight=" + weight +
                ", action_code=" + action_code +
                ", kcal=" + kcal +
                ", staticHb=" + staticHb +
                '}';
    }
}
