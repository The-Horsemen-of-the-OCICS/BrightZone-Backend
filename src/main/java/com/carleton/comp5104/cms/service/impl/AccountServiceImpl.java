package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Person;
import com.carleton.comp5104.cms.entity.Request;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.enums.RequestStatus;
import com.carleton.comp5104.cms.enums.RequestType;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.PersonRepository;
import com.carleton.comp5104.cms.repository.RequestRepository;
import com.carleton.comp5104.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RequestRepository requestRepository;

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

    @Override
    public Map<String, Object> login(String email, String password) {
        HashMap<String, Object> result = new HashMap<>();

        // should be taken over by front-end
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            result.put("success", false);
            result.put("errMsg", "Email or password is empty");
            return result;
        }

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            result.put("success", false);
            result.put("errMsg", "Account doesn't exist, please register an account");
        } else {
            if (account.getAccountStatus().equals(AccountStatus.unauthorized)) {
                result.put("success", false);
                result.put("errMsg", "Account is not authorized, please wait for admin’s authorization");
            } else if (!password.equals(account.getPassword())) {
                result.put("success", false);
                result.put("errMsg", "Wrong password");
            } else {
                result.put("success", true);
                result.put("account", account);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> createRequest(int accountId, String requestMessage, String requestType) {
        HashMap<String, Object> map = new HashMap<>();
        requestMessage = requestMessage.trim();

        // should be taken over by front-end
        if (StringUtils.isEmpty(requestMessage)) {
            map.put("success", false);
            map.put("errMsg", "Request message shouldn't be empty");
            return map;
        }

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (!optionalAccount.isPresent()) {
            map.put("success", false);
            map.put("errMsg", "Sorry, you're not allowed to create a request");
        } else {
            Account account = optionalAccount.get();
            if (AccountStatus.unauthorized.equals(account.getAccountStatus())) {
                map.put("success", false);
                map.put("errMsg", "Sorry, you're not allowed to create a request");
            } else {
                Request request = new Request();
                request.setUserId(accountId);
                request.setMessage(requestMessage);
                request.setStatus(RequestStatus.open);
                request.setType(RequestType.valueOf(requestType));
                Request save = requestRepository.save(request);
                map.put("success", true);
                map.put("request", save);
            }
        }
        return map;
    }
}
