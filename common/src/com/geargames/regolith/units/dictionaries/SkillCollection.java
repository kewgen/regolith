package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Skill;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public abstract class SkillCollection extends EntityCollection {
    public abstract Skill get(int index);
    public abstract void add(Skill skill);
    public abstract void insert(Skill skill, int index);
    public abstract void set(Skill skill, int index);
}
