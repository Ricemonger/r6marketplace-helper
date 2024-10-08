package github.ricemonger.utils.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResaleLock {
    private String itemId;
    private Date expiresAt;
}
