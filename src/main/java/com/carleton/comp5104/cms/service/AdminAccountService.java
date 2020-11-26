package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Faculty;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface AdminAccountService {
    Page<Account> getAllAccount(Integer pageNum, Integer pageSize);

    List<Faculty> getAllFaculties();

    Account getAccountById(Integer id);

    Integer addNewAccount(Account newAccount);

    Integer deleteAccountById(Integer id);

    String updateAccount(Account updateAccount);

    Map<Integer, String> getFaculties();

    Map<Integer, String> getPrograms();

    String newAccountEmailValidCheck(String newEmailAddress);
}
