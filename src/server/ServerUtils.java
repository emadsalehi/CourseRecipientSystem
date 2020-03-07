package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ServerUtils {

    private static final String FILE_ADDRESS = "database.txt";
    private List<CourseDetail> courseDetails = new ArrayList<>();
    private List<UserDetail> users = new ArrayList<>();
    private UserDetail activeUser = null;

    void loadCourseDetails() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_ADDRESS));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                courseDetails.add(new CourseDetail(line));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean addUser(String username, String password) {
        for (UserDetail user : users) {
            if (user.getUserName().equals(username))
                return false;
        }
        users.add(new UserDetail(username, password));
        return true;
    }

    boolean login(String username, String password) {
        for (UserDetail user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                activeUser = user;
                return true;
            }
        }
        return false;
    }

    boolean isUserActive() {
        return activeUser != null;
    }

    String getCourseList() {
        StringBuilder courseListBuilder = new StringBuilder();
        for (CourseDetail course : courseDetails)
            courseListBuilder.append(course.toString()).append("\n");
        return courseListBuilder.toString();
    }

    CourseResponse takeCourse(String courseName) {
        for (CourseDetail course : courseDetails) {
            if (course.getCourseName().equals(courseName)) {
                if (course.getCourseCapacity() == course.getRegistrationCount()) {
                    return CourseResponse.FULL;
                } else {
                    course.register();
                    activeUser.addUserCourse(course);
                    updateDatabaseFile();
                    return CourseResponse.DONE;
                }
            }
        }
        return CourseResponse.NOT_FOUND;
    }

    CourseResponse dropCourse(String courseName) {
        for (CourseDetail course : courseDetails) {
            if (course.getCourseName().equals(courseName)) {
                if (course.getCourseCapacity() == course.getRegistrationCount()) {
                    return CourseResponse.FULL;
                } else {
                    CourseResponse response = activeUser.removeUserCourse(course);
                    if (response == CourseResponse.DONE) {
                        course.drop();
                        updateDatabaseFile();
                    }
                    return response;
                }
            }
        }
        return CourseResponse.NOT_FOUND;
    }

    String showUserCourses() {
        StringBuilder courseListBuilder = new StringBuilder();
        for (CourseDetail course : activeUser.getUserCourses())
            courseListBuilder.append(course.toString()).append("\n");
        return courseListBuilder.toString();
    }

    private void updateDatabaseFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_ADDRESS, false));
            StringBuilder courseListBuilder = new StringBuilder();
            for (CourseDetail course : courseDetails)
                courseListBuilder.append(course.toString()).append("\n");
            bufferedWriter.write(courseListBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
