package com.breadhardit.travelagencykata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CreationalPatternExercices {
    /*
     * Banking accounts has movements. And each movement can be a deposit or withdrawal
     * After a few months operating, we need to create new types of movements:
     *   - TRANSFER: It's a withdrawal, bet we need the destination account number
     *   - ANNULMENT: It cancels a movement, so, we need the original movement
     */
    public enum MovementType {DEPOSIT, WITHDRAWAL, TRANSFER, ANNULMENT}

    public interface Movement {
        String getId();

        void apply(Account account);

        void revert(Account account);
    }

    @Data
    @AllArgsConstructor
    public static class DepositMovement implements Movement {
        String id;
        String description;
        Long amount;

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public void apply(Account account) {
            account.setBalance(account.getBalance() + amount);
        }

        @Override
        public void revert(Account account) {
            account.setBalance(account.getBalance() - amount);
        }
    }

    @Data
    @AllArgsConstructor
    public static class WithdrawalMovement implements Movement {
        String id;
        String description;
        Long amount;

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public void apply(Account account) {
            account.setBalance(account.getBalance() - amount);
        }

        @Override
        public void revert(Account account) {
            account.setBalance(account.getBalance() + amount);
        }
    }

    @Data
    @AllArgsConstructor
    public static class TransferMovement implements Movement {
        String id;
        String description;
        Long amount;
        Account destination;

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public void apply(Account account) {
            account.setBalance(account.getBalance() - amount);
            destination.setBalance(destination.getBalance() + amount);

        }

        @Override
        public void revert(Account account) {
            account.setBalance(account.getBalance() + amount);
            destination.setBalance(destination.getBalance() - amount);
        }
    }

    @Data
    @AllArgsConstructor
    public static class AnnulmentMovement implements Movement {
        String id;
        String description;
        Movement movement;

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public void apply(Account account) {
            movement.revert(account);
        }

        @Override
        public void revert(Account account) {
            movement.apply(account);
        }
    }

    public static class MovementFactory {
        public static Movement createMovement(String id, MovementType movementType, Long amount, String description,
                                              Account destination, Movement originalMovement) {
            return switch (movementType) {
                case DEPOSIT -> new DepositMovement(id, description, amount);
                case WITHDRAWAL -> new WithdrawalMovement(id, description, amount);
                case TRANSFER -> new TransferMovement(id, description, amount, destination);
                case ANNULMENT -> new AnnulmentMovement(id, description, originalMovement);
            };
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class Account {
        public static final ConcurrentHashMap<String, Movement> MOVEMENTS = new ConcurrentHashMap<>();
        final String id;
        Long balance = 0L;

        public void addMovement(Movement movement) {
            MOVEMENTS.put(movement.getId(), movement);
            movement.apply(this);
            log.info("Current balance: {}", balance);
        }

        public Movement getMovement(String id) {
            return MOVEMENTS.get(id);
        }
    }

    @Test
    public void test() {
        Account account = new Account(UUID.randomUUID().toString());
        account.addMovement(MovementFactory.createMovement("1", MovementType.DEPOSIT, 1000L, "INGRESO", null, null));
        account.addMovement(MovementFactory.createMovement("2", MovementType.WITHDRAWAL, 10L, "GASTOS VARIOS", null, null));
        account.addMovement(MovementFactory.createMovement("3", MovementType.TRANSFER, 10L, "GASTOS VARIOS", new Account(UUID.randomUUID().toString()), null));
        account.addMovement(MovementFactory.createMovement("4", MovementType.ANNULMENT, 10L, "GASTOS VARIOS", null, account.getMovement("1")));
    /* TODO
        Made the refactor to create new movement types, and avoid scalability issues applying the proper creational pattern.
        Remember, our code MUST follow SOLID Principles, so refactor the classes you need to accomplish it
     */
    }
}
