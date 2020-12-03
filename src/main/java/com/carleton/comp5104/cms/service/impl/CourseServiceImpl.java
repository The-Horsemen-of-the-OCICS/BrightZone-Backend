package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.*;
import com.carleton.comp5104.cms.enums.*;
import com.carleton.comp5104.cms.repository.*;
import com.carleton.comp5104.cms.service.CourseService;
import com.carleton.comp5104.cms.vo.CourseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private PreclusionRepository preclusionRepository;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public RegisterStatus registerCourse(int studentId, int classId) {
        Optional<Clazz> clazzOptional = clazzRepository.findByClassId(classId);
        if (!clazzOptional.isPresent()) {
            return RegisterStatus.fail;
        }
        Clazz clazz = clazzOptional.get();
        if (clazz.getEnrollCapacity() <= clazz.getEnrolled()) {
            return RegisterStatus.fail1;
        }

        if (System.currentTimeMillis() > clazz.getEnrollDeadline().getTime()) {
            return RegisterStatus.fail2;
        }
        Set<Enrollment> enrollmentSet = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.passed);
        List<Integer> clazzIdList = enrollmentSet.stream().map(p -> p.getClassId()).collect(Collectors.toList());
        Set<Integer> enrolledCourseId = clazzRepository.findAllById(clazzIdList).stream().map(e -> e.getCourseId()).collect(Collectors.toSet());

        Set<Integer> preReqId = prerequisiteRepository.findByCourseId(clazz.getCourseId()).stream().map(p -> p.getPrerequisiteId()).collect(Collectors.toSet());
        if (!enrolledCourseId.containsAll(preReqId)) {
            return RegisterStatus.fail3;
        }

        List<Integer> preCluId = preclusionRepository.findByCourseId(clazz.getCourseId()).stream().map(p -> p.getPreclusionId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(preCluId) && !preCluId.retainAll(enrolledCourseId)) {
            return RegisterStatus.fail4;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setClassId(classId);
        enrollment.setEnrollTime(new Timestamp(System.currentTimeMillis()));
        enrollment.setStatus(EnrollmentStatus.ongoing);
        enrollmentRepository.save(enrollment);

        clazz.setEnrolled(clazz.getEnrolled() + 1);
        clazzRepository.save(clazz);
        return RegisterStatus.success;
    }

    @Override
    @Transactional
    public DropStatus dropCourse(int studentId, int clazzId) {
        Optional<Clazz> clazzOptional = clazzRepository.findByClassId(clazzId);
        if (!clazzOptional.isPresent()) {
            return DropStatus.fail;
        }
        Clazz clazz = clazzOptional.get();

        if (System.currentTimeMillis() < clazz.getDropNoPenaltyDeadline().getTime()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId);
            if (enrollment.isPresent()) {
                Enrollment e = enrollment.get();
                e.setStatus(EnrollmentStatus.dropped);
                enrollmentRepository.save(e);
                clazz.setEnrolled(clazz.getEnrolled() - 1);
                clazzRepository.save(clazz);
            }
            return DropStatus.success;
        }

        if (System.currentTimeMillis() < clazz.getDropNoFailDeadline().getTime()) {
            Optional<Enrollment> enrollment = enrollmentRepository.findByClassIdAndStudentId(clazzId, studentId);
            if (enrollment.isPresent()) {
                Enrollment e = enrollment.get();
                e.setStatus(EnrollmentStatus.dropped_dr);
                enrollmentRepository.save(e);
                clazz.setEnrolled(clazz.getEnrolled() - 1);
                clazzRepository.save(clazz);
            }
            return DropStatus.success1;
        }

        return DropStatus.fail;
    }

    @Override
    public List<CourseVo> getAllOpenedCourse(int studentId) {
        List<Clazz> allByClassStatus = clazzRepository.findAllByClassStatus(ClassStatus.open);
        List<Course> courseList = courseRepository.findAll();
        Map<Integer, Course> courseMap = courseList.stream().collect(Collectors.toMap(Course::getCourseId, course -> course));

        List<Person> professorList = personRepository.findAllByTypeEquals(AccountType.professor);
        Map<Integer, Person> professorMap = professorList.stream().collect(Collectors.toMap(Person::getPersonId, professor -> professor));

        Set<Enrollment> allEnrollment = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ongoing);
        Set<Integer> enrollmentSet = allEnrollment.stream().map(Enrollment::getClassId).collect(Collectors.toSet());


        List<CourseVo> courseVoSet = allByClassStatus.stream().map(clazz -> {
            CourseVo courseVo = new CourseVo();
            courseVo.setClazzId(clazz.getClassId());
            courseVo.setCourseId(clazz.getCourseId());
            courseVo.setCourseNo(courseMap.get(clazz.getCourseId()).getCourseSubject() + courseMap.get(clazz.getCourseId()).getCourseNumber());
            courseVo.setCourseName(courseMap.get(clazz.getCourseId()).getCourseName());
            courseVo.setCourseDesc(courseMap.get(clazz.getCourseId()).getCourseDesc());
            courseVo.setProfessorId(professorMap.get(clazz.getProfId()).getPersonId());
            courseVo.setProfessorName(professorMap.get(clazz.getProfId()).getName());
            if (enrollmentSet.contains(clazz.getClassId())) {
                courseVo.setEnrolled(1);
            }
            return courseVo;
        }).collect(Collectors.toList());

        return courseVoSet;
    }

    @Override
    public Set<CourseVo> getAllRegisteredCourse(int studentId) {
        Set<Enrollment> allEnrollment = enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ongoing);

        List<Course> courseList = courseRepository.findAll();
        Map<Integer, Course> courseMap = courseList.stream().collect(Collectors.toMap(Course::getCourseId, course -> course));

        List<Person> professorList = personRepository.findAllByTypeEquals(AccountType.professor);
        Map<Integer, Person> professorMap = professorList.stream().collect(Collectors.toMap(Person::getPersonId, professor -> professor));

        List<Clazz> clazzList = clazzRepository.findAll();
        Map<Integer, Clazz> clazzMap = clazzList.stream().collect(Collectors.toMap(Clazz::getClassId, clazz -> clazz));

        Set<CourseVo> courseVoSet = allEnrollment.stream().map(enrollment -> {
            CourseVo courseVo = new CourseVo();
            courseVo.setClazzId(enrollment.getClassId());
            int courseId = clazzMap.get(enrollment.getClassId()).getCourseId();
            int professorId = clazzMap.get(enrollment.getClassId()).getProfId();
            courseVo.setCourseId(courseId);
            courseVo.setCourseNo(courseMap.get(courseId).getCourseSubject() + courseMap.get(courseId).getCourseNumber());
            courseVo.setCourseName(courseMap.get(courseId).getCourseName());
            courseVo.setCourseDesc(courseMap.get(courseId).getCourseDesc());
            courseVo.setProfessorId(professorMap.get(professorId).getPersonId());
            courseVo.setProfessorName(professorMap.get(professorId).getName());
            courseVo.setEnrolled(1);
            return courseVo;
        }).collect(Collectors.toSet());

        return courseVoSet;
    }

    @Override
    public CourseVo getCourse(int clazzId) {
        Optional<Clazz> byId = clazzRepository.findById(clazzId);
        CourseVo courseVo = new CourseVo();
        byId.ifPresent(c -> {
            courseVo.setClazzId(c.getClassId());
            courseRepository.findById(c.getCourseId()).ifPresent(course -> {
                courseVo.setCourseName(course.getCourseName());
                courseVo.setCourseNo(course.getCourseSubject() + course.getCourseNumber());
            });
        });

        return courseVo;
    }

    @Override
    @Transactional
    public void dropAllCourse(int studentId) {
        enrollmentRepository.deleteByStudentId(studentId);
    }


}
