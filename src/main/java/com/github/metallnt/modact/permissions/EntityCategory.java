package com.github.metallnt.modact.permissions;

import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class com.github.metallnt.modact.permissions
 * <p>
 * Date: 27.12.2021 5:41 27 12 2021
 *
 * @author Metall
 */
public enum EntityCategory {
    PLAYER("player", Player.class),
    ITEM("item", Item.class),
    ANIMAL("animal", Animals.class, Squid.class),
    MONSTER("monster", Monster.class, Slime.class, EnderDragon.class, Ghast.class),
    NPC("npc", NPC.class),
    PROJECTILE("projectile", Projectile.class);

    private final String name;
    private final Class<? extends Entity>[] classes;

    private final static Map<Class<? extends Entity>, EntityCategory> map = new HashMap<>();

    static {
        for (EntityCategory cat : EntityCategory.values()) {
            for (Class<? extends Entity> catClass : cat.getClasses()) {
                map.put(catClass, cat);
            }
        }
    }

    @SafeVarargs
    EntityCategory(String name, Class<? extends Entity>... classes) {
        this.name = name;
        this.classes = classes;
    }

    public String getName() {
        return this.name;
    }

    public String getNameDot() {
        return this.getName() + ".";
    }

    public Class<? extends Entity>[] getClasses() {
        return this.classes;
    }

    public static EntityCategory fromEntity(Entity entity) {
        for (Class<? extends Entity> entityClass : map.keySet()) {
            if (entityClass.isAssignableFrom(entity.getClass())) {
                return map.get(entityClass);
            }
        }
        return null;
    }
}
