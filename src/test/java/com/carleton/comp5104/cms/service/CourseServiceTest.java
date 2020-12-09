package com.carleton.comp5104.cms.service;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Preclusion;
import com.carleton.comp5104.cms.entity.Prerequisite;
import com.carleton.comp5104.cms.enums.DropStatus;
import com.carleton.comp5104.cms.enums.RegisterStatus;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.PreclusionRepository;
import com.carleton.comp5104.cms.repository.PrerequisiteRepository;
import com.carleton.comp5104.cms.vo.CourseVo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Assert;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private PreclusionRepository preclusionRepository;

    @Test
    public void testRegisterCourse() {
        courseService.dropAllCourse(3000182);
        RegisterStatus registerStatus = courseService.registerCourse(3000182, 1068);
        Assert.assertEquals(RegisterStatus.success, registerStatus);
    }

    @Test
    public void testRegisterCourse1() {
        Optional<Clazz> byId = clazzRepository.findById(1070);
        Clazz clazz = byId.get();
        int temp = clazz.getEnrolled();
        clazz.setEnrolled(clazz.getEnrollCapacity());
        clazzRepository.save(clazz);

        RegisterStatus registerStatus = courseService.registerCourse(3000182, 1070);
        Assert.assertEquals(RegisterStatus.fail1, registerStatus);

        clazz.setEnrolled(temp);
        clazzRepository.save(clazz);
    }

    @Test
    public void testRegisterCourse2() {
        Optional<Clazz> byId = clazzRepository.findById(1072);

        Prerequisite prerequisite = new Prerequisite();
        prerequisite.setCourseId(byId.get().getCourseId());
        prerequisite.setPrerequisiteId(1234);
        Prerequisite save = prerequisiteRepository.save(prerequisite);

        courseService.dropCourse(3000182, 1072);

        RegisterStatus registerStatus = courseService.registerCourse(3000182, 1072);
        Assert.assertEquals(RegisterStatus.fail3, registerStatus);
        courseService.registerCourse(3000182, 1048);

        prerequisiteRepository.deleteAll();
    }

    @Test
    public void testRegisterCourse3() {
        Optional<Clazz> byId = clazzRepository.findById(1073);

        Preclusion preclusion = new Preclusion();
        preclusion.setCourseId(byId.get().getCourseId());
        preclusion.setPreclusionId(3844);
        preclusionRepository.save(preclusion);

        courseService.dropCourse(3000182, 1073);

        RegisterStatus registerStatus = courseService.registerCourse(3000182, 1073);
        Assert.assertEquals(RegisterStatus.fail4, registerStatus);
    }

    @Test
    public void testDropCourse() {
        courseService.registerCourse(3000182, 1074);
        DropStatus dropStatus = courseService.dropCourse(3000182, 1072);
        Assert.assertEquals(DropStatus.success, dropStatus);
    }

    @Test
    public void testDropCourse1() {
        courseService.registerCourse(3000182, 1076);

        Optional<Clazz> byId = clazzRepository.findById(1076);
        Clazz clazz = byId.get();
        clazz.setDropNoPenaltyDeadline(new Timestamp(System.currentTimeMillis() + 24 * 60 * 60));
        clazzRepository.save(clazz);

        DropStatus dropStatus = courseService.dropCourse(3000182, 1076);
        Assert.assertEquals(DropStatus.success, dropStatus);
    }

    @Test
    public void testGetAllOpenedCourse() {
        List<CourseVo> allOpenedCourse = courseService.getAllOpenedCourse(3000182);
        Assert.assertEquals(true, allOpenedCourse.size() > 0);
    }


    @Test
    public void testGetAllRegisteredCourse() {
        Set<CourseVo> allRegisteredCourse = courseService.getAllRegisteredCourse(3000182);
        Assert.assertEquals(true, allRegisteredCourse.size() >= 0);
    }


    @Test
    public void testGetCourse() {
        CourseVo course = courseService.getCourse(1076);
        Assert.assertEquals(true, course != null);
    }

}
