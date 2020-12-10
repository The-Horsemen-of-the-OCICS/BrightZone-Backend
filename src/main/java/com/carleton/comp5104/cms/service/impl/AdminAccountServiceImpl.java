package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.ClassroomSchedule;
import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Faculty;
import com.carleton.comp5104.cms.enums.AccountStatus;
import com.carleton.comp5104.cms.enums.AccountType;
import com.carleton.comp5104.cms.repository.*;
import com.carleton.comp5104.cms.service.AdminAccountService;
import com.carleton.comp5104.cms.service.AdminClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private ClassroomScheduleRepository classroomScheduleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AdminClazzService adminClazzService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Override
    public Page<Account> getAllAccount(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Page<Account> getAllAccountByType(String accountType, Integer pageNum, Integer pageSize) {
        accountType = accountType.trim();
        AccountType type = AccountType.valueOf(accountType);  // convert String to AccountType
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAllByType(type, pageable);
    }

    @Override
    public Page<Account> getAllAccountByName(String name, Integer pageNum, Integer pageSize) {
        name = name.trim();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAllByName(name, pageable);
    }

    @Override
    public Page<Account> getAllAccountByTypeAndName(String accountType, String name, Integer pageNum, Integer pageSize) {
        accountType = accountType.trim();
        AccountType type = AccountType.valueOf(accountType);  // convert String to AccountType
        name = name.trim();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAllByTypeAndName(type, name, pageable);
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public Account getAccountById(Integer id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElse(null);
    }


    @Override
    public Integer addNewAccount(Account newAccount) {
        int status = -1;
        try {
            accountRepository.save(newAccount);
            status = 0;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

//        Integer newAccountUserId = newAccount.getUserId();
//        if (!accountRepository.existsById(newAccountUserId)) {
//            accountRepository.save(newAccount);
//            status = 1;
//        }
        return status;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public Integer deleteAccountById(Integer accountId) {
        int result = -1;
        try {
            Optional<Account> accountOptional = accountRepository.findById(accountId);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                if (account.getType() == AccountType.professor) {
                    //delete all the clazz the professor taught
                    List<Clazz> allClazzByProfId = clazzRepository.findByProfId(account.getUserId());
                    //this will delete all the clazz under the name of this professor
                    //1.delete all deliverable under the clazz
                    //      *1.1 and delete all submission  under the deliverable
                    //2.delete all enrollments under the clazz
                    //3.delete all classroom schedules assigned to this clazz.
                    for (Clazz clazz : allClazzByProfId) {
                        adminClazzService.deleteClassByClassId(clazz.getClassId());
                    }
                } else if (account.getType() == AccountType.student) {
                    //from the point of student. (two parts: 1. enrollment 2.submission
                    //delete all the clazz the student enrolled.
                    enrollmentRepository.deleteByStudentId(accountId);
                    //delete all submission the student submitted.
                    submissionRepository.deleteByStudentId(accountId);
                }

                //then, the request need to be delete.

                requestRepository.deleteAllByUserId(accountId);

                //finally, delete the account.

                accountRepository.deleteById(accountId);

                result = 0;
            }
        } catch (Exception ex) {
            result = -1;
        }
        return result;
    }

    @Override
    public Integer updateAccount(Account updateAccount) {
        int status = -1;
        try {
            Optional<Account> byId = accountRepository.findById(updateAccount.getUserId());
            if (byId.isPresent()) {
                Account storedAccount = byId.get();
                if (storedAccount.getAccountStatus() != updateAccount.getAccountStatus()) {
                    if (storedAccount.getAccountStatus() == AccountStatus.unauthorized && updateAccount.getAccountStatus() == AccountStatus.current) {
                        String verificationCode = this.getVerificationCode(6);
                        updateAccount.setVerificationCode(verificationCode);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendEmail("cmsserver123@gmail.com", updateAccount.getEmail(),
                                        "Account Authorization", "Authorization Code:" + verificationCode);
                            }
                        }).start();
                    }
                }
                accountRepository.save(updateAccount);
                status = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return status;
    }

    @Override
    public String newAccountEmailValidCheck(String newEmailAddress) {
        if (accountRepository.existsAccountByEmail(newEmailAddress)) {
            return "Repeat";
        } else {
            return "Valid";
        }
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
