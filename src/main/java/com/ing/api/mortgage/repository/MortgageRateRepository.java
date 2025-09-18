package com.ing.api.mortgage.repository;

import com.ing.api.mortgage.model.MortgageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageRateRepository extends JpaRepository<MortgageRate, Integer> {

}
