package com.github.homework.program.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.homework.program.domain.Program;
import com.github.homework.program.exception.ProgramNotFoundException;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.model.ProgramSaveDto.ProgramSaveDtoBuilder;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.service.ThemeService;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProgramSaveServiceTest {
    @Mock
    private ThemeService themeService;
    @Mock
    private ProgramRepository programRepository;
    @InjectMocks
    private ProgramSaveService programSaveService;
    @Captor
    private ArgumentCaptor<Program> argumentCaptor;

    @Test
    @DisplayName("프로그램 저장")
    public void saveProgramTest() {
        //given
        ProgramSaveDto programSaveDto = givenProgramSaveDto(null);
        given(themeService.getOrSaveTheme(programSaveDto.getThemeName())).willReturn(new Theme("식도락여행"));
        //when
        programSaveService.saveProgram(programSaveDto);
        //then
        verify(this.programRepository, times(1)).save(argumentCaptor.capture());
        then(argumentCaptor.getValue()).isNotNull();
        then(argumentCaptor.getValue().getName()).isEqualTo("여수 10미 먹거리");
        then(argumentCaptor.getValue().getRegion()).isEqualTo("전라남도 여수시");
        then(argumentCaptor.getValue().getIntroduction()).isEqualTo("여수시 일대 게장백반, 돌산갓김치등");
        then(argumentCaptor.getValue().getIntroductionDetail()).isEqualTo(
            "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램");
        then(argumentCaptor.getValue().getTheme().getName()).isEqualTo("식도락여행");

    }

    @Test
    @DisplayName("프로그램 수정")
    public void updateProgramTest() throws ProgramNotFoundException {
        //given
        ProgramSaveDto programSaveDto = givenProgramSaveDto(1L);
        Program mockProgram = mock(Program.class);
        given(programRepository.findById(1L)).willReturn(Optional.of(mockProgram));
        Theme givenTheme = new Theme("식도락여행");
        given(themeService.getOrSaveTheme(programSaveDto.getThemeName())).willReturn(givenTheme);

        //when
        programSaveService.updateProgram(programSaveDto);

        //then
        verify(mockProgram, times(1)).updateProgram(
            eq("여수 10미 먹거리"),
            eq("여수시 일대 게장백반, 돌산갓김치등"),
            eq("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"),
            eq("전라남도 여수시"),
            eq(givenTheme));
    }

    @Test
    @DisplayName("존재 하지 않는 프로그램 수정")
    public void updateNotFoundProgramTest() {
        //given
        ProgramSaveDto programSaveDto = givenProgramSaveDto(null);

        //when
        //then
        Assertions.assertThrows(ProgramNotFoundException.class, () -> {
            programSaveService.updateProgram(programSaveDto);
        });

    }
    private ProgramSaveDto givenProgramSaveDto(Long id) {
        ProgramSaveDtoBuilder builder = ProgramSaveDto.builder().name("여수 10미 먹거리")
            .region("전라남도 여수시").themeName("식도락여행")
            .introduction("여수시 일대 게장백반, 돌산갓김치등")
            .introductionDetail(
                "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램");
        if (id != null) {
            builder.id(id);
        }
        return builder.build();
    }
}