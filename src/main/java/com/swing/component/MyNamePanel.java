package com.swing.component;

import com.swing.dialog.GenericPanel;

public abstract class MyNamePanel extends GenericPanel {
    public abstract String getRequestName();

    public abstract String getAlias();

    public abstract String getActionName();

    public abstract String getServerIp();

    public String getRequetId() {
        return null;
    }
}
