package com.schoolhealth.schoolmedical.service.sendMedical;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.model.mapper.SendMedicationMapper;
import com.schoolhealth.schoolmedical.repository.SendMedicationRepo;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMedicalImpl implements SendMedicalService{
    @Autowired
    private SendMedicationRepo sendMedicationRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMedicationMapper sendMedicationMapper;
    @Autowired
    private PupilService pupilService;
    @Override
    public SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String userId) {
        User user = userService.findById(userId);
        Pupil pupil = pupilService.findPupilById(sendMedicationReq.getPupilId());
        SendMedication sendMedication = sendMedicationMapper.toEntity(sendMedicationReq);
        sendMedication.setUser(user);
        sendMedication.setPupil(pupil);
        return sendMedicationMapper.toDto(sendMedicationRepo.save(sendMedication));
    }
}
