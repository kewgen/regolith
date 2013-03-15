package com.geargames.concept;

import com.geargames.common.String;

/**
 * User: abarakov
 * Date: 14.03.13
 */
public interface Port {

    // Display, Screen, Desktop

    //todo: Перенести методы getDisplayWidth и getDisplayHeight в класс Display

    /**
     * Получить ширину экрана в пикселях.
     */
    int getDisplayWidth();

    /**
     * Получить высоту экрана в пикселях.
     */
    int getDisplayHeight();

    //todo: Перенести метод getModelString в класс device.Device

    /**
     * Получить информацию об устройстве - название фирмы производителя и номер модели.
     */
    String getModelString();

    //todo: Перенести метод getOSVersionString в класс env.OperationSystem

    /**
     * Получить информацию об операционной системе - название и ее версию.
     */
    String getOSVersionString();

}