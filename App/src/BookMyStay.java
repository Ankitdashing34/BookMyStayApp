import java.util.*;

public class BookMyStay {

    // Reservation
    static class Reservation {
        private String guestName;
        private String roomType;
        private String reservationId;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        public void setReservationId(String id) {
            this.reservationId = id;
        }

        public String getReservationId() {
            return reservationId;
        }
    }

    // Booking Queue
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

    // Inventory Service
    static class RoomInventory {

        private Map<String, Integer> availability = new HashMap<>();
        private Map<String, Integer> counter = new HashMap<>();
        private Set<String> allocatedIds = new HashSet<>();

        public void addRoomType(String type, int count) {
            availability.put(type, count);
            counter.put(type, 1);
        }

        public String allocateRoom(String type) {
            int available = availability.getOrDefault(type, 0);

            if (available <= 0) return null;

            String id = type.replaceAll(" ", "") + "-" + counter.get(type);
            counter.put(type, counter.get(type) + 1);

            if (allocatedIds.contains(id)) return null;

            allocatedIds.add(id);
            availability.put(type, available - 1);

            return id;
        }
    }

    // Booking History (insertion order preserved)
    static class BookingHistory {

        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public List<Reservation> getAll() {
            return history;
        }
    }

    // Booking Service
    static class BookingService {

        private RoomInventory inventory;
        private BookingHistory history;

        public BookingService(RoomInventory inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void processBooking(Reservation r) {

            String roomId = inventory.allocateRoom(r.getRoomType());

            if (roomId != null) {
                r.setReservationId(roomId);

                // Add to history ONLY after success
                history.add(r);

                System.out.println("\n✅ Booking Confirmed");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room: " + r.getRoomType());
                System.out.println("Reservation ID: " + roomId);
            } else {
                System.out.println("\n❌ Booking Failed for " + r.getGuestName());
            }
        }
    }

    // Booking Report Service
    static class BookingReportService {

        private BookingHistory history;

        public BookingReportService(BookingHistory history) {
            this.history = history;
        }

        // Display all bookings
        public void showAllBookings() {
            System.out.println("\n📋 Booking History:\n");

            List<Reservation> list = history.getAll();

            if (list.isEmpty()) {
                System.out.println("No bookings found.");
                return;
            }

            for (Reservation r : list) {
                System.out.println(
                        "ID: " + r.getReservationId() +
                                " | Guest: " + r.getGuestName() +
                                " | Room: " + r.getRoomType()
                );
            }
        }

        // Simple report: count per room type
        public void showSummaryReport() {
            System.out.println("\n📊 Booking Summary Report:\n");

            Map<String, Integer> summary = new HashMap<>();

            for (Reservation r : history.getAll()) {
                summary.put(
                        r.getRoomType(),
                        summary.getOrDefault(r.getRoomType(), 0) + 1
                );
            }

            for (String type : summary.keySet()) {
                System.out.println(type + " → Total Bookings: " + summary.get(type));
            }
        }
    }

    // Main
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v7.0\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 2);
        inventory.addRoomType("Deluxe Room", 1);

        BookingHistory history = new BookingHistory();

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Alice", "Standard Room"));
        queue.addRequest(new Reservation("Bob", "Standard Room"));
        queue.addRequest(new Reservation("Charlie", "Deluxe Room"));

        BookingService bookingService = new BookingService(inventory, history);

        // Process bookings
        while (!queue.isEmpty()) {
            bookingService.processBooking(queue.getNextRequest());
        }

        // Admin views reports
        BookingReportService reportService = new BookingReportService(history);

        reportService.showAllBookings();
        reportService.showSummaryReport();
    }
}