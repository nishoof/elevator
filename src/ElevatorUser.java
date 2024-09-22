import java.util.Scanner;

public class ElevatorUser {
    public static void main(String[] args) {
        // Elevator elevator = new Elevator();
        // Scanner scanner = new Scanner(System.in);

        // while (true) {
        //     promptForInput(elevator, scanner);
        // }
    }

    // private static void promptForInput(Elevator elevator, Scanner scanner) {
    public void promptForInput(Elevator elevator, Scanner scanner) {
        System.out.print("What floor do you want to go to?    ");

        String input = scanner.nextLine();

        elevator.goToNewFloor(Integer.parseInt(input));
    }
}
