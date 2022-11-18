package tech.developerdhairya.DigitBatua.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.developerdhairya.DigitBatua.Entity.UserTokenEntity;

import java.util.UUID;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, UUID> {
}