package com.github.homework.theme.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "theme_seq_generator",
        sequenceName = "theme_seq", allocationSize = 10)
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "theme_seq_generator")
    private Long id;

    @Column(unique = true)
    private String name;

    public Theme(String name) {
        assert !name.isEmpty() : "empty name";
        this.name = name;
    }

}
