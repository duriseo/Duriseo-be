package me.goldm0ng.duriseo_be.api.voucher.controller;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.voucher.dto.VoucherRequest;
import me.goldm0ng.duriseo_be.api.voucher.dto.VouchersResponse;
import me.goldm0ng.duriseo_be.api.voucher.service.VoucherService;
import me.goldm0ng.duriseo_be.common.response.APISuccessResponse;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/vouchers")
public class VoucherController {

    private final VoucherService voucherService;

    @PostMapping("/{restaurant_id}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<APISuccessResponse<Void>> issueVouchers(@RequestBody VoucherRequest request,
                                                              @PathVariable(name = "restaurant_id") Long restaurant_id) {
        return APISuccessResponse.of(HttpStatus.CREATED, voucherService.issueVouchers(request, restaurant_id));
    }

    @PostMapping("/donate/{restaurant_id}")
    @PreAuthorize("hasRole('DONOR')")
    public ResponseEntity<APISuccessResponse<Void>> issueVouchersByDonor(@RequestBody VoucherRequest request,
                                                                  @PathVariable(name = "restaurant_id") Long restaurant_id) {
        return APISuccessResponse.of(HttpStatus.CREATED, voucherService.issueVouchersByDonor(request, restaurant_id));
    }

    @PostMapping("/acquired/{voucher_id}")
    @PreAuthorize("hasRole('BENEFICIARY')")
    public ResponseEntity<APISuccessResponse<Void>> acquiredVoucher(@PathVariable(name = "voucher_id") Long voucherId) {
        return APISuccessResponse.of(HttpStatus.OK, voucherService.acquiredVoucher(voucherId));
    }

    @GetMapping
    @PreAuthorize("hasRole('BENEFICIARY')")
    public ResponseEntity<APISuccessResponse<VouchersResponse>> listVouchers() {
        return APISuccessResponse.of(HttpStatus.OK, voucherService.listVouchers());
    }

    @PostMapping("redeem/{voucher_id}")
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<APISuccessResponse<Void>> redeemVoucher(@PathVariable(name = "voucher_id") Long voucherId) {
        return APISuccessResponse.of(HttpStatus.OK, voucherService.redeemVoucher(voucherId));
    }

}
