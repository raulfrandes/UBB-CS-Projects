package org.contest.server.controller;

import jakarta.websocket.server.PathParam;
import org.contest.server.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/contest")
public class ContestController {
    private final ContestService contestService;

    @Autowired
    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitResults(
            @PathParam("countryId") String countryId,
            @RequestBody Map<String, Integer> results) {
        try {
            contestService.processResults(results, countryId);
            return ResponseEntity.ok("Result submitted");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Integer>> getCountryRanking(
            @PathParam("countryId") String countryId
    ) {
        try {
            Map<String, Integer> ranking = contestService.getCountryRanking(countryId);
            return ResponseEntity.ok(ranking);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/final-results")
    public ResponseEntity<byte[]> getFinalResults() {
        try {
            byte[] zipContent = contestService.getFinalResults();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=FinalResults.zip");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
