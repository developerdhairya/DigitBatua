package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.developerdhairya.DigitBatua.Entity.AppUserEntity;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUserEntity, UUID> {
}