package net.kenji.epic_colonies.gameasset;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum EpicColoniesWeaponCategory implements WeaponCategory {
    DUAL_SWORDS,
    DUAL_DAGGER;

    int id = 0;
    EpicColoniesWeaponCategory(){
        id = ENUM_MANAGER.assign(this);
    };
    @Override
    public int universalOrdinal() {
        return id;
    }
}
