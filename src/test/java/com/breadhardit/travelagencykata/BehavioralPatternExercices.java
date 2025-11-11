package com.breadhardit.travelagencykata;

import com.breadhardit.travelagencykata.domain.Customer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class BehavioralPatternExercices {
    /* EXERCISE 1
        Travels has an origin and a destination. Travels have some restrictions:
        - Travels with origin and destination in the same country require only Identity Document
        - Travels with origin and destination in schengen space, requires Passport
        - Travels with origin or destination out of schengen space, requires Visa
     */
    @Data
    @SuperBuilder
    public static abstract class Travel {
        @Builder.Default
        String id = UUID.randomUUID().toString();
        String name;
        String origin;
        String destination;

        public abstract void scanDocument();
    }

    @NoArgsConstructor
    public static class TravelFactory {
        public static final List<String> SCHENGEN_COUNTRIES = List.of("Spain", "France", "Iceland", "Italy", "Portugal");

        public Travel buildTravel(String name, String origin, String destination) {
            if (origin.equals(destination)) {
                return NationalTravel.builder().name(name).origin(origin).destination(destination).build();
            } else if (SCHENGEN_COUNTRIES.contains(origin) && SCHENGEN_COUNTRIES.contains(destination)) {
                return SchengenTravel.builder().name(name).origin(origin).destination(destination).build();
            } else {
                return InternationalTravel.builder().name(name).origin(origin).destination(destination).build();
            }
        }
    }

    @SuperBuilder
    public static class NationalTravel extends Travel {
        @Override
        public void scanDocument() {
            log.info("Scanning DNI...");
        }
    }

    @SuperBuilder
    public static class SchengenTravel extends Travel {
        @Override
        public void scanDocument() {
            log.info("Scanning passport...");
        }
    }

    @SuperBuilder
    public static class InternationalTravel extends Travel {
        @Override
        public void scanDocument() {
            log.info("Scanning visa...");
        }
    }

    @Test
    // When customer buy a new Travel we have to scan the proper documentation
    public void travelAgency() {
        TravelFactory travelFactory = new TravelFactory();
        List<Travel> travels = List.of(
                travelFactory.buildTravel("PYRAMIDS TOUR", "Spain", "EGYPT"),
                travelFactory.buildTravel("LISBOA TOUR", "Spain", "Portugal"),
                travelFactory.buildTravel("LISBOA TOUR", "Portugal", "Portugal")
        );
        for (Travel travel : travels) {
            travel.scanDocument();
        }
    }
    // Refactor code using the proper structural pattern


    /*
     * When a new employee is enrolled, company sends a greetins e-mail.
     * A notification service is querying the database every second looking for new employees to notify
     */
    @Builder
    @Data
    public static class Employee {
        final String id;
        final String name;
        final String email;
        @Builder.Default
        Boolean greetingDone = Boolean.FALSE;
    }

    public interface NotificationObserver {
        void notifyEmployee();
    }

    @Value
    public static class Notificator implements NotificationObserver {
        EmployeesRepository employeesRepository;

        @Override
        public void notifyEmployee() {
            employeesRepository.getUnnotifiedEmployees().forEach(e -> {
                log.info("Notifying {}", e);
                e.setGreetingDone(Boolean.TRUE);
            });
        }
    }

    public static class EmployeesRepository {
        private static final ConcurrentHashMap<String, Employee> EMPLOYEES = new ConcurrentHashMap<>();

        public void addEmployee(Employee employee) {
            EMPLOYEES.put(employee.getId(), employee);
        }

        public List<Employee> getUnnotifiedEmployees() {
            return EMPLOYEES.values().stream().filter(e -> !e.greetingDone).toList();
        }
    }


    @Slf4j
    @AllArgsConstructor
    public static class GreetingsNotificator {
        List<NotificationObserver> observers;
        @SneakyThrows
        public void applyNotifications() {
                log.info("Applying notifications");
                observers.forEach(NotificationObserver::notifyEmployee);
                Thread.sleep(100);
        }
    }

    @Test
    @SneakyThrows
    public void companyTest() {
        EmployeesRepository employeesRepository = new EmployeesRepository();
        Notificator notificator = new Notificator(employeesRepository);
        GreetingsNotificator greetingsNotificator = new GreetingsNotificator(List.of(notificator));
        Thread.sleep(200);
        employeesRepository.addEmployee(Employee.builder().id("1").name("Pepe").email("pepe@pepemail.com").build());
        Thread.sleep(200);
        employeesRepository.addEmployee(Employee.builder().id("2").name("Juan").email("pepe@pepemail.com").build());
        new Thread(greetingsNotificator::applyNotifications).start();
    }
    // Use the proper behavioral pattern to avoid the continuous querying to database
}