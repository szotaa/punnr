package pl.szotaa.punnr.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;
import pl.szotaa.punnr.game.holder.GameRoomHolder;

@Component
public class SocketPermissionInterceptor extends ChannelInterceptorAdapter {

    /*
        This class is a dirty workaround for Spring's Security @PreAuthorize annotation
        throwing exceptions that I cannot resolve
    */

    @Autowired
    private GameRoomHolder gameRoomHolder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.SEND.equals(headerAccessor.getCommand())){
            String destination = headerAccessor.getDestination();
            if(destination.contains("/draw")){
                String currentDrawer = gameRoomHolder.getById(getGameId(destination)).getCurrentDrawer();
                if(!currentDrawer.equals(headerAccessor.getUser().getName())){
                    throw new RuntimeException("Not-a-drawer attempted to draw");
                }
            }
            else if(destination.contains("/chat")){
                String currentDrawer = gameRoomHolder.getById(getGameId(destination)).getCurrentDrawer();
                if(currentDrawer.equals(headerAccessor.getUser().getName())){
                    throw new RuntimeException("Drawer attempted to chat");
                }
            }
        }
        return super.preSend(message, channel);
    }

    private String getGameId(String destination){
        return destination.split("/")[2];
    }
}