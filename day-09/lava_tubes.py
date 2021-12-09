import sys
from functools import reduce
from typing import List, Optional

from heightmap import Cell, parse_heightmap, Heightmap


def read_text_file(file: str) -> str:
    """Read text file as a string"""
    text_file = open(file, "r")
    text = text_file.read()
    text_file.close()

    return text


# Data
example = parse_heightmap(read_text_file("example.txt"))
puzzle_input = parse_heightmap(read_text_file("map.txt"))


def part_one():
    def calc_risk_level(heightmap: Heightmap) -> int:
        return sum([sum([cell.height + 1 for cell in row if cell.is_low()]) for row in heightmap])

    # Part one example
    assert calc_risk_level(example) == 15

    print("Part one - risk level: ", calc_risk_level(puzzle_input))


# Part two
def part_two():
    def find_basin(cell: Cell, parent: Optional[List[Cell]] = None) -> List[Cell]:
        if parent is None:
            parent = []
        basin = parent + [cell]

        for neighbor in (cell.up, cell.down, cell.left, cell.right):
            if neighbor not in basin and neighbor is not None and neighbor.height != 9:
                basin = find_basin(neighbor, basin)

        return basin

    def calc_largest_basins(heightmap: Heightmap) -> int:
        largest: List[int] = []
        for row in heightmap:
            for cell in row:
                if cell.is_low():
                    basin_size = len(find_basin(cell))
                    if len(largest) < 3:
                        largest.append(basin_size)
                        largest.sort()
                    elif basin_size > largest[0]:
                        largest[0] = basin_size
                        largest.sort()

        return reduce(lambda a, b: a * b, largest)

    # Example
    assert calc_largest_basins(example) == 1134

    print("Part two - product of 3 largest basins: ", calc_largest_basins(puzzle_input))


part_one()
part_two()
