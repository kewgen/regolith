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

    // ----- Battles ---------------------------------------------------------------------------------------------------

    public static final String BATTLES_MSG_GO_TO_BATTLE_MARKET_EXCEPTION              = "НЕ УДАЛОСЬ ИЗМЕНИТЬ СТЕЙТ НА СЕРВЕРЕ ДЛЯ КЛИЕНТА НА BATTLE MARKET";
    public static final String BATTLES_MSG_LISTEN_TO_CREATED_BATTLES_EXCEPTION        = "НЕ УДАЛОСЬ ПОДПИСАТЬСЯ НА РАССЫЛКУ ОБНОВЛЕНИЙ БИТВ";
    public static final String BATTLES_MSG_DO_NOT_LISTEN_TO_CREATED_BATTLES_EXCEPTION = "НЕ УДАЛОСЬ ОТПИСАТЬСЯ ОТ РАССЫЛКИ ОБНОВЛЕНИЙ БИТВ";
    public static final String BATTLES_MSG_CANCEL_BATTLE_EXCEPTION                    = "НЕ УДАЛОСЬ ОТМЕНИТЬ БИТВУ";
    public static final String BATTLES_MSG_START_BATTLE_EXCEPTION                     = "НЕ УДАЛОСЬ СТАРТОВАТЬ БИТВУ";
    public static final String BATTLES_MSG_SELF_EVICT_ACCOUNT_EXCEPTION               = "НЕ УДАЕТСЯ ВЫЙТИ ИЗ БИТВЫ";
    public static final String BATTLES_MSG_EVICT_ACCOUNT_EXCEPTION                    = "НЕ УДАЕТСЯ ИСКЛЮЧИТЬ ИГРОКА ИЗ БИТВЫ";
    public static final String BATTLES_MSG_DO_NOT_LISTEN_TO_BATTLE_EXCEPTION          = "НЕ УДАЛОСЬ ОТПИСАТЬСЯ ОТ РАССЫЛКИ ОБНОВЛЕНИЙ ОТДЕЛЬНОЙ БИТВЫ";

    // ----- Battle create ---------------------------------------------------------------------------------------------

    public static final String BATTLE_CREATE_PANEL_TITLE           = "СОЗДАТЬ БИТВУ";
    public static final String BATTLE_CREATE_BUTTON_USE_RANDOM_MAP = "СЛУЧАЙНАЯ КАРТА";
    public static final String BATTLE_CREATE_GROUP_SIDES           = "КОЛИЧЕСТВО КОМАНД НА БИТВУ";
    public static final String BATTLE_CREATE_GROUP_PLAYERS         = "КОЛИЧЕСТВО ИГРОКОВ НА КОМАНДУ";
    public static final String BATTLE_CREATE_GROUP_WARRIORS        = "КОЛИЧЕСТВО БОЙЦОВ НА ИГРОКА";

    public static final String BATTLE_CREATE_MSG_FIND_BATTLE_TYPE_EXCEPTION = "НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ ПО ВЫБРАННЫМ ПАРАМЕТРАМ";
//    НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ ПО ВЫБРАННЫМ ПАРАМЕТРАМ. ПОПРОБУЙТЕ ИЗМЕНИТЬ ПАРАМЕТРЫ БИТВЫ И ПОВТОРИТЬ СВОЕ ДЕЙСТВИЕ ЕЩЕ РАЗ
    public static final String BATTLE_CREATE_MSG_GET_BATTLE_MAP_EXCEPTION   = "НЕ УДАЛОСЬ ПОЛУЧИТЬ ОТ СЕРВЕРА КАРТУ БИТВЫ ПО ВЫБРАННЫМ ПАРАМЕТРАМ";
    public static final String BATTLE_CREATE_MSG_CREATE_BATTLE_EXCEPTION    = "НЕ УДАЛОСЬ СОЗДАТЬ БИТВУ";
    public static final String BATTLE_START_AWAITING = "ОЖИДАЕМ ВХОДА НА СЕРВЕР БИТВЫ";
    // ----- Browse maps -----------------------------------------------------------------------------------------------

    public static final String BROWSE_MAPS_PANEL_TITLE = "ВЫБОР КАРТЫ";
    public static final String BROWSE_MAPS_LIST_TITLE  = "ЩЕЛКНИТЕ ПО ИЗОБРАЖЕНИЮ КАРТЫ ДЛЯ ВЫБОРА";

    // ----- Select warriors -------------------------------------------------------------------------------------------

    public static final String SELECT_WARRIORS_PANEL_TITLE                    = "ВЫБОР БОЙЦОВ";
    public static final String SELECT_WARRIORS_LIST_TITLE                     = "ВЫБЕРИТЕ БОЙЦОВ ДЛЯ БИТВЫ";
    public static final String SELECT_WARRIORS_MSG_LISTEN_TO_BATTLE_EXCEPTION = "НЕ УДАЛОСЬ ПОДПИСАТЬСЯ НА БИТВУ";
    public static final String SELECT_WARRIORS_MSG_JOIN_TO_ALLIANCE_EXCEPTION = "НЕ УДАЛОСЬ ПРИСОЕДИНИТЬСЯ К ВОЕННОМУ СОЮЗУ";
    public static final String SELECT_WARRIORS_MSG_COMPLETE_GROUP_EXCEPTION   = "НЕ УДАЛОСЬ СОБРАТЬ БОЙЦОВ В БОЕВУЮ ГРУППУ";
    public static final String SELECT_WARRIORS_MSG_DISBAND_GROUP_EXCEPTION    = "НЕ УДАЛОСЬ РАСПУСТИТЬ БОЙЦОВ БОЕВОЙ ГРУППЫ";
    public static final String SELECT_WARRIORS_MSG_NOT_ENOUGH_WARRIORS        = "ДЛЯ УЧАСТИЯ В БИТВЕ ВАМ ТРЕБУЕТСЯ ВЫБРАТЬ БОЙЦОВ: %s";
    // "ДЛЯ УЧАСТИЯ В БИТВЕ ВАМ ТРЕБУЕТСЯ ВЫБРАТЬ БОЙЦОВ: %s.\nСОВЕТ: ЩЕЛКНИТЕ ПО ИКОНКЕ БОЙЦА ДЛЯ ЕГО ВЫБОРА";

    public static final String SELECT_WARRIORS_MSG_CANCEL_BATTLE_EXCEPTION    = "НЕ УДАЛОСЬ ОТМЕНИТЬ БИТВУ";
    public static final String COULD_NOT_START_BATTLE = "НЕ УДАЛОСЬ НАЧАТЬ БИТВУ";
    public static final String COULD_NOT_LOGIN_TO_BATTLE = "НЕ УДАЛОСЬ ПОДСОЕДИНИТЬСЯ К БИТВЕ";

    // ----- Player info -------------------------------------------------------------------------------------------

    public static final String PLAYER_INFO_PANEL_TITLE        = "ИНФОРМАЦИЯ ОБ ИГРОКЕ";

    public static final String PLAYER_INFO_HINT_PLAYER_NAME   = "ИМЯ ИГРОКА";
    public static final String PLAYER_INFO_HINT_PLAYER_LEVEL  = "УРОВЕНЬ ИГРОКА";
    public static final String PLAYER_INFO_HINT_PLAYER_SCORE  = "КОЛИЧЕСТВО ОЧКОВ У ИГРОКА"; // Опыт игрока
    public static final String PLAYER_INFO_HINT_PLAYER_WINS   = "КОЛИЧЕСТВО ПОПЕД";
    public static final String PLAYER_INFO_HINT_PLAYER_LOSSES = "КОЛИЧЕСТВО ПОТЕРЯННЫХ БОЙЦОВ";
    public static final String PLAYER_INFO_HINT_PLAYER_CLAN   = "КЛАН, В КОТОРЫЙ ВХОДИТ ИГРОК";

    // -----  ----------------------------------------------------------------------------------------------------------

    public static final String PANEL_WAIT_RESPONSE_TITLE = "ОЖИДАНИЕ ОТВЕТА ОТ СЕРВЕРА. ПОЖАЛУЙСТА ПОДОЖДИТЕ";

    // -----  ----------------------------------------------------------------------------------------------------------

    public static final String INFO_MESSAGE_1 = "Для присоединения к битве требуется бойцов - 3, у вас доступно только 2";

    // -----  ----------------------------------------------------------------------------------------------------------

}