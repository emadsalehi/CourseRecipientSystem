package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerUtils serverUtils = new ServerUtils();
            ServerSocket serverSocket = new ServerSocket(1234);
            Socket socket = serverSocket.accept();
            System.out.println("Connected");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            while (true) {
                String query = bufferedReader.readLine();
                String queryInput = "";
                int openParIndex = query.indexOf('(');
                int closeParIndex = query.indexOf(')');
                if (openParIndex != -1 && closeParIndex != -1)
                    queryInput = query.substring(openParIndex + 1, closeParIndex);
                if (query.equals("LoadFile")) {
                    serverUtils.loadCourseDetails();
                    outputStreamWriter.write("Database file loaded\n");
                    outputStreamWriter.write("Enter \'Login(username, password)\' to enter!\n");
                    outputStreamWriter.write("or\n");
                    outputStreamWriter.write("Enter \'SingUp(username, password)\' to create account\n");
                    outputStreamWriter.write("Enter \'Help\' to get help!\n");
                    outputStreamWriter.flush();
                } else if (query.contains("SingUp")) {
                    String[] params = queryInput.split(",");
                    boolean result = serverUtils.addUser(params[0].trim(), params[1].trim());
                    if (result) {
                        outputStreamWriter.write("Account created!\n");
                        outputStreamWriter.flush();
                    } else {
                        outputStreamWriter.write("Username already taken!\n");
                        outputStreamWriter.flush();
                    }
                    System.out.println("SignUp Done");
                } else if (query.contains("Login")) {
                    String[] params = queryInput.split(",");
                    boolean result = serverUtils.login(params[0].trim(), params[1].trim());
                    if (result) {
                        outputStreamWriter.write("Hi " + params[0].trim() + "!, You are logged in.\n");
                        outputStreamWriter.flush();
                    } else {
                        outputStreamWriter.write("Wrong username or password!\n");
                        outputStreamWriter.flush();
                    }
                    System.out.println("Login Done");
                } else if (query.equals("Help")) {
                    outputStreamWriter.write("Enter \'Help\' to get help!\n");
                    outputStreamWriter.flush();
                } else {
                    if (!serverUtils.isUserActive()) {
                        outputStreamWriter.write("You must first login.\n");
                        outputStreamWriter.flush();
                        continue;
                    }
                    if (query.equals("ViewList")) {
                        String courseList = serverUtils.getCourseList();
                        outputStreamWriter.write(courseList);
                        outputStreamWriter.flush();
                    } else if (query.contains("TakeCourse")) {
                        CourseResponse response = serverUtils.takeCourse(queryInput);
                        sendMessageByResponse(outputStreamWriter, response);
                    } else if (query.contains("DropCourse")) {
                        CourseResponse response = serverUtils.dropCourse(queryInput);
                        sendMessageByResponse(outputStreamWriter, response);
                    } else if (query.equals("MyCourse")) {
                        outputStreamWriter.write(serverUtils.showUserCourses());
                        outputStreamWriter.flush();
                    } else if (query.equals("Logout")) {
                        serverUtils.logout();
                        outputStreamWriter.write("You signed out. Bye!\n");
                        outputStreamWriter.flush();
                    } else {
                        outputStreamWriter.write("WRONG COMMAND\n");
                        outputStreamWriter.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageByResponse(OutputStreamWriter outputStreamWriter, CourseResponse response) throws IOException {
        switch (response) {
            case NOT_FOUND:
                outputStreamWriter.write("Course not found.\n");
                outputStreamWriter.flush();
                break;
            case FULL:
                outputStreamWriter.write("Course is full.\n");
                outputStreamWriter.flush();
                break;
            case DONE:
                outputStreamWriter.write("Course successfully added!\n");
                outputStreamWriter.flush();
                break;
        }
    }
}
