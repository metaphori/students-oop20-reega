package reega.views;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import reega.controllers.MainController;
import reega.controllers.UserMainController;
import reega.viewcomponents.Card;

public class UserMainView extends MainView{

    public UserMainView(UserMainController controller) {
        super(controller);
        if (controller.user().isNotNull().get()) {
            controller.getAvailableServiceTypes().forEach(svcType -> {
                Card serviceCard = new Card();
                serviceCard.getChildren().add(new Text(svcType.getName()));
                controller.getPeek(svcType).ifPresent(peek -> {
                    serviceCard.getChildren().add(new Text("Peek date: " + peek.getKey()));
                    serviceCard.getChildren().add(new Text("Peek value: " + peek.getValue()));
                });
                serviceCard.getChildren().add(new Text("Average usage: " + controller.getAverageUsage(svcType)));
                serviceCard.getChildren().add(new Text("Total usage: " + controller.getTotalUsage(svcType)));
                this.getServicesPane().getChildren().add(serviceCard);
            });
        }

        controller.user().addListener((observable,oldValue,newValue) -> {
            this.getServicesPane().getChildren().clear();
            controller.getAvailableServiceTypes().forEach(svcType -> {
                Card serviceCard = new Card();
                serviceCard.getChildren().add(new Text(svcType.getName()));
                controller.getPeek(svcType).ifPresent(peek -> {
                    serviceCard.getChildren().add(new Text("Peek date: " + peek.getKey()));
                    serviceCard.getChildren().add(new Text("Peek value: " + peek.getValue()));
                });
                serviceCard.getChildren().add(new Text("Average usage: " + controller.getAverageUsage(svcType)));
                serviceCard.getChildren().add(new Text("Total usage: " + controller.getTotalUsage(svcType)));
                this.getServicesPane().getChildren().add(serviceCard);
            });
        });
    }
}
