package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.MedicationItem;

import java.util.List;

public interface MedicationItemService {
    List<MedicationItem> saveMedicationItem(List<MedicationItem> medicationItems);
}
