package com.github.homework.theme.service;

import com.github.homework.theme.domain.Theme;
import com.github.homework.theme.repository.ThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Override
    @Transactional
    public Theme getOrSaveTheme(String themeName) {
        return this.themeRepository
                .findByName(themeName)
                .orElseGet(() -> this.themeRepository.save(new Theme(themeName)));
    }
}
