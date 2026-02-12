package com.example.oda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

public class ExceptionChain {
    public static void main(String[] args) {
        HttpClientErrorException httpEx = new HttpClientErrorException(HttpStatusCode.valueOf(404), "Not Found");

        RestClientException restEx = createRestClientException(httpEx);

        checkException(restEx);
    }

    private static RestClientException createRestClientException(HttpClientErrorException ex) {
        return ex;
    }

    private static void checkException(Throwable throwable) {
        if (throwable instanceof HttpClientErrorException) {
            System.out.println("It's a HttpClientErrorException");
        } else {
            System.out.println("It's a RuntimeException");
        }
    }

}
