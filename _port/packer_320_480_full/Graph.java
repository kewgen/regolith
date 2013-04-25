//../regolith/_port/packer_320_480_full
//Tue Apr 23 18:38:13 GMT+04:00 2013 , version: 108

package app;

public class Graph {


    final static public byte IMG_COUNT = 31;
    final static public byte ELEMENT_LINE = 31;
    final static public byte ELEMENT_RECT = 32;
    final static public byte ELEMENT_FILLRECT = 33;
    final static public byte ELEMENT_FILLRECT_x4 = 34;
    final static public byte ELEMENT_ARC = 35;
    final static public int FRAMES_COUNT = 1622;
    final static public int SPR_COUNT = 314;
    final static public int ANIM_COUNT = 1;
    final static public int UNIT_COUNT = 660;
    final static public int UNITS_COUNT = 181;
    final static public int AFFINES_COUNT = 172;
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
    final static public byte MAN_1_DIR_2_BODY = 15;
    final static public byte MAN_1_DIR_3_BODY = 25;
    final static public byte MAN_1_DIR_4_BODY = 36;
    final static public byte MAN_1_DIR_5_BODY = 46;
    final static public byte MAN_1_DIR_6_BODY = 58;
    final static public byte MAN_1_DIR_7_BODY = 69;
    final static public byte MAN_DIR_8_BODY = 80;
    final static public byte MAN_1_DIR_1_HEAD = 90;
    final static public byte MAN_1_DIR_2_HEAD = 95;
    final static public byte MAN_1_DIR_3_HEAD = 99;
    final static public byte MAN_1_DIR_4_HEAD = 105;
    final static public byte MAN_1_DIR_5_HEAD = 109;
    final static public byte MAN_1_DIR_6_HEAD = 115;
    final static public byte MAN_1_DIR_7_HEAD = 119;
    final static public byte MAN_1_DIR_8_HEAD = 125;
    final static public short MAN_1_DIR_1_FOOT = 129;
    final static public short MAN_1_DIR_2_FOOT = 178;
    final static public short MAN_1_DIR_3_FOOT = 231;
    final static public short MAN_1_DIR_4_FOOT = 292;
    final static public short MAN_1_DIR_5_FOOT = 331;
    final static public short MAN_1_DIR_6_FOOT = 383;
    final static public short MAN_1_DIR_7_FOOT = 429;
    final static public short MAN_1_DIR_8_FOOT = 489;
    final static public short MAN_1_DIR_1_ARM = 532;
    final static public short MAN_1_DIR_2_ARM = 587;
    final static public short MAN_1_DIR_3_ARM = 674;
    final static public short MAN_1_DIR_4_ARM = 750;
    final static public short MAN_1_DIR_5_ARM = 821;
    final static public short MAN_1_DIR_6_ARM = 909;
    final static public short MAN_1_DIR_7_ARM = 967;
    final static public short MAN_1_DIR_8_ARM = 1031;
    final static public short MAN_1_DIR_1_WEAPON = 1072;
    final static public short MAN_1_DIR_2_WEAPON = 1088;
    final static public short MAN_1_DIR_3_WEAPON = 1100;
    final static public short MAN_1_DIR_4_WEAPON = 1119;
    final static public short MAN_1_DIR_5_WEAPON = 1134;
    final static public short MAN_1_DIR_6_WEAPON = 1159;
    final static public short MAN_1_DIR_7_WEAPON = 1174;
    final static public short MAN_1_DIR_8_WEAPON = 1180;
    final static public short EL_HINT = 1485;
    final static public short EL_FONT = 1494;
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
    final static public byte SPR_TEAM_COLOR = 78;
    final static public byte SPR_SKILL_ICO = 85;
    final static public byte SPR_CHARACTER_IND = 93;
    final static public byte SPR_SKILL_IND = 104;
    final static public byte SPR_ICO_LIFE_IND = 125;
    final static public short SPR_BUTTON = 133;
    final static public short SPR_ICO_WEAPON = 199;
    final static public short SPR_TEXT_TEST = 213;
    final static public short SPR_FONT_SYMB = 215;
    final static public short SPR_FONT_NUM = 231;
    final static public short SPR_FONT_EN = 248;
    final static public short SPR_FONT_RU = 280;
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
    final static public byte U_MAN_RUN_SHOTGUN_DIR_1 = 69;
    final static public byte U_MAN_RUN_PISTOL_DIR_1 = 80;
    final static public byte U_MAN_RUN_LAUNCHER_DIR_1 = 91;
    final static public byte U_MAN_STAY_KNIFE_2 = 102;
    final static public byte U_MAN_STAY_FIST_2 = 105;
    final static public byte U_MAN_HIT_FIST_2 = 110;
    final static public byte U_MAN_STAY_PISTOL_2 = 113;
    final static public byte U_MAN_HIT_PISTOL_2 = 118;
    final static public byte U_MAN_DOWN_PISTOL_2 = 121;
    final static public short U_MAN_STAY_RIFLE_2 = 128;
    final static public short U_MAN_HIT_RIFLE_2 = 134;
    final static public short U_MAN_DOWN_RIFLE_2 = 138;
    final static public short U_MAN_STAY_SHOTGUN_2 = 145;
    final static public short U_MAN_HIT_SHOTGUN_2 = 150;
    final static public short U_MAN_DOWN_SHOTGUN_2 = 153;
    final static public short U_MAN_HIT_LAUNCHER_2 = 160;
    final static public short U_MAN_STAY_LAUNCHER_2 = 163;
    final static public short U_MAN_DOWN_LAUNCHER_2 = 172;
    final static public short U_MAN_STAY_GRENADE_2 = 181;
    final static public short U_MAN_RUN_FIST_2 = 191;
    final static public short U_MAN_RUN_KNIFE_2 = 199;
    final static public short U_MAN_RUN_PISTOL_2 = 200;
    final static public short U_MAN_RUN_RIFLE_2 = 208;
    final static public short U_MAN_RUN_SHOTGUN_2 = 218;
    final static public short U_MAN_STAY_LAUNCHER_3 = 226;
    final static public short U_MAN_RUN_FIST_3 = 236;
    final static public short U_MAN_SHOT_FIST_3 = 244;
    final static public short U_MAN_HIT_FIST_3 = 249;
    final static public short U_MAN_RUN_PISTOL_3 = 252;
    final static public short U_MAN_SHOT_PISTOL_3 = 260;
    final static public short U_MAN_DOWN_PISTOL_3 = 265;
    final static public short U_MAN_HIT_PISTOL_3 = 271;
    final static public short U_MAN_RUN_RIFLE_3 = 274;
    final static public short U_MAN_SHOT_RIFLE_3 = 284;
    final static public short U_MAN_HIT_RIFLE_3 = 289;
    final static public short U_MAN_SHOTDOWN_3 = 291;
    final static public short U_MAN_DOWN_RIFLE_3 = 298;
    final static public short U_MAN_RUN_SHOTGUN_3 = 303;
    final static public short U_MAN_SHOT_SHOTGUN_3 = 311;
    final static public short U_MAN_DOWN_SHOTGUN_3 = 315;
    final static public short U_MAN_HIT_SHOTGUN_3 = 320;
    final static public short U_MAN_RUN_LAUNCHER_3 = 323;
    final static public short U_MAN_SHOT_LAUNCHER_3 = 331;
    final static public short U_MAN_DOWN_LAUNCHER_3 = 336;
    final static public short U_MAN_HIT_LAUNCHER_3 = 342;
    final static public short U_MAN_SHOT_GRENAD_3 = 345;
    final static public short U_MAN_RUN_FIST_DIR_4 = 352;
    final static public short U_MAN_SHOT_FIST_DIR_4 = 360;
    final static public short U_MAN_HIT_FIST_4 = 364;
    final static public short U_MAN_HIT_RIFLE_DIR_4 = 365;
    final static public short U_MAN_RUN_RIFLE_DIR_4 = 369;
    final static public short U_MAN_SHOT_RIFLE_4 = 379;
    final static public short U_MAN_DOWN_RIFLE_4 = 383;
    final static public short U_MAN_HIT_RIFLE_4 = 388;
    final static public short U_MAN_RUN_PISTOL_4 = 392;
    final static public short U_MAN_SHOT_PISTOL_4 = 400;
    final static public short U_MAN_DOWN_PISTOL_4 = 405;
    final static public short U_MAN_HIT_PISTOL_4 = 411;
    final static public short U_MAN_RUN_SHOTGUN_4 = 417;
    final static public short U_MAN_SHOT_SHOTGUN_4 = 425;
    final static public short U_MAN_DOWN_SHOTGUN_4 = 429;
    final static public short U_MAN_HIT_SHOTGUN_4 = 434;
    final static public short U_MAN_RUN_LAUNCHER_4 = 437;
    final static public short U_MAN_SHOT_LAUNCHER_4 = 445;
    final static public short U_MAN_DOWN_LAUNCHER_4 = 450;
    final static public short U_MAN_HIT_LAUNCHER_4 = 456;
    final static public short U_MAN_SHOT_GRENADE_4 = 459;
    final static public short U_MAN_RUN_RIFLE_DIR_5 = 466;
    final static public short U_MAN_SHOT_RIFLE_DIR_5 = 476;
    final static public short U_MAN_STAY_FIST_DIR_5 = 480;
    final static public short U_MAN_DOWN_RIFLE_DIR_5 = 484;
    final static public short U_MAN_HIT_RIFLE_DIR_5 = 489;
    final static public short U_MAN_SHOTDOWN_DIR_5 = 492;
    final static public short U_MAN_RUN_PISTOL_DIR_5 = 499;
    final static public short U_MAN_SHOT_PISTOL_DIR_5 = 507;
    final static public short U_MAN_DOWN_PISTOL_DIR_5 = 512;
    final static public short U_MAN_HIT_PISTOL_DIR_5 = 519;
    final static public short U_MAN_SHOT_GRENADE_DIR_5 = 522;
    final static public short U_MAN_RUN_RIFLE_DIR_6 = 529;
    final static public short U_MAN_SHOT_RIFLE_DIR_6 = 539;
    final static public short U_MAN_HIT_RIFLE_DIR_6 = 543;
    final static public short U_MAN_DOWN_RIFLE_DIR_6 = 546;
    final static public short U_MAN_SHOT_GRENADE_DIR_6 = 551;
    final static public short U_MAN_STAY_FIST_DIR_6 = 557;
    final static public short U_MAN_RUN_RIFLE_DIR_7 = 563;
    final static public short U_MAN_SHOT_RIFLE_DIR_7 = 573;
    final static public short U_MAN_HIT_RIFLE_DIR_7 = 577;
    final static public short U_MAN_DOWN_RIFLE_7 = 580;
    final static public short U_MAN_SHOOTDOWN_DIR_7 = 585;
    final static public short U_MAN_SHOT_GRENAD_DIR_7 = 592;
    final static public short U_MAN_STAY_FIST_DIR_7 = 598;
    final static public short U_MAN_RUN_RIFLE_DIR_8 = 603;
    final static public short U_MAN_SHOT_RIFLE_DIR_8 = 613;
    final static public short U_MAN_HIT_RIFLE_DIR_8 = 617;
    final static public short U_MAN_DOWN_RIFLE_DIR_8 = 620;
    final static public short U_MAN_SHOT_GRENADE_DIR_8 = 625;
    final static public short U_MAN_STAY_FIST_DIR_8 = 631;
    final static public short U_MAN_RUN_DAGGER_1 = 636;
    final static public short U_MAN_SHOOT_PISTOL = 639;
    final static public short U_MAN_SHOOT_SHOTGUN = 642;
    final static public short U_MAN_HIT_5 = 648;
    final static public short U_MAN_GRENADE = 650;
//Unit script
    final static public byte UN_MAN_STAY_RIFLE_1 = 0;
    final static public byte UN_MAN_STAY_RIFLE_2 = 1;
    final static public byte UN_MAN_STAY_RIFLE_3 = 2;
    final static public byte UN_MAN_STAY_RIFLE_4 = 3;
    final static public byte UN_MAN_STAY_RIFLE_5 = 4;
    final static public byte UN_MAN_STAY_RIFLE_6 = 5;
    final static public byte UN_MAN_STAY_RIFLE_7 = 6;
    final static public byte UN_MAN_STAY_RIFLE_8 = 7;
    final static public byte UN_MAN_RUN_RIFLE_1 = 8;
    final static public byte UN_MAN_RUN_RIFLE_2 = 9;
    final static public byte UN_MAN_RUN_RIFLE_3 = 10;
    final static public byte UN_MAN_RUN_RIFLE_4 = 11;
    final static public byte UN_MAN_RUN_RIFLE_5 = 12;
    final static public byte UN_MAN_RUN_RIFLE_6 = 13;
    final static public byte UN_MAN_RUN_RIFLE_7 = 14;
    final static public byte UN_MAN_RUN_RIFLE_8 = 15;
    final static public byte UN_MAN_SHOT_RIFLE_1 = 16;
    final static public byte UN_MAN_SHOT_RIFLE_2 = 17;
    final static public byte UN_MAN_SHOT_RIFLE_3 = 18;
    final static public byte UN_MAN_SHOT_RIFLE_4 = 19;
    final static public byte UN_MAN_SHOT_RIFLE_5 = 20;
    final static public byte UN_MAN_SHOT_RIFLE_6 = 21;
    final static public byte UN_MAN_SHOT_RIFLE_7 = 22;
    final static public byte UN_MAN_SHOT_RIFLE_8 = 23;
    final static public byte UN_MAN_DOWN_RIFLE_1 = 24;
    final static public byte UN_MAN_DOWN_RIFLE_2 = 25;
    final static public byte UN_MAN_DOWN_RIFLE_3 = 26;
    final static public byte UN_MAN_DOWN_RIFLE_4 = 27;
    final static public byte UN_MAN_DOWN_RIFLE_5 = 28;
    final static public byte UN_MAN_DOWN_RIFLE_6 = 29;
    final static public byte UN_MAN_DOWN_RIFLE_7 = 30;
    final static public byte UN_MAN_DOWN_RIFLE_8 = 31;
    final static public byte UN_MAN_SITDOWN_RIFLE_1 = 32;
    final static public byte UN_MAN_SITDOWN_RIFLE_2 = 33;
    final static public byte UN_MAN_SITDOWN_RIFLE_3 = 34;
    final static public byte UN_MAN_SITDOWN_RIFLE_4 = 35;
    final static public byte UN_MAN_SITDOWN_RIFLE_5 = 36;
    final static public byte UN_MAN_SITDOWN_RIFLE_6 = 37;
    final static public byte UN_MAN_SITDOWN_RIFLE_7 = 38;
    final static public byte UN_MAN_SITDOWN_RIFLE_8 = 39;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_1 = 40;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_2 = 41;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_3 = 42;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_4 = 43;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_5 = 44;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_6 = 45;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_7 = 46;
    final static public byte UN_MAN_DOWN_SHOT_RIFLE_8 = 47;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_1 = 48;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_2 = 49;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_3 = 50;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_4 = 51;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_5 = 52;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_6 = 53;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_7 = 54;
    final static public byte UN_MAN_DOWN_HIT_RIFLE_8 = 55;
    final static public byte UN_MAN_UP_RIFLE_1 = 56;
    final static public byte UN_MAN_UP_RIFLE_2 = 57;
    final static public byte UN_MAN_UP_RIFLE_3 = 58;
    final static public byte UN_MAN_UP_RIFLE_4 = 59;
    final static public byte UN_MAN_UP_RIFLE_5 = 60;
    final static public byte UN_MAN_UP_RIFLE_6 = 61;
    final static public byte UN_MAN_UP_RIFLE_7 = 62;
    final static public byte UN_MAN_UP_RIFLE_8 = 63;
    final static public byte UN_MAN_HIT_RIFLE_1 = 64;
    final static public byte UN_MAN_HIT_RIFLE_2 = 65;
    final static public byte UN_MAN_HIT_RIFLE_3 = 66;
    final static public byte UN_MAN_HIT_RIFLE_4 = 67;
    final static public byte UN_MAN_HIT_RIFLE_5 = 68;
    final static public byte UN_MAN_HIT_RIFLE_6 = 69;
    final static public byte UN_MAN_HIT_RIFLE_7 = 70;
    final static public byte UN_MAN_HIT_RIFLE_8 = 71;
    final static public byte UN_MAN_SHOTDOWN_1 = 72;
    final static public byte UN_MAN_SHOTDOWN_2 = 73;
    final static public byte UN_MAN_SHOTDOWN_3 = 74;
    final static public byte UN_MAN_SHOTDOWN_4 = 75;
    final static public byte UN_MAN_SHOTDOWN_5 = 76;
    final static public byte UN_MAN_SHOTDOWN_6 = 77;
    final static public byte UN_MAN_SHOTDOWN_7 = 78;
    final static public byte UN_MAN_SHOTDOWN_8 = 79;
    final static public byte UN_MAN_RIP_1 = 80;
    final static public byte UN_MAN_RIP_2 = 81;
    final static public byte UN_MAN_RIP_3 = 82;
    final static public byte UN_MAN_RIP_4 = 83;
    final static public byte UN_MAN_RIP_5 = 84;
    final static public byte UN_MAN_RIP_6 = 85;
    final static public byte UN_MAN_RIP_7 = 86;
    final static public byte UN_MAN_RIP_8 = 87;
    final static public byte UN_MAN_SHOT_FIST_1 = 88;
    final static public byte UN_MAN_SHOT_FIST_2 = 89;
    final static public byte UN_MAN_SHOT_FIST_3 = 90;
    final static public byte UN_MAN_SHOT_FIST_4 = 91;
    final static public byte UN_MAN_SHOT_FIST_5 = 92;
    final static public byte UN_MAN_SHOT_FIST_6 = 93;
    final static public byte UN_MAN_SHOT_FIST_7 = 94;
    final static public byte UN_MAN_SHOT_FIST_8 = 95;
    final static public byte UN_MAN_SHOT_GRENADE_1 = 96;
    final static public byte UN_MAN_SHOT_GRENADE_2 = 97;
    final static public byte UN_MAN_SHOT_GRENADE_3 = 98;
    final static public byte UN_MAN_SHOT_GRENADE_4 = 99;
    final static public byte UN_MAN_SHOT_GRENADE_5 = 100;
    final static public byte UN_MAN_SHOT_GRENADE_6 = 101;
    final static public byte UN_MAN_SHOT_GRENADE_7 = 102;
    final static public byte UN_MAN_SHOT_GRENADE_8 = 103;
    final static public byte UN_MAN_RUN_SHOTGUN_1 = 105;
    final static public byte UN_MAN_RUN_PISTOL_1 = 106;
    final static public byte UN_MAN_RUN_LAUNCHER_1 = 107;
    final static public byte UN_MAN_RUN_FIST_2 = 108;
    final static public byte UN_MAN_RUN_KNIFE_2 = 109;
    final static public byte UN_MAN_RUN_PISTOL_2 = 110;
    final static public byte UN_MAN_RUN_SHOTGUN_2 = 111;
    final static public byte UN_MAN_RUN_LAUNCHER_2 = 112;
    final static public byte UN_MAN_SHOT_PISTOL_2 = 113;
    final static public byte UN_MAN_DOWN_PISTOL_2 = 114;
    final static public byte UN_MAN_DOWN_SHOT_PISTOL_2 = 115;
    final static public byte UN_MAN_UP_PISTOL_2 = 116;
    final static public byte UN_MAN_SHOT_SHOTGUN_2 = 117;
    final static public byte UN_MAN_DOWN_SHOTGUN_2 = 118;
    final static public byte UN_MAN_DOWN_SHOT_SHOTGUN_2 = 119;
    final static public byte UN_MAN_UP_SHOTGUN_2 = 120;
    final static public byte UN_MAN_SHOT_LAUNCHER_2 = 121;
    final static public byte UN_MAN_DOWN_LAUNCHER_2 = 122;
    final static public byte UN_MAN_DOWN_SHOT_LAUNCHER_2 = 123;
    final static public byte UN_MAN_UP_LAUNCHER_2 = 124;
    final static public byte UN_MAN_HIT_FIST_2 = 125;
    final static public byte UN_MAN_HIT_PISTOL_2 = 126;
    final static public byte UN_MAN_HIT_SHOTGUN_2 = 127;
    final static public short UN_MAN_HIT_LAUNCHER_2 = 128;
    final static public short UN_MAN_RUN_FIST_3 = 130;
    final static public short UN_MAN_RUN_PISTOL_3 = 131;
    final static public short UN_MAN_SHOT_PISTOL_3 = 132;
    final static public short UN_MAN_DOWN_PISTOL_3 = 133;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_3 = 134;
    final static public short UN_MAN_UP_PISTOL_3 = 135;
    final static public short UN_MAN_RUN_SHOTGUN_3 = 136;
    final static public short UN_MAN_SHOT_SHOTGUN_3 = 137;
    final static public short UN_MAN_DOWN_SHOTGUN_3 = 138;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_3 = 139;
    final static public short UN_MAN_UP_SHOTGUN_3 = 140;
    final static public short UN_MAN_RUN_LAUNCHER_3 = 141;
    final static public short UN_MAN_SHOT_LAUNCHER_3 = 142;
    final static public short UN_MAN_DOWN_LAUNCHER_3 = 143;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_3 = 144;
    final static public short UN_MAN_UP_LAUNCHER_3 = 145;
    final static public short UN_MAN_HIT_FIST_3 = 146;
    final static public short UN_MAN_HIT_PISTOL_3 = 147;
    final static public short UN_MAN_HIT_SHOTGUN_3 = 148;
    final static public short UN_MAN_HIT_LAUNCHER_3 = 149;
    final static public short UN_MAN_RUN_FIST_4 = 151;
    final static public short UN_MAN_HIT_FIST_4 = 152;
    final static public short UN_MAN_RUN_PISTOL_4 = 153;
    final static public short UN_MAN_SHOT_PISTOL_4 = 154;
    final static public short UN_MAN_DOWN_PISTOL_4 = 155;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_4 = 156;
    final static public short UN_MAN_UP_PISTOL_4 = 157;
    final static public short UN_MAN_HIT_PISTOL_4 = 158;
    final static public short UN_MAN_RUN_SHOTGUN_4 = 159;
    final static public short UN_MAN_SHOT_SHOTGUN_4 = 160;
    final static public short UN_MAN_DOWN_SHOTGUN_4 = 161;
    final static public short UN_MAN_DOWN_SHOT_SHOTGUN_4 = 162;
    final static public short UN_MAN_UP_SHOTGUN_4 = 163;
    final static public short UN_MAN_HIT_SHOTGUN_4 = 164;
    final static public short UN_MAN_RUN_LAUNCHER_4 = 165;
    final static public short UN_MAN_SHOT_LAUNCHER_4 = 166;
    final static public short UN_MAN_DOWN_LAUNCHER_4 = 167;
    final static public short UN_MAN_DOWN_SHOT_LAUNCHER_4 = 168;
    final static public short UN_MAN_UP_LAUNCHER_4 = 169;
    final static public short UN_MAN_HIT_LAUNCHER_4 = 170;
    final static public short UN_MAN_RUN_PISTOL_5 = 172;
    final static public short UN_MAN_SHOT_PISTOL_5 = 173;
    final static public short UN_MAN_DOWN_PISTOL_5_2 = 174;
    final static public short UN_MAN_DOWN_SHOT_PISTOL_5 = 175;
    final static public short UN_MAN_UP_PISTOL_5 = 176;
    final static public short UN_MAN_HIT_PISTOL_5 = 177;
    final static public short UN_MAN_RUN_RIFLE_2_len = 179;
    final static public short UN_MAN_RUN_FIST_5 = 180;
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
    final static public byte PAN_WAITING_SERVER = 19;
    final static public byte PAN_BAT_MENU = 20;
    final static public byte PAN_BAT_WEAPON = 21;
    final static public byte PAN_BAT_SEL_FIGHTER = 22;
    final static public byte PAN_BAT_FIGHTER = 23;
    final static public byte PAN_BAT_FIRE = 24;
    final static public byte OBJ_SUBPANEL_WEAPON_LEFT = 25;
    final static public byte OBJ_LIST_WEAPON_ITEM = 26;
    final static public byte OBJ_LIST_BATTLE_ITEM = 27;
    final static public byte OBJ_LIST_WEAPON = 28;
    final static public byte OBJ_LIST_BATTLE = 29;
    final static public byte OBJ_LIST_FIGHTER = 30;
    final static public byte OBJ_LIST_REWARD = 31;
    final static public byte OBJ_LIST_FIGHTER_FACE = 32;
    final static public byte OBJ_LIST_MAP = 33;
    final static public byte OBJ_REWARD_AVA_BIG = 34;
    final static public byte OBJ_PLAYER_AVA_BIG = 35;
    final static public byte OBJ_PLAYER_AVA_SMALL = 36;
    final static public byte OBJ_MAP_MINI = 37;
    final static public byte OBJ_FIGHTER_SMALL = 38;
    final static public byte OBJ_BAT_FIGHTER = 39;
    final static public byte OBJ_SIDE = 40;
    final static public byte OBJ_BATTLE = 44;
    final static public byte OBJ_IND_HEALTH = 45;
    final static public byte OBJ_IND_1 = 46;
    final static public byte OBJ_IND_CHARACTER = 47;
    final static public byte OBJ_IND_SKILL = 48;
    final static public byte OBJ_LABEL_MONEY = 49;
    final static public byte OBJ_LABEL_PLAYER_INFO = 50;
    final static public byte OBJ_BUT_LATER = 51;
    final static public byte OBJ_BUT_PREVISION = 52;
    final static public byte OBJ_BUT_BACK = 53;
    final static public byte OBJ_BUT_RADIO = 54;
    final static public byte OBJ_BUT = 64;
    final static public byte OBJ_BUT_SELECT = 65;
    final static public byte OBJ_BUT_BATTLE = 67;
    final static public byte OBJ_BUT_KICK = 69;
    final static public byte OBJ_BUT_PLAYER_INFO = 74;
    final static public byte OBJ_BUT_WIEW = 76;
    final static public byte OBJ_BUT_BATTLE_MENU = 78;
    final static public byte OBJ_CHECKBOX = 85;
    final static public byte OBJ_CHECKBOX_STRING = 86;
    final static public byte OBJ_BUT_PLUS = 87;
    final static public byte OBJ_SPINBOX = 89;
    final static public byte OBJ_HINT = 90;
    final static public byte MENU_COMMON = 91;
    final static public byte MENU_COMMON_ITEM = 92;
    final static public byte MENU_COMMON_CONFIRM = 93;
    final static public byte MENU_BOTTON_YES = 94;
    final static public byte MENU_BOTTON_NO = 95;
    final static public byte MENU_BOTTON_MENU = 96;
    final static public byte MENU_TEST = 97;
    final static public byte MENU_TEST_2 = 98;
    final static public byte OBJ_PANEL_TOP_TEST = 99;
//Location
//Map
//PColor
//GAI
//Mission
     final static int[] COLOR = {0, 65535, };
    static public int getCOLOR(int i){return COLOR[i];};

//Links
    final static public byte Frame_I_ID = 0;//1622
    final static public byte Frame_I_TYPE = 1;//1622
    final static public byte Sprite_index_I_TYPE = 2;//1163
    final static public byte Animation_index_I_TYPE = 3;//1
    final static public byte Unit_index_I_TYPE = 4;//6455
    final static public byte Unit_index_I_H = 5;//6455
    final static public byte Unit_script_index_I_TYPE = 6;//1456
    final static public byte Object_index_I_W = 7;//505
    final static public byte Object_index_I_H = 8;//505
    final static public byte Object_index_I_TYPE = 9;//505
    final static public byte Object_index_I_LAYER_TYPE = 10;//505
    final static public byte Object_index_I_SLOT = 11;//505
    final static public byte Emitter_index_I_W = 12;//0
    final static public byte Emitter_index_I_H = 13;//0
    final static public byte Emitter_index_I_TYPE = 14;//0
    final static public byte Emitter_index_I_SLOT = 15;//0
    final static public byte Affine_I_AF_TR = 16;//172
    final static public byte Affine_I_AF_SC_X = 17;//172
    final static public byte Affine_I_AF_SC_Y = 18;//172
    final static public byte Affine_I_AF_HM = 19;//172
    final static public byte Affine_I_AF_VM = 20;//172
    final static public byte PARAM_BYTE_COUNT = 21;

    final static public byte Frame_I_X = 21;//1622
    final static public byte Frame_I_Y = 22;//1622
    final static public byte Frame_I_W = 23;//1622
    final static public byte Frame_I_H = 24;//1622
    final static public byte Sprite_I_X = 25;//314
    final static public byte Sprite_I_Y = 26;//314
    final static public byte Sprite_index_I_ID = 27;//1163
    final static public byte Sprite_index_I_X = 28;//1163
    final static public byte Sprite_index_I_Y = 29;//1163
    final static public byte Animation_I_X = 30;//1
    final static public byte Animation_I_Y = 31;//1
    final static public byte Animation_index_I_ID = 32;//1
    final static public byte Animation_index_I_X = 33;//1
    final static public byte Animation_index_I_Y = 34;//1
    final static public byte Unit_I_X = 35;//660
    final static public byte Unit_I_Y = 36;//660
    final static public byte Unit_index_I_ID = 37;//6455
    final static public byte Unit_index_I_X = 38;//6455
    final static public byte Unit_index_I_Y = 39;//6455
    final static public byte Unit_script_I_X = 40;//181
    final static public byte Unit_script_I_Y = 41;//181
    final static public byte Unit_script_index_I_ID = 42;//1456
    final static public byte Unit_script_index_I_X = 43;//1456
    final static public byte Unit_script_index_I_Y = 44;//1456
    final static public byte Object_I_X = 45;//100
    final static public byte Object_I_Y = 46;//100
    final static public byte Object_index_I_ID = 47;//505
    final static public byte Object_index_I_X = 48;//505
    final static public byte Object_index_I_Y = 49;//505
    final static public byte Emitter_I_X = 50;//0
    final static public byte Emitter_I_Y = 51;//0
    final static public byte Emitter_index_I_ID = 52;//0
    final static public byte Emitter_index_I_X = 53;//0
    final static public byte Emitter_index_I_Y = 54;//0
    final static public byte Affine_I_X = 55;//172
    final static public byte Affine_I_Y = 56;//172
    final static public byte Affine_I_AF_RO = 57;//172
    final static public byte Affine_index_I_ID = 58;//172
    final static public byte Affine_index_I_X = 59;//172
    final static public byte Affine_index_I_Y = 60;//172
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
    final static public int PARAM_MAX_SIZE = 6455;//getCount of maximum array

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
