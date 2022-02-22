package protal.bcserver.clockchain;

import org.springframework.data.repository.CrudRepository;


public interface BlockchainRepository<CovidCountryData> extends CrudRepository<block, Long> {


    block findFirstByOrderByIdDesc();



}
