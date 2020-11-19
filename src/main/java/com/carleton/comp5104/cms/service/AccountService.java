package com.carleton.comp5104.cms.service;

import java.util.Map;

public interface AccountService {
    Map<String, Object> registerAccount(String email);

    Map<String, Object> login(String email, String password);

    Map<String, Object> createRequest(int accountId, String requestMessage, String requestType);
}
