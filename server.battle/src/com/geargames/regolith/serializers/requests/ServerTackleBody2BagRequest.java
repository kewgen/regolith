package com.geargames.regolith.serializers.requests;

import com.geargames.regolith.BaseConfiguration;
import com.geargames.regolith.Packets;
import com.geargames.regolith.RegolithException;
import com.geargames.regolith.serializers.*;
import com.geargames.regolith.serializers.answer.ServerConfirmationAnswer;
import com.geargames.regolith.service.Client;
import com.geargames.regolith.units.BodyParticles;
import com.geargames.regolith.units.battle.BattleGroup;
import com.geargames.regolith.units.battle.ServerBattle;
import com.geargames.regolith.units.battle.Warrior;
import com.geargames.regolith.units.battle.WarriorHelper;
import com.geargames.regolith.units.tackle.StateTackle;

/**
 * User: mikhail v. kutuzov
 * Date: 18.08.12
 * Time: 17:25
 */
public class ServerTackleBody2BagRequest extends BattleServiceOneToClientRequest {
    private ServerBattle serverBattle;
    private BaseConfiguration baseConfiguration;

    public ServerTackleBody2BagRequest(ServerBattle serverBattle, BaseConfiguration baseConfiguration) {
        this.serverBattle = serverBattle;
        this.baseConfiguration = baseConfiguration;
    }

    @Override
    protected SerializedMessage clientRequest(MicroByteBuffer from, MicroByteBuffer writeBuffer, Client client) throws RegolithException {
        byte allianceNumber = from.get();
        int groupId = SimpleDeserializer.deserializeInt(from);
        int warriorId = SimpleDeserializer.deserializeInt(from);
        byte bodyParticle = from.get();

        BattleGroup group = BattleServiceRequestUtils.getBattleGroupFromServerBattle(serverBattle, allianceNumber, groupId);
        Warrior warrior = BattleServiceRequestUtils.getWarriorFromGroup(group, warriorId);

        StateTackle tackle = null;
        switch (bodyParticle) {
            case BodyParticles.HEAD:
                tackle = warrior.getHeadArmor();
                break;
            case BodyParticles.TORSO:
                tackle = warrior.getTorsoArmor();
                break;
            case BodyParticles.LEGS:
                tackle = warrior.getLegsArmor();
                break;
            case BodyParticles.HANDS:
                tackle = warrior.getWeapon();
                break;
            default:
                throw new RegolithException();
        }
        if (WarriorHelper.putInToBag(warrior, tackle, baseConfiguration)) {
            return ServerConfirmationAnswer.answerSuccess(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG);
        } else {
            return ServerConfirmationAnswer.answerFailure(writeBuffer, Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG);
        }
    }
}
