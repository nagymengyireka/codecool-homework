package com.codecool.homework.service;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.entity.Exam;
import com.codecool.homework.model.entity.ExamResult;
import com.codecool.homework.model.MongoExam;
import com.codecool.homework.repository.CodecoolUserRepository;
import com.codecool.homework.repository.ExamRepository;
import com.codecool.homework.repository.MongoExamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExamTransferService {

    private final MongoExamRepository mongoExamRepository;
    private final ExamRepository examRepository;
    private final CodecoolUserRepository codecoolUserRepository;
    private static final Logger logger = LoggerFactory.getLogger(ExamTransferService.class);

    @Autowired
    public ExamTransferService(MongoExamRepository mongoExamRepository, ExamRepository examRepository, CodecoolUserRepository codecoolUserRepository) {
        this.mongoExamRepository = mongoExamRepository;
        this.examRepository = examRepository;
        this.codecoolUserRepository = codecoolUserRepository;
    }

    @Transactional
    public void transferNewExams() {
        Optional<LocalDate> latestExamDate = examRepository.findLatestExamDate();
        List<MongoExam> mongoExams = latestExamDate.isEmpty() ? mongoExamRepository.findAll() : mongoExamRepository.findAllByDateGreaterThan(latestExamDate.get());

        if (mongoExams.isEmpty()) {
            logger.info("No exams found to transfer.");
            return;
        }

        mongoExams.forEach(mongoExam -> {
            try {
                Exam newExam = convertMongoExam(mongoExam);
                if (newExam != null) {
                    examRepository.save(newExam);
                    logger.info("Exam transferred successfully.");
                }
            } catch (Exception e) {
                logger.error("Failed to transfer exam. Error: {}", e.getMessage());
            }
        });
    }

    private Exam convertMongoExam(MongoExam mongoExam) {
        Optional<CodecoolUser> student = codecoolUserRepository.findByEmail(mongoExam.getStudent());
        Optional<CodecoolUser> mentor = codecoolUserRepository.findByEmail(mongoExam.getMentor());

        if (student.isEmpty() || mentor.isEmpty()) {
            logger.warn("No student or mentor found for the exam.");
            return null;
        }

        Exam exam = new Exam();
        exam.setModule(mongoExam.getModule());
        exam.setStudent(student.get());
        exam.setMentor(mentor.get());
        exam.setDate(mongoExam.getDate());
        exam.setCancelled(mongoExam.isCancelled());
        exam.setSuccess(mongoExam.isSuccess());
        exam.setComment(mongoExam.getComment());
        exam.setResults(covertMongoResults(mongoExam));
        return exam;
    }

    private List<ExamResult> covertMongoResults(MongoExam mongoExam) {
        if (mongoExam.getResults() == null) {
            return null;
        }
        return mongoExam.getResults().stream().map(mongoResult -> {
            ExamResult result = new ExamResult();
            result.setDimension(mongoResult.getDimension());
            result.setResult(mongoResult.getResult());
            return result;
        }).toList();
    }
}
