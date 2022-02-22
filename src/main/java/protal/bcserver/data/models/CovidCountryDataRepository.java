package protal.bcserver.data.models;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CovidCountryDataRepository extends CrudRepository<CovidCountryData, Long> {


    List<CovidCountryData> findCovidCountryDataByLocation(@Param("location") String location);


}