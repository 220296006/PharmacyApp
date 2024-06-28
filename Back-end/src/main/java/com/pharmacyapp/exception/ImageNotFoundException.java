package com.pharmacyapp.exception;
/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
 * @Time : 19:24
 **/

public class ImageNotFoundException extends RuntimeException {
        public ImageNotFoundException(String message, String fileName) {
            super(message + " (Filename: " + fileName + ")");
        }
    }
