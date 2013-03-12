package com.geargames.regolith.application;

import com.geargames.ConsoleDebug;
import com.geargames.common.Port;
import com.geargames.common.String;
import com.geargames.common.env.SystemEnvironment;
import com.geargames.common.packer.PManager;
import com.geargames.common.Graphics;

import java.io.IOException;
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
        String name = String.valueOfC("/d0");
        try {
            InputStream is = Manager.getInstance().getMidlet().getResourceAsStream(name);
            packer.loadData(is);
            is.close();

            String pack_name = String.valueOfC("/i0");
            if (Port.IS_DOUBLE_GRAPHIC) pack_name = pack_name.concatC("_x2");
            else if (Port.IS_FOURTHIRDS_GRAPHIC) pack_name = pack_name.concatC("_x4_3");
            is = manager.getMidlet().getResourceAsStream(pack_name);
            packer.loadImages(g, is);
            is.close();

        } catch (IOException e) {
            ((ConsoleDebug) SystemEnvironment.getInstance().getDebug()).logEx(e);
        }
    }
}