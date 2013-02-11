package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Skill;

import java.util.List;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class ServerSkillCollection extends SkillCollection {
    private List<Skill> skills;

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Skill get(int index) {
        return skills.get(index);
    }

    public void add(Skill skill) {
        skills.add(skill);
    }

    public void insert(Skill skill, int index) {
        skills.add(index, skill);
    }

    public void remove(int index) {
        skills.remove(index);
    }

    public int size() {
        return skills.size();
    }

    public void set(Skill skill, int index) {
        skills.set(index, skill);
    }
}
