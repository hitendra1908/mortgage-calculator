package com.ing.api.mortgage.repository;

import com.ing.api.mortgage.model.MortgageRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MortgageRateRepository extends JpaRepository<MortgageRate, Integer> {

    /*// key = maturityPeriod
    private final Map<Integer, MortgageRate> store = new ConcurrentHashMap<>();

    public List<MortgageRate> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparingInt(MortgageRate::maturityPeriod))
                .collect(Collectors.toList());
    }

    public Optional<MortgageRate> findByMaturityPeriod(int maturityYears) {
        return Optional.ofNullable(store.get(maturityYears));
    }

    public void save(MortgageRate rate) {
        store.put(rate.maturityPeriod(), rate);
    }

    public void saveAll(Collection<MortgageRate> rates) {
        rates.forEach(r -> store.put(r.maturityPeriod(), r));
    }

    public void clear() {
        store.clear();
    }*/
}
