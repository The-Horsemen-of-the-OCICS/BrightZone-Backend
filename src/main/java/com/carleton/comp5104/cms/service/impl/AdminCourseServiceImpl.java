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
    public Integer getCourseTableSize() {
        return courseRepository.findAll().size();
    }

    @Override
    public Course getLastCourse() {
        List<Course> all = courseRepository.findAll();
        return all.get(all.size() - 1);
    }


    @Override
    public Course getCourseById(Integer courseId) {
        Optional<Course> byId = courseRepository.findById(courseId);
        return byId.orElse(null);
    }

    @Override
    public Integer addNewCourse(Course newCourse) {
        int status = -1;
        this.newCourseNameValidCheck(newCourse.getCourseName());
        try {
            String courseSubject = newCourse.getCourseSubject();
            String courseNumber = newCourse.getCourseNumber();
            String newCourseName = newCourse.getCourseName();
            if (!courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseSubject, courseNumber) && !courseRepository.existsCourseByCourseName(newCourseName)) {
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
                Course course = courseOptional.get();
                String newCourseName = updatingCourse.getCourseName();
                String newCourseNumber = updatingCourse.getCourseNumber();
                String courseSubject = updatingCourse.getCourseSubject();
                if (!newCourseName.equals(course.getCourseName())) {
                    if (courseRepository.existsCourseByCourseName(newCourseName)) {
                        return status;
                    }
                } else if (!newCourseNumber.equals(course.getCourseNumber())) {
                    if (courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseSubject, newCourseNumber)) {
                        return status;
                    }
                } else {
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
    public Integer newCourseNumberValidCheck(String courseSubject, String courseNumber) {
        int status = -1;
        if (!courseRepository.existsCourseByCourseSubjectAndCourseNumber(courseSubject, courseNumber)) {
            status = 0;
        }
        return status;
    }

    @Override
    public Course getCourseBySubjectAndNumber(String courseSubject, String courseNumber) {
        return courseRepository.findByCourseSubjectAndCourseNumber(courseSubject, courseNumber);
    }

    @Override
    public ArrayList<Course> getCourseBySubject(String courseSubject) {
        return courseRepository.findAllByCourseSubject(courseSubject);
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
    public ArrayList<HashMap<String, String>> getSubjects() {
        ArrayList<HashMap<String, String>> subjectList = new ArrayList<>();
        HashMap<Integer, String> subjects = new HashMap<>();
        List<Course> allCourses = courseRepository.findAll();
        for (Course course : allCourses) {
            HashMap<String, String> tempMap = new HashMap<>();
            tempMap.put("courseSubject", course.getCourseSubject());
            if (!subjectList.contains(tempMap)) {
                subjectList.add(tempMap);
            }
        }
        return subjectList;
    }

    @Override
    public Integer addCoursePrerequisite(ArrayList<Integer> prerequisiteList, Integer courseId) {
        int status = -1;
        try {
            System.out.println(prerequisiteList.get(0));
            System.out.println(courseId);
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
    public List<Course> getCoursePrerequisite(Integer courseId) {
        HashSet<Integer> sortedPrerequisiteCourseIdList = new HashSet<>();
        List<Course> allPrerequisiteCourse = null;
        try {
            if (courseRepository.existsById(courseId)) {

                Set<Prerequisite> allPrerequisite = prerequisiteRepository.findAllByCourseId(courseId);

                for (Prerequisite prerequisite : allPrerequisite) {
                    sortedPrerequisiteCourseIdList.add(prerequisite.getPrerequisiteId());
                }
            }
            ArrayList<Integer> prerequisiteCourseIdList = new ArrayList<>(sortedPrerequisiteCourseIdList);
            allPrerequisiteCourse = courseRepository.findAllById(prerequisiteCourseIdList);
        } catch (Exception ignored) {
        }

        return allPrerequisiteCourse;
    }

    @Override
    @Transactional
    public Integer deleteCoursePrerequisite(Integer courseId, Integer prerequisiteId) {
        int status = -1;
        try {
            if (prerequisiteRepository.existsPrerequisiteByCourseIdAndPrerequisiteId(courseId, prerequisiteId)) {
                Integer integer = prerequisiteRepository.deleteByCourseIdAndPrerequisiteId(courseId, prerequisiteId);
                if (integer > 0) {
                    status = 0;
                }
            }
        } catch (Exception exception) {
            status = -1;
        }
        return status;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class}, propagation = Propagation.REQUIRED)
    public Integer updateCoursePrerequisite(ArrayList<Integer> updatedPrerequisiteList, Integer courseId) throws Exception {
        int status = -1;

        Set<Prerequisite> allExistPrerequisite = prerequisiteRepository.findAllByCourseId(courseId);
        for (Integer prerequisiteId : updatedPrerequisiteList) {
            if (courseRepository.findById(prerequisiteId).isEmpty()) {
                return -1;
            }
        }
        if (!allExistPrerequisite.isEmpty()) { //course id 5137, old 5135,5136 , new 5134,5135  now,5135
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
                    throw new Exception("invalid course");
                }
            }
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
    public List<Course> getCoursePreclusion(Integer courseId) {
        HashSet<Integer> sortedPreclusionCourseIdList = new HashSet<>();
        List<Course> allPreclusionCourse = null;
        try {
            if (courseRepository.existsById(courseId)) {
                Set<Preclusion> allPreclusion = preclusionRepository.findAllByCourseId(courseId);
                for (Preclusion preclusion : allPreclusion) {
                    sortedPreclusionCourseIdList.add(preclusion.getPreclusionId());
                }
            }
            ArrayList<Integer> preclusionCourseIdList = new ArrayList<>(sortedPreclusionCourseIdList);
            allPreclusionCourse = courseRepository.findAllById(preclusionCourseIdList);
        } catch (Exception ignored) {
        }
        return allPreclusionCourse;
    }

    @Override
    @Transactional
    public Integer deleteCoursePreclusion(Integer courseId, Integer preclusionId) {
        int status = -1;
        try {
            if (preclusionRepository.existsPreclusionByCourseIdAndPreclusionId(courseId, preclusionId)) {
                Integer integer = preclusionRepository.deleteByCourseIdAndPreclusionId(courseId, preclusionId);
                if (integer > 0) {
                    status = 0;
                }
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
