package me.goldm0ng.duriseo_be.api.voucher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VouchersResponse {
    private List<VoucherResponse> vouchers;
}
