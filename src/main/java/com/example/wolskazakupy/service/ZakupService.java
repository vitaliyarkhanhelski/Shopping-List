package com.example.wolskazakupy.service;

import com.example.wolskazakupy.model.Zakup;
import com.example.wolskazakupy.repository.ZakupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZakupService {

    private ZakupRepository zakupRepository;

    @Autowired
    public ZakupService(ZakupRepository zakupRepository) {
        this.zakupRepository = zakupRepository;
    }

    public void save(Zakup zakup) {
        zakupRepository.save(zakup);
    }

    public List<Zakup> findAll() {
        return zakupRepository.findAll();
    }

    public Zakup findById(Long zakupId) {
        return zakupRepository.findById(zakupId).get();
    }

    public void delete(Long zakupId) {
        zakupRepository.deleteById(zakupId);
    }

    public Optional<Zakup> findFirstByNameAndComment(String name, String comment) {
        return zakupRepository.findFirstByNameAndComment(name, comment);
    }
}
