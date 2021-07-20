package com.github.homework.program.controller;

import com.github.homework.common.BaseControllerTest;
import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramSaveDto;
import com.github.homework.program.repository.ProgramRepository;
import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.repository.ThemeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProgramControllerTest extends BaseControllerTest {
    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("프로그램 단건 조회")
    public void getProgramTest() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs/{id}", program.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
                .andExpect(jsonPath("introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
                .andExpect(jsonPath("region").value("전라남도 여수시"))
                .andExpect(jsonPath("themeName").value("식도락여행"))
                .andExpect(jsonPath("count").value("1"))
                .andDo(write.document(
                        pathParameters(
                                parameterWithName("id").description("id")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 이름 조회")
    public void getProgramNameTest() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs/name").param("name", program.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("introduction").value("여수시 일대 게장백반, 돌산갓김치등"))
                .andExpect(jsonPath("introductionDetail").value("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램"))
                .andExpect(jsonPath("region").value("전라남도 여수시"))
                .andExpect(jsonPath("themeName").value("식도락여행"))
                .andExpect(jsonPath("count").value("0"))
                .andDo(write.document(
                        requestParameters(
                                parameterWithName("name").description("이름")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 단건 조회 결과 없음")
    public void getProgramFailTest() throws Exception {
        this.mockMvc.perform(get("/api/programs/{id}", 10L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("프로그램 page 조회")
    public void getPageProgramTest() throws Exception {
        givenProgram(givenTheme("식도락여행"));
        this.mockMvc.perform(get("/api/programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").isNotEmpty())
                .andExpect(jsonPath("$..name").value("여수 10미 먹거리"))
                .andExpect(jsonPath("$..themeName").value("식도락여행"))
                .andExpect(jsonPath("$..count").value(0))
                .andExpect(jsonPath("totalPages").value("1"))
                .andExpect(jsonPath("totalElements").value("1"));
    }
    @Test
    @DisplayName("조회수가 상위 10개인 프로그램 조회")
    public void getTopProgramTest() throws Exception {
        IntStream.range(0, 20).forEach(i -> {
                    Theme theme = new Theme("theme" + i);
                    themeRepository.save(theme);
                    Program program = Program.builder()
                            .name("name")
                            .introduction("introduction")
                            .introductionDetail("introductionDetail")
                            .region("region")
                            .theme(theme)
                            .build();
                    IntStream.range(i, 20).forEach(j -> {
                        program.increaseCount();
                    });
                    programRepository.save(program);
                }
        );
        this.mockMvc.perform(get("/api/programs/tops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").isNotEmpty())
                .andExpect(jsonPath("[0].name").value("name"))
                .andExpect(jsonPath("[0].themeName").value("theme0"))
                .andExpect(jsonPath("[0].count").value(20))
                .andExpect(jsonPath("[1].id").isNotEmpty())
                .andExpect(jsonPath("[1].name").value("name"))
                .andExpect(jsonPath("[1].themeName").value("theme1"))
                .andExpect(jsonPath("[1].count").value(19));
    }
    @Test
    @DisplayName("프로그램 저장 정상 케이스")
    public void saveProgramTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().name("여수 10미 먹거리")
                .region("전라남도 여수시").themeName("식도락여행")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value("true"))
                .andExpect(jsonPath("message").value("저장 성공"))
                .andDo(write.document(
                        requestFields(
                                fieldWithPath("name").description("프로그램 이름"),
                                fieldWithPath("introduction").description("소개"),
                                fieldWithPath("introductionDetail").description("상세 소개"),
                                fieldWithPath("region").description("지역 이름"),
                                fieldWithPath("themeName").description("테마 이름")
                        )
                ));
    }

    @Test
    @DisplayName("프로그램 저장 name이 없는 케이스")
    public void saveProgramFailTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder()
                .region("전라남도 여수시").themeName("식도락여행")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(post("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].field").value("name"))
                .andExpect(jsonPath("[0].objectName").value("programSaveDto"))
                .andExpect(jsonPath("[0].code").value("NotBlank"))
                .andExpect(jsonPath("[0].defaultMessage").value("must not be blank"));

    }

    @Test
    @DisplayName("프로그램 수정 정상 케이스")
    public void updateProgramTest() throws Exception {
        Program program = givenProgram(givenTheme("식도락여행"));
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().id(program.getId()).name("여수 10미 먹거리2")
                .region("전라남도 여수시").themeName("식도락여행2")
                .introduction(program.getIntroduction())
                .introductionDetail(program.getIntroductionDetail())
                .build();

        this.mockMvc.perform(put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(write.document(
                        requestFields(
                                fieldWithPath("id").description("id"),
                                fieldWithPath("name").description("프로그램 이름"),
                                fieldWithPath("introduction").description("소개"),
                                fieldWithPath("introductionDetail").description("상세 소개"),
                                fieldWithPath("region").description("지역 이름"),
                                fieldWithPath("themeName").description("테마 이름")
                        )));
    }

    @Test
    @DisplayName("프로그램 수정 없는 id로 요청 케이스")
    public void updateProgramFailTest() throws Exception {
        ProgramSaveDto programSaveDto = ProgramSaveDto.builder().id(10L)
                .name("여수 10미 먹거리2")
                .region("전라남도 여수시").themeName("식도락여행2")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail("여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .build();
        this.mockMvc.perform(put("/api/programs/").contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(programSaveDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Program givenProgram(Theme theme) {
        Program program = Program.builder().name("여수 10미 먹거리")
                .region("전라남도 여수시")
                .introduction("여수시 일대 게장백반, 돌산갓김치등")
                .introductionDetail(
                        "여행자와 현지인이 꼽은 최고의 먹거리 여행지' 에서 대한민국 229개 지방자치단체 중 여수시가 1위에 선정되어 식도락 여행에 최적화된 프로그램")
                .theme(theme)
                .build();
        return this.programRepository.save(program);
    }

    private Theme givenTheme(String name) {
        return this.themeRepository.save(new Theme(name));
    }
}