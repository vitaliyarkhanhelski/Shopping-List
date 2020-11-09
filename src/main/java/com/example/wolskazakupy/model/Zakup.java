package com.example.wolskazakupy.model;

import lombok.*;

import javax.persistence.*;

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

    private String imgLink;

    private boolean inProcess;

    public Zakup(String name, String comment, boolean inProcess) {
        this.name = name;
        this.comment = comment;
        this.inProcess = inProcess;
    }

    public Zakup(Long id, String name, String comment, boolean inProcess) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.inProcess = inProcess;
    }
}
