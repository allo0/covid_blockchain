package protal.bcserver.data.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protal.bcserver.data.models.CovidCountryData;
import protal.bcserver.data.models.CovidCountryDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin
@RestController
@RequestMapping("/api")
public class CovidDataCountryController {

    @Autowired
    CovidCountryDataRepository ccdRepository;

    Logger logger = LoggerFactory.getLogger(LoggingController.class);
    ObjectMapper mapper = new ObjectMapper();


    @GetMapping("/dataByCountry/{location}")
    public ResponseEntity<List<CovidCountryData>> getAllDataByCountry(@PathVariable(required = true) String location) {
        List<CovidCountryData> country_data = new ArrayList<CovidCountryData>();
        try {

            country_data.addAll(ccdRepository.findByLocation(location));
            //Object to JSON Conversion
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(country_data.toString());
            logger.info(jsonString);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        if (country_data.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity<>(country_data, HttpStatus.OK);


    }


    @PostMapping("/dataByCountry")
    public ResponseEntity<CovidCountryData> createDataByCountry(@RequestBody Map<String, String> body) {

        try {

            CovidCountryData _ccd = new CovidCountryData();
            _ccd.setLocation(body.get("location"));
            _ccd.setTs(Integer.parseInt(body.get("ts")));
            _ccd.setDt(body.get("dt"));
            _ccd.setActive(Integer.parseInt(body.get("active")));
            _ccd.setDeaths(Integer.parseInt(body.get("deaths")));
            _ccd.setConfirmed(Integer.parseInt(body.get("confirmed")));
            _ccd.setRecovered(Integer.parseInt(body.get("recovered")));

            ccdRepository.save(_ccd);


            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(_ccd.toString());
            logger.info(jsonString);

            return new ResponseEntity<>(_ccd, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
