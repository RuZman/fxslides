package de.ruzman.fxslides.presentation.controller.slides;

import static java.util.Locale.GERMAN;

import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import de.ruzman.fxslides.presentation.FlowFixed;
import de.ruzman.fxslides.presentation.Presentation;
import de.ruzman.fxslides.presentation.SimplePalmDragAndDropGestureProvider;
import de.ruzman.hui.OnUpdate;
import de.ruzman.hui.SkeletonApp;
import de.ruzman.hui.event.SkeletonEvent;
import io.datafx.controller.FXMLController;
import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.util.VetoException;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

@FXMLController("../../fxml/slides/slide0toSlide1.fxml")
public class Slide0toSlide1 {
	@FXML
	private StackPane presentation;

	private FlowFixed flowSlide1;
	
	private StackPane slide0;
	private StackPane slide1;

	private int startValue = 0; // Temp Drag&Drop value von Slide0 zu Slide1

	private SimplePalmDragAndDropGestureProvider dragAndDropProvider = new SimplePalmDragAndDropGestureProvider("PresentationController");

	@PostConstruct
	public void init() {
		SkeletonApp.addListener(this);
		SkeletonApp.addGestureProvider(dragAndDropProvider);

		String baseName = Presentation.class.getPackage().getName() + ".labels.labels";

		ViewConfiguration viewConfiguration = new ViewConfiguration();
		viewConfiguration.setResources(ResourceBundle.getBundle(baseName, GERMAN));

		FlowFixed flowSlide0 = new FlowFixed(Slide0.class, viewConfiguration);
		flowSlide1 = (FlowFixed) new FlowFixed(Slide1.class, viewConfiguration)
				.withLink(Slide1.class, "test", Slide2.class);
		flowSlide1.withCallMethodAction(Slide1.class, "activateSlide1", "activate");

		
		try {
			slide0 = flowSlide0.start();
			slide1 = flowSlide1.start();
			presentation.getChildren().add(slide0);
			presentation.getChildren().add(slide1);

			slide1.translateXProperty().bind(slide0.widthProperty().add(slide0.translateXProperty()));
		} catch (FlowException e) {
			e.printStackTrace();
		}
	}

	@OnUpdate
	public void onUpdate(SkeletonEvent event) {
		if (event.getGestures().contains("PresentationControllerdragStart")) {
			onDragStart((int) event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition().getX());
		}

		if (event.getGestures().contains("PresentationControllerdrag")) {
			onDragUpdate(
					(int) event.getSkeleton().getHands().get(0).getPalmPosition().get().getScreenPosition().getX());
		}
	}

	private void onDragStart(int value) {
		startValue = value - (int) slide0.getTranslateX();
	}

	private void onDragUpdate(int value) {
		if ((startValue - value) * 1.3 >= slide0.getWidth()) {
			SkeletonApp.removeGestureProvider(dragAndDropProvider);
			presentation.getChildren().remove(slide0);
			slide1.translateXProperty().unbind();
			slide1.setTranslateX(0);
			try {
				flowSlide1.getFlowHandler().handle("activateSlide1");
			} catch (VetoException | FlowException e) {
				e.printStackTrace();
			}
			return;
		}
		slide0.setTranslateX(-(startValue - value) * 1.3);
	}
}
