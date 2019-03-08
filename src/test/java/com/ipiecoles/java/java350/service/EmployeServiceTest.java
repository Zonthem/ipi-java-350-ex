package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    void testEmbaucheEmployeTechPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Obama";
        String prenom = "Captain";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(null);
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("12345");

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(nom, employeCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeCaptor.getValue().getPrenom());
        Assertions.assertEquals(tempsPartiel, employeCaptor.getValue().getTempsPartiel());
        Assertions.assertEquals("T12346", employeCaptor.getValue().getMatricule());
        Assertions.assertEquals(new Integer(0), employeCaptor.getValue().getNombreAnneeAnciennete());
        Assertions.assertEquals(1825.46, (double)employeCaptor.getValue().getSalaire().doubleValue());
    }


    @Test
    void testEmbaucheEmployeMatriculeMax() throws EmployeException {
        //Given
        String nom = "Obama";
        String prenom = "Captain";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When
        Assertions.assertThrows(EmployeException.class, () -> {
                employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        });

    }

    @Test
    void testEmbaucheEmployeMatriculeExists() throws EmployeException {
        //Given
        String nom = "Obama";
        String prenom = "Captain";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        Mockito.when(employeRepository.findByMatricule(Mockito.anyString())).thenReturn(new Employe());
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("12345");

        //When
        Assertions.assertThrows(EntityExistsException.class, () -> {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        });

    }

    @Test
    void testCalculPerformanceCommerciale() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 1000;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(emp);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1D);

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(1,  (int)employeCaptor.getValue().getPerformance());
    }

    @Test
    void testCalculPerformanceCommercialePlus10() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 1100;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(emp);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(5D);

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(2,  (int)employeCaptor.getValue().getPerformance());
    }

    @Test
    void testCalculPerformanceCommercialePlus30WithAverageBonus() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 1300;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(emp);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1D);

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(6,  (int)employeCaptor.getValue().getPerformance());
    }

    @Test
    void testCalculPerformanceCommercialeMinus20() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 5, 1.0D);
        String matricule = "C00001";
        long ca = 800;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(emp);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(5D);

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(3,  (int)employeCaptor.getValue().getPerformance());
    }

    @Test
    void testCalculPerformanceCommercialeMinus30BelowEnterpriseMinimal() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 700;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(emp);
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1D);

        //When
        employeService.calculPerformanceCommercial(matricule,ca,obj);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertEquals(1,  (int)employeCaptor.getValue().getPerformance());
    }

    @Test
    void testCalculPerformanceCommercialeCAObjNull() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 700;
        Long obj = null;

        //WhenThen
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> {
            employeService.calculPerformanceCommercial(matricule,ca,obj);
        });
        Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", ex.getMessage());
    }

    @Test
    void testCalculPerformanceCommercialeCAReelNull() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        Long ca = null;
        long obj = 1000;

        //WhenThen
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> {
            employeService.calculPerformanceCommercial(matricule,ca,obj);
        });
        Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", ex.getMessage());
    }

    @Test
    void testCalculPerformanceCommercialeMatriculeFaux() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "AHAHA_MDR";
        long ca = 700;
        long obj = 1000;

        //WhenThen
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> {
            employeService.calculPerformanceCommercial(matricule,ca,obj);
        });
        Assertions.assertEquals("Le matricule ne peut être null et doit commencer par un C !", ex.getMessage());
    }

    @Test
    void testCalculPerformanceCommercialeNoEmployeEnBase() throws EmployeException {
        //Given
        Employe emp = new Employe("Jean", "Michel", "C00001", LocalDate.now(), 1000D, 1, 1.0D);
        String matricule = "C00001";
        long ca = 700;
        long obj = 1000;

        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);

        //WhenThen
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> {
            employeService.calculPerformanceCommercial(matricule,ca,obj);
        });
        Assertions.assertEquals("Le matricule " + matricule + " n'existe pas !", ex.getMessage());
    }
}