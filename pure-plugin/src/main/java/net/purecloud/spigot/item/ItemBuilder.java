package net.purecloud.spigot.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public final class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(String skullUrl) {
        this.stack = new ItemStack(Material.PLAYER_HEAD);

        var meta = (SkullMeta) stack.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", skullUrl));

        try {
            Method mtd = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            mtd.setAccessible(true);
            mtd.invoke(meta, profile);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            exception.fillInStackTrace();
        }

        this.stack.setItemMeta(meta);
    }

    public ItemBuilder name(String name) {
        var meta = stack.getItemMeta();
        meta.displayName(Component.text(name));
        this.stack.setItemMeta(meta);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        var meta = stack.getItemMeta();
        meta.lore(lore.stream().map(Component::text).toList());
        this.stack.setItemMeta(meta);

        return this;
    }

    public ItemStack toStack() {
        return stack;
    }
}