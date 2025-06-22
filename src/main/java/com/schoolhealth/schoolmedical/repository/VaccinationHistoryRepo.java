package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import org.springframework.data.repository.CrudRepository;

public interface VaccinationHistoryRepo extends CrudRepository<VaccinationHistory,Long>
{
}
