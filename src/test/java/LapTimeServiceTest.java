import com.foxminded.LapTimeService;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class LapTimeServiceTest {

    private final LapTimeService lapTimeService = new LapTimeService();

    @Test
    void nullTest() throws IOException {
        try {
            lapTimeService.analyzeRace(null, "gdgd" , null);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException nullObject) {
        }
    }

    @Test
    void wrongNameFile() throws  IOException{
        try {
            lapTimeService.analyzeRace("wrongName", "end.log", "wrongname2.txt");
            fail("Expected exception not thrown");
        } catch (FileNotFoundException exception) {
        }
    }

    @Test
    void inputTest() throws IOException {
        String expected = """
                1|Sebastian Vettel         |FERRARI                   |01:04.415
                2|Daniel Ricciardo         |RED BULL RACING TAG HEUER |01:12.013
                3|Valtteri Bottas          |MERCEDES                  |01:12.434
                4|Lewis Hamilton           |MERCEDES                  |01:12.460
                5|Stoffel Vandoorne        |MCLAREN RENAULT           |01:12.463
                6|Kimi Raikkonen           |FERRARI                   |01:12.639
                7|Fernando Alonso          |MCLAREN RENAULT           |01:12.657
                8|Sergey Sirotkin          |WILLIAMS MERCEDES         |01:12.706
                9|Charles Leclerc          |SAUBER FERRARI            |01:12.829
                10|Sergio Perez            |FORCE INDIA MERCEDES      |01:12.848
                11|Romain Grosjean         |HAAS FERRARI              |01:12.930
                12|Pierre Gasly            |SCUDERIA TORO ROSSO HONDA |01:12.941
                13|Carlos Sainz            |RENAULT                   |01:12.950
                14|Esteban Ocon            |FORCE INDIA MERCEDES      |01:13.028
                15|Nico Hulkenberg         |RENAULT                   |01:13.065
                ----------------------------------------------------------------
                16|Brendon Hartley         |SCUDERIA TORO ROSSO HONDA |01:13.179
                17|Marcus Ericsson         |SAUBER FERRARI            |01:13.265
                18|Lance Stroll            |WILLIAMS MERCEDES         |01:13.323
                19|Kevin Magnussen         |HAAS FERRARI              |01:13.393
                """;
        assertEquals(expected, lapTimeService.analyzeRace("start.log", "end.log" , "abbreviations.txt"));
    }

    @Test
    void ifRacerless15() throws IOException {
        File startLog = File.createTempFile("start", ".txt");
        File endLog = File.createTempFile("end", ".txt");
        File abbreviations = File.createTempFile("abbreviations", ".txt");
        try {
            FileWriter startWriter = new FileWriter(startLog);
            startWriter.write("""
                SVF2018-05-24_12:02:58.917
                NHR2018-05-24_12:02:49.914
                FAM2018-05-24_12:13:04.512
                KRF2018-05-24_12:03:01.250
                SVM2018-05-24_12:18:37.735
                """);
            startWriter.close();

            FileWriter endWriter = new FileWriter(endLog);
            endWriter.write("""
                SVF2018-05-24_12:04:03.332
                NHR2018-05-24_12:04:02.979
                FAM2018-05-24_12:14:17.169
                KRF2018-05-24_12:04:13.889
                SVM2018-05-24_12:19:50.198
                """);
            endWriter.close();

            FileWriter abbreviationsWriter = new FileWriter(abbreviations);
            abbreviationsWriter.write("""
                SVF_Sebastian Vettel_FERRARI
                NHR_Nico Hulkenberg_RENAULT
                FAM_Fernando Alonso_MCLAREN RENAULT
                KRF_Kimi Raikkonen_FERRARI
                SVM_Stoffel Vandoorne_MCLAREN RENAULT
                """);
            abbreviationsWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        String expected =
            """
                1|Sebastian Vettel         |FERRARI                   |01:04.415
                2|Stoffel Vandoorne        |MCLAREN RENAULT           |01:12.463
                3|Kimi Raikkonen           |FERRARI                   |01:12.639
                4|Fernando Alonso          |MCLAREN RENAULT           |01:12.657
                5|Nico Hulkenberg          |RENAULT                   |01:13.065
                """;

        String receive = lapTimeService.analyzeRace(startLog, endLog, abbreviations);
        startLog.deleteOnExit();
        endLog.deleteOnExit();
        abbreviations.deleteOnExit();
        assertEquals(expected, receive);

    }

}
