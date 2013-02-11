package com.geargames.regolith.app;

import com.geargames.common.packer.PManager;

/**
 * User: mikhail v. kutuzov
 * Date: 08.10.12
 * Time: 14:09
 */
public class Render extends PManager {

    public void create() {
        FR_IMAGE_ID = Graph.Frame_I_ID;
        FR_X = Graph.Frame_I_X;
        FR_Y = Graph.Frame_I_Y;
        FR_W = Graph.Frame_I_W;
        FR_H = Graph.Frame_I_H;
        FR_COLOR = Graph.Frame_I_TYPE;//color записан только для геометрических фигур, для остальных побитно афинны
        FR_TRANSFORM = Graph.Frame_I_TYPE;

        SP_S = Graph.Sprite_I_X;
        SP_E = Graph.Sprite_I_Y;

        SP_I_ID = Graph.Sprite_index_I_ID;
        SP_I_X = Graph.Sprite_index_I_X;
        SP_I_Y = Graph.Sprite_index_I_Y;
        SP_I_TYPE = Graph.Sprite_index_I_TYPE;

        AN_S = Graph.Animation_I_X;
        AN_E = Graph.Animation_I_Y;

        AN_I_ID = Graph.Animation_index_I_ID;
        AN_I_X = Graph.Animation_index_I_X;
        AN_I_Y = Graph.Animation_index_I_Y;
        AN_I_TYPE = Graph.Animation_index_I_TYPE;

        UN_S = Graph.Unit_I_X;
        UN_E = Graph.Unit_I_Y;

        UN_I_ID = Graph.Unit_index_I_ID;
        UN_I_X = Graph.Unit_index_I_X;
        UN_I_Y = Graph.Unit_index_I_Y;
        UN_I_TYPE = Graph.Unit_index_I_TYPE;
        UN_I_BODY_TYLE = Graph.Unit_index_I_H;

        UNS_S = Graph.Unit_script_I_X;
        UNS_E = Graph.Unit_script_I_Y;
        UNS_BODY_TYPE = Graph.Unit_index_I_H;

        UNS_I_ID = Graph.Unit_script_index_I_ID;
        UNS_I_X = Graph.Unit_script_index_I_X;
        UNS_I_Y = Graph.Unit_script_index_I_Y;
        UNS_I_TYPE = Graph.Unit_script_index_I_TYPE;

        OBJ_S = Graph.Object_I_X;
        OBJ_E = Graph.Object_I_Y;

        OBJ_I_ID = Graph.Object_index_I_ID;
        OBJ_I_X = Graph.Object_index_I_X;
        OBJ_I_Y = Graph.Object_index_I_Y;
        OBJ_I_SHIFT = Graph.Object_index_I_W;
        OBJ_I_YY = Graph.Object_index_I_H;
        OBJ_I_TYPE = Graph.Object_index_I_TYPE;
        OBJ_I_LAYER_TYPE = Graph.Object_index_I_LAYER_TYPE;//static,dynamic,slot,exit,enter,pass
        OBJ_I_SLOT = Graph.Object_index_I_SLOT;

        AF_S = Graph.Affine_I_X;
        AF_E = Graph.Affine_I_Y;
        AffineTransparency = Graph.Affine_I_AF_TR;
        AffineScalingX = Graph.Affine_I_AF_SC_X;
        AffineScalingY = Graph.Affine_I_AF_SC_Y;
        AffineRotate = Graph.Affine_I_AF_RO;
        AffineHMirror = Graph.Affine_I_AF_HM;
        AffineVMirror = Graph.Affine_I_AF_VM;

        AF_I_ID = Graph.Affine_index_I_ID;
        AF_I_X = Graph.Affine_index_I_X;
        AF_I_Y = Graph.Affine_index_I_Y;

        EM_S = Graph.Emitter_I_X;//указатель на первый индекс
        EM_E = Graph.Emitter_I_Y;//указатель на последний индекс
        EM_I_ID = Graph.Emitter_index_I_ID;
        EM_I_TYPE = Graph.Emitter_index_I_TYPE;
        //TODO определится со значением
        //com.geargames.common.packer.Render.EM_I_LAYER = Graph.Emitter_index_I_LAYER_TYPE;
        EM_I_SLOT = Graph.Emitter_index_I_SLOT;
        EM_I_SHIFT = Graph.Emitter_index_I_W;
        EM_I_X = Graph.Emitter_index_I_X;
        EM_I_Y = Graph.Emitter_index_I_Y;
        EM_P0 = Graph.Emitter_I_EM0;//всего 20 параметров
        EM_P1 = Graph.Emitter_I_EM1;
        EM_P2 = Graph.Emitter_I_EM2;
        EM_P3 = Graph.Emitter_I_EM3;
        EM_P4 = Graph.Emitter_I_EM4;
        EM_P5 = Graph.Emitter_I_EM5;
        EM_P6 = Graph.Emitter_I_EM6;
        EM_P7 = Graph.Emitter_I_EM7;
        EM_P8 = Graph.Emitter_I_EM8;
        EM_P9 = Graph.Emitter_I_EM9;
        EM_P10 = Graph.Emitter_I_EM10;
        EM_P11 = Graph.Emitter_I_EM11;
        EM_P12 = Graph.Emitter_I_EM12;
        EM_P13 = Graph.Emitter_I_EM13;
        EM_P14 = Graph.Emitter_I_EM14;
        EM_P15 = Graph.Emitter_I_EM15;
        EM_P16 = Graph.Emitter_I_EM16;
        EM_P17 = Graph.Emitter_I_EM17;
        EM_P18 = Graph.Emitter_I_EM18;
        EM_P19 = Graph.Emitter_I_EM19;

        PARAM_BYTE_COUNT = Graph.PARAM_BYTE_COUNT;
        PARAM_SHORT_COUNT = Graph.PARAM_SHORT_COUNT;
        PARAM_INT_COUNT = Graph.PARAM_INT_COUNT;

        IMG_COUNT = Graph.IMG_COUNT;
    }

}
