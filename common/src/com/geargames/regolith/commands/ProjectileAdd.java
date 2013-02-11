package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.tackle.Projectile;

/**
 * User: mkutuzov
 * Date: 28.04.12
 */
public class ProjectileAdd extends BaseConfigurationCommand {
    private Projectile projectile;

    public ProjectileAdd(Projectile projectile) {
        this.projectile = projectile;
    }

    public void command(BaseConfiguration configuration) {
        configuration.getProjectiles().add(projectile);
    }
}
