package matrixbackend.dto;

import lombok.Data;

@Data
public class InstallCloneRequestDTO {
    private Long ancestor_id;   // parent_id
    private int place;
}
