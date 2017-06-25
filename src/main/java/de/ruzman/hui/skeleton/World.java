package de.ruzman.hui.skeleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.ruzman.hui.skeleton.Finger.FingerBuilder;
import de.ruzman.hui.skeleton.Hand.HandBuilder;
import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;
import de.ruzman.hui.skeleton.SkeletonPart.SkeletonPartBuilder;

public class World {
	Map<String, SkeletonBuilder> skeletonParts = new HashMap<>();
	Map<String, SkeletonPartBuilder<?, ?>> ids = new HashMap<>();
	List<SkeletonBuilder> skeletonBuilders = new ArrayList<>();
	List<Skeleton> skeletons = Collections.emptyList();

	Set<Integer> handIds = new HashSet<>();

	public void addSkeletonPart(SkeletonBuilder skeletonBuilder, SkeletonPartBuilder<?, ?> part) {
		ids.put(part.getIdentificator(), part);
		skeletonParts.put(part.getIdentificator(), skeletonBuilder);

		if (part instanceof HandBuilder) {
			handIds.add(part.getId());
		} else if (part instanceof SkeletonBuilder) {
			skeletonBuilders.add(skeletonBuilder);
		}
	}

	public Optional<SkeletonBuilder> containsSkeletonPart(SkeletonPart part) {
		return containsSkeletonPart(part.getIdentificator());
	}
	
	public Optional<SkeletonBuilder> containsSkeletonPart(SkeletonPartBuilder<?, ?> part) {
		return containsSkeletonPart(part.getIdentificator());
	}
	
	public Optional<SkeletonBuilder> containsSkeletonPart(String identifier) {
		return Optional.ofNullable(skeletonParts.get(identifier));
	}

	public List<Skeleton> getSkeletons() {
		return skeletons;
	}

	public void create() {
		skeletons = skeletonBuilders.stream().map(skeletonBuilder -> skeletonBuilder.create())
				.collect(Collectors.toList());
	}

	public List<SkeletonBuilder> getSkeletonBuilders() {
		return skeletonBuilders;
	}

	public void addMissingSkeletonParts(World newWorld) {
		for (Skeleton skeleton : skeletons) {
			Optional<SkeletonBuilder> newSkeletonBuilder = newWorld.containsSkeletonPart(skeleton);

			if (!newSkeletonBuilder.isPresent() && !skeleton.hasLeft()) {
				newSkeletonBuilder = Optional.of(new SkeletonBuilder(Optional.of(skeleton.getId()), skeleton).hasLeft(true));
				newWorld.skeletonBuilders.add(newSkeletonBuilder.get());
			}
			
			for(Hand hand: skeleton.getHands()) {
				HandBuilder handBuilder = null;
				if(!newWorld.getHandBuilder(hand.getId()).isPresent() && !hand.hasLeft()) {
					handBuilder = new HandBuilder(hand.getId(), hand).hasLeft(true);
					newSkeletonBuilder.get().getHandBuilders().add(handBuilder);
				}
				
				for(Finger finger: hand.getFingers()) {
					
					if(!newWorld.getFingerBuilder(finger.getId()).isPresent() && !finger.hasLeft()) {
						handBuilder.addFinger(new FingerBuilder(finger.getId(), finger.getFingerType(), finger).hasLeft(true));
					}
					
					
				}
			}
		}
	}
	
	public Optional<HandBuilder> getHandBuilder(int id) {
		return Optional.ofNullable((HandBuilder) ids.get("" + Type.HAND.name() + id));
	}
	
	public Optional<FingerBuilder> getFingerBuilder(int id) {
		return Optional.ofNullable((FingerBuilder) ids.get("" + Type.FINGER.name() + id));
	}
}
