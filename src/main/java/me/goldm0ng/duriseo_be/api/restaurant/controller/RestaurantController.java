package me.goldm0ng.duriseo_be.api.restaurant.controller;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantRequest;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantResponse;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantsResponse;
import me.goldm0ng.duriseo_be.api.restaurant.service.RestaurantService;
import me.goldm0ng.duriseo_be.common.response.APISuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("${api.prefix}/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping()
    @PreAuthorize("hasRole('RESTAURANT_OWNER')")
    public ResponseEntity<APISuccessResponse<Void>> signUpRestaurant(@Validated @RequestBody RestaurantRequest request) {
        return APISuccessResponse.of(HttpStatus.CREATED, restaurantService.signUpRestaurant(request));
    }

//    @GetMapping("/{restaurant_id}")
//    public ResponseEntity<APISuccessResponse<RestaurantResponse>> findRestaurant(@PathVariable(name = "restaurant_id") Long restaurant_id) {
//        return APISuccessResponse.of(HttpStatus.OK, restaurantService.findRestaurant(restaurant_id));
//    }

    @GetMapping()
    public ResponseEntity<APISuccessResponse<RestaurantsResponse>> findAllRestaurants() {
        return APISuccessResponse.of(HttpStatus.OK, restaurantService.findAllRestaurants());
    }


}
