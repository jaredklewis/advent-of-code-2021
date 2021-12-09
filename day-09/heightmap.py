from __future__ import annotations
from dataclasses import dataclass
from typing import Optional, List


@dataclass
class Cell:
    height: int
    col: int = 0
    row: int = 0
    up: Optional[Cell] = None
    down: Optional[Cell] = None
    left: Optional[Cell] = None
    right: Optional[Cell] = None

    def __str__(self):
        return f"Cell {self.col}, {self.row}"

    def is_low(self) -> bool:
        return (
                (self.up is None or self.height < self.up.height) and
                (self.down is None or self.height < self.down.height) and
                (self.left is None or self.height < self.left.height) and
                (self.right is None or self.height < self.right.height)
        )


Heightmap = List[List[Cell]]


def parse_heightmap(text: str) -> Heightmap:
    cells = [list(map(lambda char: Cell(height=int(char)), list(line))) for line in text.splitlines()]

    row_count = len(cells)

    for row in range(row_count):
        col_count = len(cells[row])
        for col in range(col_count):
            has_up = (row - 1) >= 0
            has_down = (row + 1) < row_count
            has_left = (col - 1) >= 0
            has_right = (col + 1) < col_count

            cell = cells[row][col]
            cell.row = row
            cell.col = col
            cell.up = cells[row - 1][col] if has_up else None
            cell.down = cells[row + 1][col] if has_down else None
            cell.left = cells[row][col - 1] if has_left else None
            cell.right = cells[row][col + 1] if has_right else None

    return cells
