package com.geargames.graphics;

/**
 * User: abarakov
 * Date: 08.03.13
 */
public class ResourceState {

//  public static final byte CREATING          = 0; // Ресурс создается
//  public static final byte CREATED           = 1; // Ресурс создан
//  public static final byte INITIALIZING      = 2; // Ресурс инициализируется
//  public static final byte INITIALIZED       = 3; // Ресурс инициализирован
    public static final byte UNLOADED          = 4; // Ресурс не загружен/выгружен
    public static final byte LOADING           = 5; // Ресурс загружается (читает свои свойства)
    public static final byte LOADED            = 6; // Ресурс уже загружен
    public static final byte WAITING_TO_UNLOAD = 7; // Ресурс помещен в очередь на выгрузку, хотя он и находится в
                                                    // загруженом состоянии, но в любой момент ResourceUnloader может
                                                    // выгрузить ресурс или ресурс может снова перейти в состояние LOADED
    public static final byte UNLOADING         = 8; // Ресурс выгружается
//  public static final byte FINALIZING        = 9; // Ресурс финализируется
//  public static final byte DESTROYING        =10; // Ресурс разрушается

}