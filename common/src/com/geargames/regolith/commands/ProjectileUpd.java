package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.ProjectileCollection;
import com.geargames.regolith.units.tackle.Projectile;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class ProjectileUpd extends BaseConfigurationCommand {
    private Projectile projectile;

    public ProjectileUpd(Projectile projectile) {
        this.projectile = projectile;
    }

    public void command(BaseConfiguration configuration) {
        ProjectileCollection dictionary = configuration.getProjectiles();
        int length = dictionary.size();
        int id = projectile.getId();
        for (int i = 0; i < length; i++) {
            if (dictionary.get(i).getId() == id) {
                Projectile local = dictionary.get(i);
                local.setWeaponTypes(projectile.getWeaponTypes());
                local.setCategory(projectile.getCategory());
                local.setFrameId(projectile.getFrameId());
                local.setName(projectile.getName());
                local.setWeight(projectile.getWeight());
                break;
            }
        }
    }
}
