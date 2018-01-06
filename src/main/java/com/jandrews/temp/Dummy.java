package com.jandrews.temp;

import javax.enterprise.inject.Model;

@Model
public class Dummy {
    public String getHello() {
        return "Hello, world!";
    }
}
