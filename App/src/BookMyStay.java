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

        public double getPrice() {
            return price;
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

    // Centralized Inventory (no direct modification outside)
    static class RoomInventory {

        private Map<String, Integer> inventory = new HashMap<>();

        public void addRoom(String roomType, int count) {
            inventory.put(roomType, count);
        }

        // Read-only access
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Internal update (not used by Guest/Search)
        public void updateAvailability(String roomType, int change) {
            int current = getAvailability(roomType);
            inventory.put(roomType, current + change);
        }

        // Expose full map (read-only usage expected)
        public Map<String, Integer> getAllInventory() {
            return inventory;
        }
    }

    // Search Service (READ-ONLY)
    static class SearchService {

        private RoomInventory inventory;
        private Map<String, Room> roomMap;

        public SearchService(RoomInventory inventory, Map<String, Room> roomMap) {
            this.inventory = inventory;
            this.roomMap = roomMap;
        }

        // Guest search (no modification allowed)
        public void searchAvailableRooms() {
            System.out.println("\nAvailable Rooms:\n");

            for (String type : inventory.getAllInventory().keySet()) {

                int available = inventory.getAvailability(type);

                // Filter unavailable rooms
                if (available > 0) {
                    Room room = roomMap.get(type);
                    room.displayDetails(available);
                }
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v3.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoom("Standard Room", 5);
        inventory.addRoom("Deluxe Room", 0); // Unavailable example

        // Create room objects
        Room standard = new StandardRoom();
        Room deluxe = new DeluxeRoom();

        Map<String, Room> roomMap = new HashMap<>();
        roomMap.put(standard.getRoomType(), standard);
        roomMap.put(deluxe.getRoomType(), deluxe);

        // Guest uses Search Service
        SearchService searchService = new SearchService(inventory, roomMap);

        // Perform search (READ-ONLY)
        searchService.searchAvailableRooms();
    }
}