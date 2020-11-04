package com.example.wolskazakupy.repository;

import com.example.wolskazakupy.model.Zakup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZakupRepository extends JpaRepository<Zakup, Long> {
    Optional<Zakup> findFirstByNameAndComment(String name, String comment);
}
