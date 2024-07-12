package com.ar.api.Api;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CorsFilter  implements Filter{
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException{
       
}

@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException{
    HttpServletResponse httpResponse = (HttpServletResponse) res;
    HttpServletRequest httpRequest = (HttpServletRequest) req;
    
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    httpResponse.setHeader("Access-Control-Max-Age", "600");
    httpResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type");

    if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return;
    }

    chain.doFilter(req, res);

}

}