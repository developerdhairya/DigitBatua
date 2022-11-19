package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.developerdhairya.DigitBatua.Entity.Funding;

import java.util.UUID;

public interface FundingRepository extends JpaRepository<Funding, UUID> {
}