//../regolith/_port/packer_320_480_full
//Fri Mar 29 18:25:01 GMT+04:00 2013 , version: 108

package app;

public class Graph {


    final static public byte IMG_COUNT = 31;
    final static public byte ELEMENT_LINE = 31;
    final static public byte ELEMENT_RECT = 32;
    final static public byte ELEMENT_FILLRECT = 33;
    final static public byte ELEMENT_FILLRECT_x4 = 34;
    final static public byte ELEMENT_ARC = 35;
    final static public int FRAMES_COUNT = 1538;
    final static public int SPR_COUNT = 297;
    final static public int ANIM_COUNT = 1;
    final static public int UNIT_COUNT = 634;
    final static public int UNITS_COUNT = 193;
    final static public int AFFINES_COUNT = 160;
//Image
    final static public byte IMG_BODY_1_1 = 1;
    final static public byte IMG_BODY_1_2 = 2;
    final static public byte IMG_BODY_1_3 = 3;
    final static public byte IMG_HEAD_1_1 = 4;
    final static public byte IMG_LEGS_1_1 = 5;
    final static public byte IMG_LEGS_1_2 = 6;
    final static public byte IMG_LEGS_1_6 = 7;
    final static public byte IMG_LEGS_1_3 = 8;
    final static public byte IMG_LEGS_1_4 = 9;
    final static public byte IMG_LEGS_1_5 = 10;
    final static public byte IMG_LEGS_1_7 = 11;
    final static public byte IMG_LEGS_1_8 = 12;
    final static public byte IMG_ARMS_1_1 = 13;
    final static public byte MG_ARMS_1_2 = 14;
    final static public byte MG_ARMS_1_3 = 15;
    final static public byte MG_ARMS_1_4 = 16;
    final static public byte MG_ARMS_1_5 = 17;
    final static public byte MG_ARMS_1_6 = 18;
    final static public byte MG_WEAPON_1_1 = 19;
    final static public byte MG_WEAPON_1_2 = 20;
    final static public byte MG_WEAPON_1_3 = 21;
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
    final static public short MAN_1_DIR_4_FOOT = 258;
    final static public short MAN_1_DIR_5_FOOT = 297;
    final static public short MAN_1_DIR_6_FOOT = 340;
    final static public short MAN_1_DIR_7_FOOT = 386;
    final static public short MAN_1_DIR_8_FOOT = 434;
    final static public short MAN_1_DIR_1_ARM = 477;
    final static public short MAN_1_DIR_2_ARM = 533;
    final static public short MAN_1_DIR_3_ARM = 620;
    final static public short MAN_1_DIR_4_ARM = 695;
    final static public short MAN_1_DIR_5_ARM = 766;
    final static public short MAN_1_DIR_6_ARM = 855;
    final static public short MAN_1_DIR_7_ARM = 913;
    final static public short MAN_1_DIR_8_ARM = 976;
    final static public short MAN_1_DIR_1_WEAPON = 1017;
    final static public short MAN_1_DIR_2_WEAPON = 1033;
    final static public short MAN_1_DIR_3_WEAPON = 1045;
    final static public short MAN_1_DIR_4_WEAPON = 1064;
    final static public short MAN_1_DIR_5_WEAPON = 1079;
    final static public short MAN_1_DIR_6_WEAPON = 1104;
    final static public short MAN_1_DIR_7_WEAPON = 1119;
    final static public short MAN_1_DIR_8_WEAPON = 1125;
    final static public short EL_HINT = 1412;
    final static public short EL_FONT = 1421;
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
    final static public byte SPR_TEAM_COLOR = 74;
    final static public byte SPR_SKILL_ICO = 81;
    final static public byte SPR_CHARACTER_IND = 89;
    final static public byte SPR_SKILL_IND = 100;
    final static public byte SPR_ICO_LIFE_IND = 121;
    final static public short SPR_BUTTON = 129;
    final static public short SPR_ICO_WEAPON = 182;
    final static public short SPR_TEXT_TEST = 196;
    final static public short SPR_FONT_SYMB = 198;
    final static public short SPR_FONT_NUM = 214;
    final static public short SPR_FONT_EN = 231;
    final static public short SPR_FONT_RU = 263;
//Animation
//Unit
    final static public byte U_MAN_IDLE_old = 0;
    final static public byte U_MAN_FIRE_AUTO = 8;
    final static public byte U_MAN_HIT = 24;
    final static public byte U_MAN_SHOT_RIFLE_DIR_1 = 32;
    final static public byte U_MAN_DOWN_RIFLE_DIR_1 = 36;
    final static public byte U_MAN_RUN_RIFLE_DIR_1 = 41;
    final static public byte U_MAN_HIT_RIFLE_DIR_1 = 51;
    final static public byte U_MAN_SHOT_GRENADE_DIR_1 = 53;
    final static public byte U_MAN_STAY_FIST = 59;
    final static public byte U_MAN_SHOTDOWN_DIR_1 = 63;
    final static public byte U_MAN_RUN_SHOTGUN_DIR_1 = 64;
    final static public byte U_MAN_RUN_PISTOL_DIR_1 = 75;
    final static public byte U_MAN_RUN_LAUNCHER_DIR_1 = 86;
    final static public byte U_MAN_STAY_KNIFE_2 = 97;
    final static public byte U_MAN_STAY_FIST_2 = 100;
    final static public byte U_MAN_HIT_FIST_2 = 105;
    final static public byte U_MAN_STAY_PISTOL_2 = 108;
    final static public byte U_MAN_HIT_PISTOL_2 = 113;
    final static public byte U_MAN_DOWN_PISTOL_2 = 116;
    final static public byte U_MAN_STAY_RIFLE_2 = 123;
    final static public short U_MAN_HIT_RIFLE_2 = 129;
    final static public short U_MAN_DOWN_RIFLE_2 = 133;
    final static public short U_MAN_STAY_SHOTGUN_2 = 140;
    final static public short U_MAN_HIT_SHOTGUN_2 = 145;
    final static public short U_MAN_DOWN_SHOTGUN_2 = 148;
    final static public short U_MAN_HIT_LAUNCHER_2 = 155;
    final static public short U_MAN_STAY_LAUNCHER_2 = 158;
    final static public short U_MAN_DOWN_LAUNCHER_2 = 167;
    final static public short U_MAN_STAY_GRENADE_2 = 176;
    final static public short U_MAN_RUN_FIST_2 = 186;
    final static public short U_MAN_RUN_KNIFE_2 = 194;
    final static public short U_MAN_RUN_PISTOL_2 = 195;
    final static public short U_MAN_RUN_RIFLE_2 = 203;
    final static public short U_MAN_RUN_SHOTGUN_2 = 213;
    final static public short U_MAN_STAY_LAUNCHER_3 = 221;
    final static public short U_MAN_RUN_FIST_3 = 231;
    final static public short U_MAN_SHOT_FIST_3 = 239;
    final static public short U_MAN_HIT_FIST_3 = 244;
    final static public short U_MAN_RUN_PISTOL_3 = 247;
    final static public short U_MAN_SHOT_PISTOL_3 = 255;
    final static public short U_MAN_DOWN_PISTOL_3 = 260;
    final static public short U_MAN_HIT_PISTOL_3 = 266;
    final static public short U_MAN_RUN_RIFLE_3 = 269;
    final static public short U_MAN_SHOT_RIFLE_3 = 279;
    final static public short U_MAN_HIT_RIFLE_3 = 284;
    final static public short U_MAN_DOWN_RIFLE_3 = 286;
    final static public short U_MAN_RUN_SHOTGUN_3 = 291;
    final static public short U_MAN_SHOT_SHOTGUN_3 = 299;
    final static public short U_MAN_DOWN_SHOTGUN_3 = 303;
    final static public short U_MAN_HIT_SHOTGUN_3 = 308;
    final static public short U_MAN_RUN_LAUNCHER_3 = 311;
    final static public short U_MAN_SHOT_LAUNCHER_3 = 319;
    final static public short U_MAN_DOWN_LAUNCHER_3 = 324;
    final static public short U_MAN_HIT_LAUNCHER_3 = 330;
    final static public short U_MAN_SHOT_GRENAD_3 = 333;
    final static public short U_MAN_RUN_FIST_DIR_4 = 340;
    final static public short U_MAN_SHOT_FIST_DIR_4 = 348;
    final static public short U_MAN_HIT_FIST_4 = 352;
    final static public short U_MAN_HIT_RIFLE_DIR_4 = 353;
    final static public short U_MAN_RUN_RIFLE_DIR_4 = 357;
    final static public short U_MAN_SHOT_RIFLE_4 = 367;
    final static public short U_MAN_DOWN_RIFLE_4 = 371;
    final static public short U_MAN_HIT_RIFLE_4 = 376;
    final static public short U_MAN_RUN_PISTOL_4 = 380;
    final static public short U_MAN_SHOT_PISTOL_4 = 388;
    final static public short U_MAN_DOWN_PISTOL_4 = 393;
    final static public short U_MAN_HIT_PISTOL_4 = 399;
    final static public short U_MAN_RUN_SHOTGUN_4 = 405;
    final static public short U_MAN_SHOT_SHOTGUN_4 = 413;
    final static public short U_MAN_DOWN_SHOTGUN_4 = 417;
    final static public short U_MAN_HIT_SHOTGUN_4 = 422;
    final static public short U_MAN_RUN_LAUNCHER_4 = 425;
    final static public short U_MAN_SHOT_LAUNCHER_4 = 433;
    final static public short U_MAN_DOWN_LAUNCHER_4 = 438;
    final static public short U_MAN_HIT_LAUNCHER_4 = 444;
    final static public short U_MAN_SHOT_GRENADE_4 = 447;
    final static public short U_MAN_RUN_RIFLE_DIR_5 = 454;
    final static public short U_MAN_SHOT_RIFLE_DIR_5 = 464;
    final static public short U_MAN_STAY_FIST_DIR_5 = 468;
    final static public short U_MAN_DOWN_RIFLE_DIR_5 = 472;
    final static public short U_MAN_HIT_RIFLE_DIR_5 = 477;
    final static public short U_MAN_RUN_PISTOL_DIR_5 = 480;
    final static public short U_MAN_SHOT_PISTOL_DIR_5 = 488;
    final static public short U_MAN_DOWN_PISTOL_DIR_5 = 493;
    final static public short U_MAN_HIT_PISTOL_DIR_5 = 500;
    final static public short U_MAN_SHOT_GRENADE_DIR_5 = 503;
    final static public short U_MAN_RUN_RIFLE_DIR_6 = 510;
    final static public short U_MAN_SHOT_RIFLE_DIR_6 = 520;
    final static public short U_MAN_HIT_RIFLE_DIR_6 = 524;
    final static public short U_MAN_DOWN_RIFLE_DIR_6 = 527;
    final static public short U_MAN_SHOT_GRENADE_DIR_6 = 532;
    final static public short U_MAN_STAY_FIST_DIR_6 = 538;
    final static public short U_MAN_RUN_RIFLE_DIR_7 = 544;
    final static public short U_MAN_SHOT_RIFLE_DIR_7 = 554;
    final static public short U_MAN_HIT_RIFLE_DIR_7 = 558;
    final static public short U_MAN_DOWN_RIFLE_7 = 561;
    final static public short U_MAN_SHOT_GRENAD_DIR_7 = 566;
    final static public short U_MAN_STAY_FIST_DIR_7 = 572;
    final static public short U_MAN_RUN_RIFLE_DIR_8 = 577;
    final static public short U_MAN_SHOT_RIFLE_DIR_8 = 587;
    final static public short U_MAN_HIT_RIFLE_DIR_8 = 591;
    final static public short U_MAN_DOWN_RIFLE_DIR_8 = 594;
    final static public short U_MAN_SHOT_GRENADE_DIR_8 = 599;
    final static public short U_MAN_STAY_FIST_DIR_8 = 605;
    final static public short U_MAN_RUN_DAGGER_1 = 610;
    final static public short U_MAN_SHOOT_PISTOL = 613;
    final static public short U_MAN_SHOOT_SHOTGUN = 616;
    final static public short U_MAN_HIT_5 = 622;
    final static public short U_MAN_GRENADE = 624;
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
    final static public byte UN_MAN_STAY_RIFLE_1 = 41;
    final static public byte UN_MAN_STAY_RIFLE_2 = 42;
    final static public byte UN_MAN_STAY_RIFLE_3 = 43;
    final static public byte UN_MAN_STAY_RIFLE_4 = 44;
    final static public byte UN_MAN_STAY_RIFLE_5 = 45;
    final static public byte UN_MAN_STAY_RIFLE_6 = 46;
    final static public byte UN_MAN_STAY_RIFLE_7 = 47;
    final static public byte UN_MAN_STAY_RIFLE_8 = 48;
    final static public byte UN_MAN_SHOT_RIFLE_1 = 50;
    final static public byte UN_MAN_SHOT_RIFLE_2 = 51;
    final static public byte UN_MAN_SHOT_RIFLE_3 = 52;
    final static public byte UN_MAN_SHOT_RIFLE_4 = 53;
    final static public byte UN_MAN_SHOT_RIFLE_6 = 54;
    final static public byte UN_MAN_SHOT_RIFLE_7 = 55;
    final static public byte UN_MAN_SHOT_RIFLE_5 = 56;
    final static public byte UN_MAN_SHOT_RIFLE_8 = 57;
    final static public byte UN_MAN_DOWN_RIFLE_1 = 59;
    final static public byte UN_MAN_DOWN_RIFLE_2 = 60;
    final static public byte UN_MAN_DOWN_RIFLE_3 = 61;
    final static public byte UN_MAN_DOWN_RIFLE_4 = 62;
    final static public byte UN_MAN_DOWN_RIFLE_5 = 63;
    final static public byte UN_MAN_DOWN_RIFLE_6 = 64;
    final static public byte UN_MAN_DOWN_RIFLE_7 = 65;
    final static public byte UN_MAN_DOWN_RIFLE_8 = 66;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_1 = 68;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_2 = 69;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_3 = 70;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_4 = 71;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_5 = 72;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_6 = 73;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_7 = 74;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_8 = 75;
    final static public byte UN_MAN_UP_RIFLE_1 = 77;
    final static public byte UN_MAN_UP_RIFLE_2 = 78;
    final static public byte UN_MAN_UP_RIFLE_3 = 79;
    final static public byte UN_MAN_UP_RIFLE_4 = 80;
    final static public byte UN_MAN_UP_RIFLE_5 = 81;
    final static public byte UN_MAN_UP_RIFLE_6 = 82;
    final static public byte UN_MAN_UP_RIFLE_7 = 83;
    final static public byte UN_MAN_UP_RIFLE_8 = 84;
    final static public byte UN_MAN_HIT_RIFLE_1 = 86;
    final static public byte UN_MAN_HIT_RIFLE_2 = 87;
    final static public byte UN_MAN_HIT_RIFLE_3 = 88;
    final static public byte UN_MAN_HIT_RIFLE_4 = 89;
    final static public byte UN_MAN_HIT_RIFLE_5 = 90;
    final static public byte UN_MAN_HIT_RIFLE_6 = 91;
    final static public byte UN_MAN_HIT_RIFLE_7 = 92;
    final static public byte UN_MAN_HIT_RIFLE_8 = 93;
    final static public byte UN_MAN_SHOT_GRENADE_1 = 95;
    final static public byte UN_MAN_SHOT_GRENADE_2 = 96;
    final static public byte UN_MAN_SHOT_GRENADE_3 = 97;
    final static public byte UN_MAN_SHOT_GRENADE_4 = 98;
    final static public byte UN_MAN_SHOT_GRENADE_5 = 99;
    final static public byte UN_MAN_SHOT_GRENADE_6 = 100;
    final static public byte UN_MAN_SHOT_GRENADE_7 = 101;
    final static public byte UN_MAN_SHOT_GRENADE_8 = 102;
    final static public byte UN_MAN_SHOT_FIST_1 = 104;
    final static public byte UN_MAN_SHOT_FIST_2 = 105;
    final static public byte UN_MAN_SHOT_FIST_3 = 106;
    final static public byte UN_MAN_SHOT_FIST_4 = 107;
    final static public byte UN_MAN_SHOT_FIST_5 = 108;
    final static public byte UN_MAN_SHOT_FIST_6 = 109;
    final static public byte UN_MAN_SHOT_FIST_7 = 110;
    final static public byte UN_MAN_SHOT_FIST_8 = 111;
    final static public byte UN_MAN_SHOTDOWN_1 = 113;
    final static public byte UN_MAN_RUN_SHOTGUN_1 = 118;
    final static public byte UN_MAN_RUN_PISTOL_1 = 119;
    final static public byte UN_MAN_RUN_LAUNCHER_1 = 120;
    final static public byte UN_MAN_RUN_FIST_2 = 121;
    final static public byte UN_MAN_RUN_KNIFE_2 = 122;
    final static public byte UN_MAN_RUN_PISTOL_2 = 123;
    final static public byte UN_MAN_RUN_SHOTGUN_2 = 124;
    final static public byte UN_MAN_RUN_LAUNCHER_2 = 125;
    final static public byte UN_MAN_SHOT_PISTOL_2 = 126;
    final static public byte UN_MAN_DOWN_PISTOL_2 = 127;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_2 = 128;
    final static public short UN_MAN_UP_PISTOL_2 = 129;
    final static public short UN_MAN_SHOT_SHOTGUN_2 = 130;
    final static public short UN_MAN_DOWN_SHOTGUN_2 = 131;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_2 = 132;
    final static public short UN_MAN_UP_SHOTGUN_2 = 133;
    final static public short UN_MAN_SHOT_LAUNCHER_2 = 134;
    final static public short UN_MAN_DOWN_LAUNCHER_2 = 135;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_2 = 136;
    final static public short UN_MAN_UP_LAUNCHER_2 = 137;
    final static public short UN_MAN_HIT_FIST_2 = 138;
    final static public short UN_MAN_HIT_PISTOL_2 = 139;
    final static public short UN_MAN_HIT_SHOTGUN_2 = 140;
    final static public short UN_MAN_HIT_LAUNCHER_2 = 141;
    final static public short UN_MAN_RUN_FIST_3 = 143;
    final static public short UN_MAN_RUN_PISTOL_3 = 144;
    final static public short UN_MAN_SHOT_PISTOL_3 = 145;
    final static public short UN_MAN_DOWN_PISTOL_3 = 146;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_3 = 147;
    final static public short UN_MAN_UP_PISTOL_3 = 148;
    final static public short UN_MAN_RUN_SHOTGUN_3 = 149;
    final static public short UN_MAN_SHOT_SHOTGUN_3 = 150;
    final static public short UN_MAN_DOWN_SHOTGUN_3 = 151;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_3 = 152;
    final static public short UN_MAN_UP_SHOTGUN_3 = 153;
    final static public short UN_MAN_RUN_LAUNCHER_3 = 154;
    final static public short UN_MAN_SHOT_LAUNCHER_3 = 155;
    final static public short UN_MAN_DOWN_LAUNCHER_3 = 156;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_3 = 157;
    final static public short UN_MAN_UP_LAUNCHER_3 = 158;
    final static public short UN_MAN_HIT_FIST_3 = 159;
    final static public short UN_MAN_HIT_PISTOL_3 = 160;
    final static public short UN_MAN_HIT_SHOTGUN_3 = 161;
    final static public short UN_MAN_HIT_LAUNCHER_3 = 162;
    final static public short UN_MAN_RUN_FIST_4 = 164;
    final static public short UN_MAN_HIT_FIST_4 = 165;
    final static public short UN_MAN_RUN_PISTOL_4 = 166;
    final static public short UN_MAN_SHOT_PISTOL_4 = 167;
    final static public short UN_MAN_DOWN_PISTOL_4 = 168;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_4 = 169;
    final static public short UN_MAN_UP_PISTOL_4 = 170;
    final static public short UN_MAN_HIT_PISTOL_4 = 171;
    final static public short UN_MAN_RUN_SHOTGUN_4 = 172;
    final static public short UN_MAN_SHOT_SHOTGUN_4 = 173;
    final static public short UN_MAN_DOWN_SHOTGUN_4 = 174;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_4 = 175;
    final static public short UN_MAN_UP_SHOTGUN_4 = 176;
    final static public short UN_MAN_HIT_SHOTGUN_4 = 177;
    final static public short UN_MAN_RUN_LAUNCHER_4 = 178;
    final static public short UN_MAN_SHOT_LAUNCHER_4 = 179;
    final static public short UN_MAN_DOWN_LAUNCHER_4 = 180;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_4 = 181;
    final static public short UN_MAN_UP_LAUNCHER_4 = 182;
    final static public short UN_MAN_HIT_LAUNCHER_4 = 183;
    final static public short UN_MAN_RUN_PISTOL_5 = 185;
    final static public short UN_MAN_SHOT_PISTOL_5 = 186;
    final static public short UN_MAN_DOWN_PISTOL_5_2 = 187;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_5 = 188;
    final static public short UN_MAN_UP_PISTOL_5 = 189;
    final static public short UN_MAN_HIT_PISTOL_5 = 190;
    final static public short UN_MAN_RUN_FIST_5 = 192;
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
    final static public byte Frame_I_ID = 0;//1538
    final static public byte Frame_I_TYPE = 1;//1538
    final static public byte Sprite_index_I_TYPE = 2;//1116
    final static public byte Animation_index_I_TYPE = 3;//1
    final static public byte Unit_index_I_TYPE = 4;//6203
    final static public byte Unit_index_I_H = 5;//6203
    final static public byte Unit_script_index_I_TYPE = 6;//1355
    final static public byte Object_index_I_W = 7;//448
    final static public byte Object_index_I_H = 8;//448
    final static public byte Object_index_I_TYPE = 9;//448
    final static public byte Object_index_I_LAYER_TYPE = 10;//448
    final static public byte Object_index_I_SLOT = 11;//448
    final static public byte Emitter_index_I_W = 12;//0
    final static public byte Emitter_index_I_H = 13;//0
    final static public byte Emitter_index_I_TYPE = 14;//0
    final static public byte Emitter_index_I_SLOT = 15;//0
    final static public byte Affine_I_AF_TR = 16;//160
    final static public byte Affine_I_AF_SC_X = 17;//160
    final static public byte Affine_I_AF_SC_Y = 18;//160
    final static public byte Affine_I_AF_HM = 19;//160
    final static public byte Affine_I_AF_VM = 20;//160
    final static public byte PARAM_BYTE_COUNT = 21;

    final static public byte Frame_I_X = 21;//1538
    final static public byte Frame_I_Y = 22;//1538
    final static public byte Frame_I_W = 23;//1538
    final static public byte Frame_I_H = 24;//1538
    final static public byte Sprite_I_X = 25;//297
    final static public byte Sprite_I_Y = 26;//297
    final static public byte Sprite_index_I_ID = 27;//1116
    final static public byte Sprite_index_I_X = 28;//1116
    final static public byte Sprite_index_I_Y = 29;//1116
    final static public byte Animation_I_X = 30;//1
    final static public byte Animation_I_Y = 31;//1
    final static public byte Animation_index_I_ID = 32;//1
    final static public byte Animation_index_I_X = 33;//1
    final static public byte Animation_index_I_Y = 34;//1
    final static public byte Unit_I_X = 35;//634
    final static public byte Unit_I_Y = 36;//634
    final static public byte Unit_index_I_ID = 37;//6203
    final static public byte Unit_index_I_X = 38;//6203
    final static public byte Unit_index_I_Y = 39;//6203
    final static public byte Unit_script_I_X = 40;//193
    final static public byte Unit_script_I_Y = 41;//193
    final static public byte Unit_script_index_I_ID = 42;//1355
    final static public byte Unit_script_index_I_X = 43;//1355
    final static public byte Unit_script_index_I_Y = 44;//1355
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
    final static public byte Affine_I_X = 55;//160
    final static public byte Affine_I_Y = 56;//160
    final static public byte Affine_I_AF_RO = 57;//160
    final static public byte Affine_index_I_ID = 58;//160
    final static public byte Affine_index_I_X = 59;//160
    final static public byte Affine_index_I_Y = 60;//160
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
    final static public int PARAM_MAX_SIZE = 6203;//getCount of maximum array

//structure of packeting images
     final static byte[] IMG_INDEX = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    static public int getIMG_INDEX(int i) {
        return IMG_INDEX[i];
    };
     final static int[] IMG_LEN = {197120,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    public Graph() {
/*ObjC uncomment*///return self;
    }

}
