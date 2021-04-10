package reega.statistics;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reega.data.models.Data;
import reega.data.models.DataType;
import reega.data.models.ServiceType;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatisticsControllerImplTest {
    private final List<Data> data = new ArrayList<>();
    private final StatisticsController statisticsController = new StatisticsControllerImpl();
    private final Date peekDate = new GregorianCalendar(2021,Calendar.APRIL,3).getTime();
    private final Pair<Date,Double> commonPeek = Pair.of(peekDate, 27.0);
    private final double commonAverage = 26.0d;
    private final double commonTotal = 52.0;

    @BeforeAll
    public void setupBeforeAll() {
        Data gasFirstContract = new Data(1, DataType.GAS);
        addRecordToData(gasFirstContract);
        Data electricityFirstContract = new Data(1, DataType.ELECTRICITY);
        addRecordToData(electricityFirstContract);
        Data waterFirstContract = new Data(1, DataType.WATER);
        addRecordToData(waterFirstContract);
        //GARBAGE
        Data plasticFirstContract = new Data(1, DataType.PLASTIC);
        addRecordToData(plasticFirstContract);
        Data glassFirstContract = new Data(1, DataType.GLASS);
        addRecordToData(glassFirstContract);
        Data paperFirstContract = new Data(1, DataType.PAPER);
        addRecordToData(paperFirstContract);
        Data mixedFirstContract = new Data(1, DataType.MIXED);
        addRecordToData(mixedFirstContract);
        data.addAll(List.of(gasFirstContract, electricityFirstContract, waterFirstContract,
                plasticFirstContract, glassFirstContract, paperFirstContract, mixedFirstContract));
        this.statisticsController.setData(data);
    }

    private void addRecordToData(Data data) {
        data.addRecord(new GregorianCalendar(2021,Calendar.APRIL,2).getTimeInMillis(), 25.0d);
        data.addRecord(peekDate.getTime(), 27.0d);
    }

    private Pair<Date,Double> peekTest(ServiceType svcType) {
        return this.statisticsController.getPeek(svcType).get();
    }

    private double averageTest(ServiceType svcType) {
        return this.statisticsController.getAverageUsage(svcType);
    }

    private double totalTest(ServiceType svcType) {
        return this.statisticsController.getTotalUsage(svcType);
    }

    @Test
    public void gasPeekTest() {
        Assertions.assertEquals(peekTest(ServiceType.GAS), commonPeek);
    }

    @Test
    public void electricityPeekTest() {
        Assertions.assertEquals(peekTest(ServiceType.ELECTRICITY), commonPeek);
    }

    @Test
    public void garbagePeekTest() {
        //Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(peekTest(ServiceType.GARBAGE), Pair.of(commonPeek.getLeft(), commonPeek.getRight() * 4));
    }

    @Test
    public void waterPeekTest() {
        Assertions.assertEquals(peekTest(ServiceType.WATER), commonPeek);
    }

    @Test
    public void gasAverageTest() {
        Assertions.assertEquals(averageTest(ServiceType.GAS), commonAverage);
    }

    @Test
    public void electricityAverageTest() {
        Assertions.assertEquals(averageTest(ServiceType.ELECTRICITY), commonAverage);
    }

    @Test
    public void garbageAverageTest() {
        //Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(averageTest(ServiceType.GARBAGE), commonAverage * 4);
    }

    @Test
    public void waterAverageTest() {
        Assertions.assertEquals(averageTest(ServiceType.WATER), commonAverage);
    }

    @Test
    public void gasTotalTest() {
        Assertions.assertEquals(totalTest(ServiceType.GAS), commonTotal);
    }

    @Test
    public void electricityTotalTest() {
        Assertions.assertEquals(totalTest(ServiceType.ELECTRICITY), commonTotal);
    }

    @Test
    public void garbageTotalTest() {
        //Multiply it by 4 because the 4 types of garbage are summed
        Assertions.assertEquals(totalTest(ServiceType.GARBAGE), commonTotal * 4);
    }

    @Test
    public void waterTotalTest() {
        Assertions.assertEquals(totalTest(ServiceType.WATER), commonTotal);
    }
}
