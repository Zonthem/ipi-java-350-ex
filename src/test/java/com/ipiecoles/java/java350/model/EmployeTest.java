package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeTest {

    @Test
    void testGetNombreAnneeAncienneteDateEmbaucheNow() {
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now());
        //When
        Integer nbAnneeAnciennete= e.getNombreAnneeAnciennete();
        //Then
        Assertions.assertEquals(0, (int)nbAnneeAnciennete);
    }

    @Test
    void testGetNombreAnneeAncienneteDateEmbaucheFutur() {
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().plusYears(2L));
        //When
        Integer nbAnneeAnciennete= e.getNombreAnneeAnciennete();
        //Then
        Assertions.assertEquals(0, (int)nbAnneeAnciennete);
    }

    @Test
    void testGetNombreAnneeAncienneteDateEmbaucheMinus2Years() {
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));
        //When
        Integer nbAnneeAnciennete= e.getNombreAnneeAnciennete();
        //Then
        Assertions.assertEquals(2, (int)nbAnneeAnciennete);
    }

    @Test
    void testGetNombreAnneeAncienneteDateEmbaucheNull() {
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);
        //When
        Integer nbAnneeAnciennete= e.getNombreAnneeAnciennete();
        //Then
        Assertions.assertEquals(0, (int)nbAnneeAnciennete);
    }

    @ParameterizedTest(name = "Employe anciennete : {0}, matricule : {1}, performance : {2}, temps partiel : {3} a une prime de {4}")
    @CsvSource({
            "0, 'M02586', 1, 1D, 1700D",
            "0, 'T02586', 1, 1D, 1000D",
            "0, 'M02586', 1, 0.5D, 850D",
            "1, 'M02586', 1, 1D, 1800D",
            "0, 'M02586', 1, 0D, 0D",
    })
    void testGetPrimeAnnuelle(int dtEmbauche, String mat, int perf, double tp, double expected) {
        //Given
        Employe e = new Employe("nom", "prenom", mat, LocalDate.now().minusYears(dtEmbauche), 2000D, perf, tp);
        //When
        double result = e.getPrimeAnnuelle();
        //Then
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testAugmenterSalairePlusCentPourcent() throws EmployeException {
        //Given
        Employe e = new Employe("Jean", "Michel", "M00025", LocalDate.now(), 1000D, 1, 1.0D);

        //When
        e.augmenterSalaire(1D);

        //Then
        Assertions.assertEquals(2000D, (double)e.getSalaire());
    }
    @Test
    void testAugmenterSalaireMoinsCentPourcent() {
        //Given
        Employe e = new Employe("Jean", "Michel", "M00025", LocalDate.now(), 1000D, 1, 1.0D);

        //When //Then
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> e.augmenterSalaire(-1D));
        Assertions.assertEquals("L'augmentation ne peut être que positive", ex.getMessage());
    }

    @Test
    void testAugmenterSalaireSalaireNull() {
        //Given
        Employe e = new Employe("Jean", "Michel", "M00025", LocalDate.now(), null, 1, 1.0D);

        //When //Then
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> e.augmenterSalaire(1D));
        Assertions.assertEquals("Cet employé ne peut pas être augmenté, il n'est pas payé...", ex.getMessage());
    }

    @Test
    void testAugmenterSalaireAugmentationNull() {
        //Given
        Employe e = new Employe("Jean", "Michel", "M00025", LocalDate.now(), 1000D, 1, 1.0D);

        //When //Then
        EmployeException ex = Assertions.assertThrows(EmployeException.class, () -> e.augmenterSalaire(null));
        Assertions.assertEquals("L'augmentation ne peut être que positive", ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "2019, 1.0D, 8",
            "2019, 0.5D, 4",
            "2021, 1D, 10",
            "2032, 1D, 11",
            "2019, 0D, 0",
            "2022, 1D, 10",
            "2044, 1D, 9",
    })
    void testGetNbRtt(int annee, double tempsPartiel, int expected) {
        //Given
        Employe e = new Employe("Jean", "Michel", "M00025", LocalDate.now(), 1000D, 1, tempsPartiel);
        //When
        int rtt = e.getNbRtt(LocalDate.now().withYear(annee));
        //Then
        Assertions.assertEquals(expected, rtt);
    }
}