package me.goldm0ng.duriseo_be.db.voucher.repository;

import me.goldm0ng.duriseo_be.db.restaurant.entity.Restaurant;
import me.goldm0ng.duriseo_be.db.voucher.entity.Voucher;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    int countByRestaurantAndStatus(Restaurant restaurant, VoucherStatus voucherStatus);
}
