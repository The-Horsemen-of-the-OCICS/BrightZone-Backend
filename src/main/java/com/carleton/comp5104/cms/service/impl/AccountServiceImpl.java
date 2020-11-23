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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private JavaMailSender javaMailSender;

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

    @Override
    public Map<String, Object> passwordRecovery(String email, String verificationCode, String newPassword) {
        HashMap<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(email)) {
            map.put("success", false);
            map.put("errMsg", "Email shouldn't be empty");
            return map;
        }

        if (StringUtils.isEmpty(verificationCode)) {
            map.put("success", false);
            map.put("errMsg", "Verification code shouldn't be empty");
            return map;
        }

        if (StringUtils.isEmpty(newPassword)) {
            map.put("success", false);
            map.put("errMsg", "New password shouldn't be empty");
            return map;
        }

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            map.put("success", false);
            map.put("errMsg", "Please register an account first");
        } else {
            if (AccountStatus.unauthorized.equals(account.getAccountStatus())) {
                map.put("success", false);
                map.put("errMsg", "Email is not authorized, please wait for admin’s authorization");
            } else {
                if (!verificationCode.equals(account.getVerificationCode())) {
                    map.put("success", false);
                    map.put("errMsg", "Wrong verification code");
                } else {
                    account.setPassword(newPassword);
                    Account save = accountRepository.save(account);
                    map.put("success", true);
                    map.put("account", save);
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> sendVerificationCode(String email) {
        email = email.trim();
        HashMap<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(email)) {
            map.put("success", false);
            map.put("errMsg", "Email shouldn't be empty");
            return map;
        }

        Boolean exist = accountRepository.existsAccountByEmail(email);
        if (!exist) {
            map.put("success", false);
            map.put("errMsg", "Email is not valid");
            return map;
        }
        String verificationCode = this.getVerificationCode(6);
        Account account = accountRepository.findByEmail(email);
        account.setVerificationCode(verificationCode);
        Account save = accountRepository.save(account);

        new Thread(new Runnable() {
            @Override
            public void run() {
                sendEmail("cmsserver123@gmail.com", account.getEmail(),
                        "Password Recovery", "Verification Code:" + verificationCode);
            }
        }).start();

        map.put("success", true);
        map.put("account", save);
        return map;
    }

    private String getVerificationCode(int length) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            code.append(new Random().nextInt(10));
        }
        return code.toString();
    }

    private void sendEmail(String from, String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
