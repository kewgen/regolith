package com.geargames.regolith.application;

import com.geargames.common.Port;
import com.geargames.common.logging.Debug;
import com.geargames.common.packer.PManager;
import com.geargames.common.Graphics;

import java.io.InputStream;


/*
* Класс предназначен для разбора данных из пакера, а именно:
* - загрузка данных
* - инициализация имиджей
* - инициализация данных
*
* На данный момент Пакер выгружает 3 файла
* - 'i0' - пакет с имиджами
* - 'd0' - пакет с данными
* - 'Graph.java' - набор констант к данным
*
*/
public class Loader {

    private Manager manager;

    public Loader(Manager manager) {
        this.manager = manager;
        /*ObjC uncomment*///return self;
    }


    public void loadPacker(Graphics g, PManager packer) {
        String name = "/d0";
        try {
            InputStream is = Manager.getInstance().getMidlet().getResourceAsStream(name);
            packer.loadData(is);
            is.close();

            String pack_name = "/i0";
            if (Port.IS_DOUBLE_GRAPHIC) {
                pack_name = "_x2";
            }
            else if (Port.IS_FOURTHIRDS_GRAPHIC) {
                pack_name = "_x4_3";
            }
            is = manager.getMidlet().getResourceAsStream(pack_name);
            packer.loadImages(g, is);
            is.close();

        } catch (Exception e) {
            Debug.error("Could not load packer resources", e);
        }
    }
}