package com.geargames.regolith.application;

import com.geargames.common.packer.PCreator;
import com.geargames.common.packer.PUnit;
import com.geargames.regolith.packer.PUnitStub;

/**
 * User: mikhail v. kutuzov
 */
public class PRegolithUnitCreator extends PCreator {

    public PUnit createUnit(int pid, int size) {
        return new PUnitStub(size);
    }
}
