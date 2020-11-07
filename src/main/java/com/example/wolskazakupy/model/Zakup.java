package com.example.wolskazakupy.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Zakup{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String comment;

    private boolean inProcess;

    public Zakup(String name, String comment, boolean inProcess) {
        this.name = name;
        this.comment = comment;
        this.inProcess = inProcess;
    }

}
