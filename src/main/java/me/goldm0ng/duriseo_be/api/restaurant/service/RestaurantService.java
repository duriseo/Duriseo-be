package me.goldm0ng.duriseo_be.api.restaurant.service;

import lombok.RequiredArgsConstructor;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantResponse;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantsResponse;
import me.goldm0ng.duriseo_be.api.restaurant.dto.RestaurantRequest;
import me.goldm0ng.duriseo_be.common.exception.DuriseoException;
import me.goldm0ng.duriseo_be.db.restaurant.entity.Restaurant;
import me.goldm0ng.duriseo_be.db.restaurant.repository.RestaurantRepository;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.db.user.repository.UserRepository;
import me.goldm0ng.duriseo_be.db.voucher.repository.VoucherRepository;
import me.goldm0ng.duriseo_be.enums.message.FailMessage;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;

    public Void signUpRestaurant(RestaurantRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();;
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DuriseoException(FailMessage.NOT_FOUND_USER));


        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .owner(user)
                .build();

        restaurantRepository.save(restaurant);

        return null;
    }

    @Transactional(readOnly = true)
    public RestaurantResponse findRestaurant(Long restaurant_id) {
        Restaurant restaurant = restaurantRepository.findById(restaurant_id)
                .orElseThrow(()-> new DuriseoException(FailMessage.NOT_FOUND_RESTAURANT));

        int remainingVouchers = voucherRepository.countByRestaurantAndStatus(restaurant, VoucherStatus.ISSUED);
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getOwner().getId(),
                remainingVouchers,
                restaurant.getAddress(),
                restaurant.getPhoneNumber(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public RestaurantsResponse findAllRestaurants() {
        List<RestaurantResponse> responses = restaurantRepository.findAll().stream()
                .map(restaurant -> {
                    long remainingVouchers = voucherRepository.countByRestaurantAndStatus(
                            restaurant, VoucherStatus.ISSUED
                    );
                    return new RestaurantResponse(
                            restaurant.getId(),
                            restaurant.getName(),
                            restaurant.getOwner().getId(),
                            (int) remainingVouchers,
                            restaurant.getAddress(),
                            restaurant.getPhoneNumber(),
                            restaurant.getLatitude(),
                            restaurant.getLongitude(),
                            restaurant.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
        return new RestaurantsResponse(responses);
    }
}
