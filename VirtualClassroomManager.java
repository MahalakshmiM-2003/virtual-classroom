
import java.util.*;
import java.util.logging.*;

public class VirtualClassroomManager {
    private static final Logger logger = Logger.getLogger(VirtualClassroomManager.class.getName());
    private Map<String, Classroom> classrooms = new HashMap<>();
    private Map<String, Student> students = new HashMap<>();

    public static void main(String[] args) {
        VirtualClassroomManager manager = new VirtualClassroomManager();
        Scanner scanner = new Scanner(System.in);
        int command;

        System.out.println("Welcome to the Virtual Classroom Manager!");
        while (true) {
            System.out.println(
                    "Enter a command (1: addClassroom, 2: listClassrooms, 3: removeClassroom, 4: addStudent, 5: listStudents, 6: scheduleAssignment, 7: submitAssignment, 8: exit):");
            try {
                command = Integer.parseInt(scanner.nextLine());
                switch (command) {
                    case 1:
                        manager.handleAddClassroom(scanner);
                        break;
                    case 2:
                        manager.listClassrooms();
                        break;
                    case 3:
                        manager.handleRemoveClassroom(scanner);
                        break;
                    case 4:
                        manager.handleAddStudent(scanner);
                        break;
                    case 5:
                        manager.handleListStudents(scanner);
                        break;
                    case 6:
                        manager.handleScheduleAssignment(scanner);
                        break;
                    case 7:
                        manager.handleSubmitAssignment(scanner);
                        break;
                    case 8:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid command.");
                }
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "Invalid input: not a number", e);
                System.out.println("Invalid input: please enter a number.");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An error occurred: ", e);
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void handleAddClassroom(Scanner scanner) {
        System.out.println("Enter classroom name:");
        String className = scanner.nextLine();
        addClassroom(className);
    }

    private void handleRemoveClassroom(Scanner scanner) {
        System.out.println("Enter classroom name to remove:");
        String className = scanner.nextLine();
        removeClassroom(className);
    }

    private void handleAddStudent(Scanner scanner) {
        System.out.println("Enter student ID:");
        String studentId = scanner.nextLine();
        System.out.println("Enter classroom name:");
        String className = scanner.nextLine();
        addStudent(studentId, className);
    }

    private void handleListStudents(Scanner scanner) {
        System.out.println("Enter classroom name to list students:");
        String className = scanner.nextLine();
        listStudents(className);
    }

    private void handleScheduleAssignment(Scanner scanner) {
        System.out.println("Enter classroom name:");
        String className = scanner.nextLine();
        System.out.println("Enter assignment details:");
        String assignmentDetails = scanner.nextLine();
        scheduleAssignment(className, assignmentDetails);
    }

    private void handleSubmitAssignment(Scanner scanner) {
        System.out.println("Enter student ID:");
        String studentId = scanner.nextLine();
        System.out.println("Enter classroom name:");
        String className = scanner.nextLine();
        System.out.println("Enter assignment details:");
        String assignmentDetails = scanner.nextLine();
        submitAssignment(studentId, className, assignmentDetails);
    }

    public void addClassroom(String name) {
        if (!classrooms.containsKey(name)) {
            classrooms.put(name, new Classroom(name));
            logger.info("Classroom " + name + " has been created.");
            System.out.println("Classroom " + name + " has been created.");
        } else {
            logger.warning("Classroom " + name + " already exists.");
            System.out.println("Classroom " + name + " already exists.");
        }
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
        } else {
            System.out.println("Classrooms:");
            classrooms.keySet().forEach(System.out::println);
        }
    }

    public void removeClassroom(String name) {
        if (classrooms.containsKey(name)) {
            classrooms.remove(name);
            logger.info("Classroom " + name + " has been removed.");
            System.out.println("Classroom " + name + " has been removed.");
        } else {
            logger.warning("Classroom " + name + " does not exist.");
            System.out.println("Classroom " + name + " does not exist.");
        }
    }

    public void addStudent(String id, String className) {
        if (!classrooms.containsKey(className)) {
            logger.warning("Classroom " + className + " does not exist.");
            System.out.println("Classroom " + className + " does not exist.");
            return;
        }

        Student student = students.computeIfAbsent(id, Student::new);
        Classroom classroom = classrooms.get(className);
        classroom.enrollStudent(student);
        logger.info("Student " + id + " has been enrolled in " + className + ".");
        System.out.println("Student " + id + " has been enrolled in " + className + ".");
    }

    public void listStudents(String className) {
        if (!classrooms.containsKey(className)) {
            logger.warning("Classroom " + className + " does not exist.");
            System.out.println("Classroom " + className + " does not exist.");
            return;
        }

        Classroom classroom = classrooms.get(className);
        List<Student> students = classroom.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students enrolled in " + className + ".");
        } else {
            System.out.println("Students in " + className + ":");
            students.forEach(student -> System.out.println(student.getId()));
        }
    }

    public void scheduleAssignment(String className, String assignmentDetails) {
        if (!classrooms.containsKey(className)) {
            logger.warning("Classroom " + className + " does not exist.");
            System.out.println("Classroom " + className + " does not exist.");
            return;
        }

        Classroom classroom = classrooms.get(className);
        classroom.scheduleAssignment(assignmentDetails);
        logger.info("Assignment for " + className + " has been scheduled.");
        System.out.println("Assignment for " + className + " has been scheduled.");
    }

    public void submitAssignment(String studentId, String className, String assignmentDetails) {
        if (!classrooms.containsKey(className)) {
            logger.warning("Classroom " + className + " does not exist.");
            System.out.println("Classroom " + className + " does not exist.");
            return;
        }

        if (!students.containsKey(studentId)) {
            logger.warning("Student " + studentId + " does not exist.");
            System.out.println("Student " + studentId + " does not exist.");
            return;
        }

        Classroom classroom = classrooms.get(className);
        classroom.submitAssignment(studentId, assignmentDetails);
        logger.info("Assignment submitted by Student " + studentId + " in " + className + ".");
        System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
    }
}

class Classroom {
    private String name;
    private List<Student> students = new ArrayList<>();
    private List<String> assignments = new ArrayList<>();

    public Classroom(String name) {
        this.name = name;
    }

    public void enrollStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
        }
    }

    public void scheduleAssignment(String assignmentDetails) {
        assignments.add(assignmentDetails);
    }

    public void submitAssignment(String studentId, String assignmentDetails) {
        // In a real application, you'd want to track submissions more rigorously
        System.out.println("Assignment " + assignmentDetails + " submitted by " + studentId);
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<String> getAssignments() {
        return assignments;
    }
}

class Student {
    private String id;

    public Student(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Student student = (Student) obj;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
