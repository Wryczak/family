package com.example.family.Controllers.HomeAndErrors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.family.MainObjectsFamilyMemberDto.MemberDto;
import com.example.family.Repositories.MemberRepository;
import com.example.family.MainObjectsFamilyMemberDto.autocompleteList.ListItemDto;
import com.example.family.services.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("list")
public class ListRestController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
private final ModelMapper modelMapper;
    public ListRestController(MemberService memberService, MemberRepository memberRepository, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public List<ListItemDto> ListItems(@RequestParam(value = "q", required = false) String query) {
        List<MemberDto> temp = memberService.getMembersDTOList();
//        List<Member> members1=memberRepository.findAll();
//        List<MemberDto> temp = Arrays.asList(modelMapper.
//                map(members1, MemberDto[].class));
        MemberDto[] members = temp.toArray(MemberDto[]::new);
        if (StringUtils.isEmpty(query)) {
            return Arrays.stream(members)
                    .limit(15)
                    .map(this::mapToListItemDto)
                    .collect(Collectors.toList());
        }

        return Arrays.stream(members)
                .filter(memberDto -> memberDto.getName()
                        .toLowerCase()
                        .contains(query))
                .limit(15)
                .map(this::mapToListItemDto)
                .collect(Collectors.toList());
    }

    private ListItemDto mapToListItemDto(MemberDto memberDto) {
        return ListItemDto.builder()
                .id(memberDto.getId())
                .text((memberDto.getName())+" "+memberDto.getFamilyName())
                .build();
    }
}
