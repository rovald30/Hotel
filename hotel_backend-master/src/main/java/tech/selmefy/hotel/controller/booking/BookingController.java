package tech.selmefy.hotel.controller.booking;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.booking.dto.BookingDTO;
import tech.selmefy.hotel.controller.booking.dto.BookingResponseDTO;
import tech.selmefy.hotel.exception.ApiRequestException;
import tech.selmefy.hotel.service.booking.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Api(tags = "Booking")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    public final BookingService bookingService;


    @GetMapping
    public BookingResponseDTO getAllBookingsWithParams(
            @RequestParam(name="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name="orderBy", defaultValue = "price") String orderBy,
            @RequestParam(name="orderType", defaultValue = "ASC") String orderType,
            @RequestParam(name = "filterBy") Optional<String> filterBy,
            @RequestParam(name = "filterValue") Optional<String> filterValue) {

        logger.info("Request getAllBookingsWithParams received");
        // Ensures that only BookingDTO fields are used for sorting/searching.
        try {
            BookingDTO.class.getDeclaredField(orderBy);
        } catch (NoSuchFieldException e) {
            throw new ApiRequestException("Cannot sort by " + orderBy);
        }

        if (filterBy.isPresent()) {
            try {
                BookingDTO.class.getDeclaredField(filterBy.get());
            } catch (NoSuchFieldException e) {
                throw new ApiRequestException("Cannot filter by " + filterBy.get());
            }
        }

        int maxPageSize = 200;
        if (pageSize > maxPageSize) {
            logger.warn("maxPageSize exceeded, reverting to default value");
            pageSize = maxPageSize;
        }

        return bookingService.getAllBookingsWithParams(pageNumber, pageSize, orderBy, orderType, filterBy, filterValue);
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingById(@PathVariable Long bookingId) {
        logger.info("Request getBookingById received");
        return bookingService.getBookingById(bookingId);
    }

    @PostMapping(path = "/public", params = {"roomId", "ownerId"})
    public void createNewBooking(@RequestBody BookingDTO bookingDTO,
                                 @RequestParam(name="roomId") Long roomId,
                                 @RequestParam(name="ownerId") String ownerId,
                                 @RequestParam(name="otherId") Optional<List<String>> otherId) {
        logger.info("Request createNewBooking received");
        bookingService.createNewBooking(bookingDTO, roomId, ownerId, otherId);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long bookingId, @RequestBody BookingDTO bookingDTO) {
        logger.info("Request updateBooking received");
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, bookingDTO));
    }
}