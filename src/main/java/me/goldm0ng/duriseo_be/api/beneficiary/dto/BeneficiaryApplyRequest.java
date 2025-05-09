package me.goldm0ng.duriseo_be.api.beneficiary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BeneficiaryApplyRequest {
    @NotBlank
    private String documentUrl;
}
