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
		return Optional.ofNullable(skeletonParts.get(part.getIdentificator()));
	}
	
	public Optional<SkeletonBuilder> containsSkeletonPart(SkeletonPartBuilder<?, ?> part) {
		return Optional.ofNullable(skeletonParts.get(part.getIdentificator()));
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
				newSkeletonBuilder = Optional.of(new SkeletonBuilder().hasLeft(true));
				newWorld.skeletonBuilders.add(newSkeletonBuilder.get());
			}
			
			for(Hand hand: skeleton.getHands()) {
				
				if(!newWorld.getHandBuilder(hand.getId()).isPresent() && !hand.hasLeft()) {
					newSkeletonBuilder.get().getHandBuilders().add(new HandBuilder(hand.getId()).hasLeft(true));
				}
			}
		}
	}
	
	public Optional<HandBuilder> getHandBuilder(int id) {
		return Optional.ofNullable((HandBuilder) ids.get("" + Type.HAND.name() + id));
	}
}
