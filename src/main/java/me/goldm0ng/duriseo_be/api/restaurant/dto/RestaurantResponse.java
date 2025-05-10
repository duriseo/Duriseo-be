package me.goldm0ng.duriseo_be.api.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private Long ownerId;
    private List<Long> voucherIds;
    private int remainingVouchers;
    private String address;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime createdAt;
}
