package reega.viewutils;

public interface ViewModel {
    /**
     * Set an {@link EventHandler} that is called whenever a new controller needs to be pushed from this controller.
     *
     * @param viewModelChangedEventHandler {@link ViewModelChangedEventHandler}
     */
    void setViewModelChangedEventHandler(ViewModelChangedEventHandler<ViewModel> viewModelChangedEventHandler);
}
