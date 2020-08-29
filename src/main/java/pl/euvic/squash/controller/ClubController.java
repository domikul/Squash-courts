package pl.euvic.squash.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euvic.squash.model.response.ClubRestModel;
import pl.euvic.squash.model.service.ClubService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<List<ClubRestModel>> liatAllClubs() {

        final List<ClubRestModel> clubList = clubService.getAll();
        return ResponseEntity.ok(clubList);
    }

    @GetMapping("/{city}")
    public ResponseEntity<List<ClubRestModel>> getClubsByCity(@PathVariable final String city) {

        final List<ClubRestModel> clubList = clubService.getByCity(city);
        return ResponseEntity.ok(clubList);
    }


    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ClubRestModel> addNewClub(@RequestBody final ClubRestModel club,
                                                    @RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(clubService.addClub(club, token));
    }


    @PutMapping(value = "/{idClub}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ClubRestModel> updateClub(@RequestBody final ClubRestModel clubRestModel,
                                                    @PathVariable Integer idClub,
                                                    @RequestHeader(value = "Authorization") String token) {

        return ResponseEntity.ok(clubService.updateClub(clubRestModel, idClub, token));

    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{idClub}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteClub(@PathVariable Integer idClub,
                                              @RequestHeader(value = "Authorization") String token) {
        clubService.deleteClub(idClub, token);
        return new ResponseEntity<>(idClub, HttpStatus.OK);
    }


}
