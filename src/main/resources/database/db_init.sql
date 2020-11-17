drop database cms;
create database cms;
drop user 'cmsadmin'@'localhost';
flush privileges;
create user 'cmsadmin'@'localhost' identified by '123456';
grant all privileges on cms.* to cmsadmin@localhost;
USE cms;

CREATE TABLE `Account` (
                           `user_id` int primary key,
                           `name` varchar(255),
                           `type` ENUM ('student', 'professor', 'teaching_assistant', 'administrator'),
                           `faculty_id` int,
                           `program` varchar(255),
                           `email` varchar(255),
                           `password` varchar(255),
                           `account_status` ENUM ( 'unauthorized', 'expelled', 'current', 'sabbatical', 'alumni'),
                           `last_login` timestamp,
                           `verification_code` varchar(255)
);

CREATE TABLE `Person` (
                          `person_id` int primary key,
                          `name` varchar(255),
                          `type` ENUM ('student', 'professor', 'teaching_assistant', 'administrator'),
                          `faculty_id` int,
                          `program` varchar(255),
                          `gender` ENUM ('male', 'female', 'other'),
                          `email` varchar(255)
);

CREATE TABLE `Faculty` (
                           `faculty_id` int primary key auto_increment,
                           `faculty_name` varchar(255)
);

CREATE TABLE `Course` (
                          `course_id` int primary key,
                          `course_subject` varchar(255),
                          `course_number` varchar(255),
                          `course_name` varchar(255),
                          `course_desc` varchar(255),
                          `credit` int
);

CREATE TABLE `Pre_Requisite` (
                                 `course_id` int,
                                 `pre_requisite_id` int
);

CREATE TABLE `Pre_Clusion` (
                               `course_id` int,
                               `pre_clusion_id` int
);

CREATE TABLE `Class` (
                         `class_id` int primary key,
                         `course_id` int,
                         `class_desc` varchar(255),
                         `class_status` ENUM ('open', 'close', 'cancel'),
                         `section` int,
                         `enrolled` int,
                         `enroll_capacity` int,
                         `prof_id` int,
                         `schedule_type` ENUM ('once_per_week', 'twice_per_week', 'three_times_per_week'),
                         `start_day_of_week` ENUM ('Mon', 'Tues', 'Wed', 'Thurs', 'Fri'),
                         `start_time` time,
                         `end_time` time,
                         `room_id` int,
                         `enroll_deadline` timestamp,
                         `drop_no_penalty_deadline` timestamp,
                         `drop_no_fail_deadline` timestamp
);

CREATE TABLE `Class_Room` (
                              `room_id` int primary key,
                              `room_capacity` int,
                              `room_desc` varchar(255)
);

CREATE TABLE `Enrollment` (
                              `student_id` int,
                              `class_id` int,
                              `status` ENUM ('ongoing', 'passed', 'dropped', 'failed'),
                              `final_grade` float,
                              `enroll_time` timestamp,
                              `drop_time` timestamp
);

CREATE TABLE `Deliverable` (
                               `deliverable_id` int primary key NOT NULL AUTO_INCREMENT,
                               `class_id` int,
                               `dead_line` timestamp,
                               `deliverable_desc` varchar(255),
                               `percent` float,
                               `is_notified` boolean
);

CREATE TABLE `Submission` (
                              `submission_id` int primary key NOT NULL AUTO_INCREMENT,
                              `submit_time` timestamp,
                              `file_name` varchar(255),
                              `student_id` int,
                              `deliverable_id` int,
                              `submission_desc` varchar(255),
                              `grade` float
);

CREATE TABLE `Request` (
                           `request_id` int primary key NOT NULL AUTO_INCREMENT,
                           `user_id` int,
                           `type` ENUM ('enroll', 'drop', 'create_course', 'cancel_course', 'assign_prof'),
                           `status` ENUM ('open', 'fulfilled', 'declined')
);

ALTER TABLE `Account` ADD FOREIGN KEY (`faculty_id`) REFERENCES `Faculty` (`faculty_id`);

ALTER TABLE `Account` ADD FOREIGN KEY (`user_id`) REFERENCES `Person` (`person_id`);

ALTER TABLE `Person` ADD FOREIGN KEY (`faculty_id`) REFERENCES `Faculty` (`faculty_id`);

ALTER TABLE `Pre_Clusion` ADD FOREIGN KEY (`pre_clusion_id`) REFERENCES `Course` (`course_id`);

ALTER TABLE `Pre_Clusion` ADD FOREIGN KEY (`course_id`) REFERENCES `Course` (`course_id`);

ALTER TABLE `Pre_Requisite` ADD FOREIGN KEY (`pre_requisite_id`) REFERENCES `Course` (`course_id`);

ALTER TABLE `Pre_Requisite` ADD FOREIGN KEY (`course_id`) REFERENCES `Course` (`course_id`);

ALTER TABLE `Class` ADD FOREIGN KEY (`course_id`) REFERENCES `Course` (`course_id`);

ALTER TABLE `Class` ADD FOREIGN KEY (`prof_id`) REFERENCES `Account` (`user_id`);

ALTER TABLE `Class` ADD FOREIGN KEY (`room_id`) REFERENCES `Class_Room` (`room_id`);

ALTER TABLE `Enrollment` ADD FOREIGN KEY (`student_id`) REFERENCES `Account` (`user_id`);

ALTER TABLE `Enrollment` ADD FOREIGN KEY (`class_id`) REFERENCES `Class` (`class_id`);

ALTER TABLE `Deliverable` ADD FOREIGN KEY (`class_id`) REFERENCES `Class` (`class_id`);

ALTER TABLE `Request` ADD FOREIGN KEY (`user_id`) REFERENCES `Account` (`user_id`);

ALTER TABLE `Submission` ADD FOREIGN KEY (`deliverable_id`) REFERENCES `Deliverable` (`deliverable_id`);

ALTER TABLE `Submission` ADD FOREIGN KEY (`student_id`) REFERENCES `Account` (`user_id`);

