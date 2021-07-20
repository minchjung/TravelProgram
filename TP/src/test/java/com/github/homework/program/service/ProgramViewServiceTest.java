package com.github.homework.program.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramViewDetailDto;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ProgramViewServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @InjectMocks
    private ProgramViewService programViewService;

    @Test
    @DisplayName("프로그램이 한개 일때")
    void getByTest() {
        //given
        Program program = Program.builder()
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .theme(new Theme("theme"))
                .build();

        given(programRepository.findById(1L)).willReturn(Optional.of(program));
        //when
        Optional<ProgramViewDetailDto> optionalProgramViewDto = programViewService.getBy(1L);
        //then
        then(optionalProgramViewDto).hasValueSatisfying(programViewDto -> {
                    then(programViewDto.getName()).isEqualTo("name");
                    then(programViewDto.getIntroduction()).isEqualTo("introduction");
                    then(programViewDto.getIntroductionDetail()).isEqualTo("introductionDetail");
                    then(programViewDto.getRegion()).isEqualTo("region");
                    then(programViewDto.getThemeName()).isEqualTo("theme");
                    then(programViewDto.getCount()).isEqualTo(0L);
                }
        );

    }
    @Test
    @DisplayName("프로그램이 이름으로 검색")
    void getByNameTest() {
        //given
        Program program = Program.builder()
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .theme(new Theme("theme"))
                .build();

        given(programRepository.findByName("name")).willReturn(Optional.of(program));
        //when
        Optional<ProgramViewDetailDto> optionalProgramViewDto = programViewService.getBy("name");
        //then
        then(optionalProgramViewDto).hasValueSatisfying(programViewDto -> {
                    then(programViewDto.getName()).isEqualTo("name");
                    then(programViewDto.getIntroduction()).isEqualTo("introduction");
                    then(programViewDto.getIntroductionDetail()).isEqualTo("introductionDetail");
                    then(programViewDto.getRegion()).isEqualTo("region");
                    then(programViewDto.getThemeName()).isEqualTo("theme");
                    then(programViewDto.getCount()).isEqualTo(0L);
                }
        );
    }

    @Test
    @DisplayName("프로그램이 이름으로 검색 결과 없음")
    void getByNameEmptyTest() {
        //given
        //when
        Optional<ProgramViewDetailDto> optionalProgramViewDto = programViewService.getBy("name");
        //then
        then(optionalProgramViewDto).isEmpty();
    }


    @Test
    @DisplayName("프로그램이 여러개 일때")
    void pageByTest() {
        //given
        ProgramViewDto programViewDto = new ProgramViewDto(1L, "name", "themeName", 1L);

        given(programRepository.findBy(PageRequest.of(0, 100)))
                .willReturn(
                        new PageImpl<>(List.of(programViewDto, programViewDto))
                );

        //when
        Page<ProgramViewDto> programViewDtos = programViewService.pageBy(PageRequest.of(0, 100));
        //then
        then(programViewDtos.getContent()).hasSize(2);
        then(programViewDtos.getContent()).allSatisfy(p -> {
                    then(p.getId()).isGreaterThan(0L);
                    then(p.getName()).isEqualTo("name");
                    then(p.getThemeName()).isEqualTo("themeName");
                    then(p.getCount()).isEqualTo(1L);
                }
        );
    }
    @Test
    @DisplayName("조회수가 상위 10개인 프로그램 ")
    public void topByTest(){
        //given
        Program program = Program.builder()
                .name("name")
                .introduction("introduction")
                .introductionDetail("introductionDetail")
                .region("region")
                .theme(new Theme("theme"))
                .build();
        program.increaseCount();

        given(programRepository.findTop10ByOrderByCountDesc()).willReturn(List.of(program));
        //when
        List<ProgramViewDetailDto> programViewDtos = programViewService.topBy();

        //then
        then(programViewDtos).hasSize(1);
        then(programViewDtos).allSatisfy(p -> {
                    then(p.getName()).isEqualTo("name");
                    then(p.getThemeName()).isEqualTo("theme");
                    then(p.getCount()).isEqualTo(1L);
                }
        );
    }
}