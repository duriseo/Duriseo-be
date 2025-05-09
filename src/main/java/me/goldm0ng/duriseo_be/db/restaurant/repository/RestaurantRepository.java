package me.goldm0ng.duriseo_be.db.restaurant.repository;

import me.goldm0ng.duriseo_be.db.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
