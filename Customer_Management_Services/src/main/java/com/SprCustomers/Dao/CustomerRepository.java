package com.SprCustomers.Dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.SprCustomers.Bo.CustomerBo;
import jakarta.transaction.Transactional;
@Repository
public interface CustomerRepository extends JpaRepository<CustomerBo, Integer> {

//	@Modifying
//	@Transactional
//	@Query("DELETE FROM CustomerBo c WHERE c.customerId = :id")
	int deleteByCustomerId(Integer id);

}
 