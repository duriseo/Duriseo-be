package me.goldm0ng.duriseo_be.api.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RestaurantsResponse {
    private List<RestaurantResponse> restaurants;
}
