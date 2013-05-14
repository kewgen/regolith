package com.geargames.regolith;

/**
 * User: abarakov
 * Date: 23.04.13
 * Набор кодов ошибок.
 */
public class ErrorCodes {

    public static final short SUCCESS = 0;        // Операция выполнена успешно. Ошибки нет
    public static final short UNKNOWN_ERROR = 1;  // Неизвестная ошибка
    public static final short INTERNAL_ERROR = 2; // Произошла внутренняя ошибка

    // ----- Коды ошибок при попытке авторизоваться в системе ----------------------------------------------------------

    public static final short LOGON_INVALID_USERNAME_OR_PASSWORD = 10; // Неверная пара логин/пароль
    public static final short LOGON_USER_IS_ALREADY_LOGGED = 11;       // Пользователь уже вошел в систему
    public static final short LOGON_LIMIT_LOGIN_ATTEMPTS_REACHED = 12; // Превышен лимит попыток посылки на сервер пар логин/пароль. Пользователю временно будет не доступен вход в систему
    public static final short LOGON_ACCOUNT_IS_BANNED = 13;            // Аккаунт заблокирован (забанен)
    public static final short LOGON_SESSION_LIMIT_REACHED = 14;        // Превышен лимит сессий (на сервере достигнуто число максимально возможных одновременно подключенных пользователей)

    // ----- Общие коды ошибок -----------------------------------------------------------------------------------------

    public static final short INVALID_BATTLE_MAP = 200;  // Недопустимый id карты
    public static final short INVALID_BATTLE_TYPE = 201; // Недопустимый id типа битвы

    // -----------------------------------------------------------------------------------------------------------------

    public static String getLocalizedError(short errorCode) {
        String errorString;
        switch (errorCode) {
            case SUCCESS:
                errorString = "Нет ошибки";
                break;
            case UNKNOWN_ERROR:
                errorString = "Произошла неизвестная ошибка";
                break;
            case INTERNAL_ERROR:
                errorString = "Произошла внутренная ошибка";
                break;

            case LOGON_INVALID_USERNAME_OR_PASSWORD:
                errorString = "Неверная пара логин/пароль";
                break;
            case LOGON_USER_IS_ALREADY_LOGGED:
                errorString = "Пользователь уже вошел в систему";
                break;
            case LOGON_LIMIT_LOGIN_ATTEMPTS_REACHED:
                errorString = "Превышен лимит попыток посылки на сервер пар логин/пароль";
                break;
            case LOGON_ACCOUNT_IS_BANNED:
                errorString = "Аккаунт забанен";
                break;
            case LOGON_SESSION_LIMIT_REACHED:
                errorString = "Превышен лимит сессий";
                break;

            case INVALID_BATTLE_MAP:
                errorString = "Недопустимый id игровой карты";
                break;
            case INVALID_BATTLE_TYPE:
                errorString = "Недопустимый id типа битвы";
                break;

            default:
                errorString = "Неизвестный код ошибки";
        }
        return errorString + " (code = " + errorCode + ")";
    }

}
