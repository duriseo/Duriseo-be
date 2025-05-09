package me.goldm0ng.duriseo_be.db.beneficiary.repository;

import me.goldm0ng.duriseo_be.db.beneficiary.entity.BeneficiaryProfile;
import me.goldm0ng.duriseo_be.enums.beneficiary.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeneficiaryProfileRepository extends JpaRepository<BeneficiaryProfile, Long> {
    Optional<BeneficiaryProfile> findByUserId(Long id);
    @Modifying
    @Query("""
      UPDATE BeneficiaryProfile p
      SET p.status = :newStatus
      WHERE p.user.id = :userId 
        AND p.status = 'PENDING'
      """)
    void updateStatus(@Param("userId") Long userId,
                     @Param("newStatus") VerificationStatus newStatus);
}
