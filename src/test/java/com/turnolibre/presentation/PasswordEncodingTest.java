package com.turnolibre.presentation;


import org.junit.Test;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import static org.junit.Assert.assertTrue;

public class PasswordEncodingTest {

    @Test
    public void shouldEncodePassword() {

        StandardPasswordEncoder encoder = new StandardPasswordEncoder();
        String result = encoder.encode("123456");

		// 2c2b8de744017843971811f555da082e7c26249f240a315560d5c4c81f7bc651d8edcbfbf9829918
        assertTrue(encoder.matches("123456", result));
    }

}
