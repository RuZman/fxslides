package de.ruzman.hui;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.ruzman.hui.device.DataProvider;
import de.ruzman.hui.event.SkeletonEvent;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Skeleton;
import de.ruzman.hui.skeleton.World;

public class SkeletonRegistry {
	private List<Object> skeletonListeners = new CopyOnWriteArrayList<>();
	private List<DataProvider> dataProviders = new CopyOnWriteArrayList<>();
	private List<GestureProvider> gestureProviders = new CopyOnWriteArrayList<>();
	private World lastWorld = new World();

	public SkeletonRegistry() {
	}

	public void addListener(Object skeletonListener) {
		skeletonListeners.add(skeletonListener);
	}

	public void removeListener(Object listener) {
		skeletonListeners.remove(listener);
	}

	public void addGestureProvider(GestureProvider gestureProvider) {
		gestureProviders.add(gestureProvider);
	}

	public void removeGestureProvider(GestureProvider gestureProvider) {
		gestureProviders.remove(gestureProvider);
	}
	
	public void addDataProvider(DataProvider dataProvider) {
		dataProviders.add(dataProvider);
	}
	
	public void update() {
		World newWorld = new World();
		
		for(DataProvider dataProvider: dataProviders) {			
			dataProvider.addSkeleton(newWorld, lastWorld);
			dataProvider.addHands(newWorld, lastWorld);
			dataProvider.addFingers(newWorld, lastWorld);
		}
		
		lastWorld.addMissingSkeletonParts(newWorld);
		newWorld.create();
		
		for(Skeleton skeleton: newWorld.getSkeletons()) {
			List<String> gestures = new ArrayList<>();
			for(GestureProvider gestureProvider: gestureProviders) {
				gestures.addAll(gestureProvider.provideGesture(skeleton));
			}
			
			SkeletonEvent event = new SkeletonEvent(skeleton, gestures);
			skeletonListeners.stream()
				.forEach(listener -> {
					List<Method> methods = getMethodsAnnotatedWith(listener, OnUpdate.class);
					methods.forEach(e -> {
						try {
							e.invoke(listener, event);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
					});
				});
		}
		lastWorld = newWorld;
	}
	
	public static List<Method> getMethodsAnnotatedWith(final Object obj, final Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    		final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(obj.getClass().getMethods()));
		        for (final Method method : allMethods) {
		            if (method.isAnnotationPresent(annotation)) {
		                methods.add(method);
		            }
		        }
	    return methods;
	}
}
