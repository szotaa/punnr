package pl.szotaa.punnr.game.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;
}
