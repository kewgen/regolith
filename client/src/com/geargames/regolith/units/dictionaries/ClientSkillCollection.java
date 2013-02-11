package com.geargames.regolith.units.dictionaries;

import com.geargames.regolith.units.Skill;

import java.util.Vector;

/**
 * User: mkutuzov
 * Date: 25.04.12
 */
public class ClientSkillCollection extends SkillCollection {
    private Vector skills;

    public Vector getSkills() {
        return skills;
    }

    public void setSkills(Vector skills) {
        this.skills = skills;
    }

    public Skill get(int index) {
        return (Skill)skills.elementAt(index);
    }

    public void add(Skill skill) {
        skills.addElement(skill);
    }

    public void insert(Skill skill, int index) {
        skills.insertElementAt(skill, index);
    }

    public void remove(int index) {
        skills.removeElementAt(index);
    }

    public int size() {
        return skills.size();
    }

    public void set(Skill skill, int index) {
        skills.set(index, skill);
    }
}
