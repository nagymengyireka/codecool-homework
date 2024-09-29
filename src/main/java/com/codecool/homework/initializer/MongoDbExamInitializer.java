package com.codecool.homework.initializer;

import com.codecool.homework.model.Module;
import com.codecool.homework.model.MongoExam;
import com.codecool.homework.model.MongoResult;
import com.codecool.homework.repository.MongoExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MongoDbExamInitializer implements CommandLineRunner {
    private final MongoExamRepository mongoExamRepository;

    @Autowired
    public MongoDbExamInitializer(MongoExamRepository mongoExamRepository) {
        this.mongoExamRepository = mongoExamRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        mongoExamRepository.deleteAll();

            MongoExam exam1 = new MongoExam();
            exam1.setModule(Module.PROG_BASICS);
            exam1.setMentor("peter.szarka@codecool.com");
            exam1.setStudent("foo@bar.com");
            exam1.setDate(LocalDate.of(2024, 2,1));
            exam1.setCancelled(true);
            exam1.setComment("Foo was sick.");
            mongoExamRepository.save(exam1);

            MongoExam exam2 = new MongoExam();
            exam2.setModule(Module.PROG_BASICS);
            exam2.setMentor("peter.szarka@codecool.com");
            exam2.setStudent("foo@bar.com");
            exam2.setDate(LocalDate.of(2024, 2,5));
            exam2.setCancelled(false);
            exam2.setSuccess(true);
            exam2.setComment("Everything was ok.");
            exam2.setResults(List.of(new MongoResult("Coding", 80), new MongoResult("Communication", 100)));
            mongoExamRepository.save(exam2);

            MongoExam exam3 = new MongoExam();
            exam3.setModule(Module.WEB);
            exam3.setMentor("mano.fabian@codecool.com");
            exam3.setStudent("foo@bar.com");
            exam3.setDate(LocalDate.of(2024, 5,11));
            exam3.setCancelled(false);
            exam3.setSuccess(false);
            exam3.setComment("Couldn't really start, just wrote some HTML.");
            exam3.setResults(List.of(new MongoResult("Coding", 0), new MongoResult("HTML", 30), new MongoResult("Communication", 30)));
            mongoExamRepository.save(exam3);

            MongoExam exam4 = new MongoExam();
            exam4.setModule(Module.WEB);
            exam4.setMentor("peter.szarka@codecool.com");
            exam4.setStudent("foo@bar.com");
            exam4.setDate(LocalDate.of(2024, 5,21));
            exam4.setCancelled(false);
            exam4.setSuccess(false);
            exam4.setComment("Wrote spaghetti code, and tried to sell it. Nice page, though.");
            exam4.setResults(List.of(new MongoResult("Coding", 20), new MongoResult("HTML", 100), new MongoResult("Communication", 80)));
            mongoExamRepository.save(exam4);

    }
}
