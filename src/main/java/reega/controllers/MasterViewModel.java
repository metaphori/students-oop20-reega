package reega.controllers;

import javax.inject.Inject;

import reega.viewutils.Navigator;
import reega.viewutils.ViewModel;
import reega.viewutils.ViewModelChangedEventHandler.ViewModelChangedEventType;

public class MasterViewModel {
    private final Navigator navigator;

    @Inject
    public MasterViewModel(final Navigator navigator) {
        this.navigator = navigator;
        this.navigator.selectedViewModelProperty().addListener((observable, oldValue, newValue) -> {
            newValue.setViewModelChangedEventHandler(evtArgs -> {
                if (evtArgs.getEventType() == ViewModelChangedEventType.POP) {
                    this.navigator.popController();
                    return;
                }
                final ViewModel viewModel = this.navigator.buildViewModel(evtArgs.getEventItem());
                evtArgs.executeAction(viewModel);
                this.navigator.pushViewModelToStack(viewModel, evtArgs.isClearNavigationStack());
            });
        });
    }

    /**
     * Start the app with the default controller.
     */
    public void initializeApp() {
        this.navigator.pushViewModel(LoginViewModel.class, false);
    }

    /**
     * Get the main navigator of the app.
     *
     * @return the main navigator of the app
     */
    public Navigator getNavigator() {
        return this.navigator;
    }
}
