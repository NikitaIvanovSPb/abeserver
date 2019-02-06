package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class AdminFTPDTOList extends ListResult<AdminFTPDTO> {
    public AdminFTPDTOList(List<AdminFTPDTO> results) {
        super(results);
    }
}
