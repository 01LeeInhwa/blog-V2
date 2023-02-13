package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
@Transactional(readOnly = true)
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    // where 절에 걸리는 파라메터를 앞에 받기
    @Transactional // 메서드가 종료됐을 때 커밋, 실패했을 때 롤백 ,롤백 기본값은 runtimeException
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {

        int result = replyRepository.insert(
                replySaveReqDto.getComment(),
                replySaveReqDto.getBoardId(),
                principalId);

        if (result != 1) {
            throw new CustomApiException("댓글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void 댓글삭제(int id, int principalId) {
        Reply reply = replyRepository.findById(id); // Reply 엔티티
        if (reply == null) {
            throw new CustomApiException("댓글이 존재하지 않습니다."); // 오버로딩 -> HttpStatus.BAD_REQUEST 생략가능

        }
        if (reply.getUserId() != principalId) {
            throw new CustomApiException("댓글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN); // 403
        }
        // 1.인증 OK(컨트롤러에서), 2.댓글존재 유무확인 ,3.권한 OK
        try {
            replyRepository.deleteById(id); // 제어가 안됨 => try catch로 잡아준다
        } catch (Exception e) {
            log.error("서버에러 : " + e.getMessage());
            throw new CustomApiException("댓글 삭제 실패 - 서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
