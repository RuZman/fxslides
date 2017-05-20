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

public class World {
	Map<String, SkeletonBuilder> skeletonParts = new HashMap<>();
	Map<String, Object> ids = new HashMap<>();
	List<SkeletonBuilder> skeletonBuilders = new ArrayList<>();
	List<Skeleton> skeletons = Collections.emptyList();

	Set<Integer> handIds = new HashSet<>();

	public void addSkeletonPart(SkeletonBuilder skeletonBuilder, Object part, Type type, Integer id) {
		ids.put(type.name() + id.toString(), part);
		skeletonParts.put(type.name() + id.toString(), skeletonBuilder);

		if (part instanceof HandBuilder) {
			handIds.add(id);
		} else if (part instanceof SkeletonBuilder) {
			skeletonBuilders.add(skeletonBuilder);
		}
	}

	public Optional<SkeletonBuilder> containsSkeletonPart(Type type, Object id) {
		return Optional.ofNullable(skeletonParts.get(type.name() + id.toString()));
	}

	public List<Skeleton> getSkeletons() {
		return skeletons;
	}

	public void create() {
		skeletons = skeletonBuilders.stream().map(skeletonBuilder -> skeletonBuilder.createSkeleton())
				.collect(Collectors.toList());
	}

	public List<SkeletonBuilder> getSkeletonBuilders() {
		return skeletonBuilders;
	}

	public void addMissingSkeletonParts(World newWorld) {
		for (Skeleton skeleton : skeletons) {
			Optional<SkeletonBuilder> newSkeletonBuilder = newWorld.containsSkeletonPart(Type.SKELETON, skeleton.getId());

			if (!newSkeletonBuilder.isPresent() && !skeleton.hasLeft()) {
				newSkeletonBuilder = Optional.of(new SkeletonBuilder().hasLeft(true));
				newWorld.skeletonBuilders.add(newSkeletonBuilder.get());
			}
			
			for(Hand hand: skeleton.getHands()) {
				Optional<HandBuilder> handBuilder = Optional.ofNullable((HandBuilder) newWorld.ids.get("" + Type.HAND.name() + hand.getId()));

				
				if(!handBuilder.isPresent() && !hand.hasLeft()) {
					newSkeletonBuilder.get().handBuilders.add(new HandBuilder(hand.getId()).hasLeft(true));
				}
			}
		}
	}
}
