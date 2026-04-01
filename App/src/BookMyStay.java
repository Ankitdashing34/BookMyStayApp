import java.util.*;

public class BookMyStay {

    // Reservation
    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    // Thread-safe Booking Queue
    static class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public synchronized void addRequest(Reservation r) {
            queue.offer(r);
            notify(); // wake up waiting threads
        }

        public synchronized Reservation getNextRequest() {
            while (queue.isEmpty()) {
                try {
                    wait(); // wait for new requests
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return queue.poll();
        }
    }

    // Inventory (critical shared resource)
    static class RoomInventory {

        private Map<String, Integer> availability = new HashMap<>();
        private Map<String, Integer> counter = new HashMap<>();

        public RoomInventory() {
            availability.put("Standard Room", 2);
            counter.put("Standard Room", 1);
        }

        // Critical section (synchronized)
        public synchronized String allocateRoom(String type) {

            int available = availability.getOrDefault(type, 0);

            if (available <= 0) {
                return null;
            }

            String id = type.replaceAll(" ", "") + "-" + counter.get(type);
            counter.put(type, counter.get(type) + 1);

            availability.put(type, available - 1);

            return id;
        }

        public synchronized void displayInventory() {
            System.out.println("\nFinal Inventory:");
            for (String type : availability.keySet()) {
                System.out.println(type + " → Available: " + availability.get(type));
            }
        }
    }

    // Concurrent Booking Processor (Thread)
    static class BookingProcessor extends Thread {

        private BookingQueue queue;
        private RoomInventory inventory;

        public BookingProcessor(String name, BookingQueue queue, RoomInventory inventory) {
            super(name);
            this.queue = queue;
            this.inventory = inventory;
        }

        @Override
        public void run() {

            // Each thread processes 2 requests (demo purpose)
            for (int i = 0; i < 2; i++) {

                Reservation r = queue.getNextRequest();

                // Critical section: allocation
                String roomId = inventory.allocateRoom(r.getRoomType());

                if (roomId != null) {
                    System.out.println(
                            getName() + " → ✅ Booked for " + r.getGuestName() +
                                    " | Room ID: " + roomId
                    );
                } else {
                    System.out.println(
                            getName() + " → ❌ Failed for " + r.getGuestName()
                    );
                }
            }
        }
    }

    // Main
    public static void main(String[] args) {

        System.out.println("Welcome to BookMyStay v10.0 (Concurrent Booking)\n");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Multiple guests submit requests simultaneously
        queue.addRequest(new Reservation("Alice", "Standard Room"));
        queue.addRequest(new Reservation("Bob", "Standard Room"));
        queue.addRequest(new Reservation("Charlie", "Standard Room"));
        queue.addRequest(new Reservation("David", "Standard Room"));

        // Multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor("Thread-1", queue, inventory);
        BookingProcessor t2 = new BookingProcessor("Thread-2", queue, inventory);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final state
        inventory.displayInventory();
    }
}