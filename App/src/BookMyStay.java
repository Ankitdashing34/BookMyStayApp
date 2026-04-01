import java.util.*;

public class BookMyStay {

    // Reservation
    static class Reservation {
        private String guestName;
        private String roomType;
        private String reservationId;
        private boolean active = true;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        public void setReservationId(String id) {
            this.reservationId = id;
        }

        public String getReservationId() { return reservationId; }

        public boolean isActive() { return active; }
        public void cancel() { this.active = false; }
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

            allocatedIds.add(id);
            availability.put(type, available - 1);

            return id;
        }

        // Rollback support
        public void releaseRoom(String roomId, String roomType) {
            if (allocatedIds.contains(roomId)) {
                allocatedIds.remove(roomId);
                availability.put(roomType, availability.get(roomType) + 1);
            }
        }

        public void displayInventory() {
            System.out.println("\nInventory:");
            for (String type : availability.keySet()) {
                System.out.println(type + " → Available: " + availability.get(type));
            }
        }
    }

    // Booking History
    static class BookingHistory {
        private Map<String, Reservation> history = new LinkedHashMap<>();

        public void add(Reservation r) {
            history.put(r.getReservationId(), r);
        }

        public Reservation get(String id) {
            return history.get(id);
        }

        public void display() {
            System.out.println("\nBooking History:");
            for (Reservation r : history.values()) {
                System.out.println(
                        "ID: " + r.getReservationId() +
                                " | Guest: " + r.getGuestName() +
                                " | Room: " + r.getRoomType() +
                                " | Status: " + (r.isActive() ? "ACTIVE" : "CANCELLED")
                );
            }
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

        public void book(Reservation r) {
            String id = inventory.allocateRoom(r.getRoomType());

            if (id != null) {
                r.setReservationId(id);
                history.add(r);

                System.out.println("\n✅ Booking Confirmed: " + id);
            } else {
                System.out.println("\n❌ Booking Failed for " + r.getGuestName());
            }
        }
    }

    // Cancellation Service
    static class CancellationService {

        private RoomInventory inventory;
        private BookingHistory history;

        private List<String> rollbackLog = new ArrayList<>();

        public CancellationService(RoomInventory inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void cancel(String reservationId) {

            System.out.println("\nProcessing cancellation for ID: " + reservationId);

            // Validate
            Reservation r = history.get(reservationId);

            if (r == null) {
                System.out.println("❌ Invalid reservation ID.");
                return;
            }

            if (!r.isActive()) {
                System.out.println("❌ Reservation already cancelled.");
                return;
            }

            // Record rollback
            rollbackLog.add(reservationId);

            // Restore inventory
            inventory.releaseRoom(r.getReservationId(), r.getRoomType());

            // Update history
            r.cancel();

            System.out.println("✅ Cancellation successful for " + r.getGuestName());
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to BookMyStay v9.0\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 2);

        BookingHistory history = new BookingHistory();

        BookingService bookingService = new BookingService(inventory, history);

        // Book rooms
        Reservation r1 = new Reservation("Alice", "Standard Room");
        Reservation r2 = new Reservation("Bob", "Standard Room");

        bookingService.book(r1);
        bookingService.book(r2);

        inventory.displayInventory();
        history.display();

        // Cancel booking
        CancellationService cancelService = new CancellationService(inventory, history);
        cancelService.cancel(r1.getReservationId());

        // After cancellation
        inventory.displayInventory();
        history.display();
    }
}