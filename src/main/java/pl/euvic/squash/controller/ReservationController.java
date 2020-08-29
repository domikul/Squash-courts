package pl.euvic.squash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euvic.squash.model.response.ReservationRestModel;
import pl.euvic.squash.model.service.ReservationService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping(value = "/own")
    public ResponseEntity<List<ReservationRestModel>> getOwnReservations(@RequestHeader(value = "Authorization") String token) {
        final List<ReservationRestModel> reservationList = reservationService.getOwn(token);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping(value = "/court")
    public ResponseEntity<List<ReservationRestModel>> getCourtReservations(@RequestParam Integer courtId) {
        final List<ReservationRestModel> reservationList = reservationService.getCourtReservations(courtId);
        return ResponseEntity.ok(reservationList);
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationRestModel> addNewReservation(@RequestBody final ReservationRestModel reservationRestModel,
                                                                  @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(reservationService.addReservation(reservationRestModel, token));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idReservation}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteReservation(@PathVariable Integer idReservation,
                                                     @RequestHeader(value = "Authorization") String token) {
        reservationService.deleteReservation(idReservation, token);
        return new ResponseEntity<>(idReservation, HttpStatus.OK);
    }

}
