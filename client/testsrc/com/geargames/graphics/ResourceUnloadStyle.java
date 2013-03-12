package com.geargames.graphics;

/**
 * User: abarakov
 * Date: 08.03.13
 */
public class ResourceUnloadStyle {

    public static final byte IMMEDIATE = 2; // Ресурс будет выгружен сразу, при его освобождении
    public static final byte NONPAGED  = 1; // Ресурс не будет выгружаться вообще (например шрифт по умолчанию)
    // DEFAULT
    public static final byte AUTO      = 0; // Ресурс будет выгружаться по мере необходимости и только в случае, если
                                            // для загрузки нового ресурса не достаточно памяти, в случае если памяти
                                            // вдостатке, может случится так, что ни один ресурс вообще не будет выгружен.

}