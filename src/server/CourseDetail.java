package server;

class CourseDetail {

    private String courseName;
    private int registrationCount;
    private int courseCapacity;
    private int courseStartHour;
    private int courseEndHour;

    CourseDetail(String databaseLine) {
        String[] params = databaseLine.split("\t+");
        courseName = params[0];
        registrationCount = Integer.parseInt(params[1]);
        courseCapacity = Integer.parseInt(params[2]);
        courseStartHour = Integer.parseInt(params[3].split("-")[0]);
        courseEndHour = Integer.parseInt(params[3].split("-")[1]);
    }
}
