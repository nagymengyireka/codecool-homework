package com.codecool.homework.service;

import com.codecool.homework.model.Module;
import com.codecool.homework.model.MongoExam;
import com.codecool.homework.model.MongoResult;
import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.entity.Exam;
import com.codecool.homework.repository.CodecoolUserRepository;
import com.codecool.homework.repository.ExamRepository;
import com.codecool.homework.repository.MongoExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamTransferServiceTest {
    private ExamTransferService examTransferService;

    @Mock
    private MongoExamRepository mongoExamRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private CodecoolUserRepository codecoolUserRepository;

    @BeforeEach
    void setUp() {
        this.examTransferService = new ExamTransferService(mongoExamRepository, examRepository, codecoolUserRepository);
    }

    @Test
    void transferNewExams_LogsMessage_WhenNoExamsFound() {
        doReturn(Optional.empty()).when(examRepository).findLatestExamDate();
        doReturn(List.of()).when(mongoExamRepository).findAll();

        examTransferService.transferNewExams();

        verify(examRepository).findLatestExamDate();
        verify(mongoExamRepository).findAll();
        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    void transferNewExams_TransfersExamsSuccessfully() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));

        CodecoolUser student = new CodecoolUser();
        student.setEmail("foo@bar.com");

        CodecoolUser mentor = new CodecoolUser();
        mentor.setEmail("peter.szarka@codecool.com");

        doReturn(Optional.empty()).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAll();
        doReturn(Optional.of(student)).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.of(mentor)).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");

        examTransferService.transferNewExams();

        verify(examRepository).save(any(Exam.class));
    }

    @Test
    void transferNewExams_logsWarning_WhenStudentIsNotFound() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));

        doReturn(Optional.empty()).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAll();
        doReturn(Optional.empty()).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.of(new CodecoolUser())).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");

        examTransferService.transferNewExams();

        verify(examRepository, never()).save(any(Exam.class));
        verify(codecoolUserRepository).findByEmail("foo@bar.com");
    }

    @Test
    void transferNewExams_logsWarning_WhenMentorIsNotFound() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));

        doReturn(Optional.empty()).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAll();
        doReturn(Optional.of(new CodecoolUser())).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.empty()).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");

        examTransferService.transferNewExams();

        verify(examRepository, never()).save(any(Exam.class));
        verify(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");
    }

    @Test
    void transferNewExams_handlesException() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));

        CodecoolUser student = new CodecoolUser();
        student.setEmail("foo@bar.com");

        CodecoolUser mentor = new CodecoolUser();
        mentor.setEmail("peter.szarka@codecool.com");

        doReturn(Optional.empty()).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAll();
        doReturn(Optional.of(student)).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.of(mentor)).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");
        doThrow(new RuntimeException("Test exception")).when(examRepository).save(any(Exam.class));

        examTransferService.transferNewExams();

        verify(examRepository).save(any(Exam.class));
    }

    @Test
    void transferNewExams_OnlyTransfersExams_AfterACertainDate() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));

        CodecoolUser student = new CodecoolUser();
        student.setEmail("foo@bar.com");

        CodecoolUser mentor = new CodecoolUser();
        mentor.setEmail("peter.szarka@codecool.com");

        LocalDate latestExamDate = LocalDate.of(2024, 1,19);

        doReturn(Optional.of(latestExamDate)).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAllByDateGreaterThan(latestExamDate);
        doReturn(Optional.of(student)).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.of(mentor)).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");

        examTransferService.transferNewExams();

        verify(examRepository).save(any(Exam.class));
    }

    @Test
    void transferNewExams_TransfersExams_WithoutResults() {
        MongoExam mongoExam = new MongoExam();
        mongoExam.setModule(Module.PROG_BASICS);
        mongoExam.setMentor("peter.szarka@codecool.com");
        mongoExam.setStudent("foo@bar.com");
        mongoExam.setDate(LocalDate.of(2024, 2,5));
        mongoExam.setCancelled(false);
        mongoExam.setSuccess(true);
        mongoExam.setComment("Everything was ok.");
        mongoExam.setResults(null);

        CodecoolUser student = new CodecoolUser();
        student.setEmail("foo@bar.com");

        CodecoolUser mentor = new CodecoolUser();
        mentor.setEmail("peter.szarka@codecool.com");

        LocalDate latestExamDate = LocalDate.of(2024, 1,19);

        doReturn(Optional.of(latestExamDate)).when(examRepository).findLatestExamDate();
        doReturn(List.of(mongoExam)).when(mongoExamRepository).findAllByDateGreaterThan(latestExamDate);
        doReturn(Optional.of(student)).when(codecoolUserRepository).findByEmail("foo@bar.com");
        doReturn(Optional.of(mentor)).when(codecoolUserRepository).findByEmail("peter.szarka@codecool.com");

        examTransferService.transferNewExams();

        verify(examRepository).save(any(Exam.class));
    }
}