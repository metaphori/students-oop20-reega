package reega.views;

import reega.controllers.MainViewModel;

public class UserMainView extends MainView {

    public UserMainView(final MainViewModel viewModel) {
        super(viewModel);
        this.populateServicesPane(viewModel);
        this.populateContractsPane(viewModel);
    }
}
