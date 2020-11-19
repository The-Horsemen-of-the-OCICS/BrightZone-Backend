package com.carleton.comp5104.cms.service.impl;

import com.carleton.comp5104.cms.entity.Clazz;
import com.carleton.comp5104.cms.entity.Course;
import com.carleton.comp5104.cms.entity.Preclusion;
import com.carleton.comp5104.cms.entity.Prerequisite;
import com.carleton.comp5104.cms.repository.ClazzRepository;
import com.carleton.comp5104.cms.repository.CourseRepository;
import com.carleton.comp5104.cms.repository.PreclusionRepository;
import com.carleton.comp5104.cms.repository.PrerequisiteRepository;
import com.carleton.comp5104.cms.service.AdminCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClazzRepository clazzRepository;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private PreclusionRepository preclusionRepository;

    @Override
    public Page<Course> getAllCourse(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return courseRepository.findAll(pageable);
    }


    @Override
    public Course getCourseById(Integer courseId) {
        Optional<Course> byId = courseRepository.findById(courseId);
        return byId.orElse(null);
    }

    @Override
    public Integer addNewCourse(Course newCourse) {
        int status = -1;
        try {
            Integer newCourseId = newCourse.getCourseId();
            Optional<Course> byId = courseRepository.findById(newCourseId);
            if (byId.isEmpty()) {
                System.out.println("asd");
                System.out.println(newCourse.getCourseId());
                courseRepository.save(newCourse);
                status = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            status = -1;
        }
        return status;
    }

    @Override
    @Transactional
    public Integer deleteACourse(Integer courseId) {
        int status = -1;
        try {
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isPresent()) {
                ArrayList<Clazz> allClazzByCourseId = clazzRepository.findAllByCourseId(courseId);
                clazzRepository.deleteAll(allClazzByCourseId);
                Integer integer = prerequisiteRepository.deleteAllByCourseId(courseId);
                preclusionRepository.deleteAllByCourseId(courseId);

                courseRepository.deleteById(courseId);
                status = 0;

            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public Integer updateACourse(Course updatingCourse) {
        int status = -1;
        try {
            Optional<Course> courseOptional = courseRepository.findById(updatingCourse.getCourseId());
            if (courseOptional.isPresent()) {
                String newCourseName = updatingCourse.getCourseName();
                String newCourseNumber = updatingCourse.getCourseNumber();
                String courseSubject = updatingCourse.getCourseSubject();
                //System.out.println(courseSubject + ":" + newCourseNumber + "  " + newCourseName);
                if (!courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseSubject, newCourseNumber) && !courseRepository.existsCourseByCourseName(newCourseName)) {
                    courseRepository.save(updatingCourse);
                    status = 0;
                }
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public Integer newCourseNumberValidCheck(String courseProject, String courseNumber) {
        int status = -1;
        if (!courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseProject, courseNumber)) {
            status = 0;
        }
        return status;
    }

    @Override
    public Integer newCourseNameValidCheck(String courseName) {
        int status = -1;
        if (!courseRepository.existsCourseByCourseName(courseName)) {
            status = 0;
        }
        return status;
    }

    @Override
    public Map<Integer, String> getSubjects() {
        HashMap<Integer, String> subjects = new HashMap<>();
        List<Course> allCourses = courseRepository.findAll();
        for (Course course : allCourses) {
            if (!subjects.containsValue(course.getCourseSubject())) {
                subjects.put(subjects.size(), course.getCourseSubject());
            }
        }
        return subjects;
    }

    @Override
    public Integer addCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId) {
        int status = -1;
        try {
            for (Integer prerequisite : prerequisiteList) { //prerequisite must be a course and it`s had not been map with the operating course.
                if (courseRepository.existsById(prerequisite) && !prerequisiteRepository.existsPrerequisiteByCourseIdAndPrerequisiteId(courseId, prerequisite)) {
                    Prerequisite newPrerequisite = new Prerequisite(prerequisite, courseId);
                    prerequisiteRepository.save(newPrerequisite);
                    status = 0;
                }
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public ArrayList<Integer> getCoursePrerequisite(Integer courseId) {
        ArrayList<Integer> prerequisiteCourseIdList = new ArrayList<>();
        HashSet<Integer> sortedPrerequisiteCourseIdList = new HashSet<>();
        try {
            if (courseRepository.existsById(courseId)) {
                Set<Prerequisite> allPrerequisite = prerequisiteRepository.findAllByCourseId(courseId);
                for (Prerequisite prerequisite : allPrerequisite) {
                    System.out.println(prerequisite.getPrerequisiteId());
                    sortedPrerequisiteCourseIdList.add(prerequisite.getPrerequisiteId());
                }
            }
            prerequisiteCourseIdList.addAll(sortedPrerequisiteCourseIdList);
        } catch (Exception ignored) {
        }
        return prerequisiteCourseIdList;
    }

    @Override
    @Transactional
    public Integer deleteCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId) {
        int status = -1;
        try {
            Set<Prerequisite> allByCourseId = prerequisiteRepository.findAllByCourseId(courseId);
            if (!allByCourseId.isEmpty()) {
                for (Integer prerequisiteId : prerequisiteList) {
                    if (prerequisiteRepository.existsByCourseIdAndPrerequisiteId(courseId, prerequisiteId)) {
                        prerequisiteRepository.deleteByCourseIdAndPrerequisiteId(courseId, prerequisiteId);
                    }
                }
                status = 0;
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    //TODO transactional usage
    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public Integer updateCoursePrerequisite(ArrayList<Integer> updatedPrerequisiteList, Integer courseId) {
        int status = -1;
        try {
            Set<Prerequisite> allExistPrerequisite = prerequisiteRepository.findAllByCourseId(courseId);
            boolean allValidCourse = true;
            for (Integer prerequisiteId : updatedPrerequisiteList) {
                if (!courseRepository.findById(prerequisiteId).isPresent()) {
                    allValidCourse = false;
                }
            }
            if (!allExistPrerequisite.isEmpty() && allValidCourse) { //course id 5137, old 5135,5136 , new 5134,5135  now,5135
                //find out the deleted prerequisite course. and delete them from table.
                for (Prerequisite prerequisite : allExistPrerequisite) {
                    if (!updatedPrerequisiteList.contains(prerequisite.getPrerequisiteId())) {
                        prerequisiteRepository.deleteByCourseIdAndPrerequisiteId(courseId, prerequisite.getPrerequisiteId());
                    }
                }
                //add new prerequisite to the course.  6155,6180
                for (Integer prerequisite : updatedPrerequisiteList) { //prerequisite must be a course and it`s had not been map with the operating course.
                    if (courseRepository.existsById(prerequisite)) {
                        if (!prerequisiteRepository.existsPrerequisiteByCourseIdAndPrerequisiteId(courseId, prerequisite)) {
                            Prerequisite newPrerequisite = new Prerequisite(prerequisite, courseId);
                            prerequisiteRepository.save(newPrerequisite);
                            status = 0;
                        }
                    } else {
                        throw new RuntimeException("invalid course");
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            status = -1;
        }
        return status;
    }

    @Override
    public Integer addCoursePreclusion(ArrayList<Integer> preclusionList, Integer courseId) {
        int status = -1;
        try {
            for (Integer preclusion : preclusionList) {
                if (courseRepository.existsById(preclusion)) {
                    Preclusion newPreclusion = new Preclusion(preclusion, courseId);
                    preclusionRepository.save(newPreclusion);
                    status = 0;
                }
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    public ArrayList<Integer> getCoursePreclusion(Integer courseId) {
        ArrayList<Integer> preclusionCourseIdList = new ArrayList<>();
        HashSet<Integer> sortedPreclusionCourseIdList = new HashSet<>();
        try {
            if (courseRepository.existsById(courseId)) {
                Set<Preclusion> allPreclusion = preclusionRepository.findAllByCourseId(courseId);
                for (Preclusion preclusion : allPreclusion) {
                    sortedPreclusionCourseIdList.add(preclusion.getPreclusionId());
                }
            }
            preclusionCourseIdList.addAll(sortedPreclusionCourseIdList);
        } catch (Exception ignored) {
        }
        return preclusionCourseIdList;
    }

    @Override
    @Transactional
    public Integer deleteCoursePreclusion(ArrayList<Integer> preclusionList, Integer courseId) {
        int status = -1;
        try {
            Set<Preclusion> allPreclusion = preclusionRepository.findAllByCourseId(courseId);
            if (!allPreclusion.isEmpty()) {
                for (Integer preclusionId : preclusionList) {
                    if (preclusionRepository.existsPreclusionByCourseIdAndPreclusionId(courseId, preclusionId)) {
                        preclusionRepository.deleteByCourseIdAndPreclusionId(courseId, preclusionId);
                    }
                }
                status = 0;
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }

    @Override
    @Transactional
    public Integer updateCoursePreclusion(ArrayList<Integer> updatedPreclusionList, Integer courseId) {
        int status = -1;
        try {
            Set<Preclusion> allExistPreclusion = preclusionRepository.findAllByCourseId(courseId);
            boolean allValidCourse = true;
            for (Integer preclusionId : updatedPreclusionList) {
                if (courseRepository.findById(preclusionId).isEmpty()) {
                    allValidCourse = false;
                }
            }
            System.out.println("asas");
            if (!allExistPreclusion.isEmpty() && allValidCourse) {
                //find out the deleted preclusion course. and delete them from table.
                for (Preclusion preclusion : allExistPreclusion) {
                    if (!updatedPreclusionList.contains(preclusion.getPreclusionId())) {
                        System.out.println(courseId);
                        System.out.println(preclusion.getPreclusionId());
                        preclusionRepository.deleteByCourseIdAndPreclusionId(courseId, preclusion.getPreclusionId());
                    }
                }
                //add new preclusion to the course.
                for (Integer preclusion : updatedPreclusionList) { //preclusion must be a course and it`s had not been map with the operating course.
                    if (courseRepository.existsById(preclusion)) {
                        if (!preclusionRepository.existsPreclusionByCourseIdAndPreclusionId(courseId, preclusion)) {
                            Preclusion newPreclusion = new Preclusion(preclusion, courseId);
                            preclusionRepository.save(newPreclusion);
                            status = 0;
                        }
                    } else {
                        throw new Exception("invalid course");
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            status = -1;
        }
        return status;
    }
}
