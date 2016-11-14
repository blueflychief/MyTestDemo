package com.example.factory;

/**
 * 抽象工厂类
 * Created by lsq on 11/2/2016.
 */

public interface PhoneFactory {
    Phone productIphone(String brand);

    Phone productXiaomi(String brand);
}
