import java.util.*;

public class BookMyStay {

    // Custom Exception for validation failures
    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }

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

    // Inventory Service
    static class RoomInventory {

        private Map<String, Integer> availability = new HashMap<>();

        public void addRoomType(String type, int count) {
            availability.put(type, count);
        }

        public boolean hasRoomType(String type) {
            return availability.containsKey(type);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void decrement(String type) {
            availability.put(type, getAvailability(type) - 1);
        }
    }

    // Validator
    static class InvalidBookingValidator {

        public static void validate(Reservation r, RoomInventory inventory)
                throws InvalidBookingException {

            // Validate guest name
            if (r.getGuestName() == null || r.getGuestName().trim().isEmpty()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }

            // Validate room type
            if (r.getRoomType() == null || r.getRoomType().trim().isEmpty()) {
                throw new InvalidBookingException("Room type must be provided.");
            }

            // Validate room existence
            if (!inventory.hasRoomType(r.getRoomType())) {
                throw new InvalidBookingException("Invalid room type selected.");
            }

            // Validate availability
            if (inventory.getAvailability(r.getRoomType()) <= 0) {
                throw new InvalidBookingException("No rooms available for selected type.");
            }
        }
    }

    // Booking Service
    static class BookingService {

        private RoomInventory inventory;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBooking(Reservation r) {

            try {
                // Step 1: Validate input + state
                InvalidBookingValidator.validate(r, inventory);

                // Step 2: Safe allocation (only if valid)
                inventory.decrement(r.getRoomType());

                System.out.println("\n✅ Booking Confirmed");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room: " + r.getRoomType());

            } catch (InvalidBookingException e) {

                // Step 3: Handle failure gracefully
                System.out.println("\n❌ Booking Failed");
                System.out.println("Reason: " + e.getMessage());
            }
        }
    }

    // Main
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v8.0\n");

        // Setup inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 1);

        BookingService service = new BookingService(inventory);

        // Test cases (valid + invalid)
        Reservation r1 = new Reservation("Alice", "Standard Room");   // valid
        Reservation r2 = new Reservation("", "Standard Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Deluxe Room");       // invalid type
        Reservation r4 = new Reservation("Charlie", "Standard Room"); // no availability

        service.processBooking(r1);
        service.processBooking(r2);
        service.processBooking(r3);
        service.processBooking(r4);
    }
}