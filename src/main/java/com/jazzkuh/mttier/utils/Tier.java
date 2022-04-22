package com.jazzkuh.mttier.utils;

// 윓윔윜윚윘윙윐윝윏윕윑윗 TIER 1-12

import lombok.Getter;
import org.bukkit.ChatColor;

public enum Tier {
    BLACK("Black", '윞', 0, null),
    GRAY("Gray", '윓', 1, 9),
    DARK_GRAY("Dark Gray", '윔', 10, 19),
    YELLOW("Yellow", '윜', 20, 29),
    BLUE("Blue", '윚', 30, 39),
    AQUA("Aqua", '윘', 40, 49),
    DARK_AQUA("Dark Aqua", '윙', 50, 59),
    DARK_GREEN("Dark Green", '윐', 60, 69),
    GOLD("Gold", '윝', 70, 79),
    GREEN("Green", '윏', 80, 89),
    PURPLE("Purple", '윑', 90, 99),
    DARK_PURPLE("Dark Purple", '윕', 100, null),
    STAFF("Staff", '윖', null, null),
    PROJECTLEIDER("ProjectLeider", '윗', null, null);

    private final @Getter String tierName;
    private final @Getter char tierIcon;
    private final @Getter Integer levelRequired;
    private final @Getter Integer maxLevel;

    Tier(String tierName, char tierIcon, Integer levelRequired, Integer maxLevel) {
        this.tierName = tierName;
        this.tierIcon = tierIcon;
        this.levelRequired = levelRequired;
        this.maxLevel = maxLevel;
    }

    public String getTierIcon() {
        return ChatColor.WHITE.toString() + tierIcon;
    }
}
