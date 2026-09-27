package com.ebike.config;

import com.ebike.model.*;
import com.ebike.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private DockingStationRepository stationRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private RentalTariffRepository tariffRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private BatteryTelemetryRepository telemetryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Initialize Users
        User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@ebikemobility.in", "System Administrator", "+91 9845012345", Role.ROLE_ADMIN);
        User finance = new User("finance", passwordEncoder.encode("finance123"), "finance@ebikemobility.in", "Finance & Invoicing Officer", "+91 9845023456", Role.ROLE_INVOICING_USER);
        userRepository.save(admin);
        userRepository.save(finance);

        // 2. Initialize Riders and Rider Users
        User riderUser1 = new User("rider1", passwordEncoder.encode("rider123"), "rahul.sharma@example.in", "Rahul Sharma", "+91 9876543001", Role.ROLE_RIDER);
        User riderUser2 = new User("rider2", passwordEncoder.encode("rider123"), "priya.patel@example.in", "Priya Patel", "+91 9876543002", Role.ROLE_RIDER);
        User riderUser3 = new User("rider3", passwordEncoder.encode("rider123"), "amit.verma@example.in", "Amit Verma", "+91 9876543003", Role.ROLE_RIDER);
        User riderUser4 = new User("rider4", passwordEncoder.encode("rider123"), "ananya.iyer@example.in", "Ananya Iyer", "+91 9876543004", Role.ROLE_RIDER);
        User riderUser5 = new User("rider5", passwordEncoder.encode("rider123"), "vikram.singh@example.in", "Vikram Singh", "+91 9876543005", Role.ROLE_RIDER);
        userRepository.save(riderUser1);
        userRepository.save(riderUser2);
        userRepository.save(riderUser3);
        userRepository.save(riderUser4);
        userRepository.save(riderUser5);

        Rider rider1 = new Rider(riderUser1, riderUser1.getFullName(), riderUser1.getEmail(), riderUser1.getPhone(), "ACTIVE");
        Rider rider2 = new Rider(riderUser2, riderUser2.getFullName(), riderUser2.getEmail(), riderUser2.getPhone(), "ACTIVE");
        Rider rider3 = new Rider(riderUser3, riderUser3.getFullName(), riderUser3.getEmail(), riderUser3.getPhone(), "ACTIVE");
        Rider rider4 = new Rider(riderUser4, riderUser4.getFullName(), riderUser4.getEmail(), riderUser4.getPhone(), "ACTIVE");
        Rider rider5 = new Rider(riderUser5, riderUser5.getFullName(), riderUser5.getEmail(), riderUser5.getPhone(), "ACTIVE");
        riderRepository.save(rider1);
        riderRepository.save(rider2);
        riderRepository.save(rider3);
        riderRepository.save(rider4);
        riderRepository.save(rider5);

        // 3. Initialize Docking Stations
        DockingStation st1 = new DockingStation("Metro Station Central", "MG Road Metro Station Gate 2, Bangalore", 15, 13, StationStatus.ACTIVE);
        DockingStation st2 = new DockingStation("Tech Park Hub", "Outer Ring Road EcoSpace, Bangalore", 20, 17, StationStatus.ACTIVE);
        DockingStation st3 = new DockingStation("City Square Gardens", "Cubbon Park North Entrance, Bangalore", 12, 10, StationStatus.ACTIVE);
        stationRepository.save(st1);
        stationRepository.save(st2);
        stationRepository.save(st3);

        // 4. Initialize E-Bikes
        Bike b1 = new Bike("EB-101", st1, 95, BikeStatus.AVAILABLE, 12.9750, 77.6050);
        Bike b2 = new Bike("EB-102", st1, 15, BikeStatus.AVAILABLE, 12.9740, 77.6060); // Low Battery
        Bike b3 = new Bike("EB-103", st2, 88, BikeStatus.AVAILABLE, 12.9260, 77.6830);
        Bike b4 = new Bike("EB-104", st2, 75, BikeStatus.AVAILABLE, 12.9270, 77.6840);
        Bike b5 = new Bike("EB-105", null, 60, BikeStatus.IN_USE, 12.9500, 77.6400); // On trip
        Bike b6 = new Bike("EB-106", st2, 40, BikeStatus.MAINTENANCE, 12.9280, 77.6850);
        Bike b7 = new Bike("EB-107", st3, 90, BikeStatus.AVAILABLE, 12.9770, 77.5910);
        Bike b8 = new Bike("EB-108", st3, 18, BikeStatus.CHARGING, 12.9780, 77.5920); // Low Battery
        bikeRepository.save(b1);
        bikeRepository.save(b2);
        bikeRepository.save(b3);
        bikeRepository.save(b4);
        bikeRepository.save(b5);
        bikeRepository.save(b6);
        bikeRepository.save(b7);
        bikeRepository.save(b8);

        // 5. Initialize Rental Tariff
        RentalTariff tariff = new RentalTariff("Standard City Tariff", 10.0, 2.0, 5.0, true);
        tariffRepository.save(tariff);

        // 6. Initialize Telemetry Logs
        telemetryRepository.save(new BatteryTelemetry(b1, 95, 12.9750, 77.6050, LocalDateTime.now().minusHours(1)));
        telemetryRepository.save(new BatteryTelemetry(b2, 15, 12.9740, 77.6060, LocalDateTime.now().minusMinutes(20))); // Alert!
        telemetryRepository.save(new BatteryTelemetry(b3, 88, 12.9260, 77.6830, LocalDateTime.now().minusHours(2)));
        telemetryRepository.save(new BatteryTelemetry(b5, 60, 12.9500, 77.6400, LocalDateTime.now().minusMinutes(5)));
        telemetryRepository.save(new BatteryTelemetry(b8, 18, 12.9780, 77.5920, LocalDateTime.now().minusMinutes(15))); // Alert!

        // 7. Initialize Chart of Accounts
        Account cash = new Account("1010", "Cash and Operating Bank", AccountType.ASSET, 250000.0);
        Account ar = new Account("1020", "Accounts Receivable", AccountType.ASSET, 1200.0);
        Account ap = new Account("2010", "Accounts Payable", AccountType.LIABILITY, 15000.0);
        Account equity = new Account("3010", "Municipal Fleet Equity", AccountType.EQUITY, 300000.0);
        Account rentalRevenue = new Account("4010", "Rider Rental Revenue", AccountType.REVENUE, 45000.0);
        Account maintExpense = new Account("5010", "Fleet Maintenance Expense", AccountType.EXPENSE, 18000.0);
        Account chargingExpense = new Account("5020", "Battery Charging Expense", AccountType.EXPENSE, 6500.0);
        accountRepository.save(cash);
        accountRepository.save(ar);
        accountRepository.save(ap);
        accountRepository.save(equity);
        accountRepository.save(rentalRevenue);
        accountRepository.save(maintExpense);
        accountRepository.save(chargingExpense);

        // 8. Initialize Sector Budgets
        budgetRepository.save(new Budget("Fleet Maintenance", 150000.0, 35000.0, "2024-2025"));
        budgetRepository.save(new Budget("Docking Stations", 200000.0, 60000.0, "2024-2025"));
        budgetRepository.save(new Budget("Battery Charging", 80000.0, 22000.0, "2024-2025"));
        budgetRepository.save(new Budget("Fleet Rebalancing", 50000.0, 15000.0, "2024-2025"));
        budgetRepository.save(new Budget("Operations", 100000.0, 30000.0, "2024-2025"));

        // 9. Initialize Vendors
        Vendor v1 = new Vendor("VoltRide Dynamics Ltd.", "Rajesh Kumar", "contact@voltride.in", "+91 9876543110", "Peenya Industrial Area 2nd Stage, Bangalore", "ACTIVE");
        Vendor v2 = new Vendor("EcoBattery Solutions India", "Suresh Mehta", "sales@ecobattery.in", "+91 9876543111", "Electronic City Phase 1, Bangalore", "ACTIVE");
        Vendor v3 = new Vendor("SmartDock Urban Systems", "Deepa Rao", "support@urbandock.in", "+91 9876543112", "Whitefield Tech Corridor, Bangalore", "ACTIVE");
        vendorRepository.save(v1);
        vendorRepository.save(v2);
        vendorRepository.save(v3);

        // 10. Initialize Purchase Orders
        PurchaseOrder po1 = new PurchaseOrder(v1, "Replacement brake pads & motor firmware check for 5 e-bikes", 8500.0, LocalDate.now().minusDays(10), POStatus.COMPLETED, "Fleet Maintenance");
        PurchaseOrder po2 = new PurchaseOrder(v2, "Procurement of 10 rapid lithium-ion spare battery packs", 28000.0, LocalDate.now().minusDays(5), POStatus.APPROVED, "Battery Charging");
        PurchaseOrder po3 = new PurchaseOrder(v3, "Smart RFID locking pin upgrades for Docking Station 2", 12500.0, LocalDate.now().minusDays(2), POStatus.CREATED, "Docking Stations");
        purchaseOrderRepository.save(po1);
        purchaseOrderRepository.save(po2);
        purchaseOrderRepository.save(po3);

        // 11. Initialize Sample Trips, Invoices & Payments
        // Trip 1 - Completed
        Trip trip1 = new Trip();
        trip1.setRider(rider1);
        trip1.setBike(b1);
        trip1.setStartStation(st1);
        trip1.setEndStation(st2);
        trip1.setStartTime(LocalDateTime.now().minusHours(3));
        trip1.setEndTime(LocalDateTime.now().minusHours(2).minusMinutes(35));
        trip1.setDurationMinutes(25L);
        trip1.setDistanceKm(4.2);
        trip1.setFare(81.0); // 10 base + 25*2 + 4.2*5 = 10 + 50 + 21 = 81
        trip1.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip1);

        Invoice inv1 = new Invoice(rider1, trip1, 81.0, LocalDate.now().plusDays(5), InvoiceStatus.PAID);
        invoiceRepository.save(inv1);

        Payment pay1 = new Payment(inv1, rider1, 81.0, PaymentMethod.UPI, PaymentStatus.SUCCESS);
        pay1.setTransactionRef("TXN-UPI-" + System.currentTimeMillis());
        paymentRepository.save(pay1);

        // Trip 2 - Completed, Unpaid
        Trip trip2 = new Trip();
        trip2.setRider(rider2);
        trip2.setBike(b3);
        trip2.setStartStation(st2);
        trip2.setEndStation(st3);
        trip2.setStartTime(LocalDateTime.now().minusHours(5));
        trip2.setEndTime(LocalDateTime.now().minusHours(4).minusMinutes(45));
        trip2.setDurationMinutes(15L);
        trip2.setDistanceKm(3.0);
        trip2.setFare(55.0); // 10 + 15*2 + 3*5 = 10 + 30 + 15 = 55
        trip2.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip2);

        Invoice inv2 = new Invoice(rider2, trip2, 55.0, LocalDate.now().plusDays(6), InvoiceStatus.UNPAID);
        invoiceRepository.save(inv2);

        // Trip 3 - Ongoing for rider3 on bike EB-105
        Trip trip3 = new Trip();
        trip3.setRider(rider3);
        trip3.setBike(b5);
        trip3.setStartStation(st1);
        trip3.setStartTime(LocalDateTime.now().minusMinutes(18));
        trip3.setStatus(TripStatus.ONGOING);
        tripRepository.save(trip3);

        // 12. Initialize Sample Journal Entries
        journalEntryRepository.save(new JournalEntry(LocalDateTime.now().minusHours(2), "Rider rental fare payment for Trip #1", pay1.getTransactionRef(), cash, rentalRevenue, 81.0));
        journalEntryRepository.save(new JournalEntry(LocalDateTime.now().minusDays(10), "Fleet maintenance expense for PO #1", po1.getPoNumber(), maintExpense, cash, 8500.0));
    }
}
