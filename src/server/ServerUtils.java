package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ServerUtils {

    private static final String FILE_ADDRESS = "database.txt";
    private List<CourseDetail> courseDetails = new ArrayList<>();

    void loadCourseDetails() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_ADDRESS));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                courseDetails.add(new CourseDetail(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
