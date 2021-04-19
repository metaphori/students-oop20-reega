package reega.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reega.data.ContractController;
import reega.data.models.Contract;
import reega.data.models.MonthlyReport;
import reega.viewutils.AbstractViewModel;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryViewModelImpl extends AbstractViewModel implements HistoryViewModel {
    private static final Logger log = LoggerFactory.getLogger(HistoryViewModelImpl.class);
    private final ContractController contractController;
    private List<Contract> contracts;

    @Inject
    public HistoryViewModelImpl(ContractController contractController) {
        this.contractController = contractController;
    }

    @Override
    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    @Override
    public List<Contract> getContracts() {
        return List.copyOf(this.contracts);
    }

    @Override
    public List<MonthlyReport> getValues() {
        try {
            return contractController.getBillsForContracts(
                    this.contracts.stream().map(Contract::getId).collect(Collectors.toList())
            );
        } catch (IOException e) {
            log.error("error while getting history:", e);
            return new ArrayList<>();
        }
    }
}
