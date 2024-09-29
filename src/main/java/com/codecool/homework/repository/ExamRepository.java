package com.codecool.homework.repository;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByStudentAndCancelledFalseOrderByDateDesc(CodecoolUser student);

    @Query("SELECT MAX(e.date) FROM Exam e")
    Optional<LocalDate> findLatestExamDate();
}
