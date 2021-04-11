package reega.data.export;

import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import reega.data.DataController;
import reega.data.DataControllerFactory;
import reega.data.exporter.ExportFormat;
import reega.data.exporter.ReegaExporterFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;
import reega.io.IOController;
import reega.io.IOControllerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static reega.data.utils.FileUtils.getFileFromResources;
import static reega.data.utils.FileUtils.getFileFromResourcesAsString;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataExportTest {
    private final long baseTimestamp = 1898938800000L;
    private String basePath;
    private RemoteConnection connection;
    private DataController controller;

    @BeforeAll
    public void setup() throws IOException {
        connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        controller = DataControllerFactory.getRemoteDatabaseController(connection);

        addContract("Address 1");
        addContract("Address 2");

        IOController ioController = IOControllerFactory.getDefaultIOController();
        basePath = ioController.getDefaultDirectoryPath() + File.separator;
    }

    @AfterAll
    public void cleanup() throws IOException {
        connection.getService().terminateTest().execute();
        connection.logout();
    }

    @Test
    @Order(1)
    public void exportEmptyData() throws IOException, URISyntaxException {
        ReegaExporterFactory.export(ExportFormat.JSON, null, basePath + "json0.json");
        checkJsonOutput("json0.json");
        deleteFile("json0.json");

        ReegaExporterFactory.export(ExportFormat.CSV, null, basePath + "csv0.csv");
        checkFileContent("csv0.csv");
        deleteFile("csv0.csv");
    }

    @Test
    @Order(2)
    public void insertData() throws IOException {
        var contracts = controller.getUserContracts();
        assertEquals(2, contracts.size());
        for (Contract c : contracts) {
            insertData(c.getId());
        }
    }

    @Test
    @Order(3)
    public void exportJSON() throws IOException, URISyntaxException {
        var data = controller.getMonthlyData(null);

        ReegaExporterFactory.export(ExportFormat.JSON, data, basePath + "json1.json");
        checkJsonOutput("json1.json");
        deleteFile("json1.json");
    }

    @Test
    @Order(4)
    public void exportCSV() throws IOException, URISyntaxException {
        var data = controller.getMonthlyData(null);

        ReegaExporterFactory.export(ExportFormat.CSV, data, basePath + "csv1.csv");
        checkFileContent("csv1.csv");
        deleteFile("csv1.csv");
    }

    private void checkJsonOutput(final String fileName) throws URISyntaxException, IOException {
        final File file = new File(basePath + fileName);
        assertTrue(file.exists());
        String fileContent = new String(Files.readAllBytes(file.toPath()));
        String testContent = getFileFromResourcesAsString("exporter/" + fileName);

        assertEquals(JsonParser.parseString(testContent), JsonParser.parseString(fileContent));
    }

    private void checkFileContent(final String fileName) throws IOException, URISyntaxException {
        final File file = new File(basePath + fileName);
        assertTrue(file.exists());

        File testFile = getFileFromResources("exporter/" + fileName);
        assertTrue(FileUtils.contentEquals(testFile, file));
    }

    private void deleteFile(final String fileName) {
        File file = new File(basePath + fileName);
        if (!file.exists() || !file.isFile() || !file.delete()) {
            fail("Invalid test file " + file.getAbsolutePath());
        }
    }

    private void insertData(int contractID) throws IOException {
        final Map<Long, Double> data = new HashMap<>() {{
            put(baseTimestamp + 1000, 5.5);
            put(baseTimestamp + 2000, 6.4);
            put(baseTimestamp + 3000, 7.3);
        }};

        controller.putUserData(new Data(contractID, DataType.ELECTRICITY, data));
        controller.putUserData(new Data(contractID, DataType.GLASS, data));
        controller.putUserData(new Data(contractID, DataType.WATER, data));
        controller.putUserData(new Data(contractID, DataType.PAPER, data));
        controller.putUserData(new Data(contractID, DataType.MIXED, data));
    }

    private void addContract(String address) throws IOException {
        List<ServiceType> services = List.of(
                ServiceType.ELECTRICITY,
                ServiceType.GARBAGE
        );
        NewContract newContract = new NewContract(address, services, "ABC123", new Date(baseTimestamp));
        controller.addContract(newContract);
    }
}
