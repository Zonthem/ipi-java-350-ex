package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeRepositoryIntegrationTest {

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    public void before() {
        employeRepository.deleteAll();

        employeRepository.save(new Employe("Jean1", "Michel", "C00001", LocalDate.now(), 1000D, 3, 1.0D));
        employeRepository.save(new Employe("Jean2", "Michel", "C00002", LocalDate.now(), 1000D, 5, 1.0D));
        employeRepository.save(new Employe("Jean3", "Michel", "C00003", LocalDate.now(), 1000D, 7, 1.0D));
        employeRepository.save(new Employe("Jean1", "Michel", "C00011", LocalDate.now(), 1000D, 12, 1.0D));
        employeRepository.save(new Employe("Jean2", "Michel", "M00002", LocalDate.now(), 1000D, 9, 1.0D));
        employeRepository.save(new Employe("Jean3", "Michel", "T00003", LocalDate.now(), 1000D, 7, 1.0D));
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithC() {
        //Given
        Double expected = (3D + 5D + 7D + 12D) / 4D;

        //When
        Double result = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertEquals(expected, result);

    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithUnknowLettre() {
        //Given

        //When
        Double result = employeRepository.avgPerformanceWhereMatriculeStartsWith("X");

        //Then
        Assertions.assertNull(result);

    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithMultipleLettre() {
        //Given

        //When
        Double result = employeRepository.avgPerformanceWhereMatriculeStartsWith("CMD");

        //Then
        Assertions.assertNull(result);

    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithNull() {
        //Given

        //When
        Double result = employeRepository.avgPerformanceWhereMatriculeStartsWith(null);

        //Then
        Assertions.assertNull(result);

    }

}