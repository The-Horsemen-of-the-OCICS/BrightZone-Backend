package com.carleton.comp5104.cms.controller.professor;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping(path = "/cms")
    public @ResponseBody String index() {
        return "helloWord";
    }


    @GetMapping("/submitDeliverable/")
    public @ResponseBody String submitDeliverableTest(@RequestBody Deliverable deliverable) {
        int code = professorService.submitDeliverable(deliverable);
        if (code == 0) {
            return ("SUCCEED");
        } else {
            return ("FAIL");
        }
    }

    @PostMapping("/submitGrade/{id}/{grade}")
    public @ResponseBody String submitDeliverableGradeTest(@PathVariable String id, String grade) {
        int i = Integer.parseInt(id);
        float g = Float.parseFloat(grade);
        int code = professorService.submitDeliverableGrade(i, g);
        if (code == 0) {
            return ("SUCCEED");
        } else {
            return ("FAIL");
        }
    }

    @PostMapping("/submitFinalGrade/{class_id}/{student_id}")
    public @ResponseBody String submitFinalGradeTest(@PathVariable String class_id, @PathVariable String student_id) {
        int cid = Integer.parseInt(class_id);
        int sid = Integer.parseInt(student_id);
        int code = professorService.submitFinalGrade(cid, sid);
        if (code == 0) {
            return ("SUCCEED");
        } else {
            return ("FAIL");
        }
    }

    @Transactional
    @DeleteMapping("/deleteDeliverable/{id}")
    public @ResponseBody String delete(@PathVariable String id) {
        int deliverableId = Integer.parseInt(id);
        int code = professorService.deleteDeliverable(deliverableId);
        if (code == 0) {
            return ("SUCCEED");
        } else {
            return ("FAIL");
        }
    }

    @Transactional
    @DeleteMapping("/deleteAllDeliverableFromClass/{class_id}")
    public @ResponseBody String deleteAllDeliverableFromClass(@PathVariable String class_id) {
        int cid = Integer.parseInt(class_id);
        int code = professorService.deleteAllDeliverable(cid);
        if (code == 0) {
            return ("SUCCEED");
        } else {
            return ("FAIL");
        }
    }


}
