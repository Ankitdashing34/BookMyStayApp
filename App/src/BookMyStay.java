 import java.util.HashMap;
import java.util.Map;

    public class BookMyStay {

        // Abstract Room class
        static abstract class Room {
            protected String roomType;
            protected double price;

            public Room(String roomType, double price) {
                this.roomType = roomType;
                this.price = price;
            }

            public String getRoomType() {
                return roomType;
            }

            public abstract void displayDetails(int availableRooms);
        }

        // Standard Room
        static class StandardRoom extends Room {
            public StandardRoom() {
                super("Standard Room", 1000.0);
            }

            @Override
            public void displayDetails(int availableRooms) {
                System.out.println(roomType + " | Price: ₹" + price + " | Available: " + availableRooms);
            }
        }

        // Deluxe Room
        static class DeluxeRoom extends Room {
            public DeluxeRoom() {
                super("Deluxe Room", 2000.0);
            }

            @Override
            public void displayDetails(int availableRooms) {
                System.out.println(roomType + " | Price: ₹" + price + " | Available: " + availableRooms);
            }
        }

        // RoomInventory using HashMap
        static class RoomInventory {

            private Map<String, Integer> inventory = new HashMap<>();

            // Register room type with count
            public void addRoom(String roomType, int count) {
                inventory.put(roomType, count);
            }

            // Get availability
            public int getAvailability(String roomType) {
                return inventory.getOrDefault(roomType, 0);
            }

            // Update availability (e.g., booking)
            public void updateAvailability(String roomType, int change) {
                int current = getAvailability(roomType);
                inventory.put(roomType, current + change);
            }

            // Display all inventory
            public void displayInventory(Map<String, Room> roomMap) {
                System.out.println("\nCurrent Room Inventory:\n");
                for (String type : inventory.keySet()) {
                    Room room = roomMap.get(type);
                    int available = inventory.get(type);
                    room.displayDetails(available);
                }
            }
        }

        // Main method
        public static void main(String[] args) {

            System.out.println("Welcome to Hotel Booking App v2.0\n");

            // Step 1: Initialize inventory
            RoomInventory inventory = new RoomInventory();

            // Step 2: Create room objects
            Room standard = new StandardRoom();
            Room deluxe = new DeluxeRoom();

            // Map to link room type → Room object
            Map<String, Room> roomMap = new HashMap<>();
            roomMap.put(standard.getRoomType(), standard);
            roomMap.put(deluxe.getRoomType(), deluxe);

            // Step 3: Register room availability
            inventory.addRoom("Standard Room", 5);
            inventory.addRoom("Deluxe Room", 3);

            // Step 4: Display inventory
            inventory.displayInventory(roomMap);

            // Step 5: Update availability (simulate booking)
            System.out.println("\nBooking 1 Standard Room...\n");
            inventory.updateAvailability("Standard Room", -1);

            // Step 6: Display updated inventory
            inventory.displayInventory(roomMap);
        }
    }