package me.goldm0ng.duriseo_be.api.voucher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VoucherRequest {

    @NotBlank
    private int count;

    @NotBlank
    private LocalDate expiredAt;
}
