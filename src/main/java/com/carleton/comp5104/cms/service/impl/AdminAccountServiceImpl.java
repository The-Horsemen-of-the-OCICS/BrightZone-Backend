package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Account;
import com.carleton.comp5104.cms.entity.Faculty;
import com.carleton.comp5104.cms.repository.AccountRepository;
import com.carleton.comp5104.cms.repository.FacultyRepository;
import com.carleton.comp5104.cms.service.AdminAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Override
    public Page<Account> getAllAccount(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account getAccountById(Integer id) {
        Optional<Account> byId = accountRepository.findById(id);
        return byId.orElse(null);
    }

    @Override
    public String addNewAccount(Account newAccount) {
        Integer newAccountUserId = newAccount.getUserId();
        if (!accountRepository.existsById(newAccountUserId)) {
            accountRepository.save(newAccount);
            return "success";
        } else {
            return "error";
        }
    }

    @Override
    public String deleteAccountById(Integer id) {
        accountRepository.deleteById(id);
        return "success";
    }

    @Override
    public String updateAccount(Account updateAccount) {
        accountRepository.save(updateAccount);
        return "success";
    }

    @Override
    public Map<Integer, String> getFaculties() {
        HashMap<Integer, String> facultyMap = new HashMap<>();
        List<Faculty> allFaculties = facultyRepository.findAll();
        for (Faculty faculty : allFaculties) {
            facultyMap.put(faculty.getFacultyId(), faculty.getFacultyName());
        }
        return facultyMap;
    }

    //TODO add program table.
    @Override
    public Map<Integer, String> getPrograms() {
        return null;
    }

    @Override
    public String newAccountEmailValidCheck(String newEmailAddress) {
        if (accountRepository.existsAccountByEmail(newEmailAddress)) {
            return "Repeat";
        } else {
            return "Valid";
        }
    }
}
