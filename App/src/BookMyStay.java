import java.util.*;

public class BookMyStay {

    // Reservation (Guest request)
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
    }

    // Booking Queue (FIFO)
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation getNextRequest() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    // Inventory Service (central state)
    static class RoomInventory {

        private Map<String, Integer> availability = new HashMap<>();
        private Map<String, Integer> roomIdCounter = new HashMap<>();
        private Set<String> allocatedRoomIds = new HashSet<>();

        // Initialize room type
        public void addRoomType(String roomType, int count) {
            availability.put(roomType, count);
            roomIdCounter.put(roomType, 1); // start IDs from 1
        }

        public int getAvailability(String roomType) {
            return availability.getOrDefault(roomType, 0);
        }

        // Generate unique room ID
        private String generateRoomId(String roomType) {
            int id = roomIdCounter.get(roomType);
            roomIdCounter.put(roomType, id + 1);
            return roomType.replaceAll(" ", "") + "-" + id;
        }

        // Allocate room safely
        public String allocateRoom(String roomType) {

            int available = getAvailability(roomType);

            if (available <= 0) {
                return null;
            }

            // Generate unique ID
            String roomId = generateRoomId(roomType);

            // Ensure no duplication (extra safety)
            if (allocatedRoomIds.contains(roomId)) {
                return null;
            }

            // Mark as allocated
            allocatedRoomIds.add(roomId);

            // Decrement inventory immediately
            availability.put(roomType, available - 1);

            return roomId;
        }

        public void displayInventory() {
            System.out.println("\nCurrent Inventory:");
            for (String type : availability.keySet()) {
                System.out.println(type + " → Available: " + availability.get(type));
            }
        }
    }

    // Booking Service (process + confirm)
    static class BookingService {

        private RoomInventory inventory;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBooking(Reservation r) {

            System.out.println("\nProcessing request for " + r.getGuestName());

            // Step 1: Check + Allocate
            String roomId = inventory.allocateRoom(r.getRoomType());

            // Step 2: Confirm or reject
            if (roomId != null) {
                System.out.println("✅ Booking Confirmed!");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room Type: " + r.getRoomType());
                System.out.println("Assigned Room ID: " + roomId);
            } else {
                System.out.println("❌ Booking Failed (No rooms available)");
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v5.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 2);
        inventory.addRoomType("Deluxe Room", 1);

        // Initialize queue
        BookingQueue queue = new BookingQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Standard Room"));
        queue.addRequest(new Reservation("Bob", "Standard Room"));
        queue.addRequest(new Reservation("Charlie", "Standard Room")); // exceeds capacity
        queue.addRequest(new Reservation("David", "Deluxe Room"));

        // Booking service
        BookingService bookingService = new BookingService(inventory);

        // Process queue
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processBooking(r);
        }

        // Final inventory state
        inventory.displayInventory();
    }
}