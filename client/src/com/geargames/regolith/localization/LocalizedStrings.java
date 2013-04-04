package com.geargames.regolith.localization;

/**
 * Список строковых констант.
 * User: abarakov
 * Date: 04.03.13
 */
// StrConsts
public class LocalizedStrings {

    // ----- Common ----------------------------------------------------------------------------------------------------

    // STR_BUTTON_OK
    public static final String BUTTON_OK     = "ОК";
    public static final String BUTTON_CANCEL = "ОТМЕНА";

    // ----- Battle create ---------------------------------------------------------------------------------------------

    public static final String BATTLE_CREATE_PANEL_TITLE           = "СОЗДАТЬ БИТВУ";
    public static final String BATTLE_CREATE_BUTTON_USE_RANDOM_MAP = "СЛУЧАЙНАЯ КАРТА";
    public static final String BATTLE_CREATE_GROUP_SIDES           = "КОЛИЧЕСТВО КОМАНД НА БИТВУ";
    public static final String BATTLE_CREATE_GROUP_PLAYERS         = "КОЛИЧЕСТВО ИГРОКОВ НА КОМАНДУ";
    public static final String BATTLE_CREATE_GROUP_WARRIORS        = "КОЛИЧЕСТВО БОЙЦОВ НА ИГРОКА";

    public static final String BATTLE_CREATE_MSG_FIND_BATTLE_TYPE_EXCEPTION = "НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ ПО ВЫБРАННЫМ ПАРАМЕТРАМ.";
//    НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ ПО ВЫБРАННЫМ ПАРАМЕТРАМ. ПОПРОБУЙТЕ ИЗМЕНИТЬ ПАРАМЕТРЫ БИТВЫ И ПОВТОРИТЬ СВОЕ ДЕЙСТВИЕ ЕЩЕ РАЗ.
    public static final String BATTLE_CREATE_MSG_GET_BATTLE_MAP_EXCEPTION = "НЕ УДАЛОСЬ ПОЛУЧИТЬ ОТ СЕРВЕРА КАРТУ БИТВЫ ПО ВЫБРАННЫМ ПАРАМЕТРАМ.";
    public static final String BATTLE_CREATE_MSG_CREATE_BATTLE_EXCEPTION = "НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ.";

    // ----- Browse maps -----------------------------------------------------------------------------------------------

    public static final String BROWSE_MAPS_PANEL_TITLE = "ВЫБОР КАРТЫ";
    public static final String BROWSE_MAPS_LIST_TITLE  = "ЩЕЛКНИТЕ ПО ИЗОБРАЖЕНИЮ КАРТЫ ДЛЯ ВЫБОРА";

    // ----- Select warriors -------------------------------------------------------------------------------------------

    public static final String SELECT_WARRIORS_PANEL_TITLE = "ВЫБОР БОЙЦОВ";
    public static final String SELECT_WARRIORS_LIST_TITLE  = "ВЫБЕРИТЕ БОЙЦОВ ДЛЯ БИТВЫ";
    public static final String SELECT_WARRIORS_MSG_LISTEN_TO_BATTLE_EXCEPTION = "НЕ УДАЛОСЬ ПОДПИСАТЬСЯ НА БИТВУ.";
    public static final String SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION = "НЕ УДАЛОСЬ ПРИСОЕДИНИТЬСЯ К ВОЕННОМУ СОЮЗУ.";
    public static final String SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION   = "ПРОИЗОШЛА НЕПРЕДВИДЕННАЯ ОШИБКА.";
    public static final String SELECT_WARRIORS_MSG_NOT_ENOUGH_WARRIORS        = "ДЛЯ УЧАСТИЯ В БИТВЕ ВАМ ТРЕБУЕТСЯ ВЫБРАТЬ БОЙЦОВ: %s.";
    // "ДЛЯ УЧАСТИЯ В БИТВЕ ВАМ ТРЕБУЕТСЯ ВЫБРАТЬ БОЙЦОВ: %s.\nСОВЕТ: ЩЕЛКНИТЕ ПО ИКОНКЕ БОЙЦА ДЛЯ ЕГО ВЫБОРА";

    public static final String SELECT_WARRIORS_MSG_CANCEL_BATTLE_EXCEPTION    = "НЕ УДАЛОСЬ ОТМЕНИТЬ БИТВУ.";

    // -----  ----------------------------------------------------------------------------------------------------------

    public static final String INFO_MESSAGE_1 = "Для присоединения к битве требуется 3 бойца, у вас доступно только 2";

    // -----  ----------------------------------------------------------------------------------------------------------

}