package com.vignesh.demo.model;

import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {
    @Id
    @Generated(GenerationTime.INSERT)
    private long id;
    private String name;
    private char gender;
}
