package shop.tryit.web.member;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import shop.tryit.domain.member.dto.MemberFormDto;
import shop.tryit.domain.member.service.MemberFacade;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberFacade memberFacade;

    /**
     * 회원 가입
     */
    @PostMapping("/new")
    public ResponseEntity newMember(@Valid @RequestBody @ModelAttribute("memberForm") MemberFormDto memberForm,
                                    BindingResult bindingResult) {

        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
        }

        // 검증 실패시 400
        if (bindingResult.hasErrors()) {
            log.info("member controller post");
            StringBuilder sb = new StringBuilder();

            for (FieldError error : bindingResult.getFieldErrors())
                sb.append(error.getDefaultMessage());

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        memberFacade.register(memberForm);

        URI location = UriComponentsBuilder.newInstance()
                .scheme("http")
                .path("tryeat.shop")
                .build()
                .toUri();

        // 성공 시 201
        return ResponseEntity.created(location).body("회원 등록 성공");
    }

    /**
     * 회원 정보 수정
     */
    @PutMapping("/update")
    public ResponseEntity editMember(@Valid @RequestBody @ModelAttribute("memberForm") MemberFormDto memberForm,
                                     BindingResult bindingResult) {
        log.info("회원 수정으로 이동");
        if (!memberForm.getPassword1().equals(memberForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "비밀번호가 일치하지 않습니다.");
        }

        // 검증 실패시 400
        if (bindingResult.hasErrors()) {
            log.info("member controller post");
            StringBuilder sb = new StringBuilder();

            for (FieldError error : bindingResult.getFieldErrors())
                sb.append(error.getDefaultMessage());

            return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        memberFacade.update(memberForm);
        URI location = UriComponentsBuilder.newInstance()
                .scheme("http")
                .path("tryeat.shop/members/update")
                .build()
                .toUri();

        // 성공 시 201
        return ResponseEntity.created(location).body("회원 수정 성공");
    }

}
