package protal.bcserver.data.controller;


import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protal.bcserver.clockchain.BlockchainCasesRepository;
import protal.bcserver.clockchain.BlockchainRepository;
import protal.bcserver.clockchain.block;
import protal.bcserver.data.models.CovidCases;
import protal.bcserver.data.models.CovidCasesMonthly;
import protal.bcserver.data.models.CovidCasesRepository;
import protal.bcserver.data.models.CovidCountryData;

import java.util.*;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:8080")
//@CrossOrigin(origins = "https://vocid.herokuapp.com")
@RestController
@RequestMapping("/api")
public class CovidDataController {

    // Initiate the list to store the blockchain, the dificulty to mine
    // and the b;pcl object
    public static List<block> blockChain = new ArrayList<>();
    public static int prefix = 3;
    block blocks = new block();

    // Create the objects that will be used for the "queries" with the db
    @Autowired
    CovidCasesRepository ccRepository;
    @Autowired
    BlockchainRepository bcRepository;
    @Autowired
    BlockchainCasesRepository bcRepository2;

    // Create an object for logging
    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/covidCountries")
    public ResponseEntity<JSONObject> getCountriesList() {
        // Initialize the response json object
        JSONObject response_json = new JSONObject();

        List<String> countries = new ArrayList<String>();
        countries.addAll(ccRepository.findDistinctByCountry());

        response_json.put("countries", countries);
        return new ResponseEntity<>(response_json, HttpStatus.OK);
    }

    @GetMapping("/covidCases")
    public ResponseEntity<JSONObject> getCovidCases(@RequestParam("country") Optional<String> country, @RequestParam("month") Optional<Integer> month, @RequestParam("year") Optional<Integer> year, @RequestParam("deaths") Optional<Integer> deaths, @RequestParam("recovered") Optional<Integer> recovered, @RequestParam("confirmed") Optional<Integer> confirmed) {

        // Initialize the response json object
        JSONObject response_json = new JSONObject();

        List<CovidCases> covid_cases = new ArrayList<CovidCases>();
        List<CovidCases> filtered_list_year_month = new ArrayList<CovidCases>();
        int records = 0;

        /**
         * Store all the results from the query based on a country
         */
        try {
            if (country.isPresent()) {
                covid_cases.addAll(ccRepository.findCovidCasesByCountry(country.get().toLowerCase()));
            } else {
                covid_cases.addAll(ccRepository.findAll());
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }


        if (covid_cases.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        /** Store the filtered results from the list, based on:
         * if there is a month or a year value present
         */
        filtered_list_year_month = covid_cases.stream().filter(c -> {
            if (month.isPresent()) {
                return c.getMonth() == month.get();
            } else return true;
        }).filter(c -> {
            if (year.isPresent()) {
                return c.getYear() == year.get();
            } else return true;
        }).collect(Collectors.toList());

        /**
         *  Count of filtered records
         */
        records = filtered_list_year_month.size();

        /**
         * Add the current country to the response
         */
        if (country.isPresent()) {
            response_json.put("country", country.get().toLowerCase());
        } else {
            response_json.put("country", "all");
        }

        response_json.put("records", records);

        /**
         * If the month is selected, add it to the response for extra details
         */
        if (month.isPresent()) {
            response_json.put("month", month.get());
        }

        /**
         * If the year is selected, add it to the response for extra details
         */
        if (year.isPresent()) {
            response_json.put("year", year.get());
        }

        /**
         * If the deaths flag is enabled, calculate the death stats
         * and add them to the response json
         */
        if (deaths.isPresent() && deaths.get() == 1) {

            JSONObject deaths_obj = new JSONObject();

            int filtered_sum_deaths = 0;
            OptionalDouble avg_deaths = null;
            OptionalInt filtered_max_deaths = null;

            filtered_sum_deaths = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getDeaths())
                    .sum();
            avg_deaths = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getDeaths())
                    .average();
            filtered_max_deaths = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getDeaths())
                    .max();

            deaths_obj.put("sum", filtered_sum_deaths);
            deaths_obj.put("avg", avg_deaths.isPresent() ? Math.round(avg_deaths.getAsDouble()) : 0);
            deaths_obj.put("max", filtered_max_deaths.isPresent() ? filtered_max_deaths.getAsInt() : 0);
            response_json.put("deaths", deaths_obj);
            logger.info("Sum deaths (all months): " + filtered_sum_deaths);
            logger.info("Avg deaths: " + (avg_deaths.isPresent() ? Math.round(avg_deaths.getAsDouble()) : 0));
            logger.info("Max deaths (month with highest death rate): " + (filtered_max_deaths.isPresent() ? filtered_max_deaths.getAsInt() : 0));
        }

        /**
         * If the recovered flag is enabled, calculate the recovered stats
         * and add them to the response json
         */
        if (recovered.isPresent() && recovered.get() == 1) {

            JSONObject recovered_obj = new JSONObject();

            int filtered_sum_recovered = 0;
            OptionalDouble avg_recovered = null;
            OptionalInt filtered_max_recovered = null;

            filtered_sum_recovered = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .sum();
            avg_recovered = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .average();
            filtered_max_recovered = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .max();

            recovered_obj.put("sum", filtered_sum_recovered);
            recovered_obj.put("avg", avg_recovered.isPresent() ? Math.round(avg_recovered.getAsDouble()) : 0);
            recovered_obj.put("max", filtered_max_recovered.isPresent() ? filtered_max_recovered.getAsInt() : 0);
            response_json.put("recovered", recovered_obj);
            logger.info("Sum recovered (all months): " + filtered_sum_recovered);
            logger.info("Avg recovered: " + (avg_recovered.isPresent() ? Math.round(avg_recovered.getAsDouble()) : 0));
            logger.info("Max recovered (month with highest recovery rate): " + (filtered_max_recovered.isPresent() ? filtered_max_recovered.getAsInt() : 0));
        }

        /**
         * If the confirmed flag is enabled, calculate the confirmed stats
         * and add them to the response json
         */
        if (confirmed.isPresent() && confirmed.get() == 1) {

            JSONObject confirmed_obj = new JSONObject();

            int filtered_sum_confirmed = 0;
            OptionalDouble avg_confirmed = null;
            OptionalInt filtered_max_confirmed = null;

            filtered_sum_confirmed = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .sum();
            avg_confirmed = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .average();
            filtered_max_confirmed = filtered_list_year_month.stream()
                    .mapToInt(c -> c.getRecovered())
                    .max();

            confirmed_obj.put("sum", filtered_sum_confirmed);
            confirmed_obj.put("avg", avg_confirmed.isPresent() ? Math.round(avg_confirmed.getAsDouble()) : 0);
            confirmed_obj.put("max", filtered_max_confirmed.isPresent() ? filtered_max_confirmed.getAsInt() : 0);
            response_json.put("confirmed", confirmed_obj);

            logger.info("Sum confirmed (all months): " + filtered_sum_confirmed);
            logger.info("Avg confirmed: " + (avg_confirmed.isPresent() ? Math.round(avg_confirmed.getAsDouble()) : 0));
            logger.info("Max confirmed (month with highest confirmed infection rate): " + (filtered_max_confirmed.isPresent() ? filtered_max_confirmed.getAsInt() : 0));
        }


        return new ResponseEntity<>(response_json, HttpStatus.OK);


    }


    @PostMapping("/dataByCountry")
    public ResponseEntity<CovidCountryData> createDataByCountry(@RequestBody Map<String, String> body) {

        try {
            // It's synchronized in order to prevent multiple concurrent requests
            // from trying to a create covid country entry and a blockchain block
            synchronized (this) {

                // Get the data from the request body
                CovidCountryData _ccd = new CovidCountryData();
                _ccd.setLocation(body.get("location").toLowerCase());
                _ccd.setTs(Integer.parseInt(body.get("ts")));
                _ccd.setDt(body.get("dt"));
                _ccd.setActive(Integer.parseInt(body.get("active")));
                _ccd.setDeaths(Integer.parseInt(body.get("deaths")));
                _ccd.setConfirmed(Integer.parseInt(body.get("confirmed")));
                _ccd.setRecovered(Integer.parseInt(body.get("recovered")));

                // create a json string with the data which will be put in blockchain
                String data = _ccd.toString();

                /**
                 * Check if the list contains any blocks.
                 * If it's empty, get the latest block inserted in the database
                 */
                if (blockChain.isEmpty()) {
                    block init_block = bcRepository.findFirstByOrderByIdDesc();

                    /**
                     * If the database is not empty, add the block to the list
                     */
                    if (init_block != null) {
                        blockChain.add(init_block);
                        long node_count = bcRepository.count();
                        logger.warn("Node: " + node_count + " restored");
                    }
                }

                /**
                 * Calculate (if exists) the previous hash and
                 * create mining the new block
                 */
                String previous_hash = blockChain.size() > 0 ? blockChain.get(blockChain.size() - 1).getHash() : "0";
                blocks = new block(previous_hash, data, new Date().getTime());
                blocks.mineBlock(prefix);


                // If the block is valid, store it in db
                if (blockChain.get(blockChain.size() - 1).getHash().equals(blocks.getPreviousHash())) {
                    blockChain.add(blocks);
                    // Save the covid data first so we have the primary key
                    blocks.setCcd(_ccd);

                    bcRepository.save(blocks);

                    logger.info("Node: " + ((int) bcRepository.count()) + " created");
                    logger.info(_ccd.toString());
                    logger.info(blocks.getHash());

                } else {
                    logger.info("Block is not properly mined");
                }


                return new ResponseEntity<>(_ccd, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/casesByCountry")
    public ResponseEntity<List<CovidCases>> createCasesByCountry(@RequestBody CovidCasesMonthly _ccm) {
        List<CovidCases> jsonString = new ArrayList<>();
        try {
            synchronized (this) {

                for (CovidCases cc : _ccm.getCases()) {

                    cc.setYear(_ccm.getYear());
                    cc.setCountry(_ccm.getCountry().toLowerCase());

                    String data = cc.toString();

                    if (blockChain.isEmpty()) {
                        block init_block = bcRepository2.findFirstByOrderByIdDesc();


                        if (init_block != null) {
                            blockChain.add(init_block);
                            long node_count = bcRepository2.count();
                            logger.warn("Node: " + node_count + " restored");
                        }
                    }

                    String previous_hash = blockChain.size() > 0 ? blockChain.get(blockChain.size() - 1).getHash() : "0";

                    blocks = new block(previous_hash, data, new Date().getTime());
                    blocks.mineBlock(prefix);


                    // If the chain is valid, store it in db
                    if (blockChain.get(blockChain.size() - 1).getHash().equals(blocks.getPreviousHash())) {
                        blockChain.add(blocks);

                        blocks.setCc(cc);
                        bcRepository2.saveAndFlush(blocks);

                        logger.info("Node: " + ((int) bcRepository2.count()) + " created");
                        logger.info(cc.toString());
                        logger.info(blocks.getHash());

                        jsonString.add(cc);
                    } else {
                        logger.info("Block is not properly mined");
                    }

                }


                return new ResponseEntity<>(jsonString, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
