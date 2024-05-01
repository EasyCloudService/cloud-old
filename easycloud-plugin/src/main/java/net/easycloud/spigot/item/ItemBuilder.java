package net.easycloud.spigot.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
public final class ItemBuilder {
    private final ItemStack stack;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.stack = itemStack;
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

    public ItemBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemStack toStack() {
        return stack;
    }
}