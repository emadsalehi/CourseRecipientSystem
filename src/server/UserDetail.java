package server;

import java.util.ArrayList;
import java.util.List;

public class UserDetail {

    private String userName;
    private String password;
    private List<CourseDetail> userCourses = new ArrayList<>();

    UserDetail(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    void addUserCourse(CourseDetail course) {
        userCourses.add(course);
    }

    String getUserName() {
        return userName;
    }

    String getPassword() {
        return password;
    }
}
