package com.example.delivery.model.entity;

import com.example.delivery.model.DeliveryAddress;
import com.example.delivery.model.DeliveryCategory;
import com.example.delivery.model.DeliveryStatus;
import com.example.delivery.model.dto.request.DeliveryUpdateRequest;
import com.example.driver.model.Driver;
import com.example.driver.model.Vehicle;
import com.example.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = {
                @Index(name = "idx_member_delivery_status", columnList = "member_id, delivery_status")
        }
)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", length = 50)
    private DeliveryStatus deliveryStatus; //결제 추가하면 상태도 추가 해줘야 함

    @Column(nullable = false, unique = true)
    private String reservationNumber;

    @Embedded
    @Column(nullable = false)
    private DeliveryCategory deliveryCategory;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Embedded
    @Column(nullable = false)
    private Vehicle vehicle;

    @Embedded
    @Column(nullable = false)
    private DeliveryAddress deliveryAddress;

    @Column(nullable = false)
    private BigDecimal deliveryFee; //BigDemical 프론트에서 처리하는거 같음

    private String deliveryOptions;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void updateReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void update(DeliveryUpdateRequest deliveryUpdateRequest) {
        this.deliveryAddress = deliveryAddress.update(deliveryUpdateRequest.getDeliveryAddress());
        this.vehicle = vehicle.update(deliveryUpdateRequest.getVehicle());
        this.deliveryDate = deliveryUpdateRequest.getDeliveryDate();
        this.deliveryOptions = deliveryUpdateRequest.getDeliveryOptions();
    }

    private Delivery(Long id, Member member, Driver driver, DeliveryStatus deliveryStatus, String reservationNumber, DeliveryCategory deliveryCategory, LocalDateTime deliveryDate, Vehicle vehicle, DeliveryAddress deliveryAddress, BigDecimal deliveryFee, String deliveryOptions) {
        this.id = id;
        this.member = member;
        this.driver = driver;
        this.deliveryStatus = deliveryStatus;
        this.reservationNumber = reservationNumber;
        this.deliveryCategory = deliveryCategory;
        this.deliveryDate = deliveryDate;
        this.vehicle = vehicle;
        this.deliveryAddress = deliveryAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryOptions = deliveryOptions;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", member=" + member +
                ", driver=" + driver +
                ", deliveryStatus=" + deliveryStatus +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", deliveryCategory=" + deliveryCategory +
                ", deliveryDate=" + deliveryDate +
                ", vehicle=" + vehicle +
                ", deliveryAddress=" + deliveryAddress +
                ", deliveryFee=" + deliveryFee +
                ", deliveryOptions='" + deliveryOptions + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Delivery delivery = (Delivery) object;
        return Objects.equals(id, delivery.id) && Objects.equals(member, delivery.member) && Objects.equals(driver, delivery.driver) && deliveryStatus == delivery.deliveryStatus && Objects.equals(reservationNumber, delivery.reservationNumber) && Objects.equals(deliveryCategory, delivery.deliveryCategory) && Objects.equals(deliveryDate, delivery.deliveryDate) && Objects.equals(vehicle, delivery.vehicle) && Objects.equals(deliveryAddress, delivery.deliveryAddress) && Objects.equals(deliveryFee, delivery.deliveryFee) && Objects.equals(deliveryOptions, delivery.deliveryOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, driver, deliveryStatus, reservationNumber, deliveryCategory, deliveryDate, vehicle, deliveryAddress, deliveryFee, deliveryOptions);
    }
}
