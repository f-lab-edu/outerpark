package com.sadowbass.outerpark.infra.session;

import com.sadowbass.outerpark.application.account.dto.LoginResult;
import com.sadowbass.outerpark.application.account.exception.AlreadyLoggedInException;
import com.sadowbass.outerpark.infra.session.exception.LoginRequiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class LoginManager {

    private final ThreadLocal<HttpSession> currentSession = new ThreadLocal<>();
    private static final String SESSION_ID = "loginUser";

    public void login(LoginResult loginResult) {
        HttpSession httpSession = this.currentSession.get();
        if (httpSession.getAttribute(SESSION_ID) != null) {
            throw new AlreadyLoggedInException();
        }

        httpSession.setAttribute(SESSION_ID, loginResult);
    }

    public LoginResult getMember() {
        LoginResult loginResult = (LoginResult) currentSession.get().getAttribute(SESSION_ID);
        if (loginResult == null) {
            throw new LoginRequiredException();
        }

        return loginResult;
    }

    public void setSession(HttpSession session) {
        this.currentSession.set(session);
    }

    public void clear() {
        this.currentSession.remove();
    }
}
