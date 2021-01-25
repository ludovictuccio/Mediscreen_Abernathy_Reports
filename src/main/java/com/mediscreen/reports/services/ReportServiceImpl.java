package com.mediscreen.reports.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediscreen.reports.domain.dto.PatientDto;
import com.mediscreen.reports.proxies.MicroservicePatientProxy;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ReportServiceImpl.class);

    @Autowired
    private MicroservicePatientProxy microservicePatientProxy;

    public PatientDto getPatientPersonalInformations(final Long patId) {
        return microservicePatientProxy.getPatientPersonalInformations(patId);
    }

}
