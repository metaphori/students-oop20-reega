package reega.controllers;

import java.util.List;

import reega.data.models.Contract;
import reega.data.models.MonthlyReport;
import reega.viewutils.ViewModel;

public interface HistoryViewModel extends ViewModel {

    void setContracts(List<Contract> contracts);

    List<Contract> getContracts();

    List<MonthlyReport> getValues();
}
