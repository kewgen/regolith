package com.geargames.regolith.localization;

import com.geargames.common.String;

/**
 * Список строковых констант.
 * User: abarakov
 * Date: 04.03.13
 */
// StrConsts
public class LocalizedStrings {

    // ----- Common ----------------------------------------------------------------------------------------------------

    // STR_BUTTON_OK
    public static final String BUTTON_OK     = String.valueOfC("ОК");
    public static final String BUTTON_CANCEL = String.valueOfC("ОТМЕНА");

    // ----- Battle create ---------------------------------------------------------------------------------------------

    public static final String BATTLE_CREATE_PANEL_TITLE           = String.valueOfC("СОЗДАТЬ БИТВУ");
    public static final String BATTLE_CREATE_BUTTON_USE_RANDOM_MAP = String.valueOfC("СЛУЧАЙНАЯ КАРТА");
    public static final String BATTLE_CREATE_GROUP_SIDES           = String.valueOfC("КОЛИЧЕСТВО КОМАНД НА БИТВУ");
    public static final String BATTLE_CREATE_GROUP_PLAYERS         = String.valueOfC("КОЛИЧЕСТВО ИГРОКОВ НА КОМАНДУ");
    public static final String BATTLE_CREATE_GROUP_WARRIORS        = String.valueOfC("КОЛИЧЕСТВО БОЙЦОВ НА ИГРОКА");

    // ----- Browse maps -----------------------------------------------------------------------------------------------

    public static final String BROWSE_MAPS_PANEL_TITLE = String.valueOfC("ВЫБОР КАРТЫ");
    public static final String BROWSE_MAPS_LIST_TITLE  = String.valueOfC("ЩЕЛКНИТЕ ПО ИЗОБРАЖЕНИЮ КАРТЫ ДЛЯ ВЫБОРА");

    // ----- Select warriors -------------------------------------------------------------------------------------------

    public static final String SELECT_WARRIORS_PANEL_TITLE = String.valueOfC("ВЫБОР БОЙЦОВ");
    public static final String SELECT_WARRIORS_LIST_TITLE  = String.valueOfC("ВЫБЕРИТЕ БОЙЦОВ");

}