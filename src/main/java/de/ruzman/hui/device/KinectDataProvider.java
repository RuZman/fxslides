package de.ruzman.hui.device;

import java.util.Optional;

import de.ruzman.hui.skeleton.Skeleton.SkeletonBuilder;
import de.ruzman.hui.skeleton.Skeleton.Type;
import de.ruzman.hui.skeleton.World;
import edu.ufl.digitalworlds.j4k.J4KSDK;

public class KinectDataProvider extends J4KSDK implements DataProvider {
	
	public KinectDataProvider() {
		start(SKELETON);

	}
	
	@Override
	public void addSkeleton(World newWorld, World lastWorld) {
		
		for(edu.ufl.digitalworlds.j4k.Skeleton skeleton: getSkeletons()) {
			if(skeleton == null || skeleton.getBodyOrientation() > -0.0001 || skeleton.getBodyOrientation() < -359.9999) {
				continue;
			}
			
			Optional<SkeletonBuilder> lastSkeletonBuilder = lastWorld.containsSkeletonPart(Type.SKELETON.name() + skeleton.getPlayerID());
			SkeletonBuilder newSkeletonBuilder = newWorld.containsSkeletonPart(Type.SKELETON.name() + skeleton.getPlayerID()).orElse(null);

			if (!lastSkeletonBuilder.isPresent()) {
				newSkeletonBuilder = new SkeletonBuilder(Optional.of(skeleton.getPlayerID()), null);
			} else {
				newSkeletonBuilder = new SkeletonBuilder(Optional.of(lastSkeletonBuilder.get().getId()),
						lastSkeletonBuilder.get().getInitializedObject());
			}
			
			newWorld.addSkeletonPart(newSkeletonBuilder, newSkeletonBuilder);
		}
	}

	@Override
	public void addHands(World newWorld, World lastWorld) {
		for(edu.ufl.digitalworlds.j4k.Skeleton skeleton: getSkeletons()) {
			if(skeleton == null || skeleton.getBodyOrientation() > -0.0001 || skeleton.getBodyOrientation() < -359.9999) {
				continue;
			}
			
			float test = (float) skeleton.get3DJointX(edu.ufl.digitalworlds.j4k.Skeleton.WRIST_RIGHT);
			float test1 = (float)skeleton.get3DJointY(edu.ufl.digitalworlds.j4k.Skeleton.WRIST_RIGHT);
			float test2 = (float) skeleton.get3DJointZ(edu.ufl.digitalworlds.j4k.Skeleton.WRIST_RIGHT);
			
			
			
			System.out.println(test + " - " + test1 + " - " + test2);
			//System.out.println(getXYZ() != null ? getXYZ()[0] : "");
		}
	}

	@Override
	public void addFingers(World newWorld, World lastWorld) {}

	@Override
	public void onColorFrameEvent(byte[] arg0) {
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_position, float[] joint_orientation,
			byte[] joint_status) {}
}
