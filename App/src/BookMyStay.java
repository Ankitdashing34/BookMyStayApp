import java.util.*;

public class BookMyStay {

    // Reservation (Guest booking)
    static class Reservation {
        private String guestName;
        private String roomType;
        private String reservationId;

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

    // Booking Service
    static class BookingService {

        private RoomInventory inventory;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
        }

        public void processBooking(Reservation r) {

            String roomId = inventory.allocateRoom(r.getRoomType());

            if (roomId != null) {
                r.setReservationId(roomId);

                System.out.println("\n✅ Booking Confirmed");
                System.out.println("Guest: " + r.getGuestName());
                System.out.println("Room: " + r.getRoomType());
                System.out.println("Reservation ID: " + roomId);
            } else {
                System.out.println("\n❌ Booking Failed for " + r.getGuestName());
            }
        }
    }

    // Add-On Service
    static class AddOnService {
        private String name;
        private double price;

        public AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    // Add-On Service Manager
    static class AddOnServiceManager {

        // reservationId → list of services
        private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        // Add services to reservation
        public void addServices(String reservationId, List<AddOnService> services) {
            serviceMap.put(reservationId, services);
        }

        // Calculate total add-on cost
        public double calculateTotalCost(String reservationId) {
            List<AddOnService> services = serviceMap.getOrDefault(reservationId, new ArrayList<>());
            double total = 0;

            for (AddOnService s : services) {
                total += s.getPrice();
            }
            return total;
        }

        // Display services
        public void displayServices(String reservationId) {
            List<AddOnService> services = serviceMap.get(reservationId);

            if (services == null || services.isEmpty()) {
                System.out.println("No add-on services selected.");
                return;
            }

            System.out.println("Add-On Services:");
            for (AddOnService s : services) {
                System.out.println("- " + s.getName() + " (₹" + s.getPrice() + ")");
            }

            System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
        }
    }

    // Main method
    public static void main(String[] args) {

        System.out.println("Welcome to Hotel Booking App v6.0\n");

        // Setup
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Standard Room", 2);

        BookingQueue queue = new BookingQueue();
        queue.addRequest(new Reservation("Alice", "Standard Room"));

        BookingService bookingService = new BookingService(inventory);

        // Process booking
        Reservation r = queue.getNextRequest();
        bookingService.processBooking(r);

        // Add-On Services (independent feature)
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService breakfast = new AddOnService("Breakfast", 300);

        List<AddOnService> selectedServices = new ArrayList<>();
        selectedServices.add(wifi);
        selectedServices.add(breakfast);

        AddOnServiceManager manager = new AddOnServiceManager();

        // Map services to reservation
        manager.addServices(r.getReservationId(), selectedServices);

        // Display services + cost
        System.out.println("\nFor Reservation ID: " + r.getReservationId());
        manager.displayServices(r.getReservationId());
    }
}