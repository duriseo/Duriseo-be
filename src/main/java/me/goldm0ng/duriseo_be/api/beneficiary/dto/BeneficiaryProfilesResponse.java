package me.goldm0ng.duriseo_be.api.beneficiary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BeneficiaryProfilesResponse {
    private List<BeneficiaryResponse> beneficiariesProfiles;
}
