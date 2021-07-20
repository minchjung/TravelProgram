package com.github.homework.program.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramSaveDto {
    private Long id;
    @NotBlank
    private String name;
    private String introduction;
    private String introductionDetail;
    @NotBlank
    private String region;
    @NotBlank
    private String themeName;

    @Builder
    public ProgramSaveDto(Long id, String name, String introduction, String introductionDetail, String region,
                          String themeName) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.introductionDetail = introductionDetail;
        this.region = region;
        this.themeName = themeName;
    }
}
