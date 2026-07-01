package net.kenji.epic_colonies.gameasset.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

import java.util.Map;

public class CitizenArmature extends HumanoidArmature {
    public final Joint EyeR;
    public final Joint EyeL;
    public final Joint BrowR;
    public final Joint BrowL;
    public final Joint Mouth;
    public final Joint MouthSmile;


    public CitizenArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap) {
        super(name, jointNumber, rootJoint, jointMap);
        this.EyeR = getOrLogException(jointMap, "Eye_R");
        this.EyeL = getOrLogException(jointMap, "Eye_L");
        this.BrowR = getOrLogException(jointMap, "Brow_R");
        this.BrowL = getOrLogException(jointMap, "Brow_L");
        this.Mouth = getOrLogException(jointMap, "Mouth");
        this.MouthSmile = getOrLogException(jointMap, "Mouth_Smile");


    }
}
