import java.util.*;

// Queue class for BFS traversal
class Queue {
    int front, size, capacity;
    int[] array;

    public Queue(int length) {
        capacity = length;
        front = size = 0;
        array = new int[capacity];
    }

    boolean isFull() {
        return (size == capacity);
    }

    boolean isEmpty() {
        return (size == 0);
    }

    void enqueue(int item) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        } else if (front == capacity) {
            front = 0;
        }

        int pos;
        pos = (front + size) % capacity;
        array[pos] = item;
        size = size + 1;
        System.out.println(item + " enqueued to queue");
    }

    int dequeue() {
        if (isEmpty()) {
            return 0;
        }
        int item = array[front];
        front = (front + 1) % capacity;
        size = size - 1;
        return item;
    }

    void display() {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }


}

// City class to represent each city
class City {
    int id;
    List<Integer> roadConnections;
    int aerialConnection;

    public City(int id) {
        this.id = id;
        this.roadConnections = new ArrayList<>();
        this.aerialConnection = -1; // -1 indicates no aerial connection
    }
}

// Traveler class with BFS-based journey planning
class Traveler {

    // Plan the journey from the start city to the destination city
    public static int planJourney(List<City> cities, int start, int destination) {
        int days = 1;
        int citiesTraveled = 0;
        int currentCity = start;
        Queue q = new Queue(cities.size());
        Set<Integer> visitedCities = new HashSet<>();

        q.enqueue(start);
        visitedCities.add(start);
        visitedCities.add(q.dequeue());

        // Loop runs until the traveler reaches the destination city
        while (currentCity != destination) {
            try {
                // Check if there is an aerial connection and limit on aerial travels
                if (cities.get(currentCity).aerialConnection != -1 && citiesTraveled < 6) {
                    currentCity = cities.get(currentCity).aerialConnection;
                    q.enqueue(currentCity);
                    visitedCities.add(q.dequeue());
                    if (currentCity != destination) {
                        days++;
                    } else {
                        break;
                    }
                } else {
                    // Moving towards the next city through road connection
                    int nextCity = getNextRoadCity(cities, currentCity, destination, visitedCities);

                    q.enqueue(nextCity);
                    visitedCities.add(nextCity);
                    visitedCities.add(q.dequeue());
                    citiesTraveled++;
                    currentCity = nextCity;
                }

                // Reset the counter if the conditions match
                if (citiesTraveled == 6 || (cities.get(currentCity).aerialConnection != -1 && citiesTraveled > 0)) {
                    citiesTraveled = 0;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
//q.display();
        return days;
    }

    // for getting the next city through road connections
    private static int getNextRoadCity(List<City> cities, int currentCity, int destination, Set<Integer> visitedCities) {
        List<Integer> roadConnections = cities.get(currentCity).roadConnections;

        // If there is an aerial connection and the condition is met, select a road connection
        if (cities.get(currentCity).aerialConnection != -1 &&
                cities.get(currentCity).aerialConnection < currentCity) {
            return roadConnections.stream()
                    .filter(city -> city < currentCity && !visitedCities.contains(city))
                    .findFirst()
                    .orElse(roadConnections.get(0));
        }

        for (int nextCity : roadConnections) {
            if (nextCity != destination && !visitedCities.contains(nextCity)) {
                return nextCity;
            }
        }
        return destination;
    }
}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tDSA Final Project");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tMADE BY TAHA SHAHID");

        System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------");


        try {
            int testCases;
            do {
                System.out.print("Number of test cases: ");
                testCases = getIntInput(scanner);
            } while (testCases <= 0);

            for (int t = 1; t <= testCases; t++) {
                int totalCities;
                do {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTest Case " + t);
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.print("Total number of cities: ");
                    totalCities = getIntInput(scanner);
                } while (totalCities <= 0);

                List<City> cities = new ArrayList<>();
                for (int i = 0; i < totalCities + 1; i++) {
                    cities.add(new City(i));
                }

                for (int i = 0; i < totalCities - 1; i++) {
                    cities.get(i).roadConnections.add(i + 1);
                }
                int z=0;
                int startCity;
                do {
                    try {
                        System.out.print("Where to start from: ");
                        startCity = getIntInput(scanner);
                        if (startCity < 0 || startCity >= totalCities) {
                            System.out.println("Error: Invalid city. Please enter a valid city index.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid input. Please enter a valid integer.");
                        scanner.nextLine(); // consume the invalid input
                        startCity = -1; // set to an invalid value to trigger the loop again
                    }
                } while (startCity < 0 || startCity >= totalCities);


                int destinationCity;
                do {
                    try {
                        System.out.print("Where to land: ");
                        destinationCity = getIntInput(scanner);
                        if (destinationCity < 0 || destinationCity > totalCities) {
                            System.out.println("Error: Invalid destination city. Please enter a valid city index different from the starting city.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid input. Please enter a valid integer.");
                        scanner.nextLine(); // consume the invalid input
                        destinationCity = -1; // set to an invalid value to trigger the loop again
                    }
                } while (destinationCity < 0 || destinationCity > totalCities);


                int numAerialRoutes;
                do {
                    System.out.print("Number of aerial routes: ");
                    numAerialRoutes = getIntInput(scanner);
                } while (numAerialRoutes < 0);

                for (int i = 0; i < numAerialRoutes; i++) {
                    System.out.println("Enter aerial route " + (i + 1));
                    int cityIndex;
                    int aerialConnection = 0;
                    do {
                        try {
                            System.out.print("From: ");
                            cityIndex = getIntInput(scanner);
                            System.out.print("To: ");
                            aerialConnection = getIntInput(scanner);

                            if (cityIndex < 0 || cityIndex >= totalCities || aerialConnection < 0 || aerialConnection >= totalCities) {
                                System.out.println("Error: Invalid input. Please enter valid city indices for the aerial route.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Error: Invalid input. Please enter valid integers for city indices.");
                            scanner.nextLine(); // consume the invalid input
                            cityIndex = -1; // set to an invalid value to trigger the loop again
                        }
                    } while (cityIndex < 0 || cityIndex >= totalCities || aerialConnection < 0 || aerialConnection >= totalCities);


                    cities.get(cityIndex).aerialConnection = aerialConnection;
                }

                // Print the graph connections
                System.out.println("Graph Connections:");
                for (City city : cities) {
                    System.out.println("City " + city.id + " - Next Road Connection: " + city.roadConnections + ", Aerial Connection: " + city.aerialConnection);
                }
                System.out.println();

                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println("Answer: ");
                int minDays = Traveler.planJourney(cities, startCity, destinationCity);
                System.out.println("Total days required to reach city[" + destinationCity + "] for Test Case " + t + ": " + minDays);

                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Get integer input from the user
    private static int getIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.next(); // consume the invalid input
        }
        return scanner.nextInt();
    }
}
