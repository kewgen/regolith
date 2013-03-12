//../regolith/_port/packer_320_480_full
//Tue Mar 12 09:45:10 MSK 2013 , version: 108

package app;

public class Graph {


    final static public byte IMG_COUNT = 30;
    final static public byte ELEMENT_LINE = 30;
    final static public byte ELEMENT_RECT = 31;
    final static public byte ELEMENT_FILLRECT = 32;
    final static public byte ELEMENT_FILLRECT_x4 = 33;
    final static public byte ELEMENT_ARC = 34;
    final static public int FRAMES_COUNT = 1426;
    final static public int SPR_COUNT = 294;
    final static public int ANIM_COUNT = 1;
    final static public int UNIT_COUNT = 537;
    final static public int UNITS_COUNT = 151;
    final static public int AFFINES_COUNT = 100;
//Image
//Frame
    final static public byte EL_EMPTY = 0;
    final static public byte MAN_1_DIR_1_BODY = 2;
    final static public byte MAN_1_DIR_2_BODY = 12;
    final static public byte MAN_1_DIR_3_BODY = 22;
    final static public byte MAN_1_DIR_4_BODY = 31;
    final static public byte MAN_1_DIR_5_BODY = 41;
    final static public byte MAN_1_DIR_6_BODY = 51;
    final static public byte MAN_1_DIR_7_BODY = 62;
    final static public byte MAN_DIR_8_BODY = 71;
    final static public byte MAN_1_DIR_1_HEAD = 81;
    final static public byte MAN_1_DIR_2_HEAD = 84;
    final static public byte MAN_1_DIR_3_HEAD = 88;
    final static public byte MAN_1_DIR_4_HEAD = 92;
    final static public byte MAN_1_DIR_5_HEAD = 96;
    final static public byte MAN_1_DIR_6_HEAD = 100;
    final static public byte MAN_1_DIR_7_HEAD = 104;
    final static public byte MAN_1_DIR_8_HEAD = 108;
    final static public byte MAN_1_DIR_1_FOOT = 112;
    final static public short MAN_1_DIR_2_FOOT = 152;
    final static public short MAN_1_DIR_3_FOOT = 205;
    final static public short MAN_1_DIR_4_FOOT = 257;
    final static public short MAN_1_DIR_5_FOOT = 296;
    final static public short MAN_1_DIR_6_FOOT = 338;
    final static public short MAN_1_DIR_7_FOOT = 382;
    final static public short MAN_1_DIR_8_FOOT = 430;
    final static public short MAN_1_DIR_1_ARM = 476;
    final static public short MAN_1_DIR_2_ARM = 524;
    final static public short MAN_1_DIR_3_ARM = 611;
    final static public short MAN_1_DIR_4_ARM = 676;
    final static public short MAN_1_DIR_5_ARM = 747;
    final static public short MAN_1_DIR_6_ARM = 823;
    final static public short MAN_1_DIR_7_ARM = 881;
    final static public short MAN_1_DIR_8_ARM = 898;
    final static public short MAN_1_DIR_1_WEAPON = 912;
    final static public short MAN_1_DIR_2_WEAPON = 928;
    final static public short MAN_1_DIR_3_WEAPON = 940;
    final static public short MAN_1_DIR_4_WEAPON = 959;
    final static public short MAN_1_DIR_5_WEAPON = 974;
    final static public short MAN_1_DIR_6_WEAPON = 998;
    final static public short MAN_1_DIR_7_WEAPON = 1012;
    final static public short MAN_1_DIR_8_WEAPON = 1018;
    final static public short EL_HINT = 1300;
    final static public short EL_FONT = 1309;
//Sprite
    final static public byte SPR_EMPTY = 0;
    final static public byte SPR_GROUND = 1;
    final static public byte SPR_BOTTON = 2;
    final static public byte SPR_RIFLE_DIR_6 = 20;
    final static public byte SPR_SHADOW = 22;
    final static public byte SPR_PICTURE_BACK = 30;
    final static public byte SPR_TEST = 31;
    final static public byte SPR_PANEL_BACK = 34;
    final static public byte SPR_PANEL_FRAME = 47;
    final static public byte SPR_SUB_PANEL = 55;
    final static public byte SPR_SIDE = 57;
    final static public byte SPR_PANEL_TOP_LEFT = 61;
    final static public byte SPR_PANEL_TOP_CENTRE = 62;
    final static public byte SPR_PANEL_TOP_RIGHT = 63;
    final static public byte SPR_TEAM_COLOR = 71;
    final static public byte SPR_SKILL_ICO = 78;
    final static public byte SPR_CHARACTER_IND = 86;
    final static public byte SPR_SKILL_IND = 97;
    final static public byte SPR_ICO_LIFE_IND = 118;
    final static public byte SPR_BUTTON = 126;
    final static public short SPR_ICO_WEAPON = 179;
    final static public short SPR_TEXT_TEST = 193;
    final static public short SPR_FONT_SYMB = 195;
    final static public short SPR_FONT_NUM = 211;
    final static public short SPR_FONT_EN = 228;
    final static public short SPR_FONT_RU = 260;
//Animation
//Unit
    final static public byte U_MAN_IDLE_old = 0;
    final static public byte U_MAN_FIRE_AUTO = 8;
    final static public byte U_MAN_HIT = 24;
    final static public byte U_MAN_IDLE = 32;
    final static public byte U_MAN_RUN_1 = 33;
    final static public byte U_MAN_SHOT_RIFLE_DIR_1 = 36;
    final static public byte U_MAN_RUN_RIFLE_DIR_1 = 40;
    final static public byte U_MAN_RUN_SHOTGUN_DIR_1 = 50;
    final static public byte U_MAN_RUN_PISTOL_DIR_1 = 61;
    final static public byte U_MAN_RUN_LAUNCHER_DIR_1 = 72;
    final static public byte U_MAN_STAY_KNIFE_2 = 83;
    final static public byte U_MAN_STAY_FIST_2 = 86;
    final static public byte U_MAN_HIT_FIST_2 = 91;
    final static public byte U_MAN_STAY_PISTOL_2 = 94;
    final static public byte U_MAN_HIT_PISTOL_2 = 99;
    final static public byte U_MAN_DOWN_PISTOL_2 = 102;
    final static public byte U_MAN_STAY_RIFLE_2 = 109;
    final static public byte U_MAN_HIT_RIFLE_2 = 115;
    final static public byte U_MAN_DOWN_RIFLE_2 = 118;
    final static public byte U_MAN_STAY_SHOTGUN_2 = 125;
    final static public short U_MAN_HIT_SHOTGUN_2 = 130;
    final static public short U_MAN_DOWN_SHOTGUN_2 = 133;
    final static public short U_MAN_HIT_LAUNCHER_2 = 140;
    final static public short U_MAN_STAY_LAUNCHER_2 = 143;
    final static public short U_MAN_DOWN_LAUNCHER_2 = 152;
    final static public short U_MAN_STAY_GRENADE_2 = 161;
    final static public short U_MAN_RUN_FIST_2 = 169;
    final static public short U_MAN_RUN_KNIFE_2 = 177;
    final static public short U_MAN_RUN_PISTOL_2 = 178;
    final static public short U_MAN_RUN_RIFLE_2 = 186;
    final static public short U_MAN_RUN_SHOTGUN_2 = 196;
    final static public short U_MAN_STAY_LAUNCHER_3 = 204;
    final static public short U_MAN_RUN_FIST_3 = 214;
    final static public short U_MAN_SHOT_FIST_3 = 222;
    final static public short U_MAN_HIT_FIST_3 = 227;
    final static public short U_MAN_RUN_PISTOL_3 = 230;
    final static public short U_MAN_SHOT_PISTOL_3 = 238;
    final static public short U_MAN_DOWN_PISTOL_3 = 243;
    final static public short U_MAN_HIT_PISTOL_3 = 249;
    final static public short U_MAN_RUN_RIFLE_3 = 252;
    final static public short U_MAN_SHOT_RIFLE_3 = 262;
    final static public short U_MAN_HIT_RIFLE_3 = 267;
    final static public short U_MAN_DOWN_RIFLE_3 = 270;
    final static public short U_MAN_RUN_SHOTGUN_3 = 275;
    final static public short U_MAN_SHOT_SHOTGUN_3 = 283;
    final static public short U_MAN_DOWN_SHOTGUN_3 = 287;
    final static public short U_MAN_HIT_SHOTGUN_3 = 292;
    final static public short U_MAN_RUN_LAUNCHER_3 = 295;
    final static public short U_MAN_SHOT_LAUNCHER_3 = 303;
    final static public short U_MAN_DOWN_LAUNCHER_3 = 308;
    final static public short U_MAN_HIT_LAUNCHER_3 = 314;
    final static public short U_MAN_SHOT_GRENAD_3 = 317;
    final static public short U_MAN_RUN_FIST_DIR_4 = 322;
    final static public short U_MAN_SHOT_FIST_DIR_4 = 330;
    final static public short U_MAN_HIT_FIST_4 = 334;
    final static public short U_MAN_HIT_RIFLE_DIR_4 = 335;
    final static public short U_MAN_RUN_RIFLE_DIR_4 = 339;
    final static public short U_MAN_SHOT_RIFLE_4 = 349;
    final static public short U_MAN_DOWN_RIFLE_4 = 353;
    final static public short U_MAN_HIT_RIFLE_4 = 358;
    final static public short U_MAN_RUN_PISTOL_4 = 362;
    final static public short U_MAN_SHOT_PISTOL_4 = 370;
    final static public short U_MAN_DOWN_PISTOL_4 = 375;
    final static public short U_MAN_HIT_PISTOL_4 = 381;
    final static public short U_MAN_RUN_SHOTGUN_4 = 387;
    final static public short U_MAN_SHOT_SHOTGUN_4 = 395;
    final static public short U_MAN_DOWN_SHOTGUN_4 = 399;
    final static public short U_MAN_HIT_SHOTGUN_4 = 404;
    final static public short U_MAN_RUN_LAUNCHER_4 = 407;
    final static public short U_MAN_SHOT_LAUNCHER_4 = 415;
    final static public short U_MAN_DOWN_LAUNCHER_4 = 420;
    final static public short U_MAN_HIT_LAUNCHER_4 = 426;
    final static public short U_MAN_SHOT_GRENADE_4 = 429;
    final static public short U_MAN_RUN_RIFLE_DIR_5 = 434;
    final static public short U_MAN_SHOT_RIFLE_DIR_5 = 444;
    final static public short U_MAN_DOWN_RIFLE_DIR_5 = 448;
    final static public short U_MAN_HIT_RIFLE_DIR_5 = 453;
    final static public short U_MAN_RUN_PISTOL_DIR_5 = 456;
    final static public short U_MAN_SHOT_PISTOL_DIR_5 = 464;
    final static public short U_MAN_DOWN_PISTOL_DIR_5 = 469;
    final static public short U_MAN_HIT_PISTOL_DIR_5 = 476;
    final static public short U_MAN_RUN_RIFLE_DIR_6 = 479;
    final static public short U_MAN_SHOT_RIFLE_DIR_6 = 489;
    final static public short U_MAN_RUN_RIFLE_DIR_7 = 493;
    final static public short U_MAN_RUN_RIFLE_DIR_8 = 503;
    final static public short U_MAN_RUN_DAGGER_1 = 513;
    final static public short U_MAN_SHOOT_PISTOL = 516;
    final static public short U_MAN_SHOOT_SHOTGUN = 519;
    final static public short U_MAN_HIT_5 = 525;
    final static public short U_MAN_GRENADE = 527;
//Unit script
    final static public byte UN_MAN_IDLE = 0;
    final static public byte UN_MAN_WALK = 8;
    final static public byte UN_MAN_FIRE_AUTO = 16;
    final static public byte UN_MAN_HIT = 24;
    final static public byte UN_MAN_RUN_RIFLE_1 = 32;
    final static public byte UN_MAN_RUN_RIFLE_2 = 33;
    final static public byte UN_MAN_RUN_RIFLE_3 = 34;
    final static public byte UN_MAN_RUN_RIFLE_4 = 35;
    final static public byte UN_MAN_RUN_RIFLE_5 = 36;
    final static public byte UN_MAN_RUN_RIFLE_6 = 37;
    final static public byte UN_MAN_RUN_RIFLE_7 = 38;
    final static public byte UN_MAN_RUN_RIFLE_8 = 39;
    final static public byte UN_MAN_SHOT_RIFLE_1 = 41;
    final static public byte UN_MAN_SHOT_RIFLE_2 = 42;
    final static public byte UN_MAN_SHOT_RIFLE_3 = 43;
    final static public byte UN_MAN_SHOT_RIFLE_4 = 44;
    final static public byte UN_MAN_SHOT_RIFLE_5 = 45;
    final static public byte UN_MAN_SHOT_RIFLE_6 = 46;
    final static public byte UN_MAN_DOWN_RIFLE_2 = 49;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_2 = 50;
    final static public byte UN_MAN_UP_RIFLE_2 = 51;
    final static public byte UN_MAN_HIT_RIFLE_2 = 52;
    final static public byte UN_MAN_SHOT_GRENADE_2 = 53;
    final static public byte UN_MAN_DOWN_RIFLE_3 = 55;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_3 = 56;
    final static public byte UN_MAN_UP_RIFLE_3 = 57;
    final static public byte UN_MAN_HIT_RIFLE_3 = 58;
    final static public byte UN_MAN_SHOT_GRENADE_3 = 59;
    final static public byte UN_MAN_DOWN_RIFLE_4 = 61;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_4 = 62;
    final static public byte UN_MAN_UP_RIFLE_4 = 63;
    final static public byte UN_MAN_HIT_RIFLE_4 = 64;
    final static public byte UN_MAN_SHOT_GRENADE_4 = 65;
    final static public byte UN_MAN_DOWN_RIFLE_5 = 67;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_5 = 68;
    final static public byte UN_MAN_UP_RIFLE_5 = 69;
    final static public byte UN_MAN_DOWN_PISTOL_5 = 70;
    final static public byte UN_MAN_HIT_RIFLE_5 = 71;
    final static public byte UN_MAN_RUN_SHOTGUN_1 = 73;
    final static public byte UN_MAN_RUN_PISTOL_1 = 74;
    final static public byte UN_MAN_RUN_LAUNCHER_1 = 75;
    final static public byte UN_MAN_RUN_FIST_2 = 76;
    final static public byte UN_MAN_RUN_KNIFE_2 = 77;
    final static public byte UN_MAN_RUN_PISTOL_2 = 78;
    final static public byte UN_MAN_RUN_SHOTGUN_2 = 79;
    final static public byte UN_MAN_RUN_LAUNCHER_2 = 80;
    final static public byte UN_MAN_SHOT_FIST_2 = 81;
    final static public byte UN_MAN_SHOT_PISTOL_2 = 82;
    final static public byte UN_MAN_DOWN_PISTOL_2 = 83;
    final static public byte UN_MAN_DOWN_SHOT_PISTOL_2 = 84;
    final static public byte UN_MAN_UP_PISTOL_2 = 85;
    final static public byte UN_MAN_SHOT_SHOTGUN_2 = 86;
    final static public byte UN_MAN_DOWN_SHOTGUN_2 = 87;
    final static public byte UN_MAN_DOWN_SHOT_SHOTGUN_2 = 88;
    final static public byte UN_MAN_UP_SHOTGUN_2 = 89;
    final static public byte UN_MAN_SHOT_LAUNCHER_2 = 90;
    final static public byte UN_MAN_DOWN_LAUNCHER_2 = 91;
    final static public byte UN_MAN_DOWN_SHOT_LAUNCHER_2 = 92;
    final static public byte UN_MAN_UP_LAUNCHER_2 = 93;
    final static public byte UN_MAN_HIT_FIST_2 = 94;
    final static public byte UN_MAN_HIT_PISTOL_2 = 95;
    final static public byte UN_MAN_HIT_SHOTGUN_2 = 96;
    final static public byte UN_MAN_HIT_LAUNCHER_2 = 97;
    final static public byte UN_MAN_RUN_FIST_3 = 99;
    final static public byte UN_MAN_SHOT_FIST_3 = 100;
    final static public byte UN_MAN_RUN_PISTOL_3 = 101;
    final static public byte UN_MAN_SHOT_PISTOL_3 = 102;
    final static public byte UN_MAN_DOWN_PISTOL_3 = 103;
    final static public byte UN_MAN_DOWN_SHOT_PISTOL_3 = 104;
    final static public byte UN_MAN_UP_PISTOL_3 = 105;
    final static public byte UN_MAN_RUN_SHOTGUN_3 = 106;
    final static public byte UN_MAN_SHOT_SHOTGUN_3 = 107;
    final static public byte UN_MAN_DOWN_SHOTGUN_3 = 108;
    final static public byte UN_MAN_DOWN_SHOT_SHOTGUN_3 = 109;
    final static public byte UN_MAN_UP_SHOTGUN_3 = 110;
    final static public byte UN_MAN_RUN_LAUNCHER_3 = 111;
    final static public byte UN_MAN_SHOT_LAUNCHER_3 = 112;
    final static public byte UN_MAN_DOWN_LAUNCHER_3 = 113;
    final static public byte UN_MAN_DOWN_SHOT_LAUNCHER_3 = 114;
    final static public byte UN_MAN_UP_LAUNCHER_3 = 115;
    final static public byte UN_MAN_HIT_FIST_3 = 116;
    final static public byte UN_MAN_HIT_PISTOL_3 = 117;
    final static public byte UN_MAN_HIT_SHOTGUN_3 = 118;
    final static public byte UN_MAN_HIT_LAUNCHER_3 = 119;
    final static public byte UN_MAN_RUN_FIST_4 = 121;
    final static public byte UN_MAN_SHOT_FIST_4 = 122;
    final static public byte UN_MAN_HIT_FIST_4 = 123;
    final static public byte UN_MAN_RUN_PISTOL_4 = 124;
    final static public byte UN_MAN_SHOT_PISTOL_4 = 125;
    final static public byte UN_MAN_DOWN_PISTOL_4 = 126;
    final static public byte UN_MAN_DOWN_SHOT_PISTOL_4 = 127;
    final static public short UN_MAN_UP_PISTOL_4 = 128;
    final static public short UN_MAN_HIT_PISTOL_4 = 129;
    final static public short UN_MAN_RUN_SHOTGUN_4 = 130;
    final static public short UN_MAN_SHOT_SHOTGUN_4 = 131;
    final static public short UN_MAN_DOWN_SHOTGUN_4 = 132;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_4 = 133;
    final static public short UN_MAN_UP_SHOTGUN_4 = 134;
    final static public short UN_MAN_HIT_SHOTGUN_4 = 135;
    final static public short UN_MAN_RUN_LAUNCHER_4 = 136;
    final static public short UN_MAN_SHOT_LAUNCHER_4 = 137;
    final static public short UN_MAN_DOWN_LAUNCHER_4 = 138;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_4 = 139;
    final static public short UN_MAN_UP_LAUNCHER_4 = 140;
    final static public short UN_MAN_HIT_LAUNCHER_4 = 141;
    final static public short UN_MAN_RUN_PISTOL_5 = 143;
    final static public short UN_MAN_SHOT_PISTOL_5 = 144;
    final static public short UN_MAN_DOWN_PISTOL_5_2 = 145;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_5 = 146;
    final static public short UN_MAN_UP_PISTOL_5 = 147;
    final static public short UN_MAN_HIT_PISTOL_5 = 148;
    final static public short UN_MAN_RUN_FIST_5 = 150;
//Object
    final static public byte OBJ_FENCE = 0;
    final static public byte OBJ_BAR = 4;
    final static public byte PAN_MAIN_MENU = 6;
    final static public byte PAN_TYPE_BATTLE = 7;
    final static public byte PAN_FIGHTER = 8;
    final static public byte PAN_FIGHTER_INFO = 9;
    final static public byte PAN_WEAPON_SELECT = 10;
    final static public byte PAN_BATTLE_LIST = 11;
    final static public byte PAN_BATTLE_CREATE = 12;
    final static public byte PAN_PLAYER_INFO = 13;
    final static public byte PAN_FIGHTER_SELECT = 14;
    final static public byte PAN_MAP_SELECT = 15;
    final static public byte PAN_LEFT = 16;
    final static public byte PAN_CENTER = 17;
    final static public byte PAN_RIGHT = 18;
    final static public byte OBJ_SUBPANEL_WEAPON_LEFT = 19;
    final static public byte OBJ_LIST_WEAPON_ITEM = 20;
    final static public byte OBJ_LIST_BATTLE_ITEM = 21;
    final static public byte OBJ_LIST_WEAPON = 22;
    final static public byte OBJ_LIST_BATTLE = 23;
    final static public byte OBJ_LIST_FIGHTER = 24;
    final static public byte OBJ_LIST_REWARD = 25;
    final static public byte OBJ_LIST_FIGHTER_FACE = 26;
    final static public byte OBJ_LIST_MAP = 27;
    final static public byte OBJ_REWARD_AVA_BIG = 28;
    final static public byte OBJ_PLAYER_AVA_BIG = 29;
    final static public byte OBJ_PLAYER_AVA_SMALL = 30;
    final static public byte OBJ_MAP_MINI = 31;
    final static public byte OBJ_FIGHTER_SMALL = 32;
    final static public byte OBJ_SIDE = 33;
    final static public byte OBJ_BATTLE = 37;
    final static public byte OBJ_IND_HEALTH = 38;
    final static public byte OBJ_IND_1 = 39;
    final static public byte OBJ_IND_CHARACTER = 40;
    final static public byte OBJ_IND_SKILL = 41;
    final static public byte OBJ_LABEL_MONEY = 42;
    final static public byte OBJ_BUT_LATER = 43;
    final static public byte OBJ_BUT_PREVISION = 44;
    final static public byte OBJ_BUT_BACK = 45;
    final static public byte OBJ_BUT_RADIO = 46;
    final static public byte OBJ_BUT = 56;
    final static public byte OBJ_BUT_SELECT = 57;
    final static public byte OBJ_BUT_BATTLE = 59;
    final static public byte OBJ_BUT_KICK = 60;
    final static public byte OBJ_BUT_PLAYER_INFO = 65;
    final static public byte OBJ_BUT_WIEW = 67;
    final static public byte OBJ_CHECKBOX = 69;
    final static public byte OBJ_CHECKBOX_STRING = 70;
    final static public byte OBJ_BUT_PLUS = 71;
    final static public byte OBJ_SPINBOX = 73;
    final static public byte OBJ_HINT = 74;
    final static public byte MENU_COMMON = 75;
    final static public byte MENU_COMMON_ITEM = 76;
    final static public byte MENU_COMMON_CONFIRM = 77;
    final static public byte MENU_BOTTON_YES = 78;
    final static public byte MENU_BOTTON_NO = 79;
    final static public byte MENU_BOTTON_MENU = 80;
    final static public byte MENU_TEST = 81;
    final static public byte MENU_TEST_2 = 82;
    final static public byte OBJ_PANEL_TOP_TEST = 83;
//Location
//Map
//PColor
//GAI
//Mission
     final static int[] COLOR = {0, 65535, };
    static public int getCOLOR(int i){return COLOR[i];};

//Links
    final static public byte Frame_I_ID = 0;//1426
    final static public byte Frame_I_TYPE = 1;//1426
    final static public byte Sprite_index_I_TYPE = 2;//1108
    final static public byte Animation_index_I_TYPE = 3;//1
    final static public byte Unit_index_I_TYPE = 4;//5223
    final static public byte Unit_index_I_H = 5;//5223
    final static public byte Unit_script_index_I_TYPE = 6;//1185
    final static public byte Object_index_I_W = 7;//448
    final static public byte Object_index_I_H = 8;//448
    final static public byte Object_index_I_TYPE = 9;//448
    final static public byte Object_index_I_LAYER_TYPE = 10;//448
    final static public byte Object_index_I_SLOT = 11;//448
    final static public byte Emitter_index_I_W = 12;//0
    final static public byte Emitter_index_I_H = 13;//0
    final static public byte Emitter_index_I_TYPE = 14;//0
    final static public byte Emitter_index_I_SLOT = 15;//0
    final static public byte Affine_I_AF_TR = 16;//100
    final static public byte Affine_I_AF_SC_X = 17;//100
    final static public byte Affine_I_AF_SC_Y = 18;//100
    final static public byte Affine_I_AF_HM = 19;//100
    final static public byte Affine_I_AF_VM = 20;//100
    final static public byte PARAM_BYTE_COUNT = 21;

    final static public byte Frame_I_X = 21;//1426
    final static public byte Frame_I_Y = 22;//1426
    final static public byte Frame_I_W = 23;//1426
    final static public byte Frame_I_H = 24;//1426
    final static public byte Sprite_I_X = 25;//294
    final static public byte Sprite_I_Y = 26;//294
    final static public byte Sprite_index_I_ID = 27;//1108
    final static public byte Sprite_index_I_X = 28;//1108
    final static public byte Sprite_index_I_Y = 29;//1108
    final static public byte Animation_I_X = 30;//1
    final static public byte Animation_I_Y = 31;//1
    final static public byte Animation_index_I_ID = 32;//1
    final static public byte Animation_index_I_X = 33;//1
    final static public byte Animation_index_I_Y = 34;//1
    final static public byte Unit_I_X = 35;//537
    final static public byte Unit_I_Y = 36;//537
    final static public byte Unit_index_I_ID = 37;//5223
    final static public byte Unit_index_I_X = 38;//5223
    final static public byte Unit_index_I_Y = 39;//5223
    final static public byte Unit_script_I_X = 40;//151
    final static public byte Unit_script_I_Y = 41;//151
    final static public byte Unit_script_index_I_ID = 42;//1185
    final static public byte Unit_script_index_I_X = 43;//1185
    final static public byte Unit_script_index_I_Y = 44;//1185
    final static public byte Object_I_X = 45;//84
    final static public byte Object_I_Y = 46;//84
    final static public byte Object_index_I_ID = 47;//448
    final static public byte Object_index_I_X = 48;//448
    final static public byte Object_index_I_Y = 49;//448
    final static public byte Emitter_I_X = 50;//0
    final static public byte Emitter_I_Y = 51;//0
    final static public byte Emitter_index_I_ID = 52;//0
    final static public byte Emitter_index_I_X = 53;//0
    final static public byte Emitter_index_I_Y = 54;//0
    final static public byte Affine_I_X = 55;//100
    final static public byte Affine_I_Y = 56;//100
    final static public byte Affine_I_AF_RO = 57;//100
    final static public byte Affine_index_I_ID = 58;//100
    final static public byte Affine_index_I_X = 59;//100
    final static public byte Affine_index_I_Y = 60;//100
    final static public byte PARAM_SHORT_COUNT = 40;

    final static public byte Emitter_I_EM0 = 61;//0
    final static public byte Emitter_I_EM1 = 62;//0
    final static public byte Emitter_I_EM2 = 63;//0
    final static public byte Emitter_I_EM3 = 64;//0
    final static public byte Emitter_I_EM4 = 65;//0
    final static public byte Emitter_I_EM5 = 66;//0
    final static public byte Emitter_I_EM6 = 67;//0
    final static public byte Emitter_I_EM7 = 68;//0
    final static public byte Emitter_I_EM8 = 69;//0
    final static public byte Emitter_I_EM9 = 70;//0
    final static public byte Emitter_I_EM10 = 71;//0
    final static public byte Emitter_I_EM11 = 72;//0
    final static public byte Emitter_I_EM12 = 73;//0
    final static public byte Emitter_I_EM13 = 74;//0
    final static public byte Emitter_I_EM14 = 75;//0
    final static public byte Emitter_I_EM15 = 76;//0
    final static public byte Emitter_I_EM16 = 77;//0
    final static public byte Emitter_I_EM17 = 78;//0
    final static public byte Emitter_I_EM18 = 79;//0
    final static public byte Emitter_I_EM19 = 80;//0
    final static public byte PARAM_INT_COUNT = 20;
    final static public int PARAM_MAX_SIZE = 5223;//getCount of maximum array

//structure of packeting images
     final static byte[] IMG_INDEX = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    static public int getIMG_INDEX(int i) {
        return IMG_INDEX[i];
    };
     final static int[] IMG_LEN = {197120,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    public Graph() {
/*ObjC uncomment*///return self;
    }

}
