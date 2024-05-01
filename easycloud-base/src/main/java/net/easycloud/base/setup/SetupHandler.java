package net.easycloud.base.setup;

import lombok.Getter;

@Getter
public final class SetupHandler {
    private boolean onSetup;

    public SetupHandler() {
        this.onSetup = false;
    }

    public void input(String input) {

    }

    public void start() {
        onSetup = true;
    }
}
