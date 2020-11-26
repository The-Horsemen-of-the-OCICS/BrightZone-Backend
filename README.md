# comp5104-cms

## 1. database setting
```sql
CREATE DATABASE cms;
create user 'cmsadmin'@'localhost' identified by '123456';
grant all privileges on cms.* to cmsadmin@localhost;
```

## 2. database populating
Database can now be populated by randomly generated data for testing purpose.
The resulting database with default setting consists of：
- 5 Admin, 100 Professors, 500 Students and their corresponding Accounts
- 6 Faculties
- 5158 Courses
- 100 Class_Room with random capacity
- 89 Classes with random schedule

## 3. create deliverable folder
```shell script
sudo mkdir /data
sudo chmod -R 777 /data
```
Usage:
- Run CmsApplication
- Open localhost:8080/populate
- Wait until `DB populated successfully` is shown
