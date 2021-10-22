package springchat.chatapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅, 퇴장
    public enum MessageType {
        ENTER, TALK, QUIT
    }
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    private long userCount; // 채팅방 인원수, 채팅방 내에서 메세지가 전달될 때 인원수 갱신 시 사용

    public ChatMessage() {
    }

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message, long userCount) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.userCount = userCount;
    }
}
