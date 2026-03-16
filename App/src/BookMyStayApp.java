public class BookMyStayApp {
    /**
     * The HotelBookingApplication class serves as the entry point for the
     * Hotel Booking System. It marks the logical boundary of the program
     * and provides the initial startup logic.
     * * @author User
     * @version 1.0
     */
    public class HotelBookingApplication {

        /**
         * The main method is the entry point of every standalone Java application.
         * The JVM looks specifically for this method signature to start execution.
         * * @param args Command-line arguments (not used in this version).
         */
        public static void main(String[] args) {

            // Use System.out.println to send text output to the console.
            // String literals are stored in the String pool and are immutable.
            System.out.println("========================================");
            System.out.println("   Welcome to the Hotel Booking System  ");
            System.out.println("========================================");

            // Displaying application metadata
            System.out.println("Application Name: StayEase Manager");
            System.out.println("Version: 1.0.0-SNAPSHOT");
            System.out.println("Status: Initialized successfully.");

            /* * Execution proceeds top to bottom. Once the last line is reached,
             * the main method finishes and the application terminates.
             */
        }
    }
}
