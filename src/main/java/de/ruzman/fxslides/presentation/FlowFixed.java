package de.ruzman.fxslides.presentation;

import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;

public class FlowFixed extends Flow {

	public FlowFixed(Class<?> startViewControllerClass) {
		this(startViewControllerClass, new ViewConfiguration());
	}
	
	public FlowFixed(Class<?> startViewControllerClass, ViewConfiguration viewConfiguration) {
		super(startViewControllerClass, viewConfiguration);
	}
	
    /**
     * @see Flow#createHandler(ViewFlowContext)
     */
	@Override
    public FlowHandler createHandler(ViewFlowContext flowContext) {
        return new FlowHandler(this, flowContext, getViewConfiguration());
    }
}
