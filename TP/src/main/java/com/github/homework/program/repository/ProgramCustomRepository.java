package com.github.homework.program.repository;

import com.github.homework.program.model.ProgramViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProgramCustomRepository {

    Page<ProgramViewDto> findBy(Pageable pageable);
}