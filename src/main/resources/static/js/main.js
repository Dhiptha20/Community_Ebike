/**
 * Community Shared E-Bike & Micromobility Fleet Network
 * Main JavaScript Interactivity (Leaflet GPS, Chart.js, Modals, Forms)
 */

document.addEventListener('DOMContentLoaded', function () {
    // Enable Bootstrap tooltips if any
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Sidebar toggle for mobile devices
    const sidebarToggleBtn = document.getElementById('sidebarToggle');
    const appSidebar = document.querySelector('.app-sidebar');
    if (sidebarToggleBtn && appSidebar) {
        sidebarToggleBtn.addEventListener('click', function () {
            appSidebar.classList.toggle('show');
        });
    }

    // Quick demo login credential filler
    window.fillCredentials = function (username, password) {
        const userInput = document.getElementById('username');
        const passInput = document.getElementById('password');
        if (userInput && passInput) {
            userInput.value = username;
            passInput.value = password;
        }
    };
});

/**
 * Initialize Leaflet OpenStreetMap for GPS fleet tracking (No paid API key required)
 */
function initFleetMap(bikesData, stationsData) {
    const mapElement = document.getElementById('fleetMap');
    if (!mapElement || typeof L === 'undefined') return;

    // Center on Bangalore/Sample coordinate default
    const map = L.map('fleetMap').setView([12.9716, 77.5946], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        maxZoom: 18
    }).addTo(map);

    // Plot Docking Stations
    if (stationsData && Array.isArray(stationsData)) {
        stationsData.forEach(st => {
            // Place stations around realistic coordinates if not specified
            const lat = 12.9716 + (Math.random() - 0.5) * 0.05;
            const lng = 77.5946 + (Math.random() - 0.5) * 0.05;

            const stationIcon = L.divIcon({
                className: 'custom-station-pin',
                html: `<div style="background-color: #0284c7; color: white; border-radius: 50%; width: 34px; height: 34px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 10px rgba(2,132,199,0.4); border: 2px solid white;"><i class="fa-solid fa-charging-station"></i></div>`,
                iconSize: [34, 34],
                iconAnchor: [17, 17]
            });

            L.marker([lat, lng], { icon: stationIcon })
                .addTo(map)
                .bindPopup(`<b>Station:</b> ${st.stationName}<br><b>Slots:</b> ${st.availableSlots}/${st.totalSlots} Available<br><b>Status:</b> ${st.status}`);
        });
    }

    // Plot E-Bikes
    if (bikesData && Array.isArray(bikesData)) {
        bikesData.forEach(b => {
            if (b.gpsLatitude && b.gpsLongitude) {
                let color = '#10b981'; // Green for Available
                if (b.batteryLevel < 20) {
                    color = '#ef4444'; // Red for Low Battery
                } else if (b.status === 'IN_USE') {
                    color = '#6366f1'; // Indigo for In Use
                } else if (b.status === 'MAINTENANCE') {
                    color = '#f59e0b'; // Amber for Maintenance
                }

                const bikeIcon = L.divIcon({
                    className: 'custom-bike-pin',
                    html: `<div style="background-color: ${color}; color: white; border-radius: 50%; width: 32px; height: 32px; display: flex; align-items: center; justify-content: center; box-shadow: 0 4px 8px rgba(0,0,0,0.25); border: 2px solid white;"><i class="fa-solid fa-bicycle" style="font-size: 14px;"></i></div>`,
                    iconSize: [32, 32],
                    iconAnchor: [16, 16]
                });

                L.marker([b.gpsLatitude, b.gpsLongitude], { icon: bikeIcon })
                    .addTo(map)
                    .bindPopup(`
                        <div style="min-width: 150px;">
                            <h6 style="margin: 0 0 4px 0; font-weight: 700; color: #1e293b;">Bike ${b.bikeCode}</h6>
                            <p style="margin: 0; font-size: 12px; color: #64748b;">
                                <b>Battery:</b> ${b.batteryLevel}% ${b.batteryLevel < 20 ? '<span style="color:red; font-weight:bold;">(LOW)</span>' : ''}<br>
                                <b>Status:</b> ${b.status}<br>
                                <b>Coords:</b> ${b.gpsLatitude.toFixed(4)}, ${b.gpsLongitude.toFixed(4)}
                            </p>
                        </div>
                    `);
            }
        });
    }
}

/**
 * Open Pay Invoice Modal
 */
function openPaymentModal(invoiceId, amount, riderId) {
    const invInput = document.getElementById('payInvoiceId');
    const amtInput = document.getElementById('payAmount');
    const riderInput = document.getElementById('payRiderId');
    const invLabel = document.getElementById('payInvoiceLabel');

    if (invInput) invInput.value = invoiceId;
    if (amtInput) amtInput.value = amount;
    if (riderInput) riderInput.value = riderId;
    if (invLabel) invLabel.innerText = 'Invoice #' + invoiceId + ' — ₹' + amount;

    const modalEl = document.getElementById('paymentModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

/**
 * Open Unlock Bike Modal for Rider
 */
function openUnlockModal(bikeId, bikeCode, batteryLevel) {
    const bikeInput = document.getElementById('unlockBikeId');
    const codeLabel = document.getElementById('unlockBikeCode');
    const batteryLabel = document.getElementById('unlockBattery');

    if (bikeInput) bikeInput.value = bikeId;
    if (codeLabel) codeLabel.innerText = bikeCode;
    if (batteryLabel) batteryLabel.innerText = batteryLevel + '%';

    const modalEl = document.getElementById('unlockModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

/**
 * Open End Trip Modal for Rider
 */
function openEndTripModal(tripId, bikeCode) {
    const tripInput = document.getElementById('endTripId');
    const tripLabel = document.getElementById('endTripLabel');

    if (tripInput) tripInput.value = tripId;
    if (tripLabel) tripLabel.innerText = 'Trip #' + tripId + ' (' + bikeCode + ')';

    const modalEl = document.getElementById('endTripModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

/**
 * Open Edit Bike Modal for Admin
 */
function openEditBikeModal(bikeId, bikeCode, battery, status, lat, lng, stationId) {
    const form = document.getElementById('editBikeForm');
    if (form) form.action = '/bikes/edit/' + bikeId;

    document.getElementById('editBikeCode').value = bikeCode;
    document.getElementById('editBatteryLevel').value = battery;
    document.getElementById('editStatus').value = status;
    document.getElementById('editLatitude').value = lat;
    document.getElementById('editLongitude').value = lng;
    if (stationId) {
        document.getElementById('editStationId').value = stationId;
    }

    const modalEl = document.getElementById('editBikeModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

/**
 * Open Edit Station Modal for Admin
 */
function openEditStationModal(stationId, name, location, totalSlots, availableSlots, status) {
    const form = document.getElementById('editStationForm');
    if (form) form.action = '/stations/edit/' + stationId;

    document.getElementById('editStationName').value = name;
    document.getElementById('editLocation').value = location;
    document.getElementById('editTotalSlots').value = totalSlots;
    document.getElementById('editAvailableSlots').value = availableSlots;
    document.getElementById('editStationStatus').value = status;

    const modalEl = document.getElementById('editStationModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

/**
 * Open Edit Vendor Modal for Invoicing
 */
function openEditVendorModal(vendorId, name, contact, email, phone, address, status) {
    const form = document.getElementById('editVendorForm');
    if (form) form.action = '/vendors/edit/' + vendorId;

    document.getElementById('editVendorName').value = name;
    document.getElementById('editContactPerson').value = contact;
    document.getElementById('editEmail').value = email;
    document.getElementById('editPhone').value = phone;
    document.getElementById('editAddress').value = address;
    document.getElementById('editVendorStatus').value = status;

    const modalEl = document.getElementById('editVendorModal');
    if (modalEl) {
        const modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}
