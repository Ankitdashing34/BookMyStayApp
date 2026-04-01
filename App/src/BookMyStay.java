public class BookMyStay {
        // Abstract base class
        static abstract class Room {
            protected String roomType;
            protected double price;
            protected int availableRooms;

            public Room(String roomType, double price, int availableRooms) {
                this.roomType = roomType;
                this.price = price;
                this.availableRooms = availableRooms;
            }

            public abstract void displayDetails();
        }

        // Standard Room class
        static class StandardRoom extends Room {

            public StandardRoom(int availableRooms) {
                super("Standard Room", 1000.0, availableRooms);
            }

            @Override
            public void displayDetails() {
                System.out.println(roomType + " | Price: ₹" + price + " | Available: " + availableRooms);
            }
        }

        // Deluxe Room class
        static class DeluxeRoom extends Room {

            public DeluxeRoom(int availableRooms) {
                super("Deluxe Room", 2000.0, availableRooms);
            }

            @Override
            public void displayDetails() {
                System.out.println(roomType + " | Price: ₹" + price + " | Available: " + availableRooms);
            }
        }

        // Main method (entry point)
        public static void main(String[] args) {

            System.out.println("Welcome to Hotel Booking App v1.0\n");

            // Create room objects
            StandardRoom standard = new StandardRoom(5);
            DeluxeRoom deluxe = new DeluxeRoom(3);

            // Display details
            standard.displayDetails();
            deluxe.displayDetails();
        }
    }