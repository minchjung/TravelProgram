package com.github.homework.theme.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.repository.ThemeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ThemeServiceImplTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeServiceImpl themeService;


    @Test
    @DisplayName("테마이름이 없는경우")
    public void emptyThemeNameTest() {
        //given
        //when
        AssertionError error = Assertions.assertThrows(AssertionError.class, () -> {
            this.themeService.getOrSaveTheme("");
        });

        //then
        Assertions.assertEquals("empty name", error.getMessage());
    }

    @Test
    @DisplayName("테마이름이 한개인 경우 저장")
    public void oneThemeNameTest() {
        //given
        given(this.themeRepository.save(isA(Theme.class))).willReturn(new Theme("자연체험"));

        //when
        Theme theme = this.themeService.getOrSaveTheme("자연체험");
        //then
        then(theme.getName()).isEqualTo("자연체험");
    }


}