package com.departement.fichedevoeux.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSecurity {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("amira123");
        System.out.println(hash);
    }
}
