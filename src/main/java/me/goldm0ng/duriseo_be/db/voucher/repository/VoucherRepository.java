package me.goldm0ng.duriseo_be.db.voucher.repository;

import me.goldm0ng.duriseo_be.db.voucher.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
}
