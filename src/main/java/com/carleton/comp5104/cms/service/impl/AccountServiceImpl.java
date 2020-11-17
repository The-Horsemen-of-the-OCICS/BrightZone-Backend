package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Map<String, Object> registerAccount(String email) {
        HashMap<String, Object> result = new HashMap<>();

        // should be taken over by front-end
        if (StringUtils.isEmpty(email)) {
            result.put("success", false);
            result.put("errMsg", "Email is empty");
            return result;
        }

        Person person = personRepository.findByEmail(email);
        if (person == null) {
            result.put("success", false);
            result.put("errMsg", "You are not allowed to register an account in CMS");
        } else {
            Account account = accountRepository.findByEmail(email);
            if (account != null) {
                AccountStatus accountStatus = account.getAccountStatus();
                result.put("success", false);
                if (accountStatus.equals(AccountStatus.unauthorized))  {
                    result.put("errMsg", "Please wait for Admin's authorization");
                } else {
                    result.put("errMsg", "Sorry, you are not allowed to register a new account");
                }
            } else {
                Account newAccount = new Account();
                newAccount.setUserId(person.getPersonId());
                newAccount.setName(person.getName());
                newAccount.setType(person.getType());
                newAccount.setFacultyId(person.getFacultyId());
                newAccount.setProgram(person.getProgram());
                newAccount.setEmail(email);
                // do not set password
                newAccount.setAccountStatus(AccountStatus.unauthorized);
                // do not set lastLogin
                // do not set verification code
                accountRepository.save(newAccount);
                result.put("success", true);
            }
        }
        return result;
    }
}
