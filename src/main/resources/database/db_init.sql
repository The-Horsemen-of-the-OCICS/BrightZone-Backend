drop database cms;
create database cms;
drop user 'cmsadmin'@'localhost';
flush privileges;
create user 'cmsadmin'@'localhost' identified by '123456';
grant all privileges on cms.* to cmsadmin@localhost;
USE cms;

CREATE TABLE `account` (
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

CREATE TABLE `person` (
                          `person_id` int primary key,
                          `name` varchar(255),
                          `type` ENUM ('student', 'professor', 'teaching_assistant', 'administrator'),
                          `faculty_id` int,
                          `program` varchar(255),
                          `gender` ENUM ('male', 'female', 'other'),
                          `email` varchar(255)
);

CREATE TABLE `faculty` (
                           `faculty_id` int primary key auto_increment,
                           `faculty_name` varchar(255)
);

CREATE TABLE `course` (
                          `course_id` int primary key,
                          `course_subject` varchar(255),
                          `course_number` varchar(255),
                          `course_name` varchar(255),
                          `course_desc` varchar(255),
                          `credit` int
);

CREATE TABLE `prerequisite` (
                                 `course_id` int,
                                 `prerequisite_id` int
);

CREATE TABLE `preclusion` (
                               `course_id` int,
                               `preclusion_id` int
);

CREATE TABLE `class` (
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

CREATE TABLE `class_room` (
                              `room_id` int primary key,
                              `room_capacity` int,
                              `room_desc` varchar(255)
);

CREATE TABLE `enrollment` (
                              `student_id` int,
                              `class_id` int,
                              `status` ENUM ('ongoing', 'passed', 'dropped', 'dropped_dr','failed'),
                              `final_grade` float,
                              `enroll_time` timestamp,
                              `drop_time` timestamp
);

CREATE TABLE `deliverable` (
                               `deliverable_id` int primary key NOT NULL AUTO_INCREMENT,
                               `class_id` int,
                               `dead_line` timestamp,
                               `deliverable_desc` varchar(255),
                               `percent` float,
                               `is_notified` boolean
);

CREATE TABLE `submission` (
                              `submission_id` int primary key NOT NULL AUTO_INCREMENT,
                              `submit_time` timestamp,
                              `file_name` varchar(255),
                              `student_id` int,
                              `deliverable_id` int,
                              `submission_desc` varchar(255),
                              `grade` float
);

CREATE TABLE `request` (
                           `request_id` int primary key NOT NULL AUTO_INCREMENT,
                           `user_id` int,
                           `type` ENUM ('enroll', 'drop', 'create_course', 'cancel_course', 'assign_prof'),
                           `status` ENUM ('open', 'fulfilled', 'declined'),
                           `message` varchar(255)
);

ALTER TABLE `account` ADD FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`);

ALTER TABLE `account` ADD FOREIGN KEY (`user_id`) REFERENCES `person` (`person_id`);

ALTER TABLE `person` ADD FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`faculty_id`);

ALTER TABLE `preclusion` ADD FOREIGN KEY (`preclusion_id`) REFERENCES `course` (`course_id`);

ALTER TABLE `preclusion` ADD FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`);

ALTER TABLE `prerequisite` ADD FOREIGN KEY (`prerequisite_id`) REFERENCES `course` (`course_id`);

ALTER TABLE `prerequisite` ADD FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`);

ALTER TABLE `class` ADD FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`);

ALTER TABLE `class` ADD FOREIGN KEY (`prof_id`) REFERENCES `account` (`user_id`);

ALTER TABLE `class` ADD FOREIGN KEY (`room_id`) REFERENCES `class_room` (`room_id`);

ALTER TABLE `enrollment` ADD FOREIGN KEY (`student_id`) REFERENCES `account` (`user_id`);

ALTER TABLE `enrollment` ADD FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`);

ALTER TABLE `deliverable` ADD FOREIGN KEY (`class_id`) REFERENCES `class` (`class_id`);

ALTER TABLE `request` ADD FOREIGN KEY (`user_id`) REFERENCES `account` (`user_id`);

ALTER TABLE `submission` ADD FOREIGN KEY (`deliverable_id`) REFERENCES `deliverable` (`deliverable_id`);

ALTER TABLE `submission` ADD FOREIGN KEY (`student_id`) REFERENCES `account` (`user_id`);


