package com.sadowbass.outerpark.infra.session.interceptor;

import com.sadowbass.outerpark.infra.session.LoginManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final LoginManager loginManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        loginManager.setSession(request.getSession());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        loginManager.clear();
    }
}
