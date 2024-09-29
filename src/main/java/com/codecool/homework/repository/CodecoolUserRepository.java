package com.codecool.homework.repository;

import com.codecool.homework.model.entity.CodecoolUser;
import com.codecool.homework.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodecoolUserRepository extends JpaRepository<CodecoolUser, Long> {
    Optional<CodecoolUser> findByEmail(String email);
    List<CodecoolUser> findAllByRole(Role role);
}
