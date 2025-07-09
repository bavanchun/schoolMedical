package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.MedicationItem;
import com.schoolhealth.schoolmedical.repository.MedicationItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationItemImpl implements MedicationItemService {
    @Autowired
    private MedicationItemRepo medicationItemRepo;
    @Override
    public List<MedicationItem> saveAllMedicationItem(List<MedicationItem> medicationItems) {
        return medicationItemRepo.saveAll(medicationItems);
    }
}
