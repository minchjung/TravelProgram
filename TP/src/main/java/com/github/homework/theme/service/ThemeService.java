package com.github.homework.theme.service;

import com.github.homework.theme.domain.Theme;

public interface ThemeService {
    Theme getOrSaveTheme(String themeName);
}
