package com.jalil.campusjobmanagement.Model;

public class EmployeeInfo {

    String name;
    String age;
    String dept;
    String year;
    String semester;

    String mobileNum;
    String email;

    String id;

    public EmployeeInfo ()
    {

    }

    public EmployeeInfo(String name, String age, String dept, String year, String semester, String mobileNum, String email, String id) {
        this.name = name;
        this.age = age;
        this.dept = dept;
        this.year = year;
        this.semester = semester;
        this.mobileNum = mobileNum;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
