package me.goldm0ng.duriseo_be.api.voucher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.goldm0ng.duriseo_be.enums.user.Role;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VoucherResponse {
    private Long id;
    private Long restaurantId;
    private Long issuedUserId;
    private Role issuedRole;
    private String code;
    private VoucherStatus status;
    private Long recipientId;
    private LocalDate expireAt;
    private LocalDateTime createdAt;
}
