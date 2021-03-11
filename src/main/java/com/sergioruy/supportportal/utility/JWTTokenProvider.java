package com.sergioruy.supportportal.utility;

import org.springframework.beans.factory.annotation.Value;

public class JWTTokenProvider {
    @Value("jwt.secret")
    private String secret;

}
