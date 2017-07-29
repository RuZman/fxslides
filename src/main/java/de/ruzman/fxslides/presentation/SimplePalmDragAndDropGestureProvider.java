package de.ruzman.fxslides.presentation;

import java.util.ArrayList;
import java.util.List;

import de.ruzman.common.Point;
import de.ruzman.hui.gesture.GestureProvider;
import de.ruzman.hui.skeleton.Hand;
import de.ruzman.hui.skeleton.Skeleton;

public class SimplePalmDragAndDropGestureProvider implements GestureProvider {
	private Integer x = null;
	private String id;
	
	public SimplePalmDragAndDropGestureProvider(String id) {
		this.id = id;
	}
	
	@Override
	public List<String> provideGesture(Skeleton skeleton) {
		List<String> list = new ArrayList<>();
		
		if(drag(skeleton)) {
			list.add(id + "drag");
		}
		if(dragStart(skeleton)) {
			list.add(id + "dragStart");
		}
		if(drop(skeleton)) {
			list.add(id + "drop");
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePalmDragAndDropGestureProvider other = (SimplePalmDragAndDropGestureProvider) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
