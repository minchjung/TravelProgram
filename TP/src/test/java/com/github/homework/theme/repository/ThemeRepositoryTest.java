package com.github.homework.theme.repository;

import static org.assertj.core.api.BDDAssertions.then;

import com.github.homework.theme.domain.Theme;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(showSql = false)
@Transactional
class ThemeRepositoryTest {
    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("저장된 테마 이름 검색")
    void saveFindByNameTest() {
        //given
        testEntityManager.persist(new Theme("test"));
        //when
        Optional<Theme> optionalTheme = this.themeRepository.findByName("test");
        //then
        then(optionalTheme).isNotNull();
    }

    @Test
    @DisplayName("저장되지않은 테마 이름 검색")
    void notSaveFindByNameTest() {
        //given
        //when
        Optional<Theme> optionalTheme = this.themeRepository.findByName("test");
        //then
        then(optionalTheme.isEmpty()).isTrue();
    }
}