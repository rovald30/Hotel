package tech.selmefy.hotel.repository.hotelserviceorder;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity(name = "hotel_service_order")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HotelServiceOrder {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Short serviceType;

    @Column(nullable = false)
    private String personId;

    @Column(nullable = false)
    private Timestamp orderTime;

    @Column(nullable = false)
    private Long price;

    @Column(length = 1000)
    private String comments;
}
