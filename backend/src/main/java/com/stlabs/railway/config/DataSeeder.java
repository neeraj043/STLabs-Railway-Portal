package com.stlabs.railway.config;

import com.stlabs.railway.model.Train;
import com.stlabs.railway.model.TrainAvailability;
import com.stlabs.railway.repository.TrainAvailabilityRepository;
import com.stlabs.railway.repository.TrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedTrains(TrainRepository repository, TrainAvailabilityRepository availabilityRepository) {
        return args -> {
            List<Train> trains = Arrays.asList(
                    new Train("ST101", "Deccan Star", "Mumbai Central", "Pune Junction", LocalTime.of(6, 15), LocalTime.of(9, 40), 120, 84, new BigDecimal("450.00"), 205),
                    new Train("ST204", "Coastal Express", "Mumbai Central", "Goa Central", LocalTime.of(7, 30), LocalTime.of(16, 10), 180, 62, new BigDecimal("980.00"), 520),
                    new Train("ST315", "Western Link", "Pune Junction", "Ahmedabad Central", LocalTime.of(8, 0), LocalTime.of(17, 25), 150, 103, new BigDecimal("1250.00"), 565),
                    new Train("ST422", "Blue Mountain", "Bengaluru City", "Chennai Central", LocalTime.of(9, 20), LocalTime.of(15, 5), 160, 9, new BigDecimal("730.00"), 345),
                    new Train("ST518", "Capital Runner", "New Delhi", "Jaipur Junction", LocalTime.of(10, 10), LocalTime.of(14, 45), 140, 47, new BigDecimal("640.00"), 275),
                    new Train("ST609", "Riverland Mail", "Kolkata Terminal", "Patna Junction", LocalTime.of(11, 40), LocalTime.of(19, 30), 190, 130, new BigDecimal("860.00"), 470),
                    new Train("ST711", "Southern Comet", "Chennai Central", "Bengaluru City", LocalTime.of(13, 5), LocalTime.of(18, 30), 160, 76, new BigDecimal("690.00"), 325),
                    new Train("ST808", "Night Owl", "New Delhi", "Mumbai Central", LocalTime.of(20, 15), LocalTime.of(8, 10), 220, 4, new BigDecimal("1490.00"), 715),
                    new Train("ST913", "Southern Western Express", "Chennai Central", "Ahmedabad Central", LocalTime.of(18, 30), LocalTime.of(8, 45), 180, 56, new BigDecimal("1780.00"), 855),
                    new Train("ST914", "Gujarat Southern", "Ahmedabad Central", "Chennai Central", LocalTime.of(19, 10), LocalTime.of(9, 25), 180, 72, new BigDecimal("1780.00"), 855),
                    new Train("ST102", "Pune City Express", "Pune Junction", "Mumbai Central", LocalTime.of(7, 0), LocalTime.of(10, 25), 120, 91, new BigDecimal("450.00"), 205),
                    new Train("ST205", "Konkan Coast", "Goa Central", "Mumbai Central", LocalTime.of(8, 20), LocalTime.of(17, 0), 180, 105, new BigDecimal("980.00"), 520),
                    new Train("ST316", "Ahmedabad Deccan", "Ahmedabad Central", "Pune Junction", LocalTime.of(6, 45), LocalTime.of(16, 10), 150, 88, new BigDecimal("1250.00"), 565),
                    new Train("ST423", "Bengaluru Blue", "Chennai Central", "Bengaluru City", LocalTime.of(10, 15), LocalTime.of(15, 55), 160, 97, new BigDecimal("730.00"), 340),
                    new Train("ST519", "Pink City Express", "Jaipur Junction", "New Delhi", LocalTime.of(15, 30), LocalTime.of(20, 5), 140, 80, new BigDecimal("640.00"), 275),
                    new Train("ST610", "Patna Riverland", "Patna Junction", "Kolkata Terminal", LocalTime.of(6, 25), LocalTime.of(14, 15), 190, 113, new BigDecimal("860.00"), 470),
                    new Train("ST809", "Mumbai Nightliner", "Mumbai Central", "New Delhi", LocalTime.of(21, 0), LocalTime.of(8, 55), 220, 92, new BigDecimal("1490.00"), 715),
                    new Train("ST111", "Capital Pune Express", "New Delhi", "Pune Junction", LocalTime.of(16, 15), LocalTime.of(7, 30), 200, 118, new BigDecimal("1680.00"), 915),
                    new Train("ST112", "Deccan Capital", "Pune Junction", "New Delhi", LocalTime.of(17, 0), LocalTime.of(8, 15), 200, 126, new BigDecimal("1680.00"), 915),
                    new Train("ST221", "Chennai Coastliner", "Chennai Central", "Goa Central", LocalTime.of(17, 20), LocalTime.of(7, 10), 190, 106, new BigDecimal("1420.00"), 830),
                    new Train("ST222", "Goa Southern Mail", "Goa Central", "Chennai Central", LocalTime.of(18, 0), LocalTime.of(7, 50), 190, 114, new BigDecimal("1420.00"), 830),
                    new Train("ST331", "Ahmedabad Capital", "New Delhi", "Ahmedabad Central", LocalTime.of(20, 30), LocalTime.of(9, 0), 210, 135, new BigDecimal("1190.00"), 750),
                    new Train("ST332", "Capital Gujarat", "Ahmedabad Central", "New Delhi", LocalTime.of(19, 45), LocalTime.of(8, 15), 210, 122, new BigDecimal("1190.00"), 750),
                    new Train("ST441", "Garden City Express", "Bengaluru City", "Hyderabad Central", LocalTime.of(7, 10), LocalTime.of(15, 20), 170, 104, new BigDecimal("810.00"), 490),
                    new Train("ST442", "Deccan Garden Mail", "Hyderabad Central", "Bengaluru City", LocalTime.of(8, 0), LocalTime.of(16, 10), 170, 98, new BigDecimal("810.00"), 490),
                    new Train("ST551", "Nizam Coastal Express", "Hyderabad Central", "Mumbai Central", LocalTime.of(18, 30), LocalTime.of(7, 45), 210, 147, new BigDecimal("1340.00"), 795),
                    new Train("ST552", "Coastal Nizam Mail", "Mumbai Central", "Hyderabad Central", LocalTime.of(19, 20), LocalTime.of(8, 35), 210, 139, new BigDecimal("1340.00"), 795),
                    new Train("ST661", "Eastern Capital Express", "Kolkata Terminal", "New Delhi", LocalTime.of(16, 40), LocalTime.of(11, 0), 230, 160, new BigDecimal("1580.00"), 1100),
                    new Train("ST662", "Capital Eastern Mail", "New Delhi", "Kolkata Terminal", LocalTime.of(17, 30), LocalTime.of(11, 50), 230, 152, new BigDecimal("1580.00"), 1100),
                    new Train("ST771", "Ganga Valley Express", "Patna Junction", "Varanasi Junction", LocalTime.of(9, 15), LocalTime.of(13, 30), 150, 87, new BigDecimal("520.00"), 255),
                    new Train("ST772", "Varanasi Ganga Mail", "Varanasi Junction", "Patna Junction", LocalTime.of(14, 20), LocalTime.of(18, 35), 150, 93, new BigDecimal("520.00"), 255),
                    new Train("ST881", "Desert Coast Express", "Jaipur Junction", "Ahmedabad Central", LocalTime.of(6, 10), LocalTime.of(13, 40), 160, 116, new BigDecimal("920.00"), 450),
                    new Train("ST882", "Coast Desert Mail", "Ahmedabad Central", "Jaipur Junction", LocalTime.of(15, 0), LocalTime.of(22, 30), 160, 109, new BigDecimal("920.00"), 450),
                    new Train("ST991", "Malabar Intercity", "Bengaluru City", "Goa Central", LocalTime.of(6, 30), LocalTime.of(14, 45), 170, 121, new BigDecimal("990.00"), 495),
                    new Train("ST992", "Western Ghats Express", "Goa Central", "Bengaluru City", LocalTime.of(15, 30), LocalTime.of(23, 45), 170, 117, new BigDecimal("990.00"), 495),
                    new Train("ST113", "Pune Hyderabad Link", "Pune Junction", "Hyderabad Central", LocalTime.of(7, 45), LocalTime.of(17, 0), 160, 101, new BigDecimal("870.00"), 555),
                    new Train("ST114", "Hyderabad Pune Link", "Hyderabad Central", "Pune Junction", LocalTime.of(8, 30), LocalTime.of(17, 45), 160, 95, new BigDecimal("870.00"), 555)
            );
            List<String> existingCodes = repository.findAll().stream().map(Train::getCode).collect(Collectors.toList());
            List<Train> missingTrains = trains.stream()
                    .filter(train -> !existingCodes.contains(train.getCode()))
                    .collect(Collectors.toList());
            repository.saveAll(missingTrains);

            List<Train> allTrains = repository.findAll();
            Map<String, List<Train>> trainsByRoute = allTrains.stream()
                    .collect(Collectors.groupingBy(train -> train.getOrigin() + "\u0000" + train.getDestination()));
            Set<String> allCodes = allTrains.stream().map(Train::getCode).collect(Collectors.toCollection(HashSet::new));
            List<Train> additionalTrains = new ArrayList<>();
            for (List<Train> routeTrains : trainsByRoute.values()) {
                if (routeTrains.size() != 1) continue;
                Train firstTrain = routeTrains.get(0);
                String secondCode = "X" + firstTrain.getCode();
                if (allCodes.add(secondCode)) {
                    additionalTrains.add(new Train(secondCode, "Daylight " + firstTrain.getName(),
                            firstTrain.getOrigin(), firstTrain.getDestination(),
                            firstTrain.getDepartureTime().plusHours(3), firstTrain.getArrivalTime().plusHours(3),
                            firstTrain.getTotalSeats(), firstTrain.getAvailableSeats(), firstTrain.getFare(),
                            firstTrain.getDurationMinutes()));
                }
            }
            repository.saveAll(additionalTrains);

            List<Train> completeSchedule = repository.findAll();
            Set<String> stations = completeSchedule.stream()
                    .flatMap(train -> java.util.stream.Stream.of(train.getOrigin(), train.getDestination()))
                    .collect(Collectors.toCollection(java.util.TreeSet::new));
            Set<String> scheduledRoutes = completeSchedule.stream()
                    .map(train -> train.getOrigin() + "\u0000" + train.getDestination())
                    .collect(Collectors.toCollection(HashSet::new));
            Set<String> completeCodes = completeSchedule.stream().map(Train::getCode)
                    .collect(Collectors.toCollection(HashSet::new));
            List<String> orderedStations = new ArrayList<>(stations);
            List<Train> allRouteTrains = new ArrayList<>();
            for (int originIndex = 0; originIndex < orderedStations.size(); originIndex++) {
                for (int destinationIndex = 0; destinationIndex < orderedStations.size(); destinationIndex++) {
                    if (originIndex == destinationIndex) continue;
                    String origin = orderedStations.get(originIndex);
                    String destination = orderedStations.get(destinationIndex);
                    if (!scheduledRoutes.add(origin + "\u0000" + destination)) continue;
                    String baseCode = String.format("R%02d%02d", originIndex + 1, destinationIndex + 1);
                    String code = baseCode;
                    int suffix = 1;
                    while (!completeCodes.add(code)) code = baseCode + "X" + suffix++;
                    int durationMinutes = 180 + Math.abs(originIndex - destinationIndex) * 35
                            + ((originIndex + destinationIndex) % 4) * 10;
                    LocalTime departure = LocalTime.of(6 + ((originIndex * 3 + destinationIndex) % 16),
                            ((originIndex + destinationIndex) % 4) * 15);
                    allRouteTrains.add(new Train(code,
                            origin.replaceAll(" (Central|Junction|Terminal|City)$", "") + " " + destination
                                    .replaceAll(" (Central|Junction|Terminal|City)$", "") + " Connector",
                            origin, destination, departure, departure.plusMinutes(durationMinutes),
                            150, 120, BigDecimal.valueOf(500 + Math.abs(originIndex - destinationIndex) * 100L),
                            durationMinutes));
                }
            }
            repository.saveAll(allRouteTrains);

            List<Train> fullSchedule = repository.findAll();
            Set<String> seededAvailability = availabilityRepository.findAll().stream()
                    .map(availability -> availability.getTrain().getId() + ":" + availability.getTravelDate())
                    .collect(Collectors.toCollection(HashSet::new));
            List<TrainAvailability> nextThirtyDays = new ArrayList<>();
            LocalDate firstDate = LocalDate.now();
            for (int day = 0; day < 30; day++) {
                LocalDate travelDate = firstDate.plusDays(day);
                for (Train train : fullSchedule) {
                    if (seededAvailability.add(train.getId() + ":" + travelDate)) {
                        nextThirtyDays.add(new TrainAvailability(train, travelDate, train.getAvailableSeats()));
                    }
                }
            }
            availabilityRepository.saveAll(nextThirtyDays);
        };
    }
}
