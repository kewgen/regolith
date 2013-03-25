package com.geargames.regolith;

/**
 * Описание пакетов коммуникации с сервером
 * CLN - пакет передает клиент(мобила)
 * SRV - пакет передается с сервера
 */
public interface Packets {

    int HEAD_SIZE = 4;
    int SIZE_FIELD_SIZE = 2;
    int TYPE_FIELD_SIZE = 2;

    byte SUCCESS = 0;
    byte FAILURE = 1;

    short MESSAGE_TYPE_NULL = -1;

    short MESSAGE_IS_LATE = -2;

    short BATCH_MESSAGE = -3;

    short FRAME_MESSAGE = -4;
    /*
    Общий формат посылки любого пакета
        size short       - размер пакета
        pid  short       - ид пакета
        packet[size]    - пакет
    */
    //***************************** Пакеты ********************************

    short CLIENT_RECONNECT = 0;

    short CLIENT_CONNECT_TO_BATTLE_SEVER = 1;

    /*
    Регистрация клиента
        client_id[int]  - ид клиента - приложения установленного на девайсе. При первой регистрации 0.
        user_id[int]    - ид пользователя
    */
    short CLIENT_REGISTRATION = 2;

    /*
    Подтверждение регистрации сервером
        client_id[int]  - ид клиента - новое, если клиент не был зарегистрирован
        user_id[int]    - ид пользователя - новое, если пользователь не был зарегистрирован
    */

    short LOGIN = 3;

    short LOGOUT = 4;

    short CHECK_FOR_NAME = 5;

    short JOIN_BASE_WARRIORS_TO_ACCOUNT = 6;

    short CREATE_BATTLE = 8;

    short CANCEL_BATTLE = 9;

    short START_BATTLE = 10;

    short JOIN_TO_BATTLE_ALLIANCE = 11;

    short LISTEN_TO_BATTLE = 12;

    short GROUP_COMPLETE = 13;

    short DO_NOT_LISTEN_TO_BATTLE = 14;

    short BROWSE_CREATED_BATTLES = 15;

    short BROWSE_BATTLE_MAPS = 16;

    short EVICT_ACCOUNT_FROM_ALLIANCE = 17;

    short GROUP_IS_READY = 18;

    short GROUP_IS_NOT_READY = 19;

    short LISTEN_TO_BROWSED_CREATED_BATTLES = 20;

    short DO_NOT_LISTEN_TO_BROWSED_CREATED_BATTLES = 21;


    short GO_TO_WARRIOR_MARKET = 30;

    short GO_TO_TACKLE_MARKET = 31;

    short GO_TO_BATTLE_MARKET = 32;

    short GO_TO_BASE = 33;


    short CHANGE_ACTIVE_ALLIANCE = 40;

    short EXIT_BATTLE = 41;

    short CONTROL_SUM = 42;

    short FINISH_BATTLE = 43;

    short SHOOT = 49;

    short MOVE_ALLY = 50;

    short MOVE_ENEMY = 51;




    short TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BAG = 60;

    short TAKE_MEDIKIT_FROM_BOX_PUT_INTO_BAG = 61;


    short TAKE_MEDIKIT_FROM_BAG_PUT_INTO_GROUND = 62;

    short TAKE_MEDIKIT_FROM_BOX_PUT_INTO_GROUND = 63;

    short TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_GROUND = 64;


    short TAKE_MEDIKIT_FROM_BAG_PUT_INTO_BOX = 65;

    short TAKE_MEDIKIT_FROM_GROUND_PUT_INTO_BOX = 66;


    short TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BAG = 67;

    short TAKE_PROJECTILE_FROM_BOX_PUT_INTO_BAG = 68;


    short TAKE_PROJECTILE_FROM_BAG_PUT_ON_GROUND = 69;

    short TAKE_PROJECTILE_FROM_BOX_PUT_INTO_GROUND = 70;

    short TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_GROUND = 71;

    short TAKE_PROJECTILE_FROM_BAG_PUT_INTO_BOX = 72;

    short TAKE_PROJECTILE_FROM_GROUND_PUT_INTO_BOX = 73;


    short RECHARGE_WEAPON = 76;

    short PUT_PROJECTILE_OUT_OF_WEAPON = 77;

    short PUT_PROJECTILE_INTO_WEAPON = 78;

    short USE_MEDIKIT = 79;



    short TAKE_TACKLE_FROM_GROUND_PUT_INTO_BAG = 82;

    short TAKE_TACKLE_FROM_GROUND_PUT_INTO_BODY = 83;

    short TAKE_TACKLE_FROM_GROUND_PUT_ON_GROUND = 84;

    short TAKE_TACKLE_FROM_GROUND_PUT_INTO_BOX = 85;

    short TAKE_TACKLE_FROM_BOX_PUT_INTO_BAG = 86;

    short TAKE_TACKLE_FROM_BOX_PUT_INTO_BODY = 87;

    short TAKE_TACKLE_FROM_BOX_PUT_ON_GROUND = 88;

    short TAKE_TACKLE_FROM_BODY_PUT_INTO_BOX = 89;

    short TAKE_TACKLE_FROM_BODY_PUT_ON_GROUND = 90;

    short TAKE_TACKLE_FROM_BODY_PUT_INTO_BAG = 91;

    short TAKE_TACKLE_FROM_BAG_PUT_INTO_BOX = 92;

    short TAKE_TACKLE_FROM_BAG_PUT_ON_GROUND = 93;

    short TAKE_TACKLE_FROM_BAG_PUT_ON_BODY = 94;

    short TAKE_TACKLE_FROM_STORE_HOUSE_PUT_INTO_BAG = 95;

    short TAKE_TACKLE_FROM_STORE_HOUSE_PUT_ON_WARRIOR = 96;

    short TAKE_TACKLE_FROM_BAG_PUT_INTO_STORE_HOUSE = 97;

    short TAKE_AMMUNITION_FROM_BAG_PUT_INTO_STORE_HOUSE = 98;

    short TAKE_TACKLE_FROM_BODY_PUT_INTO_STORE_HOUSE = 99;

    short TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_ONTO_WARRIOR = 100;

    short TAKE_AMMUNITION_FROM_BAG_PUT_ON_WARRIOR = 101;

    short TAKE_AMMUNITION_FROM_STORE_HOUSE_PUT_INTO_BAG = 102;

    short BATTLE_SERVICE_NETWORK_ADDRESS = 200;

    short BATTLE_SERVICE_LOGIN = 201;

    short BATTLE_SERVICE_LOGOUT = 202;

    short BATTLE_SERVICE_NEW_CLIENT_LOGIN = 203;



}
