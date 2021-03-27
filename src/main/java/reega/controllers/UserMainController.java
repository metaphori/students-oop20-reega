package reega.controllers;

import javafx.collections.ObservableList;
import reega.data.models.Contract;

public interface UserMainController extends MainController {
    ObservableList<Contract> getSelectedContracts();
}
