import java.io.*;
import java.util.*;

public class BookMyStay {

    // Reservation (Serializable)
    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;

        private String guestName;
        private String roomType;
        private String reservationId;

        public Reservation(String guestName, String roomType, String reservationId) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.reservationId = reservationId;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public String getReservationId() { return reservationId; }
    }

    // Inventory (Serializable)
    static class RoomInventory implements Serializable {
        private static final long serialVersionUID = 1L;

        private Map<String, Integer> availability = new HashMap<>();
        private Map<String, Integer> counter = new HashMap<>();

        public void addRoomType(String type, int count) {
            availability.put(type, count);
            counter.put(type, 1);
        }

        public String allocateRoom(String type) {
            int available = availability.getOrDefault(type, 0);
            if (available <= 0) return null;

            String id = type.replaceAll(" ", "") + "-" + counter.get(type);
            counter.put(type, counter.get(type) + 1);
            availability.put(type, available - 1);

            return id;
        }

        public Map<String, Integer> getAvailability() {
            return availability;
        }
    }

    // Booking History (Serializable)
    static class BookingHistory implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<Reservation> history = new ArrayList<>();

        public void add(Reservation r) {
            history.add(r);
        }

        public List<Reservation> getAll() {
            return history;
        }
    }

    // Persistence Service
    static class PersistenceService {

        private static final String FILE_NAME = "bookings.dat";

        // Save state
        public static void save(RoomInventory inventory, BookingHistory history) {
            try (ObjectOutputStream oos =
                         new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                oos.writeObject(inventory);
                oos.writeObject(history);

                System.out.println("\n💾 State saved to file.");

            } catch (IOException e) {
                System.out.println("❌ Error saving data: " + e.getMessage());
            }
        }

        // Load state
        public static Object[] load() {
            try (ObjectInputStream ois =
                         new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                RoomInventory inventory = (RoomInventory) ois.readObject();
                BookingHistory history = (BookingHistory) ois.readObject();

                System.out.println("📂 State loaded from file.\n");

                return new Object[]{inventory, history};

            } catch (Exception e) {
                System.out.println("⚠️ No previous data found. Starting fresh.\n");
                return null;
            }
        }
    }

    // Main
    public static void main(String[] args) {

        System.out.println("Welcome to BookMyStay v11.0 (Persistence Demo)\n");

        RoomInventory inventory;
        BookingHistory history;

        // Step 1: Load previous state (if exists)
        Object[] data = PersistenceService.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            // Fresh start
            inventory = new RoomInventory();
            inventory.addRoomType("Standard Room", 2);
            history = new BookingHistory();
        }

        // Step 2: Perform operations
        String roomId = inventory.allocateRoom("Standard Room");

        if (roomId != null) {
            Reservation r = new Reservation("Alice", "Standard Room", roomId);
            history.add(r);

            System.out.println("✅ Booking Confirmed: " + roomId);
        } else {
            System.out.println("❌ No rooms available");
        }

        // Step 3: Display current state
        System.out.println("\nCurrent Bookings:");
        for (Reservation r : history.getAll()) {
            System.out.println(
                    r.getReservationId() + " | " +
                            r.getGuestName() + " | " +
                            r.getRoomType()
            );
        }

        System.out.println("\nInventory:");
        for (String type : inventory.getAvailability().keySet()) {
            System.out.println(type + " → Available: " +
                    inventory.getAvailability().get(type));
        }

        // Step 4: Save state before shutdown
        PersistenceService.save(inventory, history);

        System.out.println("\n🔁 Restart the program to see recovery in action.");
    }
}