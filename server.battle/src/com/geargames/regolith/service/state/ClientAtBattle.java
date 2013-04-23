package com.geargames.regolith.service.state;

import com.geargames.regolith.*;
import com.geargames.common.serialization.MicroByteBuffer;
import com.geargames.regolith.serializers.requests.*;
import com.geargames.regolith.service.*;
import com.geargames.regolith.units.battle.ServerBattle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ClientAtBattle extends BattleState {
    private static Logger logger = LoggerFactory.getLogger(ClientAtBattle.class);
    private ServerBattle serverBattle;
    private ClientWriter writer;
    private RegolithConfiguration regolithConfiguration;

    public ClientAtBattle(ServerBattle serverBattle) {
        this.serverBattle = serverBattle;
        BattleServiceConfiguration configuration = BattleServiceConfigurationFactory.getConfiguration();
        this.writer = configuration.getWriter();
        this.regolithConfiguration = configuration.getRegolithConfiguration();
    }

    @Override
    protected void execute(MicroByteBuffer from, Client client, short type) throws RegolithException {
        ServerRequest request = null;
        switch (type) {
            case Packets.EXIT_BATTLE:

                break;
            case Packets.MOVE_WARRIOR:
                request = new ServerMoveWarriorRequest(serverBattle);
                break;
            case Packets.SHOOT:
                request = new ServerShootRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BAG:
                request = new ServerTackleGround2BagRequest(serverBattle, regolithConfiguration.getBaseConfiguration());
                break;
            case Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BODY:
                request = new ServerTackleGround2BodyRequest(serverBattle, regolithConfiguration.getBaseConfiguration());
                break;
            case Packets.TAKE_TACKLE_FROM_GROUND_PUT_ON_GROUND:
                request = new ServerGround2GroundRequest(serverBattle, Packets.TAKE_TACKLE_FROM_GROUND_PUT_ON_GROUND);
                break;
            case Packets.TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX:
                request = new ServerTackleGround2BoxRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BOX_PUT_INTO_BAG:
                request = new ServerTackleBox2BagRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BOX_PUT_INTO_BODY:
                request = new ServerTackleBox2BodyRequest(serverBattle, regolithConfiguration.getBaseConfiguration());
                break;
            case Packets.TAKE_TACKLE_FROM_BOX_PUT_ON_GROUND:
                request = new ServerTackleBox2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BOX:
                request = new ServerTackleBody2BoxRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BODY_PUT_ON_GROUND:
                request = new ServerTackleBody2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG:
                request = new ServerTackleBody2BagRequest(serverBattle, regolithConfiguration.getBaseConfiguration());
                break;
            case Packets.TAKE_TACKLE_FROM_BAG_PUT_INTO_BOX:
                request = new ServerTackleBag2BoxRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_GROUND:
                request = new ServerTackleBag2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_TACKLE_FROM_BAG_PUT_ON_BODY:
                request = new ServerTackleBag2BodyRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BAG:
                request = new ServerMedikitGround2BagRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_BOX_PUT_INTO_BAG:
                request = new ServerMedikitBox2BagRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_BAG_PUT_INTO_GROUND:
                request = new ServerMedikitBag2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_BAG_PUT_INTO_BOX:
                request = new ServerMedikitBag2BoxRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_BOX_PUT_INTO_GROUND:
                request = new ServerMedikitBag2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_GROUND:
                request = new ServerGround2GroundRequest(serverBattle, Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_GROUND);
                break;
            case Packets.TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX:
                request = new ServerMedikitGround2BoxRequest(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BAG:
                request = new ServerMagazineGround2BagRequest(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_BOX_PUT_INTO_GROUND:
                request = new ServerMedikitBox2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_GROUND:
                request = new ServerGround2GroundRequest(serverBattle, Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_GROUND);
                break;
            case Packets.TAKE_PROJECTILE_FROM_BOX_PUT_INTO_BAG:
                request = new ServerMagazineBox2BagRequest(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_BAG_PUT_ON_GROUND:
                request = new ServerMagazineBag2GroundRequest(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_BAG_PUT_INTO_BOX:
                request = new ServerMagazineBag2Box(serverBattle);
                break;
            case Packets.TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BOX:
                request = new ServerMagazineGround2BoxRequest(serverBattle);
                break;
            case Packets.RECHARGE_WEAPON:
                request = new ServerRechargeWeaponRequest(serverBattle);
                break;
            case Packets.PUT_PROJECTILE_OUT_OF_WEAPON:
                request = new ServerProjectileWeapon2BagRequest(serverBattle);
                break;
            case Packets.PUT_PROJECTILE_INTO_WEAPON:
                break;
            case Packets.USE_MEDIKIT:
                request = new ServerUseMedikitRequest(serverBattle, regolithConfiguration);
                break;
            default:
                logger.debug("An unknown battle type {} ", type);
        }
        List<MessageToClient> messages = null;
        if (request != null) {
            messages = request.request(from, getWriteBuffer(), client);
        } else {
            messages = new ArrayList<MessageToClient>(0);
        }
        for (MessageToClient message : messages) {
            writer.addMessageToClient(message);
        }
    }
}
