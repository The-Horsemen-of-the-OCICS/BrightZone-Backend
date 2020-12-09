package com.carleton.comp5104.cms.controller;

import com.carleton.comp5104.cms.entity.Deliverable;
import com.carleton.comp5104.cms.service.DeliverableService;
import com.carleton.comp5104.cms.service.impl.ProfessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
class DBPopulateController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DeliverableService deliverableService;

    @Autowired
    ProfessorService professorService;

    @GetMapping(path = "/populate")
    public void populate() throws IOException {
        populateFaculty();
        populatePeople();
        populateCourse();
        populateRoom();
        populateClass();
        populateTestData();
        populateCypressData();
    }

    public void populateFaculty() throws IOException {
        String file = "src/main/resources/database/faculties.txt";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String currentLine = reader.readLine();
        while (currentLine != null) {
            String sql = "INSERT INTO cms.faculty (faculty_name) VALUES (?)";
            jdbcTemplate.update(sql, currentLine);
            currentLine = reader.readLine();
        }
        reader.close();
    }

    public void populatePeople() throws IOException {
        int admin_base = 1000000;
        int professor_base = 2000000;
        int student_base = 3000000;
        Random rand = new Random();

        String ahs_programs = "src/main/resources/database/ahs_programs.txt";
        String arts_programs = "src/main/resources/database/arts_programs.txt";
        String eng_programs = "src/main/resources/database/eng_programs.txt";
        String env_programs = "src/main/resources/database/env_programs.txt";
        String math_programs = "src/main/resources/database/math_programs.txt";
        String sci_programs = "src/main/resources/database/sci_programs.txt";
        String[] program_category = {ahs_programs, arts_programs, eng_programs, env_programs, math_programs, sci_programs};

        List<List<String>> programs = new ArrayList<>();

        for (String p : program_category) {
            BufferedReader reader = new BufferedReader(new FileReader(p));
            List<String> lines = new ArrayList<String>();

            String currentLine = reader.readLine();
            while (currentLine != null) {
                lines.add(currentLine);
                currentLine = reader.readLine();
            }
            reader.close();
            programs.add(lines);
        }

        int count = 0;
        String names = "src/main/resources/database/names.txt";
        BufferedReader namereader = new BufferedReader(new FileReader(names));
        String curName = namereader.readLine();
        while (curName != null) {
            Integer id = student_base + count - 105;
            String type = "student";
            int faculty_id = rand.nextInt(program_category.length - 1) + 2;

            if (count < 5) {
                id = admin_base + count;
                type = "administrator";
                faculty_id = 1;
            } else if (count >= 5 && count < 105) {
                id = professor_base + count - 5;
                type = "professor";
            }
            String program_name = faculty_id == 1 ? "Not Applicable" : programs.get(faculty_id - 2).get(rand.nextInt(programs.get(faculty_id - 2).size()));
            String email = curName.replaceAll("\\s+", "").toLowerCase() + "@uottawa.ca";
            String password = "123456";
            String account_status = "current";
            Timestamp last_login = new Timestamp(1000000000);
            Integer gender = rand.nextInt(3) + 1;

            String person_sql = "INSERT INTO cms.person VALUES (?,?,?,?,?,?,?)";  // populate table peron first
            jdbcTemplate.update(person_sql, id.toString(), curName, type, faculty_id, program_name, gender, email);

            if (count < 300) {  // populate table account after peron, and table account should have less records than table person at the beginning
                String account_sql = "INSERT INTO cms.account VALUES (?,?,?,?,?,?,?,?,?,?)";
                if (count >= 250) {  // set last 50 account_status as unauthorized
                    account_status = "unauthorized";
                }
                if (id < 2000090 || id > 2000099) {
                    // don't populate last 10 professors
                    jdbcTemplate.update(account_sql, id.toString(), curName, type, faculty_id, program_name, email, password, account_status, last_login, "NONE");
                }
            }

            curName = namereader.readLine();
            count += 1;
        }
        namereader.close();

    }

    public void populateCourse() throws IOException {
        Integer course_base = 1000;
        String file = "src/main/resources/database/courses.csv";
        List<List<String>> records = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String currentLine = reader.readLine();
        Integer count = 0;
        while (currentLine != null) {
            String[] values = currentLine.split(",");
            Integer course_id = course_base + count;
            String course_subj = values[0];
            String course_number = values[1];
            String course_name = values[2];
            String sql = "INSERT INTO cms.course VALUES (?,?,?,?,?,?)";
            jdbcTemplate.update(sql, course_id, course_subj, course_number, course_name, "This course is about " + course_name, 3.0);
            currentLine = reader.readLine();
            count += 1;
        }
    }

    public void populateRoom() {
        String[] capacity = {"30", "50", "70", "100", "200"};
        int[] roomCapacity = {30, 50, 70, 100, 200};
        Random rand = new Random();
        for (int i = 0; i < 100; ++i) {
            String sql = "INSERT INTO cms.classroom VALUES (?, ? ,?)";
            jdbcTemplate.update(sql, i, roomCapacity[rand.nextInt(5)], "This is a room with maximum capacity of " + roomCapacity[rand.nextInt(5)]);
        }
    }

    public void populateClass() {
        //CS XXX courses
        String course_sql = "SELECT * FROM course WHERE course_subject='CS'";
        List<Map<String, Object>> courseList = jdbcTemplate.queryForList(course_sql);

        String room_sql = "SELECT * FROM classroom;";
        List<Map<String, Object>> roomList = jdbcTemplate.queryForList(room_sql);

        //String[] types = {"once_per_week", "twice_per_week", "three_times_per_week"};
        Random rand = new Random();
        int prof_base = 2000000;
        int class_base = 1000;
        int count = 0;
        for (Map<String, Object> m : courseList) {
            int class_id = count + class_base;
            int course_id = (int) m.get("course_id");
            String class_desc = "This is a class for course <" + (String) m.get("course_subject") + (String) m.get("course_number") + ">";
            String class_status = "open";
            int section = 1;
            int enrolled = 0;
            int room_capacity = (int) roomList.get(count).get("room_capacity");
            int prof_id = count + prof_base;
//            String scheduled_type = types[rand.nextInt(3)];
//            Time start_time = Time.valueOf("08:30:00");
//            Time end_time = Time.valueOf("10:30:00");
//            Integer room_id = count;
            Timestamp enroll_deadline = Timestamp.valueOf("2020-12-24 08:30:00.000");
            Timestamp nopen_deadline = Timestamp.valueOf("2021-01-24 08:30:00.000");
            Timestamp nofail_deadline = Timestamp.valueOf("2021-03-24 08:30:00.000");

//            int days_of_week = 1;
//            if (scheduled_type == "once_per_week") {
//                days_of_week = rand.nextInt(5) + 1;
//            } else if (scheduled_type == "twice_per_week") {
//                days_of_week = rand.nextInt(2) + 1;
//            }

            String sql = "INSERT INTO cms.class VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, class_id, course_id, class_desc, class_status, section, enrolled, room_capacity, prof_id, enroll_deadline, nopen_deadline, nofail_deadline);
            count += 1;
        }
    }

    public void populateTestData() throws IOException {
        /* ----- Data required for <DeliverableServiceTest> and <Submit Final Grade> ----- */
        String sql = "INSERT INTO cms.enrollment VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, 3000182, 1032, "ongoing", 0, Timestamp.valueOf("2020-12-01 08:30:00.000"), null);
        sql = "INSERT INTO cms.enrollment VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, 3000112, 1032, "ongoing", 0, Timestamp.valueOf("2020-12-01 08:30:00.000"), null);

        Deliverable newDeliverable_1 = new Deliverable();
        newDeliverable_1.setClassId(1032);
        newDeliverable_1.setDead_line(Timestamp.valueOf("2022-10-24 10:10:10.0"));
        newDeliverable_1.setDesc("Assignment 1");
        newDeliverable_1.setPercent(0.25f);
        int newId_1 = professorService.submitDeliverable(newDeliverable_1);

        Deliverable newDeliverable_2 = new Deliverable();
        newDeliverable_2.setClassId(1032);
        newDeliverable_2.setDead_line(Timestamp.valueOf("2022-12-24 10:10:10.0"));
        newDeliverable_2.setDesc("Assignment 2");
        newDeliverable_2.setPercent(0.14f);
        int newId_2 = professorService.submitDeliverable(newDeliverable_2);
        /* -----     End of data required     ----- */

    }

    public void populateCypressData() throws IOException {
        /* ----- Data required for <Grade A submission> and <Submit Final Grade> ----- */
        String sql = "INSERT INTO cms.enrollment VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, 3000001, 1006, "ongoing", 0, Timestamp.valueOf("2020-12-01 08:30:00.000"), null);
        sql = "INSERT INTO cms.enrollment VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, 3000002, 1006, "ongoing", 0, Timestamp.valueOf("2020-12-01 08:30:00.000"), null);

        Deliverable newDeliverable_1 = new Deliverable();
        newDeliverable_1.setClassId(1006);
        newDeliverable_1.setDead_line(Timestamp.valueOf("2022-10-24 10:10:10.0"));
        newDeliverable_1.setDesc("Assignment 1");
        newDeliverable_1.setPercent(0.25f);
        int newId_1 = professorService.submitDeliverable(newDeliverable_1);

        Deliverable newDeliverable_2 = new Deliverable();
        newDeliverable_2.setClassId(1006);
        newDeliverable_2.setDead_line(Timestamp.valueOf("2022-12-24 10:10:10.0"));
        newDeliverable_2.setDesc("Assignment 2");
        newDeliverable_2.setPercent(0.14f);
        int newId_2 = professorService.submitDeliverable(newDeliverable_2);

        MultipartFile file
                = new MockMultipartFile(
                "file",
                "myAssignment.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        deliverableService.submitDeliverable(3000001, newId_1, file, "This is myA1");
        deliverableService.submitDeliverable(3000002, newId_1, file, "This is myA1");
        deliverableService.submitDeliverable(3000001, newId_2, file, "This is myA2");
        deliverableService.submitDeliverable(3000002, newId_2, file, "This is myA2");
        /* -----     End of data required     ----- */

    }

}
