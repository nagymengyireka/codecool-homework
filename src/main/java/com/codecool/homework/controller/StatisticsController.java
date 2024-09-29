package com.codecool.homework.controller;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.repository.CodecoolUserRepository;
import com.codecool.homework.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RequestMapping("/api/statistics")
@RestController
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final CodecoolUserRepository codecoolUserRepository;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, CodecoolUserRepository codecoolUserRepository) {
        this.statisticsService = statisticsService;
        this.codecoolUserRepository = codecoolUserRepository;
    }

    @GetMapping("/all")
    public Map<String, Map<String, Double>> getAllStudentAverages() {
        return statisticsService.getAverageResults();
    }

    @GetMapping("/{email}")
    public Map<String, Double> getStudentAverages(@PathVariable String email) {
        Optional<CodecoolUser> student = codecoolUserRepository.findByEmail(email);
        return student
                .map(statisticsService::calculateStudentAverage)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student email: " + email));
    }
}
