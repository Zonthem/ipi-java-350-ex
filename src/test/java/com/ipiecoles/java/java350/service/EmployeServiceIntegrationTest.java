package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
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
class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService;

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    public void before() {
        employeRepository.deleteAll();
    }

    @Test
    void testEmbaucheEmploye() throws EmployeException {
        //Given
        String nom = "Obama";
        String prenom = "Captain";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        Employe e = employeRepository.findByMatricule("T00001");

        //Then
        Assertions.assertNotNull(e);
        Assertions.assertEquals(nom, e.getNom());
        Assertions.assertEquals(prenom, e.getPrenom());
        Assertions.assertEquals(tempsPartiel, e.getTempsPartiel());
        Assertions.assertEquals(new Integer(0), e.getNombreAnneeAnciennete());
        Assertions.assertEquals(1825.46, e.getSalaire().doubleValue());
    }

    @Test
    void testCalculPerformanceCommercialePlus30WithAverageBonus() throws EmployeException {
        //Given
        employeRepository.save(new Employe("Jean1", "Michel", "C00001", LocalDate.now(), 1000D, 3, 1.0D));
        employeRepository.save(new Employe("Jean2", "Michel", "C00002", LocalDate.now(), 1000D, 5, 1.0D));
        employeRepository.save(new Employe("Jean3", "Michel", "C00003", LocalDate.now(), 1000D, 7, 1.0D));

        String matricule = "C00001";
        long ca = 1300;
        long obj = 1000;

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);
        Employe employe = employeRepository.findByMatricule("C00001");

        //Then
        Assertions.assertEquals(3+5, (int) employe.getPerformance());

    }
}