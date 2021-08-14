package me.luucka.duels.utils;

import me.luucka.duels.DuelsPlugin;
import me.luucka.lcore.item.ItemBuilder;
import me.luucka.lcore.utils.ColorTranslate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ConfigurationUtility {

    public static void saveLocation(Location location, ConfigurationSection section) {
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    public static Location readLocation(ConfigurationSection section) {
        return new Location(
                Bukkit.getWorld(section.getString("world")),
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }

    public static ItemStack createSetupItem(DuelsPlugin plugin, ConfigurationSection section) {
        return new ItemBuilder(
                Material.valueOf(section.getString("type")))
                .setDisplayName(ColorTranslate.translate(section.getString("name")))
                .setLore(ColorTranslate.translate(section.getStringList("lore")))
                .setPersistentDataContainer(plugin, "setup-value", section.getName())
                .toItemStack();
    }

}
