package com.codecool.homework.service;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.entity.Exam;
import com.codecool.homework.model.Module;
import com.codecool.homework.model.Role;
import com.codecool.homework.repository.CodecoolUserRepository;
import com.codecool.homework.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final ExamRepository examRepository;
    private final CodecoolUserRepository codecoolUserRepository;

    @Autowired
    public StatisticsService(ExamRepository examRepository, CodecoolUserRepository codecoolUserRepository) {
        this.examRepository = examRepository;
        this.codecoolUserRepository = codecoolUserRepository;
    }

    public Map<String, Map<String, Double>> getAverageResults() {
        List<CodecoolUser> students = codecoolUserRepository.findAllByRole(Role.STUDENT);
        Map<String, Map<String, Double>> studentAverages = new HashMap<>();

        students.forEach(student -> {
            studentAverages.put(student.getEmail(), calculateStudentAverage(student));
        });

        return studentAverages;
    }

    public Map<String, Double> calculateStudentAverage(CodecoolUser student) {
        List<Exam> exams = examRepository.findByStudentAndCancelledFalseOrderByDateDesc(student);

        Map<Module, Exam> latestExams = new HashMap<>();
        exams.forEach(exam -> {
            latestExams.putIfAbsent(exam.getModule(), exam);
        });

        Map<String, List<Integer>> results = new HashMap<>();
        latestExams.values().forEach(exam -> {
            exam.getResults().forEach(result -> {
                results.computeIfAbsent(result.getDimension(), k -> new ArrayList<>()).add(result.getResult());
            });
        });

        Map<String, Double> averages = new HashMap<>();
        results.forEach((dimension, result) -> {
            double average = result.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            averages.put(dimension, average);
        });

        return averages;
    }
}
