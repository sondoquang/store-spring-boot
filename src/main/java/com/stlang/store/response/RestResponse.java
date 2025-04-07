package com.stlang.store.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {

    private int statusCode;
    private String errorMessage;

    private Object message;
    private T data;

}
