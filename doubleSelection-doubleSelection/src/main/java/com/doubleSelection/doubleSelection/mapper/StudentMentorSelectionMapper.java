package com.doubleSelection.doubleSelection.mapper;

import com.doubleSelection.doubleSelection.domain.StudentMentorSelection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMentorSelectionMapper {
    List<StudentMentorSelection> list(StudentMentorSelection studentMentorSelection);

    void insert(StudentMentorSelection selection);

    void delete(StudentMentorSelection selection);

    void update(StudentMentorSelection selection);
}
