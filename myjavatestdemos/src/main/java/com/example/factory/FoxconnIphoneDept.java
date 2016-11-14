package com.example.factory;

/**
 * Created by lsq on 11/2/2016.
 */

public class FoxconnIphoneDept implements PhoneFactory {
    @Override
    public Phone productIphone(String brand) {
        return null;
    }

    @Override
    public Phone productXiaomi(String brand) {
        return null;
    }
}
