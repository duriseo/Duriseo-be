package me.goldm0ng.duriseo_be.api.beneficiary.service;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryApplyRequest;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryResponse;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryProfilesResponse;
import me.goldm0ng.duriseo_be.common.exception.DuriseoException;
import me.goldm0ng.duriseo_be.db.beneficiary.entity.BeneficiaryProfile;
import me.goldm0ng.duriseo_be.db.beneficiary.repository.BeneficiaryProfileRepository;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.db.user.repository.UserRepository;
import me.goldm0ng.duriseo_be.enums.beneficiary.VerificationStatus;
import me.goldm0ng.duriseo_be.enums.message.FailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryProfileRepository  profileRepository;
    private final UserRepository userRepository;

    @Transactional
    public BeneficiaryResponse apply(BeneficiaryApplyRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_USER));

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw new DuriseoException(FailMessage.CONFLICT_DOCUMENT);
        }

        BeneficiaryProfile profile = BeneficiaryProfile.builder()
                .user(user)
                .documentUrl(request.getDocumentUrl())
                .status(VerificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        profileRepository.save(profile);

        return new BeneficiaryResponse(
                profile.getId(),
                user.getId(),
                profile.getDocumentUrl(),
                profile.getStatus().name(),
                profile.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public BeneficiaryProfilesResponse read() {
        return new BeneficiaryProfilesResponse(
                profileRepository.findAll().stream().map(profile -> new BeneficiaryResponse(
                        profile.getId(),
                        profile.getUser().getId(),
                        profile.getDocumentUrl(),
                        profile.getStatus().name(),
                        profile.getCreatedAt()
                )).toList());
    }

    @Transactional
    public Void approve(Long userId) {
        BeneficiaryProfile beneficiaryProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_BENEFICIARY));
        if (beneficiaryProfile.getStatus().name().equals("PENDING")) {
            profileRepository.updateStatus(userId, VerificationStatus.APPROVED);
        }
        return null;
    }

    @Transactional
    public Void reject(Long userId) {
        BeneficiaryProfile beneficiaryProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_BENEFICIARY));
        if (beneficiaryProfile.getStatus().name().equals("PENDING")) {
            profileRepository.updateStatus(userId, VerificationStatus.REJECTED);
        }
        return null;
    }
}
