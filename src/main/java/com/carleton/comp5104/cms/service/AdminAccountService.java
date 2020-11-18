package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


public interface AdminAccountService {
    Page<Account> getAllAccount(Integer pageNum, Integer pageSize);

    Account getAccountById(Integer id);

    String addNewAccount(Account newAccount);

    String deleteAccountById(Integer id);

    String updateAccount(Account updateAccount);

    Map<Integer, String> getFaculties();

    Map<Integer, String> getPrograms();

    String newAccountEmailValidCheck(String newEmailAddress);
}
