package com.example.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.domain.Ad;
import com.example.demo.dto.AdFilterDTO;
import com.example.demo.dto.TextResponse;
import com.example.demo.service.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class AdController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AdService adService;

    @PostMapping(path = "/postAd", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TextResponse> postAd(@RequestParam("file") MultipartFile file, @RequestParam("details") String adDetails) throws IOException, SQLException {
        TextResponse textResponse = new TextResponse();
        Ad ad = new ObjectMapper().readValue(adDetails, Ad.class);
        adService.postNewAd(ad, file);
        textResponse.setMessage("✔ Congratulations! Your ad will be available soon.");
        return new ResponseEntity<>(textResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/getAllAds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ad>> getAllAds() {
        List<Ad> all = adService.getAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping(path = "/getFilterAds", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ad>> getFilteredAds(@RequestBody AdFilterDTO adFilterDTO) {
        List<Ad> all = adService.getFilteredAds(adFilterDTO);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

}
