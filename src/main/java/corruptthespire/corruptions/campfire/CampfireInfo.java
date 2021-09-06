package corruptthespire.corruptions.campfire;

import corruptthespire.corruptions.campfire.options.AbstractCorruptedCampfireOption;

import java.util.ArrayList;

public class CampfireInfo {
    public boolean isDone = false;
    public final ArrayList<AbstractCorruptedCampfireOption> options = new ArrayList<>();
}
