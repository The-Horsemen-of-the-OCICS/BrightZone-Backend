package com.carleton.comp5104.cms.controller;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.vo.ResultVo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public abstract class BaseController {


    private static final String SESSION_KEY = "userId";

    public void setUserId(Account account) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(SESSION_KEY, account.getUserId());
    }

    public int getUserId() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        HttpSession session = request.getSession();

        Integer userId = (Integer) session.getAttribute(SESSION_KEY);
        if (userId == null) {
            throw new RuntimeException("Login error");
        }
        System.out.println(userId);
        return userId;
    }

    protected <T> ResultVo<T> getSuccessResult(T data, String desc) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setStatus(200);
        resultVo.setDesc(desc);
        return resultVo;
    }

    protected <T> ResultVo<T> getFailedResult(T data, String desc) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(data);
        resultVo.setStatus(500);
        resultVo.setDesc(desc);
        return resultVo;
    }
}
