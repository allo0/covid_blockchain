package protal.bcserver.data.models;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CovidCountryDataRepository extends CrudRepository<CovidCountryData, Long> {


    List<CovidCountryData> findByLocation(@Param("location") String location);

//    List<CovidCountryData> findByDeaths(int deaths);
//    List<CovidCountryData> findByLocation(String location);
//    List<CovidCountryData> findByLocation(String location);

}