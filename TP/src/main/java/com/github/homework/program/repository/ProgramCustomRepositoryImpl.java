package com.github.homework.program.repository;


import com.github.homework.program.domain.Program;
import com.github.homework.program.model.ProgramViewDto;
import com.github.homework.program.model.QProgramViewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.Objects;

import static com.github.homework.program.domain.QProgram.program;
import static com.github.homework.theme.domain.QTheme.theme;

public class ProgramCustomRepositoryImpl extends QuerydslRepositorySupport implements ProgramCustomRepository {

    public ProgramCustomRepositoryImpl() {
        super(Program.class);
    }

    @Override
    public Page<ProgramViewDto> findBy(Pageable pageable) {
        JPQLQuery<ProgramViewDto> query = Objects.requireNonNull(getQuerydsl())
                .applyPagination(pageable, from(program)
                        .innerJoin(program.theme, theme)
                ).select(new QProgramViewDto(
                        program.id,
                        program.name,
                        program.theme.name,
                        program.count
                ));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }
}