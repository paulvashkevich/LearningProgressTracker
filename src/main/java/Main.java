import tracker.LearningPlatform;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Learning Progress Tracker");
        LearningPlatform learningPlatform = LearningPlatform.getInstance();
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                System.out.println("Bye!");
                break;
            }
            if (command.isBlank()) {
                System.out.println("No input.");
                continue;
            }
            learningPlatformService(learningPlatform, command);
        }
    }


    private static void learningPlatformService(LearningPlatform learningPlatform, String command) {
        switch (command) {
            case "back":
                System.out.println("Enter 'exit' to exit the program.");
                break;
            case "add students":
                addStudent(learningPlatform);
                break;
            case "list":
                listStudents(learningPlatform);
                break;
            case "add points":
                addPoints(learningPlatform);
                break;
            case "find":
                findStudent(learningPlatform);
                break;
            case "statistics":
                retrieveStatistic(learningPlatform);
                break;
            case "notify":
                notifyStudentIfCourseComplete(learningPlatform);
                break;
            default:
                System.out.println("Unknown command!");
                break;
        }
    }

    private static void notifyStudentIfCourseComplete(LearningPlatform learningPlatform) {
        while (true) {
            learningPlatform.notifyStudentByEmailIfCourseComplete();
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                return;
            }
        }
    }

    private static void addStudent(LearningPlatform learningPlatform) {
        System.out.println("Enter student credentials or 'back' to return");
        int numOfAddedStudents = 0;
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                System.out.printf("Total %d students have been added.%n", numOfAddedStudents);
                return;
            }
            if (learningPlatform.registerStudentToPlatform(userInput)) {
                numOfAddedStudents += 1;
            }
        }
    }

    private static void addPoints(LearningPlatform learningPlatform) {
        System.out.println("Enter an id and points or 'back' to return");
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                return;
            }
            learningPlatform.addPoints(userInput);
        }
    }

    private static void findStudent(LearningPlatform learningPlatform) {
        System.out.println("Enter an id or 'back' to return");
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                return;
            }
            learningPlatform.printStudentPointsInAllPlatformCourses(userInput);
        }
    }

    private static void listStudents(LearningPlatform learningPlatform) {
        learningPlatform.listStudentsRegisteredOnPlatform();
    }

    private static void retrieveStatistic(LearningPlatform learningPlatform) {
        System.out.println("Type the name of a course to see details or 'back' to quit:");
        learningPlatform.retrieveCoursesStatistic();
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("back")) {
                return;
            }
            learningPlatform.getTopLearnersForSpecificCourse(userInput);
        }
    }
}
