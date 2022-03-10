package protal.bcserver.data.models;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CovidCasesRepository extends CrudRepository<CovidCases, Long> {

    List<CovidCases> findCovidCasesByCountry(@Param("country") String location);


    @Query("SELECT DISTINCT cc.country FROM CovidCases cc")
    List<String> findDistinctByCountry();

    List<CovidCases> findAll();


}
