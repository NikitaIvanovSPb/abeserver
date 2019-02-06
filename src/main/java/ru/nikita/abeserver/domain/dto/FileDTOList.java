package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class FileDTOList extends ListResult<FileDTO> {
    public FileDTOList(List<FileDTO> results) {
        super(results);
    }
}
