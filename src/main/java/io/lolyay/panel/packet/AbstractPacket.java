package io.lolyay.panel.packet;

import com.google.gson.annotations.Expose;

import java.util.Objects;

/**
 * Abstract base class for packets that provides common functionality.
 */
public abstract class AbstractPacket implements Packet {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(getOpcode(), ((Packet) obj).getOpcode());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{opcode=" + getOpcode() + "}";
    }

     @Expose
     public int opcode = getOpcode();
}
