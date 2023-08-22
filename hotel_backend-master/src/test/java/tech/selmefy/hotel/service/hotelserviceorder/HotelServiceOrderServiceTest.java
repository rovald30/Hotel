package tech.selmefy.hotel.service.hotelserviceorder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.hotelservice.dto.HotelServiceDTO;
import tech.selmefy.hotel.controller.hotelserviceorder.dto.HotelServiceOrderDTO;
import tech.selmefy.hotel.controller.room.dto.RoomDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.mapper.BookingMapper;
import tech.selmefy.hotel.mapper.HotelServiceOrderMapper;
import tech.selmefy.hotel.mapper.RoomMapper;
import tech.selmefy.hotel.repository.booking.Booking;
import tech.selmefy.hotel.repository.hotelservice.HotelService;
import tech.selmefy.hotel.repository.hotelservice.HotelServiceRepository;
import tech.selmefy.hotel.repository.hotelserviceorder.HotelServiceOrder;
import tech.selmefy.hotel.repository.hotelserviceorder.HotelServiceOrderRepository;
import tech.selmefy.hotel.repository.room.Room;
import tech.selmefy.hotel.service.hotelservice.HotelServiceService;
import tech.selmefy.hotel.service.room.type.RoomType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class HotelServiceOrderServiceTest {

    private List<HotelServiceOrder> hotelServiceOrders;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private Timestamp timestampFuture = new Timestamp(System.currentTimeMillis() + 100000);
    private HotelServiceOrder service1 = new HotelServiceOrder(1L, (short) 2, "1111111", timestamp, 20L, "Parking");
    private HotelServiceOrder service2 = new HotelServiceOrder(2L, (short) 8, "1111112", timestamp, 20L, "Restaurant");
    private HotelServiceOrder service3 = new HotelServiceOrder(3L, (short) 2, "1111113", timestamp, 20L, "Parking");
    private HotelServiceOrder service4 = new HotelServiceOrder(4L, (short) 8, "1111114", timestamp, 20L, "Restaurant");

    @Mock
    private HotelServiceOrderRepository hotelServiceOrderRepository;

    @InjectMocks
    private HotelServiceOrderService hotelServiceOrderService;


    @BeforeEach
    void setUp() {
        hotelServiceOrders = new ArrayList<>();

    }

    @AfterEach
    void tearDown() {
        hotelServiceOrders.clear();

    }

    @Test
    void getAllHotelServiceOrders_returnsHotelServiceOrderDTO_WhenRequested() {

        // given
        hotelServiceOrders.add(service1);
        hotelServiceOrders.add(service2);
        hotelServiceOrders.add(service3);
        hotelServiceOrders.add(service4);

        BDDMockito.given(hotelServiceOrderRepository.findAll()).willReturn(hotelServiceOrders);

        // when
        List<HotelServiceOrderDTO> result = hotelServiceOrderService.getAllHotelServiceOrders();

        // then
        assertEquals(hotelServiceOrders.size(), result.size());

        // Confirming all services are here
        assertEquals(hotelServiceOrders.get(0).getPersonId(), result.get(0).getPersonId());
        assertEquals(hotelServiceOrders.get(1).getPersonId(), result.get(1).getPersonId());
        assertEquals(hotelServiceOrders.get(2).getPersonId(), result.get(2).getPersonId());
        assertEquals(hotelServiceOrders.get(3).getPersonId(), result.get(3).getPersonId());

    }

    @Test
    void getHotelServiceOrderById_returnHotelServiceOrderDTO_WhenRequestedWithId() {

        // given
        hotelServiceOrders.add(service1);
        hotelServiceOrders.add(service2);
        hotelServiceOrders.add(service3);
        hotelServiceOrders.add(service4);

        BDDMockito.given(hotelServiceOrderRepository.findById(2L)).willReturn(Optional.ofNullable(service2));

        // when
        HotelServiceOrderDTO result = hotelServiceOrderService.getHotelServiceOrderById(2L);

        // then
        assertEquals(service2.getServiceType(), result.getServiceType());
        assertEquals(service2.getPrice(), result.getPrice());
        assertEquals(service2.getOrderTime(), result.getOrderTime());
        assertEquals(service2.getComments(), result.getComments());
        assertEquals(service2.getPersonId(), result.getPersonId());
        assertNotEquals(service1.getPersonId(), result.getPersonId());
    }

    @Test
    void createNewHotelServiceOrder_success() {

        //given
        HotelServiceOrder validHotelServiceOrder = HotelServiceOrder.builder()
                .id(3L)
                .serviceType((short) 8)
                .personId("1111113")
                .orderTime(timestampFuture)
                .price(20L)
                .comments("Parking")
                .build();

        HotelServiceOrderDTO validHotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(validHotelServiceOrder);

        //when
        BDDMockito.lenient().when(hotelServiceOrderRepository.save(any(HotelServiceOrder.class))).thenReturn(validHotelServiceOrder);

        //then
        assertDoesNotThrow(() -> {
            hotelServiceOrderService.createNewHotelServiceOrder(validHotelServiceOrderDTO);
        });
    }
    @Test
    void createNewHotelServiceOrder_throwsAPIRequestException_orderTimeCannotBeInThePast() {

        //given
        HotelServiceOrder invalidHotelServiceOrder = HotelServiceOrder.builder()
                .id(3L)
                .serviceType((short) 8)
                .personId("1111113")
                .orderTime(timestamp)
                .price(20L)
                .comments("Parking")
                .build();

        HotelServiceOrderDTO invalidHotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(invalidHotelServiceOrder);

        //when
        BDDMockito.lenient().when(hotelServiceOrderRepository.save(any(HotelServiceOrder.class))).thenReturn(invalidHotelServiceOrder);

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            hotelServiceOrderService.createNewHotelServiceOrder(invalidHotelServiceOrderDTO);
        });

        //then
        assertEquals(
            "Order time cannot be in the past!", exception.getMessage());
    }
    @Test
    void createNewHotelServiceOrder_throwsAPIRequestException_priceCantBeNegative() {

        //given
        HotelServiceOrder invalidHotelServiceOrder = HotelServiceOrder.builder()
                .id(3L)
                .serviceType((short) 8)
                .personId("1111113")
                .orderTime(timestampFuture)
                .price(-20L)
                .comments("Parking")
                .build();

        HotelServiceOrderDTO invalidHotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(invalidHotelServiceOrder);

        //when
        BDDMockito.lenient().when(hotelServiceOrderRepository.save(any(HotelServiceOrder.class))).thenReturn(invalidHotelServiceOrder);

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            hotelServiceOrderService.createNewHotelServiceOrder(invalidHotelServiceOrderDTO);
        });

        //then
        assertEquals(
                "Price can't be negative!", exception.getMessage());
    }

    @Test
    void updateHotelServiceOrder_throwsAPIRequestException_bookingDoesNotExist() {

        //given
        HotelServiceOrder invalidHotelServiceOrder = HotelServiceOrder.builder()
                .id(6L)
                .serviceType((short) 8)
                .personId("1111113")
                .orderTime(timestampFuture)
                .price(20L)
                .comments("Parking")
                .build();

        HotelServiceOrderDTO invalidHotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(invalidHotelServiceOrder);

        //when
        BDDMockito.lenient().when(hotelServiceOrderRepository.save(any(HotelServiceOrder.class))).thenReturn(invalidHotelServiceOrder);

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            hotelServiceOrderService.updateHotelServiceOrder(3L, invalidHotelServiceOrderDTO);
        });

        //then
        assertEquals(
                "Booking does not exist with id: 3", exception.getMessage());
    }

    @Test
    void updateHotelServiceOrder_success_changesPrice() {

        //given
        hotelServiceOrders.add(service1);
        BDDMockito.given(hotelServiceOrderRepository.findById(1L)).willReturn(Optional.of(service1));

        //when
        HotelServiceOrder validHotelServiceOrder = HotelServiceOrder.builder()
                .id(1L)
                .serviceType((short) 8)
                .personId("1111113")
                .orderTime(timestampFuture)
                .price(100L)
                .comments("Parking")
                .build();

        HotelServiceOrderDTO validHotelServiceOrderDTO = HotelServiceOrderMapper.INSTANCE.toDTO(validHotelServiceOrder);
        hotelServiceOrderService.updateHotelServiceOrder(1L, validHotelServiceOrderDTO);
        //then

        assertEquals(100L, (long) service1.getPrice());
    }
}
