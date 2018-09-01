package com.example.demo.service;

import com.example.demo.domain.Ad;
import com.example.demo.domain.User;
import com.example.demo.dto.AdFilterDTO;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AdService.class);

    @Autowired
    AdRepository adRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    public Ad postNewAd(Ad ad, MultipartFile file) throws IOException, SQLException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = null;
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        }
        String userName = userDetails.getUsername();
        Ad savedAd = createAd(ad, userName, file);
        return savedAd;
    }

    @Transactional
    public Ad createAd(Ad ad, String username, MultipartFile file) throws IOException, SQLException {
        Ad newAd = new Ad();
        newAd.setTitle(ad.getTitle());
        newAd.setCategory(ad.getCategory());
        newAd.setCity(ad.getCity());
        newAd.setCondition(ad.getCondition());
        newAd.setDescription(ad.getDescription());
        newAd.setFirstname(ad.getFirstname());
        newAd.setLastname(ad.getLastname());
        newAd.setPhone(ad.getPhone());
        newAd.setState(ad.getState());
        newAd.setPrice(ad.getPrice());
        newAd.setCreatedate(new Date(Calendar.getInstance().getTime().getTime()));

        try {
            newAd.setImage(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }


        User byEmail = userRepository.findByEmail(username);
        newAd.setUser(byEmail);

        Ad saved = adRepository.save(newAd);
        LOGGER.info("New ad saved: {}", saved.getId());
        return saved;
    }

    @Transactional
    public List<Ad> getAll() {
        List<Ad> all = adRepository.findAll();
        return all;
    }

    @Transactional
    public List<Ad> getFilteredAds(AdFilterDTO adFilterDTO) {
        List<Ad> notFilteredbyCustomWord;

        if (onlyCustomWord(adFilterDTO)) {
            notFilteredbyCustomWord = adRepository.findAll();
        } else if (locationAndCategory(adFilterDTO)) {
            notFilteredbyCustomWord = adRepository.findByCategoryAndCityOrCategoryAndState(
                    adFilterDTO.getCategory(), adFilterDTO.getLocation(),adFilterDTO.getCategory(), adFilterDTO.getLocation());
        } else if (onlyCategory(adFilterDTO)) {
            notFilteredbyCustomWord = adRepository.findByCategory(adFilterDTO.getCategory());
        } else if (onlyLocation(adFilterDTO)){
            notFilteredbyCustomWord = adRepository.findByCityOrState(adFilterDTO.getLocation(), adFilterDTO.getLocation());
        } else {
            notFilteredbyCustomWord = adRepository.findAll();
        }

        if (customWord(adFilterDTO)) {
            notFilteredbyCustomWord = notFilteredbyCustomWord
                    .stream()
                    .filter(el -> el.getTitle().contains(adFilterDTO.getCustomWord()))
                    .collect(Collectors.toList());

        }
        return notFilteredbyCustomWord;
    }

    private boolean customWord(AdFilterDTO adFilterDTO) {
        return adFilterDTO.getCustomWord() != null && !adFilterDTO.getCustomWord().isEmpty();
    }

    private boolean onlyCustomWord(AdFilterDTO adFilterDTO) {
        return !onlyLocation(adFilterDTO) && !onlyCategory(adFilterDTO);
    }

    private boolean onlyLocation(AdFilterDTO adFilterDTO) {
        return adFilterDTO.getLocation() != null && !adFilterDTO.getLocation().isEmpty();
    }

    private boolean onlyCategory(AdFilterDTO adFilterDTO) {
        return adFilterDTO.getCategory() != null && !adFilterDTO.getCategory().isEmpty();
    }

    private boolean locationAndCategory(AdFilterDTO adFilterDTO) {
        return (adFilterDTO.getLocation() != null && !adFilterDTO.getLocation().isEmpty()) &&
                (adFilterDTO.getCategory() != null && !adFilterDTO.getCategory().isEmpty());
    }
}
