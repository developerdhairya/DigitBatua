package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.developerdhairya.DigitBatua.Entity.TransactionEntity;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}