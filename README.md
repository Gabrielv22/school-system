# School System RESTful API


 **Monolith** school grading system application for managing students/courses/scores. 
Technologies used:
 - **Java**
 - **Spring Security**
 - **PostgreSQL**
 - **Spring Data**
 - **Hibernate**
 - **JUnit5**
 - **Mockito**
 - **Docker**
 - **Maven**

## Authentication
Security layer - **JSON Web Token** used as authentication point.
  More about on  [JWT](https://jwt.io/) 


## Authorization

**User Roles**: 
 - TEACHER
 - STUDENT

**Teacher** can manage the data of all objects in the system (get, add, edit, delete
students, courses, marks)

**Student** can only get the data about themselves

## API
Various endpoints for:

 - creating
 - deleting
 - updating
 - reading
 **Students** - **Courses** - **Scores**
 
A **Student** attends multiple courses.
A **Course** is attended by multiple students.
Each **Student** has multiple marks for each course.

## CSV API

Importing  database tables - values in form of **.csv** file
