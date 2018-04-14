package pl.szotaa.punnr.game.message;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("line")
public class Line {

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
}
