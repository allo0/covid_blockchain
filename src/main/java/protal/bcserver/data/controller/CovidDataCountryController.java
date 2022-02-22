package protal.bcserver.data.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import protal.bcserver.clockchain.BlockchainRepository;
import protal.bcserver.clockchain.ChainValidator;
import protal.bcserver.clockchain.block;
import protal.bcserver.data.models.CovidCountryData;
import protal.bcserver.data.models.CovidCountryDataRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


@CrossOrigin(origins = "http://localhost:8080")
//@CrossOrigin(origins = "https://vocid.herokuapp.com")
@RestController
@RequestMapping("/api")
public class CovidDataCountryController {

    public static List<block> blockChain = new ArrayList<>();
    public static int prefix = 3;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    CovidCountryDataRepository ccdRepository;
    @Autowired
    BlockchainRepository<CovidCountryData> bcRepository;

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    ObjectMapper mapper = new ObjectMapper();
    block blocks = new block();

    @GetMapping("/dataByCountry/{location}")
    public ResponseEntity<List<CovidCountryData>> getAllDataByCountry(@PathVariable(required = true) String location) {
        List<CovidCountryData> country_data = new ArrayList<CovidCountryData>();

        try {

//            synchronized (this) {
//            if (blockChain.isEmpty()) {
//                block init_block = bcRepository.findFirstByOrderByIdDesc();
//
//                if (init_block != null) {
//                    blockChain.add(init_block);
//                    Long node_count = bcRepository.count();
//
//                }
//
//            }
//            String previous_hash = blockChain.size() > 0 ? blockChain.get(blockChain.size() - 1).getHash() : "0";
//
//            blocks = new block(previous_hash, "Very important data", new Date().getTime());
//            blocks.mineBlock(prefix);
//            blockChain.add(blocks);

//            country_data.addAll(ccdRepository.findCovidCountryDataByLocation(location));
            country_data.addAll(ccdRepository.findCovidCountryDataByLocation(location));

//            bcRepository.save(blocks);
//            logger.info("Node: " + ((int) bcRepository.count()) + " created");
//
//            logger.info(blocks.getHash());
//            logger.info("BlockChain is valid? :" + ChainValidator.isChainValid(prefix, blockChain));

//            }

            //Object to JSON Conversion
            //String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(country_data.toString());

        } catch (Exception ex) {
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
            synchronized (this) {

                CovidCountryData _ccd = new CovidCountryData();
                _ccd.setLocation(body.get("location"));
                _ccd.setTs(Integer.parseInt(body.get("ts")));
                _ccd.setDt(body.get("dt"));
                _ccd.setActive(Integer.parseInt(body.get("active")));
                _ccd.setDeaths(Integer.parseInt(body.get("deaths")));
                _ccd.setConfirmed(Integer.parseInt(body.get("confirmed")));
                _ccd.setRecovered(Integer.parseInt(body.get("recovered")));

                String data = _ccd.toString();

                if (blockChain.isEmpty()) {
                    block init_block = bcRepository.findFirstByOrderByIdDesc();


                    if (init_block != null) {
                        blockChain.add(init_block);
                        long node_count = bcRepository.count();
                        logger.warn("Node: " + node_count + " restored");
                    }
                }

                String previous_hash = blockChain.size() > 0 ? blockChain.get(blockChain.size() - 1).getHash() : "0";
                blocks = new block(previous_hash, data, new Date().getTime());
                blocks.mineBlock(prefix);
                blockChain.add(blocks);


//                ccdRepository.save(_ccd);
                if (ChainValidator.isChainValid(prefix, blockChain)) { // If the cahin is valid, store it in db
                    blocks.setCcd(_ccd);
                    bcRepository.save(blocks);
                }else{
                    return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
                }


                //String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(_ccd.toString());
                logger.info("Node: " + ((int) bcRepository.count()) + " created");
                logger.info(_ccd.toString());
                logger.info(blocks.getHash());
//                logger.info("BlockChain is valid? :" + ChainValidator.isChainValid(prefix, blockChain));

                return new ResponseEntity<>(_ccd, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
