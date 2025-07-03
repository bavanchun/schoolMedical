package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.MedicationItem;

import java.util.List;

public interface MedicationItemService {
    List<MedicationItem> saveAllMedicationItem(List<MedicationItem> medicationItems);

}
