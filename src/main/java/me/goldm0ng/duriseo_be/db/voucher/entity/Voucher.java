package me.goldm0ng.duriseo_be.db.voucher.entity;

import jakarta.persistence.*;
import lombok.*;
import me.goldm0ng.duriseo_be.db.restaurant.entity.Restaurant;
import me.goldm0ng.duriseo_be.db.user.entity.User;
import me.goldm0ng.duriseo_be.enums.user.Role;
import me.goldm0ng.duriseo_be.enums.voucher.VoucherStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36, nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_id", nullable = false)
    private User issuedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role issuedByRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 32)
    private VoucherStatus status;

    @Column(nullable = false)
    private LocalDate expiredAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime usedAt;

    @Builder
    public Voucher(final String code, final Restaurant restaurant, final User issuedBy, final Role issuedByRole, final User recipient, final VoucherStatus status, final LocalDate expiredAt, final LocalDateTime createdAt, final LocalDateTime usedAt) {
        this.code = code;
        this.restaurant = restaurant;
        this.issuedBy = issuedBy;
        this.issuedByRole = issuedByRole;
        this.recipient = recipient;
        this.status = status;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
    }
}
