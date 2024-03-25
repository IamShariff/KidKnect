package com.kidknect.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kidknect.enums.AttendenceStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Attendence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "roll_number")
    @JsonBackReference
    private Student student;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendenceStatus status;

}
