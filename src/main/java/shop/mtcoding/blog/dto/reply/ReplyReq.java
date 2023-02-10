package shop.mtcoding.blog.dto.reply;

import lombok.Getter;
import lombok.Setter;

public class ReplyReq {

    @Setter
    @Getter
    public static class ReplySaveReqDto {
        private String comment;
        private Integer boardId; // Integer : 값이 없으면 null로 들어옴
    }

}
