package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private HttpSession session;

    @PostMapping("/reply")
    public String save(ReplySaveReqDto replySaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomException("comment를 작성해주세요");
        }
        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomException("boardId가 넘어오지않았습니다");
        }
        // 서비스 호출 (replySaveReqDto, principal.gerId())
        replyService.댓글쓰기(replySaveReqDto, principal.getId());
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

    @Setter
    @Getter
    public static class ReplySaveReqDto {
        private String comment;
        private Integer boardId; // Integer : 값이 없으면 null로 들어옴
    }
}
