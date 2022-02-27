package protal.bcserver.clockchain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BlockchainCasesRepository extends JpaRepository<block, Long> {


    block findFirstByOrderByIdDesc();


}
