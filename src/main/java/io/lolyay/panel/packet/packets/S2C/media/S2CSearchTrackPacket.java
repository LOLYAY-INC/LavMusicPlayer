package io.lolyay.panel.packet.packets.S2C.media;

import com.google.gson.annotations.Expose;
import io.lolyay.search.Search;
import io.lolyay.panel.packet.AbstractPacket;
import io.lolyay.panel.packet.S2CPacket;

public class S2CSearchTrackPacket extends AbstractPacket implements S2CPacket {
    @Expose
    public String query;

    @Expose
    public Search[] results;

    public S2CSearchTrackPacket(String query, Search[] results) {
        this.query = query;
        this.results = results;
    }

    @Override
    public int getOpcode() {
        return 111;
    }
}
