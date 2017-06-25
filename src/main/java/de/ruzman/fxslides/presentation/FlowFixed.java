package de.ruzman.fxslides.presentation;

import io.datafx.controller.ViewConfiguration;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.ViewFlowContext;

public class FlowFixed extends Flow {
	private FlowHandler flowHandler;
	
	public FlowFixed(Class<?> startViewControllerClass) {
		this(startViewControllerClass, new ViewConfiguration());
	}
	
	public FlowFixed(Class<?> startViewControllerClass, ViewConfiguration viewConfiguration) {
		super(startViewControllerClass, viewConfiguration);
	}
	
    /**
     * FIXED: Use FlowHandler to trigger actions on the current Flow.
     * FIXED: Pass the globalViewConfiguration.
     * @see Flow#createHandler(ViewFlowContext)
     */
	@Override
    public FlowHandler createHandler(ViewFlowContext flowContext) {
		this.flowHandler = new FlowHandler(this, flowContext, getViewConfiguration());
        return flowHandler;
    }
	
	/**
	 * FIXED: Use FlowHandler to trigger actions on the current Flow.
	 */
	public FlowHandler getFlowHandler() {
		return flowHandler;
	}
}
