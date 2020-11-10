package com.carleton.comp5104.cms.controller;



import com.carleton.comp5104.cms.entity.Enrollment;
import com.carleton.comp5104.cms.repository.EnrollmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EnrollmentController  {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping(path = "/findAll")
    public @ResponseBody Iterable  findAll() {
        return enrollmentRepository.findAll();
    }
}
