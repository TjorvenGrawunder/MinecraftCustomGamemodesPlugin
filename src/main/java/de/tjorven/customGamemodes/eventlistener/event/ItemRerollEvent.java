package de.tjorven.customGamemodes.eventlistener.event;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class ItemRerollEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Material item;

    public ItemRerollEvent(@NotNull Entity entity, Material newItem) {
        super(entity);
        this.item = newItem;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @NotNull
    public Material getItem() {
        return this.item;
    }

}
