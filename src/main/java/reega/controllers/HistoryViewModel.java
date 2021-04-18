package reega.controllers;

import reega.data.models.Contract;
import reega.data.models.MonthlyReport;
import reega.viewutils.Controller;

import java.util.List;

public interface HistoryViewModel extends Controller {

    void setContracts(List<Contract> contracts);

    List<Contract> getContracts();

    List<MonthlyReport> getValues();
}
