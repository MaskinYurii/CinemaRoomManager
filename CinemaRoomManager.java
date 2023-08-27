package cinema;

import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;

public class CinemaRoomManager {
    // Number rows in cinema room
    static int rowsNumber;
    // Number seats in one row in cinema room
    static int seatsInRowNumber;
    // All seats in cinema room
    static int allSeats;
    // 2D array with tickets. Each ticket have own row and seat
    static ArrayList<int[]> reserved = new ArrayList<>();
    // Variable for exit from program. When user choice 3 (Exit) this variable will false
    static boolean isNotExit = true;
    // Income from tickets sold
    static int ticketsIncome = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask user the number rows in cinema room
        System.out.println("Enter the number of rows: ");
        rowsNumber = scanner.nextInt();

        // Ask number the seats for each row in cinema room
        System.out.println("Enter the number of seats in each row: ");
        seatsInRowNumber = scanner.nextInt();

        // All seats in cinema room
        allSeats = seatsInRowNumber * rowsNumber;

        // Program life loop
        // Each time show menu to user and get answer from user.
        // After this do some to do
        while (isNotExit) {
            makeEmptyRow();

            showMenu();
            int choice = scanner.nextInt();
            checkMenuChoice(choice, allSeats);

            makeEmptyRow();
        }
    }

    // This method make empty row for good output style
    private static void makeEmptyRow() {
        System.out.print("\n");
    }

    /* Show cinema room scheme. Empty seats is "S". Reserved seats is "B" */
    private static void showCinemaRoom() {
        makeEmptyRow();
        // Print "Cinema"
        System.out.println("Cinema: ");

        // Print number for seats cols
        for (int i = 1; i <= seatsInRowNumber; i++) {
            if (i == 1) {
                System.out.print("  " + i);
            } else if (i == seatsInRowNumber) {
                System.out.print(" " + i + "\n");
            } else {
                System.out.print(" " + i);
            }
        }

        // Print rows. First value - number of row. Others values - S
        for (int i = 1; i <= rowsNumber; i++) {
            for (int j = 0; j <= seatsInRowNumber; j++) {
                boolean isReserved = isReserved(i, j);
                System.out.print(j == 0 ? (i == 0 ? " " : i) :
                        (isReserved ? " B" : " S"));
            }
            System.out.println();
        }

    }

    /* Get from user row number and seat number. If this seat is free - reserve it */
    private static void buyTicket() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Ask user row number
            System.out.println("Enter a row number: ");
            int userRowNumber = scanner.nextInt();

            // Ask user seat number
            System.out.println("Enter a seat number in that row: ");
            int userSeatNumber = scanner.nextInt();

            boolean isReserved = isReserved(userRowNumber, userSeatNumber);
            boolean isCorrectValue = isCorrectValue(userRowNumber);

            if (isCorrectValue) {
                if (isReserved) {
                    System.out.println("That ticket has already been purchased!");
                } else {
                    // Reserve user ticket in cinema room
                    reserveTicket(userSeatNumber, userRowNumber);

                    // Calculate ticket price and add this number to total income from tickets
                    int ticketPrice = calcTicketPrice(rowsNumber, seatsInRowNumber, userRowNumber);
                    ticketsIncome += ticketPrice;

                    // Show ticket price to user
                    System.out.println("Ticket price: $" + ticketPrice);

                    break;
                }
            } else {
                // If user entered incorrect value for row - show about it
                System.out.println("Wrong input!");
            }
        }
    }

    /**
     * Method for checking user input value
     *
     * @param userRowNumber - number row in user ticket
     * @return - if ticket have correct value - return true. Else - false.
     */
    private static boolean isCorrectValue(int userRowNumber) {
        return userRowNumber <= rowsNumber;
    }

    /**
     * Reserve user ticket in cinema room
     *
     * @param userSeatNumber - user seat number in ticket
     * @param userRowNumber - user row number in ticket
     */
    private static void reserveTicket(int userSeatNumber, int userRowNumber) {
        int[] ticket = {userRowNumber, userSeatNumber};
        reserved.add(ticket);
    }

    /**
     * Check reserve in entered ticket
     *
     * @param userRow - row in user ticket
     * @param userSeat - seat in row in user ticket
     *
     * @return if user seat is reserved - return true. Else - false.
     */
    private static boolean isReserved(int userRow, int userSeat) {
        boolean isReserved = false;

        for (int[] place : reserved) {
            if (place[0] == userRow && place[1] == userSeat) {
                isReserved = true;
                break;
            }
        }

        return isReserved;
    }

    /**
     * Calculate total cost of cinema room.
     * @return number of total room cost
     */
    private static int calcTotalRoomCost() {
        // Variable for total cost
        int total;

        /* If the total number of seats in the screen room is not more than 60,
         * then the price of each ticket is 10 dollars.
         * In a larger room, the tickets are 10 dollars for
         * the front half of the rows and 8 dollars for the back half.
         */
        if (allSeats < 60) {
            total = allSeats * 10;
        } else {
            // Front rows of seats costs 10 dollars per seat
            // Back rows of seats costs 8 dollars per seat
            if (rowsNumber % 2 == 0) {
                // Divided room in 2 parts. One part of room - front half.
                // Other part - back half of room.
                int halfOfRoom = rowsNumber / 2;

                // Count the number of seats in each half of room
                int numberSeats = halfOfRoom * seatsInRowNumber;
                total = numberSeats * 10;
            } else {
                // Count the number of rows in front half of room
                int frontRows = rowsNumber / 2;

                // Count the number of rows in back half of room
                int backRows = (rowsNumber / 2) + 1;

                // Count the total seats for front and back rows
                int frontSeats = frontRows * seatsInRowNumber;
                int backSeats = backRows * seatsInRowNumber;

                // Count the total cost
                total = (frontSeats * 10) + (backSeats * 8);
            }

        }

        return total;
    }

    private static int calcTicketPrice(int rows, int seats, int userRow) {
        // Ticket price
        int price;

        if (rows * seats < 60) {
            price = 10;
        } else {
            // Count the number of rows in front half of room
            int frontRows = rows / 2;

            // Calculate price for ticket
            price = userRow <= frontRows ? 10 : 8;
        }

        return price;
    }

    /**
     *  Show statistic. It includes: purchased tickets, occupancy of cinema room in percentage,
     * number current income from tickets and total income from cinema room
     *
     * @param allSeats - number of all seats in cinema room
     */
    private static void showStatistic(int allSeats) {
        // Count and print number of purchased tickets
        int purchasedTickets = countPurchasedTickets();
        System.out.println("Number of purchased tickets: " + purchasedTickets);

        // The number of purchased tickets represented as a percentage.
        // Percentages should be rounded to 2 decimal places
        double percentage = countPercentage(allSeats);
        if (percentage == 0) {
            System.out.println("Percentage: 0.00%");
        } else {
            System.out.println("Percentage: " + String.format("%.2f%%", percentage));
        }

        // Count current income from tickets
        System.out.println("Current income: $" + ticketsIncome);

        // Count total income from all tickets of cinema room
        int totalIncome = calcTotalRoomCost();
        System.out.println("Total income: $" + totalIncome);
    }

    /**
     * Calculate the occupancy of the cinema room in percentage
     *
     * @param allSeats - number of all seats in cinema room
     * @return number the occupancy of the cinema room in percentage
     */
    private static double countPercentage(int allSeats) {
        int reservedTickets = reserved.size();

        double percentage = reserved.size() > 0 ? (reservedTickets / (double) allSeats) * 100 : 0;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String decimalPercentage = decimalFormat.format(percentage);

        return Double.parseDouble(decimalPercentage);
    }

    /** Count how many tickets was bought
     *
     * @return number of purchased tickets
     */
    private static int countPurchasedTickets() {
        int counter = 0;

        for (int[] ignored : reserved) {
            counter++;
        }

        return counter;
    }

    /* Show list with menu orders */
    private static void showMenu() {
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
    }

    /** Check user's answer from menu and do some to do
     *
     * @param choice - the menu item that the user selected from the menu
     * @param allSeats - number of all seats in cinema room
     */
    private static void checkMenuChoice(int choice, int allSeats) {
        switch (choice) {
            case 1 -> showCinemaRoom();
            case 2 -> buyTicket();
            case 3 -> showStatistic(allSeats);
            case 0 -> isNotExit = false;
            default -> System.out.println("Incorrect value!");
        }
    }
}