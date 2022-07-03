package com.example.application.startup;

import com.example.application.exception.DataPersistException;
import com.example.application.model.dto.ReservationDto;
import com.example.application.service.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Application Runner to execute on load steps
 * The run method will load the sample data from the resources locations/
 * Parse the json data and upload to database
 * */
@Slf4j
@Component
@Order(0)
class OnLoadInitializeDataRunner implements ApplicationRunner {

    @Value("${app.init.data}")
    private String sampleDataPath;

    private CrudService crudService;

    private ObjectMapper objectMapper;

    @Autowired
    public OnLoadInitializeDataRunner(CrudService crudService, ObjectMapper objectMapper) {
        this.crudService = crudService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(ApplicationArguments args) throws DataPersistException, IOException {

        var javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ReservationDto.class);
        List<ReservationDto> reservations = objectMapper.readValue(new File(sampleDataPath), javaType);

        crudService.saveAllReservations(reservations);

        log.info("ApplicationRunner#run() complete");
    }

}