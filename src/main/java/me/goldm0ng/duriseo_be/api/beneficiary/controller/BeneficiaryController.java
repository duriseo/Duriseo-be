package me.goldm0ng.duriseo_be.api.beneficiary.controller;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryApplyRequest;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryProfilesResponse;
import me.goldm0ng.duriseo_be.api.beneficiary.dto.BeneficiaryResponse;
import me.goldm0ng.duriseo_be.api.beneficiary.service.BeneficiaryService;
import me.goldm0ng.duriseo_be.common.response.APISuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('BENEFICIARY')")
    public ResponseEntity<APISuccessResponse<BeneficiaryResponse>> apply(@Validated @RequestBody BeneficiaryApplyRequest request) {
        return APISuccessResponse.of(HttpStatus.CREATED, beneficiaryService.apply(request));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APISuccessResponse<BeneficiaryProfilesResponse>> read() {
        return APISuccessResponse.of(HttpStatus.OK, beneficiaryService.read());
    }

    @PostMapping("/{userId}/admin/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APISuccessResponse<Void>> approve(@PathVariable(name = "userId") Long userId) {
        return APISuccessResponse.of(HttpStatus.OK, beneficiaryService.approve(userId));
    }

    @PostMapping("/{userId}/admin/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APISuccessResponse<Void>> reject(@PathVariable(name = "userId") Long userId) {
        return APISuccessResponse.of(HttpStatus.OK, beneficiaryService.reject(userId));
    }
}

