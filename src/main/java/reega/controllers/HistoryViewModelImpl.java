package reega.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import reega.data.ContractController;
import reega.data.models.Contract;
import reega.data.models.MonthlyReport;
import reega.logging.ExceptionHandler;
import reega.viewutils.AbstractViewModel;

public class HistoryViewModelImpl extends AbstractViewModel implements HistoryViewModel {
    private final ContractController contractController;
    private final ExceptionHandler exceptionHandler;
    private List<Contract> contracts;

    @Inject
    public HistoryViewModelImpl(final ContractController contractController, final ExceptionHandler exceptionHandler) {
        this.contractController = contractController;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContracts(final List<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contract> getContracts() {
        return List.copyOf(this.contracts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MonthlyReport> getValues() {
        try {
            return this.contractController
                    .getBillsForContracts(this.contracts.stream().map(Contract::getId).collect(Collectors.toList()));
        } catch (final IOException e) {
            this.exceptionHandler.handleException(e, "error while getting history");
            return new ArrayList<>();
        }
    }
}
