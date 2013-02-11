package com.geargames.regolith.commands;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.BaseConfigurationCommand;
import com.geargames.regolith.units.dictionaries.WeaponCategoryCollection;
import com.geargames.regolith.units.dictionaries.WeaponTypeCollection;
import com.geargames.regolith.units.tackle.WeaponCategory;
import com.geargames.regolith.units.tackle.WeaponType;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class WeaponTypeUpd extends BaseConfigurationCommand {
    private WeaponType weaponType;

    public WeaponTypeUpd(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public void command(BaseConfiguration configuration) {
        WeaponCategoryCollection categories = configuration.getWeaponCategories();
        for (int i = 0; i < categories.size(); i++) {
            WeaponTypeCollection types = categories.get(i).getWeaponTypes();
            for (int j = 0; j < types.size(); j++) {
                if (types.get(j).getId() == weaponType.getId()) {
                    WeaponType local = types.get(j);
                    local.setAccuracy(weaponType.getAccuracy());
                    local.setAccurateAction(weaponType.getAccurateAction());
                    local.setAmmunitionPerShoot(weaponType.getAmmunitionPerShoot());
                    local.setBaseFirmness(weaponType.getBaseFirmness());
                    local.setCapacity(weaponType.getCapacity());
                    local.setCriticalDamage(weaponType.getCriticalDamage());
                    local.setDistance(weaponType.getDistance());
                    local.setMaxDamage(weaponType.getMaxDamage());
                    local.setMinDamage(weaponType.getMinDamage());
                    local.setMinSkill(weaponType.getMinSkill());
                    local.setProjectiles(weaponType.getProjectiles());
                    local.setQuickAction(weaponType.getQuickAction());
                    local.setFrameId(weaponType.getFrameId());
                    local.setName(weaponType.getName());
                    local.setWeight(weaponType.getWeight());
                    WeaponCategory category = types.get(j).getCategory();
                    if (category != weaponType.getCategory()) {
                        types.remove(j);
                        weaponType.getCategory().getWeaponTypes().add(weaponType);
                    }
                    break;
                }
            }
        }
    }
}
