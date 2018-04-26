package com.enrsolidr.energyanalysis.services;

import com.enrsolidr.energyanalysis.entity.Member;
import com.enrsolidr.energyanalysis.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers(){
        List<Member> list = new ArrayList<>();
        memberRepository.findAll().forEach(e -> list.add(e));
        return list;
    }

    public Optional<Member> getMemberByEmail(String email) {
        List<Member> members = memberRepository.findByUserEmail(email);
        if(members.size() > 0) {
            members.get(0).getUser().setEmail(email);
            return Optional.of(members.get(0));
        } else {
            return Optional.empty();
        }
    }

    public boolean hasMemberPaid(String email, String year) {
        Optional<Member> member = this.getMemberByEmail(email);
        if(member.isPresent() && member.get().getMemberPayments().contains(year)) {
            return true;
        } else {
            return false;
        }
    }

    public void setMemberPaid(String email, String year) throws Exception {
        Optional<Member> member = this.getMemberByEmail(email);
        if(member.isPresent()) {
            member.get().getMemberPayments().add(year);
        } else {
            throw new Exception("Could not find member");
        }
    }

    public void addMember(Member inputMember) throws Exception {
        Optional<Member> member = this.getMemberByEmail(inputMember.getUser().getEmail());
        if(member.isPresent()) {
            throw new Exception("Member already exists");
        } else {
            memberRepository.insert(inputMember);
        }
    }
}
