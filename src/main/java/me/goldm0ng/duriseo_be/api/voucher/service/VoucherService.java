package me.goldm0ng.duriseo_be.api.voucher.service;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.voucher.dto.VoucherRequest;
import me.goldm0ng.duriseo_be.api.voucher.dto.VoucherResponse;
import me.goldm0ng.duriseo_be.api.voucher.dto.VouchersResponse;
import me.goldm0ng.duriseo_be.common.exception.DuriseoException;
import me.goldm0ng.duriseo_be.db.restaurant.entity.Restaurant;
import me.goldm0ng.duriseo_be.db.restaurant.repository.RestaurantRepository;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.db.user.repository.UserRepository;
import me.goldm0ng.duriseo_be.db.voucher.entity.Voucher;
import me.goldm0ng.duriseo_be.db.voucher.repository.VoucherRepository;
import me.goldm0ng.duriseo_be.enums.message.FailMessage;
import me.goldm0ng.duriseo_be.enums.user.Role;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public Void issueVouchers(VoucherRequest request, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_RESTAURANT));

        for (int i = 0; i <request.getCount() ; i++) {
            Voucher voucher = Voucher.builder()
                    .code(String.valueOf(UUID.randomUUID()))
                    .restaurant(restaurant)
                    .issuedBy(restaurant.getOwner())
                    .issuedByRole(Role.RESTAURANT_OWNER)
                    .status(VoucherStatus.ISSUED)
                    .recipient(null)
                    .expiredAt(request.getExpiredAt())
                    .createdAt(LocalDateTime.now())
                    .usedAt(null)
                    .build();

            voucherRepository.save(voucher);
        }
        return null;
    }

    @Transactional
    public Void acquiredVoucher(Long voucherId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_USER));

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_VOUCHER));
        if (voucher.getRecipient() != null) {
            throw new DuriseoException(FailMessage.FORBIDDEN);
        }
        if (voucher.getExpiredAt() != null && voucher.getExpiredAt().isBefore(LocalDate.now())) {
            throw new DuriseoException(FailMessage.NOT_FOUND_VOUCHER_BY_EXPIRED);
        }

        voucher.setRecipient(user);
        voucher.setCreatedAt(LocalDateTime.now());
        voucher.setStatus(VoucherStatus.REDEEMED_TO_OTHER);
        voucherRepository.save(voucher);

        return null;
    }

    public VouchersResponse listVouchers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_USER));

        List<Voucher> vouchers = new ArrayList<>();
        for (Voucher voucher : voucherRepository.findAll()) {
            if (voucher.getRecipient() != null && voucher.getRecipient().equals(user)) {
                vouchers.add(voucher);
            }
        }

        List<VoucherResponse> dtos = vouchers.stream()
                .map(v -> new VoucherResponse(
                        v.getId(),
                        v.getRestaurant().getId(),
                        v.getIssuedBy().getId(),
                        v.getIssuedByRole(),
                        v.getCode(),
                        v.getStatus(),
                        v.getRecipient() != null ? v.getRecipient().getId() : null,
                        v.getExpiredAt(),
                        v.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new VouchersResponse(dtos);
    }
}
