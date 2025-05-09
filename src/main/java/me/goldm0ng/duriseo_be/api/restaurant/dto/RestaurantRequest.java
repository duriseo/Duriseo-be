package me.goldm0ng.duriseo_be.api.restaurant.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RestaurantRequest {
    private String name;
    private String address;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
