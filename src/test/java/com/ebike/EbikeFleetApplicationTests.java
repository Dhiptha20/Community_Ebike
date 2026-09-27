package com.ebike;

import com.ebike.dto.*;
import com.ebike.model.*;
import com.ebike.repository.*;
import com.ebike.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EbikeFleetApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BikeService bikeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private TripService tripService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private DockingStationRepository stationRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    @DisplayName("TC-01: Register rider successfully")
    @Transactional
    void testRegisterRider() {
        RiderRegistrationDto dto = new RiderRegistrationDto();
        dto.setUsername("testrider99");
        dto.setPassword("password123");
        dto.setFullName("Test Rider 99");
        dto.setEmail("testrider99@example.com");
        dto.setPhone("+91 9999988888");

        User savedUser = userService.registerRider(dto);
        assertNotNull(savedUser.getId());
        assertEquals("testrider99", savedUser.getUsername());
        assertEquals(Role.ROLE_RIDER, savedUser.getRole());
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));

        Rider rider = userService.getRiderByUsername("testrider99");
        assertNotNull(rider);
        assertEquals("Test Rider 99", rider.getName());
    }

    @Test
    @DisplayName("TC-02: User login and credential validation")
    void testLogin() {
        UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
        assertNotNull(adminDetails);
        assertTrue(passwordEncoder.matches("admin123", adminDetails.getPassword()));
        assertTrue(adminDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("TC-03: Add new e-bike")
    @Transactional
    void testAddBike() {
        BikeDto dto = new BikeDto();
        dto.setBikeCode("EB-TEST-01");
        dto.setBatteryLevel(90);
        dto.setStatus(BikeStatus.AVAILABLE);
        dto.setGpsLatitude(12.9800);
        dto.setGpsLongitude(77.6000);

        Bike bike = bikeService.createBike(dto);
        assertNotNull(bike.getBikeId());
        assertEquals("EB-TEST-01", bike.getBikeCode());
        assertEquals(BikeStatus.AVAILABLE, bike.getStatus());
        assertEquals(90, bike.getBatteryLevel());
    }

    @Test
    @DisplayName("TC-04: Add new docking station")
    @Transactional
    void testAddDockingStation() {
        StationDto dto = new StationDto();
        dto.setStationName("Test Metro Station");
        dto.setLocation("Platform 1 East");
        dto.setTotalSlots(10);
        dto.setStatus(StationStatus.ACTIVE);

        DockingStation station = stationService.createStation(dto);
        assertNotNull(station.getStationId());
        assertEquals(10, station.getTotalSlots());
        assertEquals(10, station.getAvailableSlots());
        assertEquals(0, station.getOccupiedSlots());
    }

    @Test
    @DisplayName("TC-05: Record battery and GPS telemetry")
    @Transactional
    void testRecordTelemetry() {
        Bike bike = bikeRepository.findAll().get(0);

        TelemetryDto dto = new TelemetryDto();
        dto.setBikeId(bike.getBikeId());
        dto.setBatteryLevel(82);
        dto.setLatitude(12.9725);
        dto.setLongitude(77.5930);

        BatteryTelemetry telemetry = telemetryService.recordTelemetry(dto);
        assertNotNull(telemetry.getTelemetryId());
        assertEquals(82, telemetry.getBatteryLevel());
        assertFalse(telemetry.isLowBatteryAlert());

        Bike updatedBike = bikeService.getBikeById(bike.getBikeId());
        assertEquals(82, updatedBike.getBatteryLevel());
    }

    @Test
    @DisplayName("TC-06: Battery below 20% triggers automatic alert")
    @Transactional
    void testBatteryBelow20() {
        Bike bike = bikeRepository.findAll().get(0);

        TelemetryDto dto = new TelemetryDto();
        dto.setBikeId(bike.getBikeId());
        dto.setBatteryLevel(14); // < 20%
        dto.setLatitude(12.9730);
        dto.setLongitude(77.5940);

        BatteryTelemetry telemetry = telemetryService.recordTelemetry(dto);
        assertTrue(telemetry.isLowBatteryAlert(), "Battery under 20% must trigger lowBatteryAlert = true");

        Bike updatedBike = bikeService.getBikeById(bike.getBikeId());
        assertTrue(updatedBike.isLowBattery());
    }

    @Test
    @DisplayName("TC-07: Unlock bike and initiate trip")
    @Transactional
    void testUnlockBike() {
        Rider rider = riderRepository.findAll().stream()
                .filter(r -> tripRepository.findByRiderAndStatus(r, TripStatus.ONGOING).isEmpty())
                .findFirst().orElseThrow();

        Bike availableBike = bikeRepository.findByStatus(BikeStatus.AVAILABLE).get(0);

        TripStartDto startDto = new TripStartDto();
        startDto.setRiderId(rider.getRiderId());
        startDto.setBikeId(availableBike.getBikeId());

        Trip trip = tripService.unlockBikeAndStartTrip(startDto);
        assertNotNull(trip.getTripId());
        assertEquals(TripStatus.ONGOING, trip.getStatus());

        Bike bikeNow = bikeService.getBikeById(availableBike.getBikeId());
        assertEquals(BikeStatus.IN_USE, bikeNow.getStatus());
    }

    @Test
    @DisplayName("TC-08: Complete trip and calculate dynamic fare")
    @Transactional
    void testCompleteTrip() {
        Rider rider = riderRepository.findAll().stream()
                .filter(r -> tripRepository.findByRiderAndStatus(r, TripStatus.ONGOING).isEmpty())
                .findFirst().orElseThrow();

        Bike availableBike = bikeRepository.findByStatus(BikeStatus.AVAILABLE).get(0);
        DockingStation endStation = stationRepository.findAll().get(0);

        TripStartDto startDto = new TripStartDto();
        startDto.setRiderId(rider.getRiderId());
        startDto.setBikeId(availableBike.getBikeId());
        Trip trip = tripService.unlockBikeAndStartTrip(startDto);

        TripEndDto endDto = new TripEndDto();
        endDto.setTripId(trip.getTripId());
        endDto.setEndStationId(endStation.getStationId());
        endDto.setDistanceKm(5.0); // 5 km distance

        Trip completedTrip = tripService.completeTrip(endDto);
        assertEquals(TripStatus.COMPLETED, completedTrip.getStatus());
        assertTrue(completedTrip.getFare() > 0.0);
        // Base fare 10 + 1 min * 2 + 5 km * 5 = 10 + 2 + 25 = 37.00
        assertEquals(37.0, completedTrip.getFare());

        Bike bikeNow = bikeService.getBikeById(availableBike.getBikeId());
        assertEquals(BikeStatus.AVAILABLE, bikeNow.getStatus());
    }

    @Test
    @DisplayName("TC-09: Automatic invoice generation upon trip completion")
    @Transactional
    void testGenerateInvoice() {
        Rider rider = riderRepository.findAll().stream()
                .filter(r -> tripRepository.findByRiderAndStatus(r, TripStatus.ONGOING).isEmpty())
                .findFirst().orElseThrow();

        Bike availableBike = bikeRepository.findByStatus(BikeStatus.AVAILABLE).get(0);
        DockingStation endStation = stationRepository.findAll().get(0);

        TripStartDto startDto = new TripStartDto();
        startDto.setRiderId(rider.getRiderId());
        startDto.setBikeId(availableBike.getBikeId());
        Trip trip = tripService.unlockBikeAndStartTrip(startDto);

        TripEndDto endDto = new TripEndDto();
        endDto.setTripId(trip.getTripId());
        endDto.setEndStationId(endStation.getStationId());
        endDto.setDistanceKm(3.0);

        Trip completedTrip = tripService.completeTrip(endDto);

        Optional<Invoice> invoiceOpt = invoiceRepository.findByTrip(completedTrip);
        assertTrue(invoiceOpt.isPresent(), "Invoice must be automatically generated for completed trip");
        Invoice invoice = invoiceOpt.get();
        assertEquals(InvoiceStatus.UNPAID, invoice.getStatus());
        assertEquals(completedTrip.getFare(), invoice.getAmount());
    }

    @Test
    @DisplayName("TC-10: Make payment and update invoice to PAID")
    @Transactional
    void testMakePayment() {
        Invoice unpaidInvoice = invoiceRepository.findAll().stream()
                .filter(i -> i.getStatus() == InvoiceStatus.UNPAID)
                .findFirst().orElseThrow();

        PaymentDto payDto = new PaymentDto();
        payDto.setInvoiceId(unpaidInvoice.getInvoiceId());
        payDto.setAmount(unpaidInvoice.getAmount());
        payDto.setPaymentMethod(PaymentMethod.UPI);

        Payment payment = paymentService.processPayment(payDto);
        assertNotNull(payment.getPaymentId());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());

        Invoice updatedInvoice = invoiceService.getInvoiceById(unpaidInvoice.getInvoiceId());
        assertEquals(InvoiceStatus.PAID, updatedInvoice.getStatus());

        // Check that double-entry journal entry was posted
        List<JournalEntry> entries = accountingService.getAllJournalEntries();
        assertFalse(entries.isEmpty());
        assertTrue(entries.stream().anyMatch(e -> e.getReference().equals(payment.getTransactionRef())));
    }

    @Test
    @DisplayName("TC-11: Budget variance calculation")
    @Transactional
    void testBudgetVariance() {
        BudgetDto dto = new BudgetDto();
        dto.setSector("Testing Sector");
        dto.setAllocatedFund(100000.0);
        dto.setActualSpend(25000.0);
        dto.setFiscalYear("2024-2025");

        Budget budget = budgetService.saveBudget(dto);
        assertEquals(75000.0, budget.getVariance(), "Variance must equal allocatedFund - actualSpend");

        budgetService.recordSpend("Testing Sector", 10000.0);
        Budget updated = budgetService.getBudgetBySector("Testing Sector").orElseThrow();
        assertEquals(35000.0, updated.getActualSpend());
        assertEquals(65000.0, updated.getVariance());
    }

    @Test
    @DisplayName("TC-12: Role-based access control authorities")
    void testRoleBasedAccess() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        assertTrue(admin.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        UserDetails finance = userDetailsService.loadUserByUsername("finance");
        assertTrue(finance.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INVOICING_USER")));

        UserDetails rider = userDetailsService.loadUserByUsername("rider1");
        assertTrue(rider.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_RIDER")));
    }
}
