package com.defense.demo.model;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    private int id;
    private String name;
    private String fname;
    private String mname;
    private String dept;
    private String faculty;
    private int level;
    private int semester;
    private String hall;

    // Getters and setters (omit for brevity)

}
