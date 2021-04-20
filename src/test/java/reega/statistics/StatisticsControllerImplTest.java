package reega.statistics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.ServiceType;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class StatisticsControllerImplTest {
    private static final double COMMON_AVERAGE = 26.0d;
    private static final double COMMON_TOTAL = 52.0;
    private final List<Data> data = new ArrayList<>();
    private final StatisticsController statisticsController = new StatisticsControllerImpl();
    private final Date peekDate = new GregorianCalendar(2021, Calendar.APRIL, 3).getTime();
    private final Pair<Date, Double> commonPeek = Pair.of(this.peekDate, 27.0);

    @BeforeAll
    public void setupBeforeAll() {
        final Data gasFirstContract = new Data(1, DataType.GAS);
        this.addRecordToData(gasFirstContract);
        final Data electricityFirstContract = new Data(1, DataType.ELECTRICITY);
        this.addRecordToData(electricityFirstContract);
        final Data waterFirstContract = new Data(1, DataType.WATER);
        this.addRecordToData(waterFirstContract);
        // GARBAGE
        final Data plasticFirstContract = new Data(1, DataType.PLASTIC);
        this.addRecordToData(plasticFirstContract);
        final Data glassFirstContract = new Data(1, DataType.GLASS);
        this.addRecordToData(glassFirstContract);
        final Data paperFirstContract = new Data(1, DataType.PAPER);
        this.addRecordToData(paperFirstContract);
        final Data mixedFirstContract = new Data(1, DataType.MIXED);
        this.addRecordToData(mixedFirstContract);
        this.data.addAll(List.of(gasFirstContract, electricityFirstContract, waterFirstContract, plasticFirstContract,
                glassFirstContract, paperFirstContract, mixedFirstContract));
        this.statisticsController.setData(this.data);
    }

    private void addRecordToData(final Data data) {
        data.addRecord(new GregorianCalendar(2021, Calendar.APRIL, 2).getTimeInMillis(), 25.0d);
        data.addRecord(this.peekDate.getTime(), 27.0d);
    }

    private Pair<Date, Double> peekTest(final ServiceType svcType) {
        return this.statisticsController.getPeek(svcType).get();
    }

    private double averageTest(final ServiceType svcType) {
        return this.statisticsController.getAverageUsage(svcType);
    }

    private double totalTest(final ServiceType svcType) {
        return this.statisticsController.getTotalUsage(svcType);
    }

    @Test
    public void gasPeekTest() {
        Assertions.assertEquals(this.peekTest(ServiceType.GAS), this.commonPeek);
    }

    @Test
    public void electricityPeekTest() {
        Assertions.assertEquals(this.peekTest(ServiceType.ELECTRICITY), this.commonPeek);
    }

    @Test
    public void garbagePeekTest() {
        // Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(this.peekTest(ServiceType.GARBAGE),
                Pair.of(this.commonPeek.getLeft(), this.commonPeek.getRight() * 4));
    }

    @Test
    public void waterPeekTest() {
        Assertions.assertEquals(this.peekTest(ServiceType.WATER), this.commonPeek);
    }

    @Test
    public void gasAverageTest() {
        Assertions.assertEquals(this.averageTest(ServiceType.GAS), StatisticsControllerImplTest.COMMON_AVERAGE);
    }

    @Test
    public void electricityAverageTest() {
        Assertions.assertEquals(this.averageTest(ServiceType.ELECTRICITY), StatisticsControllerImplTest.COMMON_AVERAGE);
    }

    @Test
    public void garbageAverageTest() {
        // Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(this.averageTest(ServiceType.GARBAGE), StatisticsControllerImplTest.COMMON_AVERAGE * 4);
    }

    @Test
    public void waterAverageTest() {
        Assertions.assertEquals(this.averageTest(ServiceType.WATER), StatisticsControllerImplTest.COMMON_AVERAGE);
    }

    @Test
    public void gasTotalTest() {
        Assertions.assertEquals(this.totalTest(ServiceType.GAS), StatisticsControllerImplTest.COMMON_TOTAL);
    }

    @Test
    public void electricityTotalTest() {
        Assertions.assertEquals(this.totalTest(ServiceType.ELECTRICITY), StatisticsControllerImplTest.COMMON_TOTAL);
    }

    @Test
    public void garbageTotalTest() {
        // Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(this.totalTest(ServiceType.GARBAGE), StatisticsControllerImplTest.COMMON_TOTAL * 4);
    }

    @Test
    public void waterTotalTest() {
        Assertions.assertEquals(this.totalTest(ServiceType.WATER), StatisticsControllerImplTest.COMMON_TOTAL);
    }
}
