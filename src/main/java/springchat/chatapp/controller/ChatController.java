package springchat.chatapp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import springchat.chatapp.dto.ChatMessage;
import springchat.chatapp.pubsub.RedisPublisher;
import springchat.chatapp.repository.ChatRoomReposiroy;
import springchat.chatapp.service.JwtTokenProvider;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChannelTopic channelTopic;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token) {
        String nickname = jwtTokenProvider.getUserNameFromJwt(token);

        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 입장시에는 대화명과 메시지를 자동으로 세팅한다.
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(nickname + "님이 입장하셨습니다.");
        }
        // Websocket에 발행된 메시지를 redis로 발행한다. (publish)
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
