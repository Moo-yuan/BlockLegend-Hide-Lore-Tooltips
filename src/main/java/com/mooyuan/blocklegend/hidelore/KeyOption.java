package com.mooyuan.blocklegend.hidelore;

public enum KeyOption {
    CAPS_LOCK(280, "Caps Lock"),
    LEFT_CONTROL(341, "Left Ctrl"),
    LEFT_ALT(342, "Left Alt"),
    // LEFT_SHIFT 已移除（存在冲突BUG）
    RIGHT_SHIFT(344, "Right Shift");

    private final int keyCode;
    private final String displayName;

    KeyOption(int keyCode, String displayName) {
        this.keyCode = keyCode;
        this.displayName = displayName;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static KeyOption fromName(String name) {
        for (KeyOption option : values()) {
            if (option.name().equalsIgnoreCase(name)) {
                return option;
            }
        }
        return CAPS_LOCK;
    }
}
