package me.goldm0ng.duriseo_be.api.beneficiary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BeneficiaryResponse {
    private Long id;
    private Long userId;
    private String documentUrl;
    private String status;
    private LocalDateTime createdAt;
}
