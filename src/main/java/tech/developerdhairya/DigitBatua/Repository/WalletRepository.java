package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.developerdhairya.DigitBatua.Entity.WalletEntity;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {
}