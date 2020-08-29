package pl.euvic.squash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euvic.squash.model.response.CourtRestModel;
import pl.euvic.squash.model.service.CourtService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<CourtRestModel>> liatAllCourts() {

        final List<CourtRestModel> courtList = courtService.getAll();
        return ResponseEntity.ok(courtList);
    }

    @GetMapping
    public ResponseEntity<List<CourtRestModel>> liatCourtsInClub(@RequestParam Integer clubId) {

        final List<CourtRestModel> courtList = courtService.getCourtsInClub(clubId);
        return ResponseEntity.ok(courtList);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourtRestModel> addNewCourt(@RequestBody final CourtRestModel courtRestModel,
                                                      @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(courtService.addCourt(courtRestModel, token));
    }

    @PutMapping(value = "/{idCourt}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CourtRestModel> updateCourt(@RequestBody final CourtRestModel courtRestModel,
                                                      @PathVariable Integer idCourt,
                                                      @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(courtService.updateCourt(courtRestModel, idCourt, token));
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idCourt}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteCourt(@PathVariable Integer idCourt,
                                               @RequestHeader(value = "Authorization") String token) {
        courtService.deleteCourt(idCourt, token);
        return new ResponseEntity<>(idCourt, HttpStatus.OK);
    }

}
