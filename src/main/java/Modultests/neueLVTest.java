package Modultests;

import Client.Lehrveranstaltung.Lehrveranstaltung;
import Server.Datenbank;
import org.junit.jupiter.api.BeforeAll;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class neueLVTest {
    private Datenbank db;

    @BeforeAll
    public void before() {
        db = new Datenbank();
    }

    @Test
    public void testLV() {
        Lehrveranstaltung nlv = new Lehrveranstaltung("BWL", "Vorlesung", "SoSe 2021");
        db.DbLVHinzufuegen(nlv, 1000042);
        boolean vorhanden = db.checkVorhandenLV("BWL", "Vorlesung", "SoSe 2021");
        assertTrue(vorhanden);
    }
}
