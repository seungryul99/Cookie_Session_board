package example.demo.service;

import example.demo.domain.Member;
import example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;


    @Transactional
    public void saveMemberInDB(Member member){
        memberRepository.save(member);
    }

}
