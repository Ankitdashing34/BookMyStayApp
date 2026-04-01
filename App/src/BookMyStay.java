import java.util.LinkedList;
import java.util.Queue;

public class BookMyStay {

    // Reservation (Guest booking intent)
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

        public void display() {
            System.out.println("Guest: " + guestName + " | Requested: " + roomType);
        }
    }

    // Booking Request Queue (FIFO)
    static class BookingQueue {

        private Queue<Reservation> queue = new LinkedList<>();

        // Add request to queue
        public void addRequest(Reservation reservation) {
            queue.offer(reservation);
            System.out.println("Request added for " + reservation.getGuestName());
        }

        // View all queued requests (without removing)
        public void viewQueue() {
            System.out.println("\nCurrent Booking Queue:\n");

            if (queue.isEmpty()) {
                System.out.println("No pending requests.");
                return;
            }

            for (Reservation r : queue) {
                r.display();
            }
        }

        // Process next request (FIFO)
        public Reservation getNextRequest() {
            return queue.poll(); // removes from front
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v4.0\n");

        // Initialize booking queue
        BookingQueue bookingQueue = new BookingQueue();

        // Guests submit booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Standard Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Deluxe Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Standard Room"));

        // View queue (arrival order preserved)
        bookingQueue.viewQueue();

        // Simulate processing next request (no inventory change yet)
        System.out.println("\nProcessing next request...\n");
        Reservation next = bookingQueue.getNextRequest();

        if (next != null) {
            System.out.println("Processing:");
            next.display();
        }

        // View remaining queue
        bookingQueue.viewQueue();
    }
}