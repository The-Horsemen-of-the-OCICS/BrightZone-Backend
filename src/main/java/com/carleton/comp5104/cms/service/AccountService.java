package com.carleton.comp5104.cms.service;

import java.util.Map;

public interface AccountService {
    Map<String, Object> registerAccount(String emailOrUserId);

    Map<String, Object> login(String emailOrUserId, String password);

    Map<String, Object> createRequest(int accountId, String requestMessage, String requestType);

    Map<String, Object> passwordRecovery(String email, String verificationCode, String newPassword);

    Map<String, Object> sendVerificationCode(String email);

    Map<String, Object> updateEmail(int userId, String email);

    Map<String, Object> updatePassword(int userId, String password);
}
