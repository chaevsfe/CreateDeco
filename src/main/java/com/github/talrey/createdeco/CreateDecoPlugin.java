package com.github.talrey.createdeco;

import com.zurrtum.create.api.registry.CreateRegisterPlugin;

public final class CreateDecoPlugin implements CreateRegisterPlugin {
    private static boolean blocksRegistered;

    @Override
    public void onBlockRegister() {
        if (blocksRegistered) {
            throw new IllegalStateException("Create Fly invoked Create Deco block registration more than once");
        }
        CreateDecoMod.init();
        blocksRegistered = true;
    }

    public static void verifyEarlyRegistrationComplete() {
        if (!blocksRegistered) {
            throw new IllegalStateException("Create Fly did not invoke Create Deco early registration");
        }
    }
}
