package com.stlang.store.utils;

import com.stlang.store.response.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
        int statusCode = resp.getStatus();
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(statusCode);
        if(statusCode >= 400 || body instanceof byte [] || body instanceof String ) {
            return body;
        }
        String path = request.getURI().getPath();
        if(path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")){
            return body;
        }
        else{
            restResponse.setData(body);
            restResponse.setMessage("Success");
        }
        return restResponse;
    }

}
