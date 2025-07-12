package io.lolyay.features;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.lolyay.config.ConfigManager;
import io.lolyay.panel.packet.handlers.PacketHandler;
import io.lolyay.panel.packet.packets.S2C.youtube.S2COauthFinishPacket;
import io.lolyay.utils.Logger;

public class YoutubeOauth2Handler extends AppenderBase<ILoggingEvent> {
    public static boolean success = false;
    public static boolean loginRequired = false;
    public static String deviceCode = "";

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {

        if (iLoggingEvent.getFormattedMessage().contains("refreshed successfully")) {
            success = true;
            Logger.success("Youtube oauth2 refreshed successfully");
        }
        if (iLoggingEvent.getFormattedMessage().contains("and enter code ")) {
            loginRequired = true;
            Logger.warn("Youtube oauth2 login required");
            deviceCode = iLoggingEvent.getFormattedMessage().split("and enter code ")[1];
        }
        if(iLoggingEvent.getFormattedMessage().contains("Token retrieved successfully")) {
            loginRequired = false;
            success = true;
            ConfigManager.getConfig().getAdditionalSources().setYoutubeOauth2RefreshToken(
                   getRefreshTokenFromLog(iLoggingEvent.getFormattedMessage())
            );
            Logger.success("Youtube oauth2 refreshed and saved successfully");
            PacketHandler.broadcastPacket(new S2COauthFinishPacket(true, ConfigManager.getConfig().getAdditionalSources().getYoutubeOauth2RefreshToken()));

        }
       // Logger.debug(iLoggingEvent.getFormattedMessage());

    }

    private String getRefreshTokenFromLog(String log) {
        return log
                .split("\\(")[1]
                .replaceAll("\\)", "");
    }
}
