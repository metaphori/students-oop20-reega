package reega.data.export;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.gson.JsonParser;

import reega.data.ContractController;
import reega.data.DataController;
import reega.data.exporter.ExportFormat;
import reega.data.exporter.ReegaExporterFactory;
import reega.data.factory.ContractControllerFactory;
import reega.data.factory.DataControllerFactory;
import reega.data.mock.TestConnection;
import reega.data.models.Contract;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.ServiceType;
import reega.data.models.gson.NewContract;
import reega.data.remote.RemoteConnection;
import reega.io.IOController;
import reega.io.IOControllerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public final class DataExportTest {
    private static final long BASE_TIMESTAMP = 1898938800000L;
    private String basePath;
    private RemoteConnection connection;
    private ContractController contractController;
    private DataController dataController;

    @BeforeAll
    public void setup() throws IOException {
        this.connection = new TestConnection().getTestConnection("admin@reega.it", "AES_PASSWORD");
        this.contractController = ContractControllerFactory.getRemoteDatabaseController(this.connection);
        this.dataController = DataControllerFactory.getDefaultDataController(this.connection);

        this.addContract("Address 1");
        this.addContract("Address 2");

        final IOController ioController = IOControllerFactory.getDefaultIOController();
        this.basePath = ioController.getDefaultDirectoryPath() + File.separator;
    }

    @AfterAll
    public void cleanup() throws IOException {
        this.connection.getService().terminateTest().execute();
        this.connection.logout();
    }

    @Test
    @Order(1)
    public void exportEmptyData() throws IOException, URISyntaxException {
        ReegaExporterFactory.export(ExportFormat.JSON, null, this.basePath + "json0.json");
        this.checkJsonOutput("json0.json");
        this.deleteFile("json0.json");

        ReegaExporterFactory.export(ExportFormat.CSV, null, this.basePath + "csv0.csv");
        this.checkFileContent("csv0.csv");
        this.deleteFile("csv0.csv");
    }

    @Test
    @Order(2)
    public void insertData() throws IOException {
        final var contracts = this.contractController.getUserContracts();
        Assertions.assertEquals(2, contracts.size());
        for (final Contract c : contracts) {
            this.insertData(c.getId());
        }
    }

    @Test
    @Order(3)
    public void exportJSON() throws IOException, URISyntaxException {
        final var data = this.dataController.getMonthlyData(null);

        ReegaExporterFactory.export(ExportFormat.JSON, data, this.basePath + "json1.json");
        this.checkJsonOutput("json1.json");
        this.deleteFile("json1.json");
    }

    @Test
    @Order(4)
    public void exportCSV() throws IOException, URISyntaxException {
        final var data = this.dataController.getMonthlyData(null);

        ReegaExporterFactory.export(ExportFormat.CSV, data, this.basePath + "csv1.csv");
        this.checkFileContent("csv1.csv");
        this.deleteFile("csv1.csv");
    }

    private void checkJsonOutput(final String fileName) throws URISyntaxException, IOException {
        final File file = new File(this.basePath + fileName);
        Assertions.assertTrue(file.exists());
        final String fileContent = new String(Files.readAllBytes(file.toPath()));
        final String testContent = reega.data.utils.FileUtils.getFileFromResourcesAsString("exporter/" + fileName);

        Assertions.assertEquals(JsonParser.parseString(testContent), JsonParser.parseString(fileContent));
    }

    private void checkFileContent(final String fileName) throws IOException, URISyntaxException {
        final File file = new File(this.basePath + fileName);
        Assertions.assertTrue(file.exists());

        final File testFile = reega.data.utils.FileUtils.getFileFromResources("exporter/" + fileName);
        Assertions.assertTrue(FileUtils.contentEquals(testFile, file));
    }

    private void deleteFile(final String fileName) {
        final File file = new File(this.basePath + fileName);
        if (!file.exists() || !file.isFile() || !file.delete()) {
            Assertions.fail("Invalid test file " + file.getAbsolutePath());
        }
    }

    private void insertData(final int contractID) throws IOException {
        final Map<Long, Double> data = new HashMap<>() {
            {
                this.put(DataExportTest.BASE_TIMESTAMP + 1000, 5.5);
                this.put(DataExportTest.BASE_TIMESTAMP + 2000, 6.4);
                this.put(DataExportTest.BASE_TIMESTAMP + 3000, 7.3);
            }
        };

        this.dataController.putUserData(new Data(contractID, DataType.ELECTRICITY, data));
        this.dataController.putUserData(new Data(contractID, DataType.GLASS, data));
        this.dataController.putUserData(new Data(contractID, DataType.WATER, data));
        this.dataController.putUserData(new Data(contractID, DataType.PAPER, data));
        this.dataController.putUserData(new Data(contractID, DataType.MIXED, data));
    }

    private void addContract(final String address) throws IOException {
        final List<ServiceType> services = List.of(ServiceType.ELECTRICITY, ServiceType.GARBAGE);
        final NewContract newContract = new NewContract(address, services, "ABC123",
                new Date(DataExportTest.BASE_TIMESTAMP));
        this.contractController.addContract(newContract);
    }
}
