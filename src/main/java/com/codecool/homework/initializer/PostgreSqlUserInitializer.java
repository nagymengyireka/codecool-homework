package com.codecool.homework.initializer;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.Role;
import com.codecool.homework.repository.CodecoolUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PostgreSqlUserInitializer implements CommandLineRunner {
    private final CodecoolUserRepository codecoolUserRepository;

    @Autowired
    public PostgreSqlUserInitializer(CodecoolUserRepository codecoolUserRepository) {
        this.codecoolUserRepository = codecoolUserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (codecoolUserRepository.count() == 0) {
            CodecoolUser mentor1 = new CodecoolUser();
            mentor1.setName("Peter Szarka");
            mentor1.setEmail("peter.szarka@codecool.com");
            mentor1.setRole(Role.MENTOR);
            codecoolUserRepository.save(mentor1);

            CodecoolUser mentor2 = new CodecoolUser();
            mentor2.setName("Mano Fabian");
            mentor2.setEmail("mano.fabian@codecool.com");
            mentor2.setRole(Role.MENTOR);
            codecoolUserRepository.save(mentor2);

            CodecoolUser student1 = new CodecoolUser();
            student1.setName("Foo Bar");
            student1.setEmail("foo@bar.com");
            student1.setRole(Role.STUDENT);
            codecoolUserRepository.save(student1);
        }
    }
}
