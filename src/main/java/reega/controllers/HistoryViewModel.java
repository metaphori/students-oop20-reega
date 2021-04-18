package reega.controllers;

import reega.data.models.Contract;
import reega.viewutils.Controller;

import java.util.List;

public interface HistoryViewModel extends Controller {

    void setContracts(List<Contract> contracts);
}
