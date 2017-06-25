package de.ruzman.fxslides.presentation;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.common.Point;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.hui.skeleton.Skeleton;

public class SimplePalmDragAndDropGestureProvider implements GestureProvider {
	private Integer x = null;
	
	@Override
	public List<String> provideGesture(Skeleton skeleton) {
		List<String> list = new ArrayList<>();
		
		if(drag(skeleton)) {
			list.add("drag");
		}
		if(dragStart(skeleton)) {
			list.add("dragStart");
		}
		if(drop(skeleton)) {
			list.add("drop");
		}
		
		return list;
	}
	
	private boolean dragStart(Skeleton skeleton) {
		Integer oldx = x;
		x = 0;
		return drag(skeleton) && oldx == null;
	}
	
	private boolean drag(Skeleton skeleton) {
		boolean shouldDrag = false;
		
		if (skeleton.getHands().size() == 1) {
			Hand hand = skeleton.getHands().get(0);

			if (hand.getPalmPosition().isPresent()) {
				Point palmPosition = hand.getPalmPosition().get();

				if (palmPosition.getAbsolutePosition().getZ() < 0) {
					shouldDrag = true;
				}
			}
		}
		
		return shouldDrag;
	}
	
	public boolean drop(Skeleton skeleton) {
		boolean shouldDrop = false;
		
		if (skeleton.getHands().size() == 1) {
			Hand hand = skeleton.getHands().get(0);

			if (hand.getPalmPosition().isPresent()) {
				Point palmPosition = hand.getPalmPosition().get();

				if (palmPosition.getAbsolutePosition().getZ() >= 0 && x != null) {
					shouldDrop = true;
				}
			} else {
				shouldDrop = true;
			}
		} else {
			shouldDrop = true;
		}
		
		if(shouldDrop) {
			x = null;
		}
		
		return shouldDrop;
	}

}
