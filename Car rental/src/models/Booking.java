package models;

import enums.BookingStatus;
import enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import strategies.PaymentStrategy;

import java.awt.print.Book;
import java.time.LocalDateTime;

@Getter
public class Booking {
    private final String id;
    private final Vehicle vehicle;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Branch pickUpBranch;
    private final Branch dropOffBranch;
    @Setter
    private double amount;
    private final PaymentStrategy paymentStrategy;
    @Setter
    private BookingStatus bookingStatus;
    @Setter
    private PaymentStatus paymentStatus;

    public Booking(Builder builder){
        this.id = builder.id;
        this.vehicle = builder.vehicle;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.pickUpBranch = builder.pickUpBranch;
        this.dropOffBranch = builder.dropOffBranch;
        this.amount = builder.amount;
        this.paymentStrategy = builder.paymentStrategy;
        this.bookingStatus = BookingStatus.CREATED;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public static Builder getBuilder(){
        return new Builder();
    }

    @Override
    public String toString() {
        return """
            Booking Details
            ----------------------------
            Booking Id      : %s
            Vehicle         : %s
            Pickup Branch   : %s
            Dropoff Branch  : %s
            Start Time      : %s
            End Time        : %s
            Amount          : %.2f
            Booking Status  : %s
            Payment Status  : %s
            Payment Method  : %s
            ----------------------------
            """.formatted(
                id,
                vehicle,
                pickUpBranch.getId(),
                dropOffBranch.getId(),
                startTime,
                endTime,
                amount,
                bookingStatus,
                paymentStatus,
                paymentStrategy.getClass().getSimpleName()
        );
    }

    public static class Builder{

        private String id;
        private Vehicle vehicle;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Branch pickUpBranch;
        private Branch dropOffBranch;
        private double amount;
        private PaymentStrategy paymentStrategy;

        public Builder setId(String id){
            this.id = id;
            return this;
        }

        public Builder setVehicle(Vehicle vehicle){
            this.vehicle = vehicle;
            return this;
        }

        public Builder setStartTime(LocalDateTime startTime){
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime){
            this.endTime = endTime;
            return this;
        }

        public Builder setPickUpBranch(Branch branch){
            this.pickUpBranch = branch;
            return this;
        }

        public Builder setDropOffBranch(Branch branch){
            this.dropOffBranch = branch;
            return this;
        }

        public Builder setAmount(double amount){
            this.amount = amount;
            return this;
        }

        public Builder setPaymentStrategy(PaymentStrategy paymentStrategy){
            this.paymentStrategy = paymentStrategy;
            return this;
        }

        public Booking build(){
            return new Booking(this);
        }
    }

}
