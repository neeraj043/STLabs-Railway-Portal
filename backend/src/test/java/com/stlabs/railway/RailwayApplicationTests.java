package com.stlabs.railway;

import com.stlabs.railway.model.Train;
import com.stlabs.railway.model.TrainAvailability;
import com.stlabs.railway.repository.TrainAvailabilityRepository;
import com.stlabs.railway.repository.TrainRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RailwayApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TrainAvailabilityRepository availabilityRepository;

    @Test
    void seededTrainsCanBeBookedListedAndCancelled() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/trains"))
                .andExpect(status().isUnauthorized());

        MvcResult signup = mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"name\":\"Test Passenger\",\"email\":\"test@example.com\",\"password\":\"secure-pass-123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andReturn();
        MockHttpSession session = (MockHttpSession) signup.getRequest().getSession(false);
        mockMvc.perform(get("/api/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Passenger"));

        mockMvc.perform(get("/api/trains").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("ST101"))
                .andExpect(jsonPath("$[0].serviceFrequency").value("DAILY"))
                .andExpect(jsonPath("$[8].code").value("ST913"))
                .andExpect(jsonPath("$[8].origin").value("Chennai Central"))
                .andExpect(jsonPath("$[8].destination").value("Ahmedabad Central"))
                .andExpect(jsonPath("$.length()").value(168));

        List<Train> allTrains = trainRepository.findAll();
        List<String> stations = allTrains.stream()
                .flatMap(train -> java.util.stream.Stream.of(train.getOrigin(), train.getDestination()))
                .distinct().collect(Collectors.toList());
        Map<String, Long> routeCounts = allTrains.stream().collect(Collectors.groupingBy(
                train -> train.getOrigin() + " -> " + train.getDestination(), Collectors.counting()));
        org.junit.jupiter.api.Assertions.assertEquals(12, stations.size());
        org.junit.jupiter.api.Assertions.assertEquals(stations.size() * (stations.size() - 1), routeCounts.size());
        for (String origin : stations) {
            for (String destination : stations) {
                if (!origin.equals(destination)) {
                    org.junit.jupiter.api.Assertions.assertTrue(routeCounts.containsKey(origin + " -> " + destination),
                            "Missing direct route " + origin + " -> " + destination);
                }
            }
        }
        for (int day = 0; day < 30; day++) {
            LocalDate travelDate = LocalDate.now().plusDays(day);
            List<TrainAvailability> dailyAvailability = availabilityRepository.findAllByTravelDate(travelDate);
            org.junit.jupiter.api.Assertions.assertEquals(168, dailyAvailability.size());
            org.junit.jupiter.api.Assertions.assertTrue(dailyAvailability.stream()
                    .allMatch(availability -> availability.getAvailableSeats() > 0),
                    "Every scheduled train must have seats available on " + travelDate);
            long dailyRouteCount = dailyAvailability.stream()
                    .filter(availability -> availability.getAvailableSeats() > 0)
                    .map(availability -> availability.getTrain().getOrigin() + " -> "
                            + availability.getTrain().getDestination())
                    .distinct().count();
            org.junit.jupiter.api.Assertions.assertEquals(132, dailyRouteCount,
                    "Every station pair must have an available train on " + travelDate);
            mockMvc.perform(get("/api/trains?date=" + travelDate).session(session))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(168));
        }

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"trainId\":1,\"passengerName\":\"Test Passenger\",\"email\":\"test@example.com\",\"seats\":1,\"travelDate\":\"2030-01-15\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        mockMvc.perform(get("/api/bookings").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].passengerName").value("Test Passenger"))
                .andExpect(jsonPath("$[0].trainCode").value("ST101"));

        Train outbound = trainRepository.findAll().stream()
                .filter(train -> "ST101".equals(train.getCode())).findFirst().orElseThrow();
        Train returning = trainRepository.findAll().stream()
                .filter(train -> "ST102".equals(train.getCode())).findFirst().orElseThrow();
        mockMvc.perform(get("/api/hotels?city=Pune").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].city").value("Pune"));

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"trainId\":" + outbound.getId()
                                + ",\"passengerName\":\"Aanya Sharma\",\"passengerNames\":[\"Aanya Sharma\"]"
                                + ",\"email\":\"test@example.com\",\"seats\":1,\"travelDate\":\"2030-01-15\","
                                + "\"travelInsurance\":{\"insuredName\":\"Aanya Sharma\",\"insuredDateOfBirth\":\"1990-04-20\","
                                + "\"insuredGender\":\"FEMALE\",\"insuredPhone\":\"9876543210\","
                                + "\"insuredEmail\":\"aanya@example.com\",\"insuredAddress\":\"12 Sample Road, Pune\","
                                + "\"nomineeName\":\"Ravi Sharma\",\"nomineeRelationship\":\"SPOUSE\","
                                + "\"nomineeDateOfBirth\":\"1988-08-12\"}}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"trainId\":" + outbound.getId()
                                + ",\"passengerName\":\"Aanya Sharma\",\"passengerNames\":[\"Aanya Sharma\",\"Ravi Sharma\"]"
                                + ",\"email\":\"test@example.com\",\"seats\":2,\"travelDate\":\"2030-01-15\",\"mealPreference\":\"JAIN\","
                                + "\"travelInsurance\":{\"insuredName\":\"Aanya Sharma\",\"insuredDateOfBirth\":\"1990-04-20\","
                                + "\"insuredGender\":\"FEMALE\",\"insuredPhone\":\"9876543210\","
                                + "\"insuredEmail\":\"aanya@example.com\",\"insuredAddress\":\"12 Sample Road, Pune\","
                                + "\"nomineeName\":\"Ravi Sharma\",\"nomineeRelationship\":\"SPOUSE\","
                                + "\"nomineeDateOfBirth\":\"1988-08-12\",\"nomineePhone\":\"9876543211\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalFare").value(902.0))
                .andExpect(jsonPath("$.insurancePremium").value(2.0))
                .andExpect(jsonPath("$.insuranceCoverPerPerson").value(200000))
                .andExpect(jsonPath("$.mealPreference").value("JAIN"))
                .andExpect(jsonPath("$.insurancePolicyNumber").value(org.hamcrest.Matchers.startsWith("STI-")));

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"trainId\":" + outbound.getId()
                                + ",\"passengerName\":\"Test Passenger\",\"email\":\"test@example.com\","
                                + "\"seats\":1,\"travelDate\":\"2030-01-15\",\"mealPreference\":\"GLUTEN_FREE\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"trainId\":" + outbound.getId()
                                + ",\"returnTrainId\":" + returning.getId()
                                + ",\"passengerName\":\"Aanya Sharma\",\"passengerNames\":[\"Aanya Sharma\",\"Ravi Sharma\"]"
                                + ",\"email\":\"test@example.com\",\"seats\":2,\"travelDate\":\"2030-01-15\",\"returnDate\":\"2030-01-20\",\"mealPreference\":\"VEG\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seats").value(2))
                .andExpect(jsonPath("$.mealPreference").value("VEG"))
                .andExpect(jsonPath("$.passengers.length()").value(2))
                .andExpect(jsonPath("$.returnTrainCode").value("ST102"))
                .andExpect(jsonPath("$.returnDate").value("2030-01-20"))
                .andExpect(jsonPath("$.totalFare").value(1800.0));

        mockMvc.perform(get("/api/trains?date=2030-01-15").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availableSeats").value(79));
        mockMvc.perform(get("/api/trains?date=2030-01-16").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availableSeats").value(84));

        mockMvc.perform(get("/api/bookings/1").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        mockMvc.perform(get("/api/bookings/999999").session(session))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/bookings/1/cancel").session(session).with(csrf())
                        .contentType("application/json")
                        .content("{\"refundMethod\":\"ORIGINAL_ACCOUNT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.refundMethod").value("ORIGINAL_ACCOUNT"))
                .andExpect(jsonPath("$.trainName", not(isEmptyOrNullString())));
        mockMvc.perform(post("/api/bookings/1/cancel").session(session).with(csrf())
                        .contentType("application/json")
                        .content("{\"refundMethod\":\"CREDIT_CARD\"}"))
                .andExpect(status().isConflict());
        mockMvc.perform(get("/api/bookings/1").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
        mockMvc.perform(get("/api/trains?date=2030-01-15").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].availableSeats").value(80));

        mockMvc.perform(post("/api/hotel-bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"hotelId\":\"pune-hotel-1\",\"checkIn\":\"2030-01-15\","
                                + "\"checkOut\":\"2030-01-17\",\"guests\":2,\"guestName\":\"Aanya Sharma\","
                                + "\"email\":\"test@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.confirmationNumber").value(org.hamcrest.Matchers.startsWith("STH-")))
                .andExpect(jsonPath("$.city").value("Pune"))
                .andExpect(jsonPath("$.totalPrice").value(6400.0));
        mockMvc.perform(get("/api/hotel-bookings").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hotelName").value("Pune Grand Hotel"));
        MvcResult anotherSignup = mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"name\":\"Another Passenger\",\"email\":\"another@example.com\",\"password\":\"secure-pass-123\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        mockMvc.perform(get("/api/hotel-bookings")
                        .session((MockHttpSession) anotherSignup.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        mockMvc.perform(post("/api/hotel-bookings")
                        .session(session)
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"hotelId\":\"pune-hotel-1\",\"checkIn\":\"2030-01-15\","
                                + "\"checkOut\":\"2030-01-15\",\"guests\":2,\"guestName\":\"Aanya Sharma\","
                                + "\"email\":\"test@example.com\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/auth/logout").session(session).with(csrf()))
                .andExpect(status().isNoContent());

        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content("{\"email\":\"test@example.com\",\"password\":\"secure-pass-123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andReturn();
        mockMvc.perform(get("/api/auth/me").session((MockHttpSession) login.getRequest().getSession(false)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}
